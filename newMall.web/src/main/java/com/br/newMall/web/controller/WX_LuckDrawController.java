package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_LuckDrawHandler;
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
 * @Description 抽奖Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxLuckDraw", produces = "application/json;charset=utf-8")
public class WX_LuckDrawController {

    private static final Logger logger = LoggerFactory.getLogger(WX_LuckDrawController.class);

    @Autowired
    private WX_LuckDrawHandler.Client wxLuckDrawHandler;

    /**
     * 获取抽奖的产品列表
     * @param request
     * @return
     */
    @RequestMapping("/getLuckDrawProductList")
    @ResponseBody
    public Map<String, Object> getLuckDrawProductList(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取抽奖的产品列表-getLuckDrawProductList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxLuckDrawHandler.getLuckDrawProductList(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取抽奖的产品列表-getLuckDrawProductList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取抽奖的产品列表-getLuckDrawProductList,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 添加抽奖信息
     * @param request
     * @return
     */
    @RequestMapping("/addLuckDraw")
    @ResponseBody
    public Map<String, Object> addLuckDraw(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中添加抽奖信息-addLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxLuckDrawHandler.addLuckDraw(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中添加抽奖信息-addLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中添加抽奖信息-addLuckDraw,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除抽奖信息
     * @param request
     * @return
     */
    @RequestMapping("/deleteLuckDraw")
    @ResponseBody
    public Map<String, Object> deleteLuckDraw(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中删除抽奖信息-deleteLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxLuckDrawHandler.deleteLuckDraw(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中删除抽奖信息-deleteLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中删除抽奖信息-deleteLuckDraw,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改抽奖信息
     * @param request
     * @return
     */
    @RequestMapping("/updateLuckDraw")
    @ResponseBody
    public Map<String, Object> updateLuckDraw(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中修改抽奖信息-updateLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxLuckDrawHandler.updateLuckDraw(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中修改抽奖信息-updateLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中修改抽奖信息-updateLuckDraw,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的抽奖信息
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleLuckDrawByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleLuckDrawByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取单一的抽奖信息-getSimpleLuckDrawByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxLuckDrawHandler.getSimpleLuckDrawByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取单一的抽奖信息-getSimpleLuckDrawByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取单一的抽奖信息-getSimpleLuckDrawByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取某商家下待领取奖励的队列
     * @param request
     * @return
     */
    @RequestMapping("/getWaitLuckDrawRankByCondition")
    @ResponseBody
    public Map<String, Object> getWaitLuckDrawRankByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultMapDTO resultMapDTO = wxLuckDrawHandler.getWaitLuckDrawRankByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取某商家下已领取奖励的队列
     * @param request
     * @return
     */
    @RequestMapping("/getRecevicedLuckDrawRankByCondition")
    @ResponseBody
    public Map<String, Object> getRecevicedLuckDrawRankByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取某商家下已领取奖励的队列-getRecevicedLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultMapDTO resultMapDTO = wxLuckDrawHandler.getRecevicedLuckDrawRankByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取某商家下已领取奖励的队列-getRecevicedLuckDrawRankByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取某商家下已领取奖励的队列-getRecevicedLuckDrawRankByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取某商家下所有参与过领取奖励的队列
     * @param request
     * @return
     */
    @RequestMapping("/getAllLuckDrawRankByCondition")
    @ResponseBody
    public Map<String, Object> getAllLuckDrawRankByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取某商家下所有参与过领取奖励的队列-getAllLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultMapDTO resultMapDTO = wxLuckDrawHandler.getAllLuckDrawRankByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取某商家下所有参与过领取奖励的队列-getAllLuckDrawRankByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取某商家下所有参与过领取奖励的队列-getAllLuckDrawRankByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取待领取奖励的商家列表
     * @param request
     * @return
     */
    @RequestMapping("/getWaitLuckDrawShopByCondition")
    @ResponseBody
    public Map<String, Object> getWaitLuckDrawShopByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxLuckDrawHandler.getWaitLuckDrawShopByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取已领取奖励的商家列表
     * @param request
     * @return
     */
    @RequestMapping("/getRecevicedLuckDrawShopByCondition")
    @ResponseBody
    public Map<String, Object> getRecevicedLuckDrawShopByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取已领取奖励的商家列表-getRecevicedLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxLuckDrawHandler.getRecevicedLuckDrawShopByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取已领取奖励的商家列表-getRecevicedLuckDrawShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取已领取奖励的商家列表-getRecevicedLuckDrawShopByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取参加过抽奖的商家列表
     * @param request
     * @return
     */
    @RequestMapping("/getAllLuckDrawShopByCondition")
    @ResponseBody
    public Map<String, Object> getAllLuckDrawShopByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取参加过抽奖的商家列表-getAllLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxLuckDrawHandler.getAllLuckDrawShopByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取参加过抽奖的商家列表-getAllLuckDrawShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取参加过抽奖的商家列表-getAllLuckDrawShopByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 兑换积分
     * @param request
     * @return
     */
    @RequestMapping("/convertIntegral")
    @ResponseBody
    public Map<String, Object> convertIntegral(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中兑换积分-convertIntegral,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxLuckDrawHandler.convertIntegral(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中兑换积分-convertIntegral is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中兑换积分-convertIntegral,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }
}
