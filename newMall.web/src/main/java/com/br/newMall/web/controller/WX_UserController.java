package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.MessageDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_UserHandler;
import com.br.newMall.web.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 用户Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxUser", produces = "application/json;charset=utf-8")
public class WX_UserController {

    private static final Logger logger = LoggerFactory.getLogger(WX_UserController.class);

    @Autowired
    private WX_UserHandler.Client wxUserHandler;

    /**
     * 获取用户的基本信息
     * @param request
     * @return
     */
    @RequestMapping("/getUserBaseInfo")
    @ResponseBody
    public Map<String, Object> getUserBaseInfo(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取用户的基本信息-getUserBaseInfo,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxUserHandler.getUserBaseInfo(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
            resultMap.put("data", resultMapDTO.getResultMap());
        } catch (Exception e) {
            logger.error("在【controller】中获取用户的基本信息-getUserBaseInfo is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取用户的基本信息-getUserBaseInfo,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 登录(首次微信授权，则使用openId创建用户)
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中登录(首次微信授权，则使用openId创建用户)-login,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxUserHandler.login(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
            resultMap.put("data", resultMapDTO.getResultMap());
        } catch (Exception e) {
            logger.error("在【controller】中登录(首次微信授权，则使用openId创建用户)-login is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中登录(首次微信授权，则使用openId创建用户)-login,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public Map<String, Object> updateUser(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中更新用户信息-updateUser,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxUserHandler.updateUser(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中更新用户信息-updateUser is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中更新用户信息-updateUser,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 检测用户会话是否过期
     * @param request
     * @return
     */
    @RequestMapping("/checkSession")
    @ResponseBody
    public Map<String, Object> checkSession(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中检测用户会话是否过期-checkSession,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxUserHandler.checkSession(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中检测用户会话是否过期-checkSession is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中检测用户会话是否过期-checkSession,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 校验手机验证码-[暂时未使用]
     * @param request
     * @return
     */
    @RequestMapping("/getCheckVerificationCode")
    @ResponseBody
    public Map<String, Object> getCheckVerificationCode(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中校验手机验证码-getCheckVerificationCode,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            MessageDTO messageDTO = wxUserHandler.getCheckVerificationCode(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", messageDTO.getCode());
            resultMap.put("message", messageDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中校验手机验证码-getCheckVerificationCode is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中校验手机验证码-getCheckVerificationCode,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
