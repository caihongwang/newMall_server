package com.br.newMall.web.utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by caihongwang on 2018/1/23.
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 获取Http请求中的参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = Maps.newHashMap();
        //获取请求参数能够获取到并解析
        String characterEncoding = (String) request.getCharacterEncoding();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paramKey = (String) enu.nextElement();
            if (paramKey != null && !"".equals(paramKey)) {
                String paramValue = request.getParameter(paramKey);
                if (paramValue != null) {
                    try {
                        //暂时不对数据进行解码，前端目前没有进行编码
//                        paramValue = URLDecoder.decode(paramValue, characterEncoding);
                        paramMap.put(paramKey, paramValue);
                    } catch (Exception e) {
                        //paramValue 中含有 特殊字符未进行编码，比如%等符号
                        paramMap.put(paramKey, paramValue);
                        logger.error("Exception occurred during getRequestParams, paramKey={}, paramValue={}.", paramKey, paramValue, e);
                        continue;
                    }
                }
            }
        }
        paramMap.put("requestUrl", request.getRequestURL().toString());
        paramMap.put("remoteAddr", request.getRemoteAddr().toString());
        paramMap.put("localAddr", request.getLocalAddr().toString());
        return paramMap;
    }

    /**
     * 获取真实的ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
