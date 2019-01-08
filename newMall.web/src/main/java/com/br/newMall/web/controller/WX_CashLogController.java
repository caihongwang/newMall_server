package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_CashLogHandler;
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
 * @Description 提现日志Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxCashLog", produces = "application/json;charset=utf-8")
public class WX_CashLogController {

    private static final Logger logger = LoggerFactory.getLogger(WX_CashLogController.class);

    @Autowired
    private WX_CashLogHandler.Client wxCashLogHandler;

    /**
     * 获取提现规则列表
     * @param request
     * @return
     */
    @RequestMapping("/getCashFeeList")
    @ResponseBody
    public Map<String, Object> getCashFeeList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取提现规则列表-getCashFeeList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxCashLogHandler.getCashFeeList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取提现规则列表-getCashFeeList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取提现规则列表-getCashFeeList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 添加提现日志
     * @param request
     * @return
     */
    @RequestMapping("/addCashLog")
    @ResponseBody
    public Map<String, Object> addCashLog(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】添加提现日志-addCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxCashLogHandler.addCashLog(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】添加提现日志-addCashLog is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】添加提现日志-addCashLog,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除提现日志
     * @param request
     * @return
     */
    @RequestMapping("/deleteCashLog")
    @ResponseBody
    public Map<String, Object> deleteCashLog(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】删除提现日志-deleteCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxCashLogHandler.deleteCashLog(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】删除提现日志-deleteCashLog is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】删除提现日志-deleteCashLog,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改提现日志
     * @param request
     * @return
     */
    @RequestMapping("/updateCashLog")
    @ResponseBody
    public Map<String, Object> updateCashLog(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】修改提现日志-updateCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxCashLogHandler.updateCashLog(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】修改提现日志-updateCashLog is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】修改提现日志-updateCashLog,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的提现日志
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleCashLogByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleCashLogByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】获取单一的提现日志-getSimpleCashLogByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxCashLogHandler.getSimpleCashLogByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】获取单一的提现日志-getSimpleCashLogByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】获取单一的提现日志-getSimpleCashLogByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 提现用户余额到微信零钱
     * @param request
     * @return
     */
    @RequestMapping("/cashBalanceToWx")
    @ResponseBody
    public Map<String, Object> cashBalanceToWx(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("【controller】提现用户余额到微信零钱-cashBalanceToWx,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxCashLogHandler.cashBalanceToWx(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("【controller】提现用户余额到微信零钱-cashBalanceToWx is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【controller】提现用户余额到微信零钱-cashBalanceToWx,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
