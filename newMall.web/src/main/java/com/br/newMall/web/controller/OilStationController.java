package com.br.newMall.web.controller;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.OilStationHandler;
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
@RequestMapping(value = "/oilStation", produces = "application/json;charset=utf-8")
public class OilStationController {

    private static final Logger logger = LoggerFactory.getLogger(OilStationController.class);

    @Autowired
    private OilStationHandler.Client oilStationHandler;

    @RequestMapping("/addOrUpdateOilStation")
    @ResponseBody
    public Map<String, Object> addOrUpdateOilStation(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中添加或者更新加油站-addOrUpdateOilStation,请求-paramMap:" + paramMap);
        try {
            BoolDTO boolDTO = oilStationHandler.addOrUpdateOilStation(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中添加或者更新加油站-addOrUpdateOilStation is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中添加或者更新加油站-addOrUpdateOilStation,响应-response:" + resultMap);
        return resultMap;
    }

    @RequestMapping("/getOilStationList")
    @ResponseBody
    public Map<String, Object> getOilStationList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取加油站列表-getOilStationList,请求-paramMap:" + paramMap);
        try {
            ResultDTO resultDTO = oilStationHandler.getOilStationList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取加油站列表-getOilStationList is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取加油站列表-getOilStationList,响应-response:" + resultMap);
        return resultMap;
    }

    @RequestMapping("/getOilStationByLonLat")
    @ResponseBody
    public Map<String, Object> getOilStationByLonLat(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中根据经纬度地址获取所处的加油站-getOilStationByLonLat,请求-paramMap:" + paramMap);
        try {
            ResultDTO resultDTO = oilStationHandler.getOilStationByLonLat(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中根据经纬度地址获取所处的加油站-getOilStationByLonLat is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中根据经纬度地址获取所处的加油站-getOilStationByLonLat,响应-response:" + resultMap);
        return resultMap;
    }

    @RequestMapping("/getOneOilStationByCondition")
    @ResponseBody
    public Map<String, Object> getOneOilStationByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取一个加油站信息-getOneOilStationByCondition,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = oilStationHandler.getOneOilStationByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取一个加油站信息-getOneOilStationByCondition is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取一个加油站信息-getOneOilStationByCondition,响应-response:" + resultMap);
        return resultMap;
    }

}
