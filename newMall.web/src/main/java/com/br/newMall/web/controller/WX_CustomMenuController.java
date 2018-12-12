package com.br.newMall.web.controller;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_CustomMenuHandler;
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

@Controller
@RequestMapping(value = "/wx_CustomMenu", produces = "application/json;charset=utf-8")
public class WX_CustomMenuController {

    private static final Logger logger = LoggerFactory.getLogger(WX_CustomMenuController.class);

    @Autowired
    private WX_CustomMenuHandler.Client wx_CustomMenuHandler;

    @RequestMapping("/getCustomMenu")
    @ResponseBody
    public Map<String, Object> getCustomMenu(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取公众号自定义菜单-getCustomMenu,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.getCustomMenu(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取公众号自定义菜单-getCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取公众号自定义菜单-getCustomMenu,响应-response:" + resultMap);
        return resultMap;
    }


    @RequestMapping("/createCustomMenu")
    @ResponseBody
    public Map<String, Object> createCustomMenu(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中创建公众号自定义菜单-createCustomMenu,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.createCustomMenu(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中创建公众号自定义菜单-createCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中创建公众号自定义菜单-createCustomMenu,响应-response:" + resultMap);
        return resultMap;
    }


    @RequestMapping("/deleteCustomMenu")
    @ResponseBody
    public Map<String, Object> deleteCustomMenu(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中删除公众号自定义菜单-deleteCustomMenu,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.deleteCustomMenu(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中删除公众号自定义菜单-deleteCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中删除公众号自定义菜单-deleteCustomMenu,响应-response:" + resultMap);
        return resultMap;
    }

    @RequestMapping("/createPersonalMenu")
    @ResponseBody
    public Map<String, Object> createPersonalMenu(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中创建公众号个性化菜单-createPersonalMenu,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.createPersonalMenu(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中创建公众号个性化菜单-createPersonalMenu is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中创建公众号个性化菜单-createPersonalMenu,响应-response:" + resultMap);
        return resultMap;
    }


    @RequestMapping("/deletePersonalMenu")
    @ResponseBody
    public Map<String, Object> deletePersonalMenu(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中删除公众号个性化菜单-deletePersonalMenu,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.deletePersonalMenu(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中删除公众号个性化菜单-deletePersonalMenu is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中删除公众号个性化菜单-deletePersonalMenu,响应-response:" + resultMap);
        return resultMap;
    }

    @RequestMapping("/getCurrentSelfMenuInfo")
    @ResponseBody
    public Map<String, Object> getCurrentSelfMenuInfo(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_CustomMenuHandler.getCurrentSelfMenuInfo(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,响应-response:" + resultMap);
        return resultMap;
    }

}
