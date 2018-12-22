package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.service.WX_AddressHandler;
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
 * @Description 地址Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxAddress", produces = "application/json;charset=utf-8")
public class WX_AddressController {

    private static final Logger logger = LoggerFactory.getLogger(WX_AddressController.class);

    @Autowired
    private WX_AddressHandler.Client wxAddressHandler;

    /**
     * 默认获取中国省份列表
     * @param request
     * @return
     */
    @RequestMapping("/getProvinceList")
    @ResponseBody
    public Map<String, Object> getProvinceList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中默认获取中国省份列表-getProvinceList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxAddressHandler.getProvinceList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中默认获取中国省份列表-getProvinceList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中默认获取中国省份列表-getProvinceList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 根据省份ID获取城市列表
     * @param request
     * @return
     */
    @RequestMapping("/getCityList")
    @ResponseBody
    public Map<String, Object> getCityList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中根据省份ID获取城市列表-getCityList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxAddressHandler.getCityList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中根据省份ID获取城市列表-getCityList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中根据省份ID获取城市列表-getCityList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 根据城市ID获取地区列表
     * @param request
     * @return
     */
    @RequestMapping("/getRegionList")
    @ResponseBody
    public Map<String, Object> getRegionList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中根据城市ID获取地区列表-getRegionList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxAddressHandler.getRegionList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中根据城市ID获取地区列表-getRegionList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中根据城市ID获取地区列表-getRegionList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 根据地区ID获取街道列表
     * @param request
     * @return
     */
    @RequestMapping("/getStreetList")
    @ResponseBody
    public Map<String, Object> getStreetList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中根据地区ID获取街道列表-getStreetList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxAddressHandler.getStreetList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中根据地区ID获取街道列表-getStreetList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中根据地区ID获取街道列表-getStreetList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 添加地址
     * @param request
     * @return
     */
    @RequestMapping("/addAddress")
    @ResponseBody
    public Map<String, Object> addAddress(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中添加地址-addAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxAddressHandler.addAddress(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中添加地址-addAddress is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中添加地址-addAddress,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除地址
     * @param request
     * @return
     */
    @RequestMapping("/deleteAddress")
    @ResponseBody
    public Map<String, Object> deleteAddress(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中删除地址-deleteAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxAddressHandler.deleteAddress(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中删除地址-deleteAddress is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中删除地址-deleteAddress,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改地址
     * @param request
     * @return
     */
    @RequestMapping("/updateAddress")
    @ResponseBody
    public Map<String, Object> updateAddress(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中修改地址-updateAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxAddressHandler.updateAddress(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中修改地址-updateAddress is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中修改地址-updateAddress,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的地址
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleAddressByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleAddressByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取单一的地址-getSimpleAddressByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxAddressHandler.getSimpleAddressByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取单一的地址-getSimpleAddressByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取单一的地址-getSimpleAddressByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
