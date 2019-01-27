package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_IntegralLogService;
import com.br.newMall.center.service.WX_LuckDrawService;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_AddressDao;
import com.br.newMall.dao.WX_LuckDrawDao;
import com.br.newMall.dao.WX_OrderDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 抽奖Service
 * @author caihongwang
 */
@Service
public class WX_LuckDrawServiceImpl implements WX_LuckDrawService {

    private static final Logger logger = LoggerFactory.getLogger(WX_LuckDrawServiceImpl.class);

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_LuckDrawDao wxLuckDrawDao;

    @Autowired
    private WX_OrderDao wxOrderDao;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_IntegralLogService wxIntegralLogService;

    /**
     * 获取抽奖的产品列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getLuckDrawProductList(Map<String, Object> paramMap) {
        logger.info("【service】获取抽奖的产品列表-getLuckDrawProductList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "luckDraw";
        if(!"".equals(dicType)){
            paramMap.put("dicType", dicType);
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.LUCKDRAW_LUCKDRAWTYPE_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_LUCKDRAWTYPE_IS_NULL.getMessage());
        }
        logger.info("【service】获取抽奖的产品列表-getLuckDrawProductList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 抽奖
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getLuckDraw(Map<String, Object> paramMap) {
        logger.info("【service】抽奖-getLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        String luckDrawCode = "";
        if (!"".equals(uid) && !"".equals(wxOrderId)) {
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("uid", uid);
            orderMap.put("wxOrderId", wxOrderId);
            List<Map<String, Object>> orderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(orderList != null && orderList.size() > 0){
                String orderStatus = orderList.get(0).get("status").toString();
                if("1".equals(orderStatus)){            //订单状态: 0是待支付，1是已支付
                    //检测当前用户的订单号是否已经抽过奖
                    Map<String, Object> LuckDrawMap = Maps.newHashMap();
                    LuckDrawMap.put("uid", uid);
                    LuckDrawMap.put("wxOrderId", wxOrderId);
                    List<Map<String, Object>> currentUserLuckDrawList = wxLuckDrawDao.getSimpleLuckDrawByCondition(LuckDrawMap);
                    if(currentUserLuckDrawList == null
                            || currentUserLuckDrawList.size() <= 0){
                        //获取所有抽奖产品，并按照起概率来决定哪一个中奖
                        Map<String, Object> LuckDrawProductParamMap = Maps.newHashMap();
                        LuckDrawProductParamMap.put("dicType", "luckDraw");
                        List<Map<String, String>> luckDrawProductList = getLuckDrawProductList(LuckDrawProductParamMap).getResultList();
                        if(luckDrawProductList != null && luckDrawProductList.size() > 0){
                            //根据抽奖概率选出抽到的奖品
                            Double currentProbability = Math.random()*100;                 //当前openId对应的微信用户的中间概率
                            for (Map<String, String> luckDrawProductMap: luckDrawProductList) {
                                String probabilityMinStr = luckDrawProductMap.get("probabilityMin")!=null?luckDrawProductMap.get("probabilityMin").toString():"0%";
                                String[] probabilityMinArr = probabilityMinStr.split("%");
                                String probabilityMaxStr = luckDrawProductMap.get("probabilityMax")!=null?luckDrawProductMap.get("probabilityMax").toString():"0%";
                                String[] probabilityMaxArr = probabilityMaxStr.split("%");
                                if(probabilityMinArr.length >= 1 && probabilityMaxArr.length >= 1){
                                    Double probabilityMinNum = Double.parseDouble(probabilityMinArr[0]);
                                    Double probabilityMaxNum = Double.parseDouble(probabilityMaxArr[0]);
                                    if(currentProbability > probabilityMinNum
                                            && currentProbability <= probabilityMaxNum){
                                        luckDrawCode = luckDrawProductMap.get("luckDrawCode");
                                        resultMap.putAll(luckDrawProductMap);
                                        resultMap.remove("probabilityMin");
                                        resultMap.remove("probabilityMax");
                                        logger.info("当前微信用户 uid = " + uid +
                                                " 在交易完订单 wxOrderId = " + wxOrderId +
                                                " 抽到的奖品 luckDrawName = "+ luckDrawProductMap.get("luckDrawName") );
                                        break;
                                    }
                                }
                            }
                            if(!"".equals(luckDrawCode)){
                                paramMap.put("luckDrawCode", luckDrawCode);
                                paramMap.put("status", "0");        //抽奖状态，0是未发放，1是已发放，2是已删除
                                try{
                                    addNum = wxLuckDrawDao.addLuckDraw(paramMap);
                                    if (addNum != null && addNum > 0) {
                                        resultMapDTO.setResultMap(resultMap);
                                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                                    } else {
                                        resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                                        resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                                    }
                                } catch (Exception e) {
                                    logger.info("当前用户 uid = " + uid +
                                            " , 完成微信订单 wxOrderId = " + wxOrderId +
                                            "后，已抽过奖。如想再次抽奖，请再交易一笔订单。此异常可忽略.");
                                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_GETPRIZE_HAS_GETED.getNo());
                                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_GETPRIZE_HAS_GETED.getMessage());
                                }
                            } else {
                                resultMapDTO.setCode(NewMallCode.LUCKDRAW_GETPRIZE_IS_FAILED.getNo());
                                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_GETPRIZE_IS_FAILED.getMessage());
                            }
                        } else {
                            resultMapDTO.setCode(NewMallCode.LUCKDRAW_PRODUCT_IS_NULL.getNo());
                            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_PRODUCT_IS_NULL.getMessage());
                        }
                    } else {
                        Map<String, Object> currentUserLuckDraw = currentUserLuckDrawList.get(0);
                        resultMapDTO.setResultMap(MapUtil.getStringMap(currentUserLuckDraw));
                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_ORDER_IS_NOT_PAYED.getNo());
                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_ORDER_IS_NOT_PAYED.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getMessage());
        }
        logger.info("【service】抽奖-getLuckDraw,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 添加抽奖信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO addLuckDraw(Map<String, Object> paramMap) {
        logger.info("【service】添加抽奖信息-addLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        String luckDrawCode = "";
        if (!"".equals(uid) && !"".equals(wxOrderId)) {
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            List<Map<String, Object>> orderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(orderList != null && orderList.size() > 0){
                String orderStatus = orderList.get(0).get("status").toString();
                if("1".equals(orderStatus)){            //订单状态: 0是待支付，1是已支付
                    //获取所有抽奖产品，并按照起概率来决定哪一个中奖
                    Map<String, Object> LuckDrawProductParamMap = Maps.newHashMap();
                    LuckDrawProductParamMap.put("dicType", "luckDraw");
                    List<Map<String, String>> luckDrawProductList = getLuckDrawProductList(LuckDrawProductParamMap).getResultList();
                    if(luckDrawProductList != null && luckDrawProductList.size() > 0){
                        //根据抽奖概率选出抽到的奖品
                        Double currentProbability = Math.random()*100;                 //当前openId对应的微信用户的中间概率
                        for (Map<String, String> luckDrawProductMap: luckDrawProductList) {
                            String probabilityMinStr = luckDrawProductMap.get("probabilityMin")!=null?luckDrawProductMap.get("probabilityMin").toString():"0%";
                            String[] probabilityMinArr = probabilityMinStr.split("%");
                            String probabilityMaxStr = luckDrawProductMap.get("probabilityMax")!=null?luckDrawProductMap.get("probabilityMax").toString():"0%";
                            String[] probabilityMaxArr = probabilityMaxStr.split("%");
                            if(probabilityMinArr.length >= 1 && probabilityMaxArr.length >= 1){
                                Double probabilityMinNum = Double.parseDouble(probabilityMinArr[0]);
                                Double probabilityMaxNum = Double.parseDouble(probabilityMaxArr[0]);
                                if(currentProbability > probabilityMinNum
                                        && currentProbability <= probabilityMaxNum){
                                    luckDrawCode = luckDrawProductMap.get("luckDrawCode");
                                    resultMap.putAll(luckDrawProductMap);
                                    resultMap.remove("probabilityMin");
                                    resultMap.remove("probabilityMax");
                                    logger.info("当前微信用户 uid = " + uid +
                                            " 在交易完订单 wxOrderId = " + wxOrderId +
                                            " 抽到的奖品 luckDrawName = "+ luckDrawProductMap.get("luckDrawName") );
                                    break;
                                }
                            }
                        }
                        if(!"".equals(luckDrawCode)){
                            paramMap.put("luckDrawCode", luckDrawCode);
                            paramMap.put("status", "0");        //抽奖状态，0是未发放，1是已发放，2是已删除
                            try{
                                addNum = wxLuckDrawDao.addLuckDraw(paramMap);
                                if (addNum != null && addNum > 0) {
                                    resultMapDTO.setResultMap(resultMap);
                                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                                } else {
                                    resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                                    resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                                }
                            } catch (Exception e) {
                                logger.info("当前用户 uid = " + uid +
                                        " , 完成微信订单 wxOrderId = " + wxOrderId +
                                        "后，已抽过奖。如想再次抽奖，请再交易一笔订单。此异常可忽略.");
                                resultMapDTO.setCode(NewMallCode.LUCKDRAW_GETPRIZE_HAS_GETED.getNo());
                                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_GETPRIZE_HAS_GETED.getMessage());
                            }
                        } else {
                            resultMapDTO.setCode(NewMallCode.LUCKDRAW_GETPRIZE_IS_FAILED.getNo());
                            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_GETPRIZE_IS_FAILED.getMessage());
                        }
                    } else {
                        resultMapDTO.setCode(NewMallCode.LUCKDRAW_PRODUCT_IS_NULL.getNo());
                        resultMapDTO.setMessage(NewMallCode.LUCKDRAW_PRODUCT_IS_NULL.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_ORDER_IS_NOT_PAYED.getNo());
                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_ORDER_IS_NOT_PAYED.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getMessage());
        }
        logger.info("【service】添加抽奖信息-addLuckDraw,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 删除抽奖信息
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteLuckDraw(Map<String, Object> paramMap) {
        logger.info("【service】删除抽奖信息-deleteLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxLuckDrawDao.deleteLuckDraw(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.LUCKDRAW_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.LUCKDRAW_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】删除抽奖信息-deleteLuckDraw,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改抽奖信息
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateLuckDraw(Map<String, Object> paramMap) {
        logger.info("【service】修改抽奖信息-updateLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxLuckDrawDao.updateLuckDraw(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.LUCKDRAW_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.LUCKDRAW_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改抽奖信息-updateLuckDraw,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的抽奖信息信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleLuckDrawByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的抽奖信息-getSimpleLuckDrawByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> luckDrawStrList = Lists.newArrayList();
        List<Map<String, Object>> luckDrawList = wxLuckDrawDao.getSimpleLuckDrawByCondition(paramMap);
        if (luckDrawList != null && luckDrawList.size() > 0) {
            luckDrawStrList = MapUtil.getStringMapList(luckDrawList);
            Integer total = wxLuckDrawDao.getSimpleLuckDrawTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(luckDrawStrList);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
        }
        logger.info("【service】获取单一的抽奖信息-getSimpleLuckDrawByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取某商家下待领取奖励的队列
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getWaitLuckDrawRankByCondition(Map<String, Object> paramMap){
        logger.info("【service】获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        paramMap.put("status", "0");        //抽奖状态，0是未发放，1是已发放，2是已删除
        if(!"".equals(shopId) && !"".equals(uid)){
            List<Map<String, Object>> my_waitGetLuckDrawRankList = Lists.newArrayList();
            List<Map<String, Object>> all_waitGetLuckDrawRankList = wxLuckDrawDao.getLuckDrawRankByCondition(paramMap);
            if(all_waitGetLuckDrawRankList != null &&
                    all_waitGetLuckDrawRankList.size() > 0){
                //处理数据
                for (int i = 0; i < all_waitGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_waitGetLuckDrawRankList.get(i);
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", (i+1)+"");
                    waitGetLuckDrawMap.remove("luckDrawRule");
                }
                //准备数据
                //商家信息
                Map<String, String> shopMap = Maps.newHashMap();
                String shopTitle = all_waitGetLuckDrawRankList.get(0).get("shopTitle").toString();
                String shopHeadImgUrl = all_waitGetLuckDrawRankList.get(0).get("shopHeadImgUrl").toString();
                shopMap.put("shopTitle", shopTitle);
                shopMap.put("shopHeadImgUrl", shopHeadImgUrl);
                //当前用户的排队
                for (int i = 0; i < all_waitGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_waitGetLuckDrawRankList.get(i);
                    if(uid.equals(waitGetLuckDrawMap.get("uid").toString())){
                        my_waitGetLuckDrawRankList.add(waitGetLuckDrawMap);
                    }
                }
                //总数
                Integer total = wxLuckDrawDao.getLuckDrawRankTotalByCondition(paramMap);
                //当前所有的排队
                if((start+size) > total){
                    all_waitGetLuckDrawRankList = all_waitGetLuckDrawRankList.subList(start, total);
                } else {
                    all_waitGetLuckDrawRankList = all_waitGetLuckDrawRankList.subList(start, (start+size));
                }
                //整理回传参数
                resultMap.put("shopMap",
                        JSONObject.toJSONString(shopMap) );
                resultMap.put("my_waitGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(my_waitGetLuckDrawRankList)) );
                resultMap.put("all_waitGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(all_waitGetLuckDrawRankList)) );
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setResultListTotal(total);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取某商家下已领取奖励的队列
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getRecevicedLuckDrawRankByCondition(Map<String, Object> paramMap){
        logger.info("【service】获取某商家下已领取奖励的队列-getRecevicedLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        paramMap.put("status", "1");        //抽奖状态，0是未发放，1是已发放，2是已删除
        if(!"".equals(shopId) && !"".equals(uid)){
            List<Map<String, Object>> my_recevicedGetLuckDrawRankList = Lists.newArrayList();
            List<Map<String, Object>> all_recevicedGetLuckDrawRankList = wxLuckDrawDao.getLuckDrawRankByCondition(paramMap);
            if(all_recevicedGetLuckDrawRankList != null &&
                    all_recevicedGetLuckDrawRankList.size() > 0){
                //处理数据
                for (int i = 0; i < all_recevicedGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_recevicedGetLuckDrawRankList.get(i);
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", (i+1)+"");
                    waitGetLuckDrawMap.remove("luckDrawRule");
                }
                //准备数据
                //商家信息
                Map<String, String> shopMap = Maps.newHashMap();
                String shopTitle = all_recevicedGetLuckDrawRankList.get(0).get("shopTitle").toString();
                String shopHeadImgUrl = all_recevicedGetLuckDrawRankList.get(0).get("shopHeadImgUrl").toString();
                shopMap.put("shopTitle", shopTitle);
                shopMap.put("shopHeadImgUrl", shopHeadImgUrl);
                //当前用户的排队
                for (int i = 0; i < all_recevicedGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_recevicedGetLuckDrawRankList.get(i);
                    if(uid.equals(waitGetLuckDrawMap.get("uid").toString())){
                        my_recevicedGetLuckDrawRankList.add(waitGetLuckDrawMap);
                    }
                }
                //总数
                Integer total = wxLuckDrawDao.getLuckDrawRankTotalByCondition(paramMap);
                //当前所有的排队
                if((start+size) > total){
                    all_recevicedGetLuckDrawRankList = all_recevicedGetLuckDrawRankList.subList(start, total);
                } else {
                    all_recevicedGetLuckDrawRankList = all_recevicedGetLuckDrawRankList.subList(start, (start+size));
                }
                //整理回传参数
                resultMap.put("shopMap",
                        JSONObject.toJSONString(shopMap) );
                resultMap.put("my_recevicedGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(my_recevicedGetLuckDrawRankList)) );
                resultMap.put("all_recevicedGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(all_recevicedGetLuckDrawRankList)) );
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setResultListTotal(total);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取某商家下已领取奖励的队列-getRecevicedLuckDrawRankByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取某商家下所有参与过领取奖励的队列
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getAllLuckDrawRankByCondition(Map<String, Object> paramMap){
        logger.info("【service】获取某商家下所有参与过领取奖励的队列-getAllLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        if(!"".equals(shopId) && !"".equals(uid)){
            List<Map<String, Object>> my_allGetLuckDrawRankList = Lists.newArrayList();
            List<Map<String, Object>> all_allGetLuckDrawRankList = wxLuckDrawDao.getLuckDrawRankByCondition(paramMap);
            if(all_allGetLuckDrawRankList != null &&
                    all_allGetLuckDrawRankList.size() > 0){
                //处理数据
                for (int i = 0; i < all_allGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_allGetLuckDrawRankList.get(i);
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", (i+1)+"");
                    waitGetLuckDrawMap.remove("luckDrawRule");
                }
                //准备数据
                //商家信息
                Map<String, String> shopMap = Maps.newHashMap();
                String shopTitle = all_allGetLuckDrawRankList.get(0).get("shopTitle").toString();
                String shopHeadImgUrl = all_allGetLuckDrawRankList.get(0).get("shopHeadImgUrl").toString();
                shopMap.put("shopTitle", shopTitle);
                shopMap.put("shopHeadImgUrl", shopHeadImgUrl);
                //当前用户的排队
                for (int i = 0; i < all_allGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_allGetLuckDrawRankList.get(i);
                    if(uid.equals(waitGetLuckDrawMap.get("uid").toString())){
                        my_allGetLuckDrawRankList.add(waitGetLuckDrawMap);
                    }
                }
                //总数
                Integer total = wxLuckDrawDao.getLuckDrawRankTotalByCondition(paramMap);
                //当前所有的排队
                if((start+size) > total){
                    all_allGetLuckDrawRankList = all_allGetLuckDrawRankList.subList(start, total);
                } else {
                    all_allGetLuckDrawRankList = all_allGetLuckDrawRankList.subList(start, (start+size));
                }
                //整理回传参数
                resultMap.put("shopMap",
                        JSONObject.toJSONString(shopMap) );
                resultMap.put("my_allGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(my_allGetLuckDrawRankList)) );
                resultMap.put("all_allGetLuckDrawRankList",
                        JSONObject.toJSONString(MapUtil.getStringMapList(all_allGetLuckDrawRankList)) );
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setResultListTotal(total);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取某商家下所有参与过领取奖励的队列-getAllLuckDrawRankByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取待领取奖励的商家列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getWaitLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception{
        logger.info("【service】获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        paramMap.put("status", "0");        //抽奖状态，0是未发放，1是已发放，2是已删除
        if(!"".equals(uid)){
            List<Map<String, Object>> all_waitGetLuckDrawShopList = wxLuckDrawDao.getLuckDrawShopByCondition(paramMap);
            if(all_waitGetLuckDrawShopList != null &&
                    all_waitGetLuckDrawShopList.size() > 0){
                //处理数据
                Map<String, Object> shopMap = Maps.newHashMap();
                for (int i = 0; i < all_waitGetLuckDrawShopList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_waitGetLuckDrawShopList.get(i);
                    String shopId = waitGetLuckDrawMap.get("shopId").toString();
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", (i+1)+"");
                    waitGetLuckDrawMap.remove("luckDrawRule");
                    if(shopMap.containsKey(shopId)){
                        //返现金额
                        Map<String, Object> temp = (Map<String, Object>)shopMap.get(shopId);
                        Double luckDrawMoneyTemp = temp.get("luckDrawMoney")!=null?Double.parseDouble(temp.get("luckDrawMoney").toString()):0.0;
                        luckDrawMoney = luckDrawMoneyTemp + luckDrawMoney;
                        bg = new BigDecimal(luckDrawMoney);
                        luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //支付总额
                        Double payMoneyTemp = temp.get("payMoney")!=null?Double.parseDouble(temp.get("payMoney").toString()):0.0;
                        payMoney = payMoneyTemp + payMoney;
                        bg = new BigDecimal(payMoney);
                        payMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        temp.put("payMoney", payMoney.toString());
                        temp.put("luckDrawMoney", luckDrawMoney.toString());
                    } else {
                        shopMap.put(shopId, waitGetLuckDrawMap);
                    }
                }
                //准备数据
                //商家列表信息
                List<Map<String, Object>> shopList = new LinkedList<Map<String, Object>>();
                Set<Map.Entry<String, Object>> entrySet  =  shopMap.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                while(iterator.hasNext()) {
                    shopList.add((Map<String, Object>)iterator.next().getValue());
                }
                //总数
                Integer total = shopList.size();
                if((start+size) > shopList.size()){
                    shopList = shopList.subList(start, shopList.size());
                } else {
                    shopList = shopList.subList(start, (start+size));
                }
                //整理回传参数
                resultDTO.setResultList(MapUtil.getStringMapList(shopList));
                resultDTO.setResultListTotal(total);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已领取奖励的商家列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getRecevicedLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception{
        logger.info("【service】获取已领取奖励的商家列表-getRecevicedLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        paramMap.put("status", "1");        //抽奖状态，0是未发放，1是已发放，2是已删除
        if(!"".equals(uid)){
            List<Map<String, Object>> all_recevicedGetLuckDrawShopList = wxLuckDrawDao.getLuckDrawShopByCondition(paramMap);
            if(all_recevicedGetLuckDrawShopList != null &&
                    all_recevicedGetLuckDrawShopList.size() > 0){
                //处理数据
                Map<String, Object> shopMap = Maps.newHashMap();
                for (int i = 0; i < all_recevicedGetLuckDrawShopList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_recevicedGetLuckDrawShopList.get(i);
                    String shopId = waitGetLuckDrawMap.get("shopId").toString();
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", (i+1)+"");
                    waitGetLuckDrawMap.remove("luckDrawRule");
                    if(shopMap.containsKey(shopId)){
                        //返现金额
                        Map<String, Object> temp = (Map<String, Object>)shopMap.get(shopId);
                        Double luckDrawMoneyTemp = temp.get("luckDrawMoney")!=null?Double.parseDouble(temp.get("luckDrawMoney").toString()):0.0;
                        luckDrawMoney = luckDrawMoneyTemp + luckDrawMoney;
                        bg = new BigDecimal(luckDrawMoney);
                        luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //支付总额
                        Double payMoneyTemp = temp.get("payMoney")!=null?Double.parseDouble(temp.get("payMoney").toString()):0.0;
                        payMoney = payMoneyTemp + payMoney;
                        bg = new BigDecimal(payMoney);
                        payMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        temp.put("payMoney", payMoney.toString());
                        temp.put("luckDrawMoney", luckDrawMoney.toString());
                    } else {
                        shopMap.put(shopId, waitGetLuckDrawMap);
                    }
                }
                //准备数据
                //商家列表信息
                List<Map<String, Object>> shopList = new LinkedList<Map<String, Object>>();
                Set<Map.Entry<String, Object>> entrySet  =  shopMap.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                while(iterator.hasNext()) {
                    shopList.add((Map<String, Object>)iterator.next().getValue());
                }
                //总数
                Integer total = shopList.size();
                if((start+size) > shopList.size()){
                    shopList = shopList.subList(start, shopList.size());
                } else {
                    shopList = shopList.subList(start, (start+size));
                }
                //整理回传参数
                resultDTO.setResultList(MapUtil.getStringMapList(shopList));
                resultDTO.setResultListTotal(total);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取已领取奖励的商家列表-getRecevicedLuckDrawShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取参加过抽奖的商家列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getAllLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception{
        logger.info("【service】获取参加过抽奖的商家列表-getAllLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        if(!"".equals(uid)){
            List<Map<String, Object>> all_allGetLuckDrawShopList = wxLuckDrawDao.getLuckDrawShopByCondition(paramMap);
            if(all_allGetLuckDrawShopList != null &&
                    all_allGetLuckDrawShopList.size() > 0){
                //处理数据
                Map<String, Object> shopMap = Maps.newHashMap();
                for (int i = 0; i < all_allGetLuckDrawShopList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_allGetLuckDrawShopList.get(i);
                    String shopId = waitGetLuckDrawMap.get("shopId").toString();
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawStatusCode = waitGetLuckDrawMap.get("luckDrawStatusCode")!=null?waitGetLuckDrawMap.get("luckDrawStatusCode").toString():"0";
                    String luckDrawStatusName = waitGetLuckDrawMap.get("luckDrawStatusName")!=null?waitGetLuckDrawMap.get("luckDrawStatusName").toString():"0";
                    String luckDrawRule = waitGetLuckDrawMap.get("luckDrawRule").toString();
                    Map<String, String> luckDrawRuleMap = JSONObject.parseObject(luckDrawRule, Map.class);
                    Double proportion = luckDrawRuleMap.get("proportion")!=null?Double.parseDouble(luckDrawRuleMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.remove("luckDrawRule");
                    if(shopMap.containsKey(shopId)){
                        //返现金额
                        Map<String, Object> temp = (Map<String, Object>)shopMap.get(shopId);
                        Double luckDrawMoneyTemp = temp.get("luckDrawMoney")!=null?Double.parseDouble(temp.get("luckDrawMoney").toString()):0.0;
                        luckDrawMoney = luckDrawMoneyTemp + luckDrawMoney;
                        bg = new BigDecimal(luckDrawMoney);
                        luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //支付总额
                        Double payMoneyTemp = temp.get("payMoney")!=null?Double.parseDouble(temp.get("payMoney").toString()):0.0;
                        payMoney = payMoneyTemp + payMoney;
                        bg = new BigDecimal(payMoney);
                        payMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        temp.put("payMoney", payMoney.toString());
                        temp.put("luckDrawMoney", luckDrawMoney.toString());
                        if("0".equals(luckDrawStatusCode)){     //如果存在待奖励，则全部显示待奖励
                            temp.put("luckDrawStatusCode", luckDrawStatusCode.toString());
                            temp.put("luckDrawStatusName", luckDrawStatusName.toString());
                        }
                    } else {
                        shopMap.put(shopId, waitGetLuckDrawMap);
                    }
                }
                //准备数据
                //商家列表信息
                List<Map<String, Object>> shopList = new LinkedList<Map<String, Object>>();
                Set<Map.Entry<String, Object>> entrySet  =  shopMap.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                while(iterator.hasNext()) {
                    shopList.add((Map<String, Object>)iterator.next().getValue());
                }
                //总数
                Integer total = shopList.size();
                if((start+size) > shopList.size()){
                    shopList = shopList.subList(start, shopList.size());
                } else {
                    shopList = shopList.subList(start, (start+size));
                }
                //整理回传参数
                resultDTO.setResultList(MapUtil.getStringMapList(shopList));
                resultDTO.setResultListTotal(total);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultDTO.setCode(NewMallCode.LUCKDRAW_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.LUCKDRAW_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取参加过抽奖的商家列表-getAllLuckDrawShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 奖励兑换用户积分
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO convertIntegral(Map<String, Object> paramMap) {
        logger.info("【service】奖励兑换用户积分-convertIntegral,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        if (!"".equals(uid) && !"".equals(wxOrderId)) {
            paramMap.put("status", "0");
            List<Map<String, Object>> luckDrawList = wxLuckDrawDao.getLuckDrawByCondition(paramMap);
            if(luckDrawList != null && luckDrawList.size() > 0){
                //获取订单的交易总金额
                Map<String, Object> luckDrawMap = luckDrawList.get(0);
                Double payMoney = luckDrawMap.get("payMoney")!=null?Double.parseDouble(luckDrawMap.get("payMoney").toString()):0.0;
                Double useBalanceMonney = luckDrawMap.get("useBalanceMonney")!=null?Double.parseDouble(luckDrawMap.get("useBalanceMonney").toString()):0.0;
                Double orderPayMoney = payMoney + useBalanceMonney;     //交易的总金额转化为积分
                //获取用户的总积分
                Double integral = luckDrawMap.get("integral")!=null?Double.parseDouble(luckDrawMap.get("integral").toString()):0.0;
                Double newIntegral = integral + orderPayMoney;
                BigDecimal bg = new BigDecimal(newIntegral);
                newIntegral = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //更新 抽奖状态 为 已发送
                Map<String, Object> luckDrawParamMap = Maps.newHashMap();
                luckDrawMap.put("status", "1");    //抽奖状态，0是未发放，1是已发放，2是已删除
                luckDrawMap.put("wxOrderId", wxOrderId);
                luckDrawMap.put("remark", "奖励到用户积分：" + orderPayMoney + "个，当前用户积分总数：" + newIntegral + "个.");
                updateNum = wxLuckDrawDao.updateLuckDraw(luckDrawMap);
                if (updateNum != null && updateNum > 0) {
                    //更新 用户积分 为 原积分数量+订单交易总金额
                    Map<String, Object> userParamMap = Maps.newHashMap();
                    userParamMap.clear();
                    userParamMap.put("id", uid);
                    userParamMap.put("integral", newIntegral);
                    updateNum = wxUserDao.updateUser(userParamMap);
                    String exchangeStatus = "0";
                    if (updateNum != null && updateNum > 0) {
                        exchangeStatus = "1";
                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        exchangeStatus = "0";
                        resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_USER_INEGRAL_IS_FAILED.getNo());
                        resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_USER_INEGRAL_IS_FAILED.getMessage());
                    }

                    //插入兑换积分日志
                    Map<String, Object> integralLog = Maps.newHashMap();
                    integralLog.put("uid", uid);
                    integralLog.put("exchangeToUserIntegral", orderPayMoney);
                    integralLog.put("userIntegral", newIntegral);
                    integralLog.put("exchangeStatus", exchangeStatus);
                    integralLog.put("remark", "用户兑换积分：" + orderPayMoney + "个，当前用户积分总数：" + newIntegral + "个.");
                    wxIntegralLogService.addIntegralLog(integralLog);

                } else {
                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getNo());
                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            //订单不存在或者已被奖励
            logger.info("当前用户 uid = " + paramMap.get("uid") +
                    " ，订单金额 payMoney = " + paramMap.get("payMoney") +
                    " ，订单不存在或者已被奖励.");
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
        }
        logger.info("【service】奖励兑换用户积分-convertIntegral,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 奖励兑换用户余额
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO convertBalance(Map<String, Object> paramMap) throws Exception{
        logger.info("【service】奖励兑换用户余额-convertBalance,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //获取待奖励的列表
        paramMap.put("status", "0");         //抽奖状态，0是未发放，1是已发放，2是已删除
        List<Map<String, Object>> luckDrawList = wxLuckDrawDao.getLuckDrawByCondition(paramMap);
        if (luckDrawList != null && luckDrawList.size() > 0) {
            for (Map<String, Object> luckDrawMap : luckDrawList) {
                String id = luckDrawMap.get("id")!=null?luckDrawMap.get("id").toString():"";
                String uid = luckDrawMap.get("uid")!=null?luckDrawMap.get("uid").toString():"";
                String wxOrderId = luckDrawMap.get("wxOrderId")!=null?luckDrawMap.get("wxOrderId").toString():"";
                String openId = luckDrawMap.get("openId")!=null?luckDrawMap.get("openId").toString():"";
                String balanceStr = luckDrawMap.get("balance")!=null?luckDrawMap.get("balance").toString():"0";
                String payMoneyStr = luckDrawMap.get("payMoney")!=null?luckDrawMap.get("payMoney").toString():"0";
                String cashBackModeJson = luckDrawMap.get("cashBackModeJson")!=null?luckDrawMap.get("cashBackModeJson").toString():"";
                String createTime = luckDrawMap.get("createTime")!=null?luckDrawMap.get("createTime").toString():"";
                if (!"".equals(id) && !"".equals(uid) && !"".equals(wxOrderId)
                        && !"".equals(openId) && !"".equals(balanceStr)
                        && !"".equals(payMoneyStr) && !"".equals(cashBackModeJson)
                        && !"".equals(createTime)) {
                    boolean isConvertBalanceFlag = false;
                    //判断奖励的创建时间是否小于或者等于
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date createDate = formatter.parse(createTime);
                    Date currentDate = new Date();
                    if(createDate.before(currentDate)){
                        isConvertBalanceFlag = true;
                    } else if(currentDate.equals(createDate)){
                        isConvertBalanceFlag = true;
                    } else {
                        isConvertBalanceFlag = false;
                    }
                    if(isConvertBalanceFlag){
                        //对返现模式json进行解析
                        Map<String, String> cashBackModeMap = JSONObject.parseObject(cashBackModeJson, Map.class);
                        String proportionStr = cashBackModeMap.get("proportion");
                        if(!"".equals(proportionStr)){
                            try {
                                //当前用户余额
                                Double balance = Double.parseDouble(balanceStr);
                                //计算待反还的金额
                                Double payMoney = Double.parseDouble(payMoneyStr);
                                Double proportion = Double.parseDouble(proportionStr);
                                Double cashBackMoney = payMoney * proportion;
                                //领取返现后的新余额
                                Double newBalance = balance + cashBackMoney;
                                //double转换为小数点后两位
                                BigDecimal bg = new BigDecimal(newBalance);
                                newBalance = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                //更新 抽奖状态 为 已发送
                                Map<String, Object> luckDrawParamMap = Maps.newHashMap();
                                luckDrawParamMap.put("status", "1");    //抽奖状态，0是未发放，1是已发放，2是已删除
                                luckDrawParamMap.put("wxOrderId", wxOrderId);
                                luckDrawParamMap.put("remark", "奖励到用户余额：" + cashBackMoney + "元，当前用户余额总数：" + newBalance + "元.");
                                updateNum = wxLuckDrawDao.updateLuckDraw(luckDrawParamMap);
                                if (updateNum != null && updateNum > 0) {
                                    //更新 用户积分 为 原积分数量+订单交易总金额
                                    Map<String, Object> userMap = Maps.newHashMap();
                                    userMap.clear();
                                    userMap.put("id", uid);
                                    userMap.put("balance", newBalance);
                                    updateNum = wxUserDao.updateUser(userMap);
                                    if (updateNum != null && updateNum > 0) {
                                        logger.info("当前用户 uid = " + uid +
                                                " ，订单金额 payMoney = " + payMoneyStr +
                                                " ，返现比例 proportion = "+ proportionStr +
                                                " 奖励返现成功，快去设置自动提现吧.");
                                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                                    } else {
                                        logger.info("当前用户 uid = " + uid +
                                                " ，订单金额 payMoney = " + payMoneyStr +
                                                " ，返现比例 proportion = "+ proportionStr +
                                                " 转换积分时更新用户余额失败，请联系管理员.");
                                        resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_USER_BANLANCE_IS_FAILED.getNo());
                                        resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_USER_BANLANCE_IS_FAILED.getMessage());
                                    }
                                } else {
                                    logger.info("当前用户 uid = " + uid +
                                            " ，订单金额 payMoney = " + payMoneyStr +
                                            " ，返现比例 proportion = "+ proportionStr +
                                            " 转换积分时更新奖励状态失败，请联系管理员.");
                                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getNo());
                                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getMessage());
                                }
                            } catch (Exception e) {
                                logger.info("当前用户 uid = " + uid +
                                        " ，订单金额 payMoney = " + payMoneyStr +
                                        " ，返现比例 proportion = "+ proportionStr +
                                        " 不正确(非数字)，请联系管理员.");
                                resultMapDTO.setCode(NewMallCode.LUCKDRAW_BALANCE_OR_PAYMONEY_OR_PROPORTION_IS_NOT_NUMBER.getNo());
                                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_BALANCE_OR_PAYMONEY_OR_PROPORTION_IS_NOT_NUMBER.getMessage());
                                continue;
                            }
                        } else {
                            //返现比例不存在
                            logger.info("当前用户 uid = " + uid +
                                    " ，订单金额 payMoney = " + payMoneyStr +
                                    " ，返现比例不存在，请联系管理员.");
                            resultMapDTO.setCode(NewMallCode.LUCKDRAW_BALANCE_PROPORTION_IS_NOT_NUMBER.getNo());
                            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_BALANCE_PROPORTION_IS_NOT_NUMBER.getMessage());
                        }
                    }
                } else {
                    logger.info("当前用户 uid = " + uid +
                            " 存在垃圾数据，请联系管理员.");
                    continue;
                }
            }
        } else {
            //订单不存在或者已被奖励
            logger.info("当前用户 uid = " + paramMap.get("uid") +
                    " ，订单金额 payMoney = " + paramMap.get("payMoney") +
                    " ，订单不存在或者已被奖励.");
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
        }
        logger.info("【service】奖励兑换用户余额-convertBalance,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
