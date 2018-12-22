package com.br.newMall.web.filter;


import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.service.WX_UserHandler;
import com.br.newMall.web.utils.HttpUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * 用户session过滤，判断用户是否登录
 * Created by yongtao.chen on 2016.8.25
 */
public class SessionFilter implements Filter {

    @Autowired
    private WX_UserHandler.Client wxUserHandler;

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    private static final Set<String> anonymousUrl = new ConcurrentSkipListSet<>();

    private static final Set<String> totallyAnonymousUrl = new ConcurrentSkipListSet<>();

    static {
        anonymousUrl.add("callback.do");
    }

    static {
        totallyAnonymousUrl.add("transferImage.do");
        //todo 单测暂时放开

        totallyAnonymousUrl.add("login.do");

        totallyAnonymousUrl.add("redActivityRulePage.do");        //获取 红包活动规则页面

        totallyAnonymousUrl.add("getOauth.do");                   //获取 整合openId的对外链接
        totallyAnonymousUrl.add("getToOauthUrl.do");              //获取 微信整合链接
        totallyAnonymousUrl.add("unifiedOrderPay.do");            //微信统一下订单
        totallyAnonymousUrl.add("batchGetMaterial.do");           //批量获取 公众号文章
        totallyAnonymousUrl.add("receviceAndSendCustomMessage.do");//客服消息
        totallyAnonymousUrl.add("getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber.do");

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Map<String, String> paramMap = HttpUtil.getRequestParams(req);
        String grayStatus = "0";
        try {
            //获取请求用户的基本信息
            String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
            if (!"".equals(uid)) {
                Map<String, String> userParamMap = Maps.newHashMap();
                userParamMap.put("uid", uid);
                ResultDTO userResultDTO = wxUserHandler.getSimpleUserByCondition(0, userParamMap);
                List<Map<String, String>> userList = userResultDTO.getResultList();
                if (userList != null && userList.size() > 0) {
                    for (Map<String, String> userMap : userList) {
                        String grayStatusTemp = userMap.get("grayStatus");
                        if (grayStatusTemp != null && !"".equals(grayStatusTemp)) {
                            grayStatus = userMap.get("grayStatus");
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("在sessionFilter中获取单一的字典-doFilter is error, paramMap : " + paramMap + ", e : " + e);
        }

        //fortest
        OutputStream out = response.getOutputStream();

        String sessionKey = getSessionKey(req);
        //过滤方法名
        String url = req.getRequestURL().toString();
        String key = url.substring(url.lastIndexOf("/") + 1).trim();

        logger.info("请求地址： url = " + url);

        com.br.newMall.api.dto.BoolDTO boolDTO;
        if ("1".equals(grayStatus)) {
            // 完全不需要验证页面
            chainDoFilter(req, res, out, chain);
        } else {
            if (totallyAnonymousUrl.contains(key)) {
                // 完全不需要验证页面
                chainDoFilter(req, res, out, chain);
            } else {
                if (sessionKey != null) {
                    boolDTO = validateSessionKey(paramMap);
                    if (boolDTO.isSuccess() && boolDTO.isValue()) {
                        // 验证token用过
                        chainDoFilter(req, res, out, chain);
                        return;
                    } else {
                        writeAjaxResponse(out, req, res, boolDTO.getCode(), boolDTO.getMessage(), boolDTO.getMessage());
                        logger.warn("Check sessionKey failed, paramMap is " + paramMap);
                    }
                }
                if (anonymousUrl.contains(key)) {
                    // 不需要验证页面
                    chainDoFilter(req, res, out, chain);
                } else {
                    writeAjaxResponse(out, req, res, NewMallCode.SESSION_KEY_IS_NOT_NULL.getNo(), NewMallCode.SESSION_KEY_IS_NOT_NULL.getMessage(), NewMallCode.SESSION_KEY_IS_NOT_NULL.getMessage());
                    logger.info("请求地址： url = " + url);
                    logger.warn("Not anonymous url, paramMap is " + paramMap);
                }
            }
        }
    }

    @Override
    public void destroy() {
    }

    //chain with jsonp
    private void chainDoFilter(ServletRequest request, ServletResponse response, OutputStream out, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if ("post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
//          httpServletResponse.addHeader("Access-Control-Allow-Origin", "https://d1.shuqudata.com");             //日常
            httpServletResponse.addHeader("Access-Control-Allow-Origin", "https://www.rongshu.cn");             //线上
        }

        String callback = request.getParameter("callback");
        if (StringUtils.isNotEmpty(callback)) {
            out.write((callback + "(").getBytes());
            chain.doFilter(request, response);
            out.write(")".getBytes());
        } else {
            chain.doFilter(request, response);
        }
        out.close();
    }

    private String getSessionKey(HttpServletRequest req) {
        //app请求的时候通过sessionId参数名获取
        String sessionKey = req.getHeader("sessionKey");
        if (sessionKey == null) {
            sessionKey = req.getParameter("sessionKey");
        }
        return sessionKey == null || sessionKey.isEmpty() ? null : sessionKey;
    }

    private com.br.newMall.api.dto.BoolDTO validateSessionKey(Map<String, String> paramMap) {
        com.br.newMall.api.dto.BoolDTO boolDTO = new com.br.newMall.api.dto.BoolDTO();
        try {
            boolDTO = wxUserHandler.checkSession(0, paramMap);
            logger.info("Validate validateSessionKey, paramMap is " + paramMap);
        } catch (TException e) {
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        return boolDTO;
    }

    // 验证失败, 直接返回用户失败结果
    private String writeAjaxResponse(OutputStream out, HttpServletRequest request, HttpServletResponse response, int code, String message, String debugMessage) {
        Map<String, Object> map = new HashMap<>();
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json");
            map.put("code", code);
            map.put("message", message);
//          map.put("debugMessage", debugMessage);
            if (StringUtils.isNotEmpty(request.getParameter("callback"))) {
                String callback = request.getParameter("callback");
                logger.info("Callback : " + callback + "(" + JSONObject.toJSONString(map) + ")");
                out.write((callback + "(" + JSONObject.toJSONString(map) + ")").getBytes());
            } else {
                logger.info("Callback : " + JSONObject.toJSONString(map));
                out.write(JSONObject.toJSONString(map).getBytes());
            }
            out.close();
        } catch (IOException e) {
            logger.error("Write ajax response error : ", e);
        }
        return null;
    }
}
