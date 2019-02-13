package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_FoodHandler;
import com.br.newMall.web.utils.HttpUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 食物Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxFood", produces = "application/json;charset=utf-8")
public class WX_FoodController {

    private static final Logger logger = LoggerFactory.getLogger(WX_FoodController.class);

    @Autowired
    private WX_FoodHandler.Client wxFoodHandler;

    /**
     * 添加食物
     * @param request
     * @return
     */
    @RequestMapping("/addFood")
    @ResponseBody
    public Map<String, Object> addFood(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】添加食物-addFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxFoodHandler.addFood(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】添加食物-addFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】添加食物-addFood,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除食物
     * @param request
     * @return
     */
    @RequestMapping("/deleteFood")
    @ResponseBody
    public Map<String, Object> deleteFood(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】删除食物-deleteFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxFoodHandler.deleteFood(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】删除食物-deleteFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】删除食物-deleteFood,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改食物
     * @param request
     * @return
     */
    @RequestMapping("/updateFood")
    @ResponseBody
    public Map<String, Object> updateFood(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】修改食物-updateFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxFoodHandler.updateFood(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】修改食物-updateFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】修改食物-updateFood,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的食物
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleFoodByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleFoodByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取单一的食物-getSimpleFoodByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxFoodHandler.getSimpleFoodByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取单一的食物-getSimpleFoodByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取单一的食物-getSimpleFoodByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    @RequestMapping("/getMenuByCondition")
    @ResponseBody
    public Map<String, Object> getMenuByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取菜单食物列表-getMenuByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxFoodHandler.getMenuByCondition(0, paramMap);
            Map<String, Object> dataMap = Maps.newHashMap();
            Map<String, String> resultStrMap = resultMapDTO.getResultMap();
            for (Map.Entry<String, String> data : resultStrMap.entrySet()) {
                String key = data.getKey();
                String value = data.getValue();
                Map<String, Object> tempMap = Maps.newHashMap();
                tempMap = JSONObject.parseObject(value, Map.class);
//                String menuListStr = tempMap.get("menuList") != null ?
//                        tempMap.get("menuList").toString() : "";
//                List<Map<String, Object>> menuList = JSONObject.parseObject(menuListStr, List.class);
//                tempMap.put("menuList", menuList);
                dataMap.put(key, tempMap);
            }
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", dataMap);
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取菜单食物列表-getMenuByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取菜单食物列表-getMenuByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }
}
