package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_DicHandler;
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
 * @Description 字典Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxDic", produces = "application/json;charset=utf-8")
public class WX_DicController {

    private static final Logger logger = LoggerFactory.getLogger(WX_DicController.class);

    @Autowired
    private WX_DicHandler.Client wxDicHandler;

    /**
     * 添加字典
     * @param request
     * @return
     */
    @RequestMapping("/addDic")
    @ResponseBody
    public Map<String, Object> addDic(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中添加字典-addDic,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxDicHandler.addDic(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中添加字典-addDic is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中添加字典-addDic,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除字典
     * @param request
     * @return
     */
    @RequestMapping("/deleteDic")
    @ResponseBody
    public Map<String, Object> deleteDic(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中删除字典-deleteDic,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxDicHandler.deleteDic(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中删除字典-deleteDic is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中删除字典-deleteDic,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改字典
     * @param request
     * @return
     */
    @RequestMapping("/updateDic")
    @ResponseBody
    public Map<String, Object> updateDic(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中修改字典-updateDic,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxDicHandler.updateDic(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中修改字典-updateDic is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中修改字典-updateDic,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的字典
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleDicByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleDicByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取单一的字典-getSimpleDicByCondition,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxDicHandler.getSimpleDicByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取单一的字典-getSimpleDicByCondition is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取单一的字典-getSimpleDicByCondition,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    @RequestMapping("/getMoreDicByCondition")
    @ResponseBody
    public Map<String, Object> getMoreDicByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取多个的字典-getMoreDicByCondition,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxDicHandler.getMoreDicByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取多个的字典-getMoreDicByCondition is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取多个的字典-getMoreDicByCondition,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
