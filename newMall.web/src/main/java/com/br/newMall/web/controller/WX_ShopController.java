package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_ShopHandler;
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
 * @Description 店铺Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxShop", produces = "application/json;charset=utf-8")
public class WX_ShopController {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopController.class);

    @Autowired
    private WX_ShopHandler.Client wxShopHandler;

    /**
     * 获取订单排序类型
     * @param request
     * @return
     */
    @RequestMapping("/getOrderSortTypeList")
    @ResponseBody
    public Map<String, Object> getOrderSortTypeList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取订单排序类型-getOrderSortTypeList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxShopHandler.getOrderSortTypeList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取订单排序类型-getOrderSortTypeList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取订单排序类型-getOrderSortTypeList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 添加店铺
     * @param request
     * @return
     */
    @RequestMapping("/addShop")
    @ResponseBody
    public Map<String, Object> addShop(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】添加店铺-addShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxShopHandler.addShop(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】添加店铺-addShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】添加店铺-addShop,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除店铺
     * @param request
     * @return
     */
    @RequestMapping("/deleteShop")
    @ResponseBody
    public Map<String, Object> deleteShop(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】删除店铺-deleteShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxShopHandler.deleteShop(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】删除店铺-deleteShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】删除店铺-deleteShop,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改店铺
     * @param request
     * @return
     */
    @RequestMapping("/updateShop")
    @ResponseBody
    public Map<String, Object> updateShop(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】修改店铺-updateShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxShopHandler.updateShop(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】修改店铺-updateShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】修改店铺-updateShop,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的店铺
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleShopByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleShopByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        String start = paramMap.get("start")!=null?paramMap.get("start").toString():"";
        String size = paramMap.get("size")!=null?paramMap.get("size").toString():"";
        if("".equals(start)){
            paramMap.put("start", "0");
        } else {
            try {
                if(Integer.parseInt(start) < 0){
                    paramMap.put("start", "0");
                } else {
                    paramMap.put("start", start);
                }
            } catch (Exception e){
                paramMap.put("start", "0");
            }
        }
        if("".equals(size)){
            paramMap.put("size", "10");
        } else {
            try {
                if(Integer.parseInt(size) < 0){
                    paramMap.put("size", "10");
                } else {
                    paramMap.put("size", size);
                }
            } catch (Exception e){
                paramMap.put("size", "0");
            }
        }
        try {
            ResultDTO resultDTO = wxShopHandler.getSimpleShopByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取单一的店铺-getSimpleShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取单一的店铺-getSimpleShopByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 根据条件查询店铺相关信息
     * @param request
     * @return
     */
    @RequestMapping("/getShopByCondition")
    @ResponseBody
    public Map<String, Object> getShopByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】根据条件查询店铺相关信息-getShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        String start = paramMap.get("start")!=null?paramMap.get("start").toString():"";
        String size = paramMap.get("size")!=null?paramMap.get("size").toString():"";
        if("".equals(start)){
            paramMap.put("start", "0");
        } else {
            try {
                if(Integer.parseInt(start) < 0){
                    paramMap.put("start", "0");
                } else {
                    paramMap.put("start", start);
                }
            } catch (Exception e){
                paramMap.put("start", "0");
            }
        }
        if("".equals(size)){
            paramMap.put("size", "10");
        } else {
            try {
                if(Integer.parseInt(size) < 0){
                    paramMap.put("size", "10");
                } else {
                    paramMap.put("size", size);
                }
            } catch (Exception e){
                paramMap.put("size", "0");
            }
        }
        try {
            ResultDTO resultDTO = wxShopHandler.getShopByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】根据条件查询店铺相关信息-getShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】中根据条件查询店铺相关信息获取单一的店铺-getShopByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 根据条件查询店铺相关信息
     * @param request
     * @return
     */
    @RequestMapping("/getMiniProgramCode")
    @ResponseBody
    public Map<String, Object> getMiniProgramCode(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxShopHandler.getMiniProgramCode(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultMapSize());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
