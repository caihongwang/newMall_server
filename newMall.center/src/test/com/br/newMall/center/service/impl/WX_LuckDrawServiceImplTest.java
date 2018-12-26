package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_LuckDrawDao;
import com.br.newMall.dao.WX_OrderDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_LuckDrawServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_LuckDrawServiceImpl.class);

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_LuckDrawDao wxLuckDrawDao;

    @Autowired
    private WX_OrderDao wxOrderDao;

    @Autowired
    private WX_UserDao wxUserDao;

    @Test
    public void Test() throws Exception{
//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        paramMap.put("wxOrderId", "fd0fe3e433114da19b2c3c6f28f95d8e");
//        addLuckDraw(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        paramMap.put("shopId", "1");
//        paramMap.put("start", "0");
//        paramMap.put("size", "2");
//        getWaitLuckDrawRankByCondition(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        paramMap.put("start", "0");
//        paramMap.put("size", "1");
//        getWaitLuckDrawShopByCondition(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", "1");
        paramMap.put("wxOrderId", "24aa84a13c974dc6bfcbd38af00f3b78");
        convertIntegral(paramMap);
    }

    public ResultMapDTO convertIntegral(Map<String, Object> paramMap) {
        logger.info("在【service】中兑换积分-convertIntegral,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        if (!"".equals(uid) && !"".equals(wxOrderId)) {
            Map<String, Object> userMap = Maps.newHashMap();
            userMap.put("id", uid);
            List<Map<String, Object>> currentUserList = wxUserDao.getSimpleUserByCondition(userMap);
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            List<Map<String, Object>> currentOrderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(currentOrderList != null && currentOrderList.size() > 0
                    && currentUserList != null && currentUserList.size() > 0){
                //获取订单的交易总金额
                Map<String, Object> currentOrderMap = currentOrderList.get(0);
                Double payMoney = currentOrderMap.get("payMoney")!=null?Double.parseDouble(currentOrderMap.get("payMoney").toString()):0.0;
                Double useBalanceMonney = currentOrderMap.get("useBalanceMonney")!=null?Double.parseDouble(currentOrderMap.get("useBalanceMonney").toString()):0.0;
                Double orderPayMoney = payMoney + useBalanceMonney;     //交易的总金额转化为积分
                //获取用户的总积分
                Map<String, Object> currentUserMap = currentUserList.get(0);
                Double integral = currentUserMap.get("integral")!=null?Double.parseDouble(currentUserMap.get("integral").toString()):0.0;
                Double newIntegral = integral + orderPayMoney;
                BigDecimal bg = new BigDecimal(newIntegral);
                newIntegral = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                //更新 抽奖状态 为 已发送
                Map<String, Object> luckDrawMap = Maps.newHashMap();
                luckDrawMap.put("status", "1");    //抽奖状态，0是未发放，1是已发放，2是已删除
                luckDrawMap.put("wxOrderId", wxOrderId);
                luckDrawMap.put("remark", "奖励积分：" + newIntegral + "个.");
                updateNum = wxLuckDrawDao.updateLuckDraw(luckDrawMap);
                if (updateNum != null && updateNum > 0) {
                    //更新 用户积分 为 原积分数量+订单交易总金额
                    userMap.clear();
                    userMap.put("id", uid);
                    userMap.put("integral", newIntegral);
                    updateNum = wxUserDao.updateUser(userMap);
                    if (updateNum != null && updateNum > 0) {
                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_USER_INEGRAL_IS_FAILED.getNo());
                        resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_USER_INEGRAL_IS_FAILED.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getNo());
                    resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UPDATE_STATUS_IS_FAILED.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.LUCKDRAW_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.LUCKDRAW_UID_OR_WXORDERID_IS_NULL.getMessage());
        }
        logger.info("在【service】中兑换积分-convertIntegral,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    public ResultDTO getWaitLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception{
        logger.info("在【service】中获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        if(!"".equals(uid)){
            List<Map<String, Object>> my_waitGetLuckDrawShopList = Lists.newArrayList();
            List<Map<String, Object>> all_waitGetLuckDrawShopList = wxLuckDrawDao.getLuckDrawShopByCondition(paramMap);
            if(all_waitGetLuckDrawShopList != null &&
                    all_waitGetLuckDrawShopList.size() > 0){
                //处理数据
                Map<String, Object> shopMap = Maps.newHashMap();
                for (int i = 0; i < all_waitGetLuckDrawShopList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_waitGetLuckDrawShopList.get(i);
                    String shopId = waitGetLuckDrawMap.get("shopId").toString();
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRemark = waitGetLuckDrawMap.get("luckDrawRemark").toString();
                    Map<String, String> luckDrawRemarkMap = JSONObject.parseObject(luckDrawRemark, Map.class);
                    Double proportion = luckDrawRemarkMap.get("proportion")!=null?Double.parseDouble(luckDrawRemarkMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", i+"");
                    waitGetLuckDrawMap.remove("luckDrawRemark");
                    if(shopMap.containsKey(shopId)){
                        Map<String, Object> temp = (Map<String, Object>)shopMap.get(shopId);
                        Double luckDrawMoneyTemp = temp.get("luckDrawMoney")!=null?Double.parseDouble(temp.get("luckDrawMoney").toString()):0.0;
                        luckDrawMoney = luckDrawMoneyTemp + luckDrawMoney;
                        bg = new BigDecimal(luckDrawMoney);
                        luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
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
                shopList = shopList.subList(start, (start+size));
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
        logger.info("在【service】中获取待领取奖励的商家列表-getWaitLuckDrawShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultMapDTO getWaitLuckDrawRankByCondition(Map<String, Object> paramMap){
        logger.info("在【service】中获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        Integer start = paramMap.get("start") != null ? Integer.parseInt(paramMap.get("start").toString()) : 0;
        Integer size = paramMap.get("size") != null ? Integer.parseInt(paramMap.get("size").toString()) : 10;
        if(!"".equals(shopId) && !"".equals(uid)){
            List<Map<String, Object>> my_waitGetLuckDrawRankList = Lists.newArrayList();
            List<Map<String, Object>> all_waitGetLuckDrawRankList = wxLuckDrawDao.getLuckDrawRankByCondition(paramMap);
            if(all_waitGetLuckDrawRankList != null &&
                    all_waitGetLuckDrawRankList.size() > 0){
                //处理数据
                for (int i = 0; i < all_waitGetLuckDrawRankList.size(); i++) {
                    Map<String, Object> waitGetLuckDrawMap = all_waitGetLuckDrawRankList.get(i);
                    Double payMoney = waitGetLuckDrawMap.get("payMoney")!=null?Double.parseDouble(waitGetLuckDrawMap.get("payMoney").toString()):0.0;
                    String luckDrawRemark = waitGetLuckDrawMap.get("luckDrawRemark").toString();
                    Map<String, String> luckDrawRemarkMap = JSONObject.parseObject(luckDrawRemark, Map.class);
                    Double proportion = luckDrawRemarkMap.get("proportion")!=null?Double.parseDouble(luckDrawRemarkMap.get("proportion").toString()):1.0;
                    Double luckDrawMoney = payMoney * proportion;
                    BigDecimal bg = new BigDecimal(luckDrawMoney);
                    luckDrawMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    waitGetLuckDrawMap.put("luckDrawMoney", luckDrawMoney.toString());
                    waitGetLuckDrawMap.put("rankIndex", i+"");
                    waitGetLuckDrawMap.remove("luckDrawRemark");
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
                //当前所有的排队
                all_waitGetLuckDrawRankList = all_waitGetLuckDrawRankList.subList(start, (start+size));
                //总数
                Integer total = wxLuckDrawDao.getLuckDrawRankTotalByCondition(paramMap);
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
        logger.info("在【service】中获取某商家下待领取奖励的队列-getWaitLuckDrawRankByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    public ResultMapDTO addLuckDraw(Map<String, Object> paramMap) {
        logger.info("在【service】中添加抽奖信息-addLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("在【service】中添加抽奖信息-addLuckDraw,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    public ResultDTO getLuckDrawProductList(Map<String, Object> paramMap) {
        logger.info("在【service】中获取抽奖的产品列表-getLuckDrawProductList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "luckDraw";
        if(!"".equals(dicType)){
            paramMap.put("dicType", dicType);
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.LUCKDRAW_LUCKDRAWTYPE_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.LUCKDRAW_LUCKDRAWTYPE_IS_NULL.getMessage());
        }
        logger.info("在【service】中获取抽奖的产品列表-getLuckDrawProductList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
}