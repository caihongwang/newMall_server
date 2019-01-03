package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.NumberUtil;
import com.br.newMall.center.utils.TimestampUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.br.newMall.center.utils.wxpay.WXPayUtil;
import com.br.newMall.dao.WX_OrderDao;
import com.br.newMall.dao.WX_ProductDao;
import com.br.newMall.dao.WX_ShopDao;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_OrderServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_OrderServiceImpl.class);

    @Autowired
    private WX_ShopDao wxShopDao;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_OrderDao wxOrderDao;

    @Autowired
    private WX_ProductDao wxProductDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    @Test
    public void TEST() throws Exception {

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "2");
//        paramMap.put("shopId", "1");
//        paramMap.put("payMoney", "0.02");
//        paramMap.put("payIntegral", "0");
//        paramMap.put("payBalance", "0.01");
//        paramMap.put("useBalanceFlag", "true");
//        paramMap.put("useIntegralFlag", "false");
//        paramMap.put("spbillCreateIp", "https://www.91caihongwang.com");
//        this.payTheBillInMiniProgram(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("productId", "1");
//        paramMap.put("productNum", "1");
//        paramMap.put("addressId", "1");
//        paramMap.put("openId", "o8-g249hJL8mmxq6MGsxIAAz4ZaM");
//        paramMap.put("spbillCreateIp", "https://www.91caihongwang.com");
//        this.purchaseProductInMiniProgram(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        getWaitPayGoodsOrder(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        getAlreadyPayGoodsOrder(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        getAlreadyDeliverGoodsOrder(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        getCompletedGoodsOrder(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("orderId", "9");
        confirmReceiptGoodsOrder(paramMap);
    }


    /**
     * 对商品订单进行确认收货
     * @param paramMap
     * @return
     */
    public BoolDTO confirmReceiptGoodsOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中对商品订单进行确认收货-confirmReceiptGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String orderId = paramMap.get("orderId")!=null?paramMap.get("orderId").toString():"";
        if(!"".equals(orderId)){
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("id", orderId);        //订单ID
            List<Map<String, Object>> orderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(orderList != null && orderList.size() > 0){
                String status = orderList.get(0).get("status").toString();
                String expressNumber = orderList.get(0).get("expressNumber").toString();
                //订单的状态必须已发货或者发货的快递编号非空
                if("2".equals(status) && !"".equals(expressNumber)){
                    paramMap.clear();
                    paramMap.put("id", orderId);        //订单ID
                    paramMap.put("status", "3");        //已完成
                    updateNum = wxOrderDao.updateOrder(paramMap);
                    if (updateNum != null && updateNum > 0) {
                        boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                        boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                        boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                    }
                } else {
                    boolDTO.setCode(NewMallCode.ORDER_STATUS_IS_NOT_2_OR_EXPRESSNUMBER_IS_NULL.getNo());
                    boolDTO.setMessage(NewMallCode.ORDER_STATUS_IS_NOT_2_OR_EXPRESSNUMBER_IS_NULL.getMessage());
                }
            } else {
                boolDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
                boolDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ORDER_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中对商品订单进行确认收货-confirmReceiptGoodsOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取待支付的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getWaitPayGoodsOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中获取待支付的商品订单-getWaitPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> orderStrList = Lists.newArrayList();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(uid)){
            paramMap.put("status", "0");        //待支付
            List<Map<String, Object>> goodsOrderList = wxOrderDao.getGoodsOrderByCondition(paramMap);
            if (goodsOrderList != null && goodsOrderList.size() > 0) {
                orderStrList = MapUtil.getStringMapList(goodsOrderList);
                Integer total = wxOrderDao.getGoodsOrderTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(orderStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.ORDER_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取待支付的商品订单-getWaitPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已支付的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getAlreadyPayGoodsOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中获取已支付的商品订单-getAlreadyPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> orderStrList = Lists.newArrayList();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(uid)){
            paramMap.put("status", "1");        //已支付
            List<Map<String, Object>> goodsOrderList = wxOrderDao.getGoodsOrderByCondition(paramMap);
            if (goodsOrderList != null && goodsOrderList.size() > 0) {
                orderStrList = MapUtil.getStringMapList(goodsOrderList);
                Integer total = wxOrderDao.getGoodsOrderTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(orderStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.ORDER_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取已支付的商品订单-getAlreadyPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已发货的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getAlreadyDeliverGoodsOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中获取已发货的商品订单-getAlreadyDeliverGoods,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> orderStrList = Lists.newArrayList();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(uid)){
            paramMap.put("status", "2");        //已发货
            paramMap.put("expressNumber", "MUST_HAVE_EXPRESSNUMBER");  //快递编号必须存在
            List<Map<String, Object>> goodsOrderList = wxOrderDao.getGoodsOrderByCondition(paramMap);
            if (goodsOrderList != null && goodsOrderList.size() > 0) {
                orderStrList = MapUtil.getStringMapList(goodsOrderList);
                Integer total = wxOrderDao.getGoodsOrderTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(orderStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.ORDER_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取已发货的商品订单-getAlreadyDeliverGoods,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已完成的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getCompletedGoodsOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中获取已完成的商品订单-getCompletedGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> orderStrList = Lists.newArrayList();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(uid)){
            paramMap.put("status", "3");        //已支付
            paramMap.put("expressNumber", "MUST_HAVE_EXPRESSNUMBER");  //快递编号必须存在
            List<Map<String, Object>> goodsOrderList = wxOrderDao.getGoodsOrderByCondition(paramMap);
            if (goodsOrderList != null && goodsOrderList.size() > 0) {
                orderStrList = MapUtil.getStringMapList(goodsOrderList);
                Integer total = wxOrderDao.getGoodsOrderTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(orderStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.ORDER_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取已完成的商品订单-getCompletedGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultMapDTO payTheBillInMiniProgram(Map<String, Object> paramMap) throws Exception {
        logger.info("在【service】中买单-payTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        //支付的金额
        String payMoneyStr = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "0";
        //用于抵扣的积分
        String payIntegralStr = paramMap.get("payIntegral") != null ? paramMap.get("payIntegral").toString() : "0";
        //用于抵扣的余额
        String payBalanceStr = paramMap.get("payBalance") != null ? paramMap.get("payBalance").toString() : "0";
        //付款用户的uid
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        //收钱商家的店铺id
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        //是否使用积分抵扣标志
        Boolean useIntegralFlag = paramMap.get("useIntegralFlag") != null ? Boolean.parseBoolean(paramMap.get("useIntegralFlag").toString()) : false;
        //是否使用余额抵扣标志
        Boolean useBalanceFlag = paramMap.get("useBalanceFlag") != null ? Boolean.parseBoolean(paramMap.get("useBalanceFlag").toString()) : false;
        //生成的随机字符串,微信用于校验
        String nonce_str = WXPayUtil.generateUUID();
        //商品名称
        String body = "向商家付款";
        //统一订单编号,即微信订单号
        String out_trade_no = WXPayUtil.generateUUID();
        //发起支付的IP地址
        String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "";      //获取发起支付的IP地址
        //默认订单状态为待支付
        String orderStatus = "0";
        if (!"".equals(uid) && !"".equals(shopId)
                && !"".equals(spbillCreateIp)) {
            if ("".equals(payMoneyStr)) {       //支付金额不允许为空
                resultMapDTO.setCode(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getMessage());
            } else {
                //1.通过uid获取付款用户的openId
                Map<String, Object> userMap = Maps.newHashMap();
                userMap.put("id", uid);
                List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(userMap);
                if(userList != null && userList.size() > 0){
                    //用户openId
                    String openId = userList.get(0).get("openId").toString();
                    //用户余额
                    String userBalanceStr = userList.get(0).get("balance")!=null?userList.get(0).get("balance").toString():"0";
                    Double userBalance = Double.parseDouble(userBalanceStr);
                    //用户积分
                    String userIntegralStr = userList.get(0).get("integral")!=null?userList.get(0).get("integral").toString():"0";
                    Double userIntegral = Double.parseDouble(userIntegralStr);
                    //支付的金额
                    Double payMoney = Double.parseDouble(payMoneyStr != "" ? payMoneyStr : "10");
                    //抵扣的积分
                    Double payIntegral = Double.parseDouble(payIntegralStr != "" ? payIntegralStr : "0");
                    //抵扣的余额
                    Double payBalance = Double.parseDouble(payBalanceStr != "" ? payBalanceStr : "0");
                    //实际支付金额，用户最新余额，用户最新积分
                    Double actualPayMoney = 0.0;    //实际支付金额
                    Double newUserBalance = 0.0;    //用户最新余额
                    Double newUserIntegral = 0.0;   //用户最新积分
                    Double allPayAmount = payMoney; //总支付金额
                    if(useIntegralFlag){     //使用积分进行抵扣支付(优先使用积分抵扣)
                        if(userIntegral > 0){//用户的积分大于0，才可以进行抵扣
                            if(userIntegral >= payIntegral){
                                actualPayMoney = payMoney - payIntegral;
                                newUserIntegral = userIntegral - payIntegral;
                            } else {         //用户积分不够，则不扣积分，全额支付
                                actualPayMoney = payMoney;
                                newUserIntegral = userIntegral;
                            }
                        } else {             //用户无积分，则全额支付
                            actualPayMoney = payMoney;
                            newUserIntegral = userIntegral;
                        }
                        newUserBalance = userBalance;
                    } else if(useBalanceFlag){//使用余额进行抵扣支付
                        if(userBalance > 0){//用户的余额大于0，才可以进行抵扣
                            if(userBalance >= payBalance){
                                actualPayMoney = payMoney - payBalance;
                                newUserBalance = userBalance - payBalance;
                            } else {        //用户余额不够，则不扣余额，全额支付
                                actualPayMoney = payMoney;
                                newUserBalance = userBalance;
                            }
                        } else {             //用户无余额，则全额支付
                            actualPayMoney = payMoney;
                            newUserBalance = userBalance;
                        }
                        newUserIntegral = userIntegral;
                    } else {               //不使用余额和积分进行支付，则全额支付
                        actualPayMoney = payMoney;
                        newUserIntegral = userIntegral;
                        newUserBalance = userBalance;
                    }
                    //用于购买商品更新付款用户的积分和余额,同事将店铺ID传递过去，便于给店铺的商家打钱
                    Map<String, String> attachMap = Maps.newHashMap();
                    attachMap.put("shopId", shopId);        //用于给店家打钱
                    attachMap.put("balance", NumberUtil.getPointTowNumber(newUserBalance).toString());
                    attachMap.put("integral", NumberUtil.getPointTowNumber(newUserIntegral).toString());
                    attachMap.put("payMoney", NumberUtil.getPointTowNumber(payMoney).toString());
                    //判断是否需要付钱
                    boolean isNeedPay = true;
                    if(actualPayMoney > 0){     //还需要付钱
                        isNeedPay = true;
                        orderStatus = "0";      //订单状态: 0是待支付，1是已支付
                    } else {                    //不需要付钱，已使用积分或者余额抵扣完了.
                        isNeedPay = false;
                        orderStatus = "1";      //订单状态: 0是待支付，1是已支付
                    }
                    logger.info(
                            " 用户 uid : {}", uid,
                            " , 在店铺 shopId : {}", shopId,
                            " , 消费总额 : {}", allPayAmount,
                            " , 实际支付 : {}", actualPayMoney,
                            " , 是否使用余额抵扣 : {}", useBalanceFlag, " , 抵扣余额 : {}", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0",
                            " , 是否使用积分抵扣 : {}", useIntegralFlag, " , 抵扣积分 : {}", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0"
                    );
                    if(isNeedPay){
                        //准备获取支付相关的验签等数据
                        actualPayMoney = NumberUtil.getPointTowNumber(actualPayMoney);
                        String total_fee = ((int) (actualPayMoney * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                        logger.info("支付费用(转化前) payMoney = {}" + actualPayMoney + ", 支付费用(转化后) total_fee = {}" + total_fee);
//                        resultMap = WX_PublicNumberUtil.unifiedOrderForMiniProgram(
//                                nonce_str, body, out_trade_no,
//                                total_fee, spbillCreateIp, NewMallCode.WX_PAY_NOTIFY_URL_wxPayNotifyForPayTheBillInMiniProgram,
//                                openId, JSONObject.toJSONString(attachMap)
//                        );
                        resultMap.put("code", NewMallCode.SUCCESS.getNo());
                        if(resultMap.get("code").toString().equals((NewMallCode.SUCCESS.getNo()+""))){
                            //创建订单，状态设为待支付
                            Map<String, Object> orderMap = Maps.newHashMap();
                            orderMap.put("uid", uid);
                            orderMap.put("wxOrderId", out_trade_no);
                            orderMap.put("orderType", "payTheBill");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                            orderMap.put("shopId", shopId);
                            orderMap.put("allPayAmount", allPayAmount);
                            orderMap.put("payMoney", actualPayMoney);
                            orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                            orderMap.put("useIntegralNum", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                            orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                            orderMap.put("createTime", TimestampUtil.getTimestamp());
                            orderMap.put("updateTime", TimestampUtil.getTimestamp());
                            BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                            resultMapDTO.setCode(addOrderBoolDTO.getCode());
                            resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                        } else {
                            resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                        }
                    } else {
                        //创建订单，状态设为已支付
                        Map<String, Object> orderMap = Maps.newHashMap();
                        orderMap.put("uid", uid);
                        orderMap.put("wxOrderId", out_trade_no);
                        orderMap.put("order_type", "payTheBill");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                        orderMap.put("shopId", shopId);
                        orderMap.put("allPayAmount", allPayAmount);
                        orderMap.put("payMoney", actualPayMoney);
                        orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                        orderMap.put("useIntegralNum", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                        orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                        orderMap.put("createTime", TimestampUtil.getTimestamp());
                        orderMap.put("updateTime", TimestampUtil.getTimestamp());
                        BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                        //更新用户积分和余额信息
                        Map<String, Object> updateMap = Maps.newHashMap();
                        updateMap.put("out_trade_no", out_trade_no);
                        updateMap.put("openId", openId);
                        updateMap.put("attach", JSONObject.toJSONString(attachMap));
                        this.wxPayNotifyForPayTheBillInMiniProgram(updateMap);
                        //设置返回值
                        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                        resultMapDTO.setCode(addOrderBoolDTO.getCode());
                        resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                    }
                    //只要使用余额或者积分进行支付均布允许进行抽奖
                    if(useBalanceFlag || useIntegralFlag){
                        resultMap.put("isLuckDrawFlag", false);
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.USER_IS_NULL.getNo());
                    resultMapDTO.setMessage(NewMallCode.USER_IS_NULL.getMessage());
                }
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_UID_SHOPID_SPBILLCREATEIP_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_UID_SHOPID_SPBILLCREATEIP_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中买单-payTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }


    /**
     * 买单成功后的回调通知
     * @param paramMap
     * @return
     */
    public ResultMapDTO wxPayNotifyForPayTheBillInMiniProgram(Map<String, Object> paramMap) {
        logger.info("在【service】中买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String wxOrderId = paramMap.get("out_trade_no") != null ? paramMap.get("out_trade_no").toString() : "";
        String attach = paramMap.get("attach") != null ? paramMap.get("attach").toString() : "";
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";
        if (!"".equals(wxOrderId) && !"".equals(attach)
                && !"".equals(openId)) {
            //修改订单状态为已付款
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            orderMap.put("status", "1");            //订单状态: 0是待支付，1是已支付
            updateNum = wxOrderDao.updateOrder(orderMap);
            if (updateNum != null && updateNum > 0) {
                Map<String, String> attachMap = JSONObject.parseObject(attach, Map.class);
                String balance = attachMap.get("balance");
                String shopId = attachMap.get("shopId");
                String payMoneyStr = attachMap.get("payMoney");      //付款用户支付的金额
                //更新用户和余额
                Map<String, Object> userMap = Maps.newHashMap();
                userMap.put("openId", openId);
                userMap.put("balance", balance);
                updateNum = wxUserDao.updateUser(userMap);
                if (updateNum != null && updateNum > 0) {
                    //想商家按照之前约定好的折扣值进行向商家发送费用
                    Map<String, Object> shopMap = Maps.newHashMap();
                    shopMap.put("shopId", shopId);
                    List<Map<String, Object>> shopList = wxShopDao.getShopByCondition(shopMap);
                    if(shopList != null && shopList.size() > 0){
                        //获取店铺与平台的折扣值ID
                        String shopOpenId = shopList.get(0).get("shopOpenId")!=null?shopList.get(0).get("shopOpenId").toString():"";
                        String shopDiscountId = shopList.get(0).get("shopDiscountId")!=null?shopList.get(0).get("shopDiscountId").toString():"";
                        if(!"".equals(shopDiscountId)){
                            //根据折扣值ID或者需要向商家发送的金额
                            Map<String, Object> dicMap = Maps.newHashMap();
                            dicMap.put("dicCode", shopDiscountId);
                            dicMap.put("dicType", "shopDiscount");
                            ResultDTO dicResultDTO = wxDicService.getSimpleDicByCondition(dicMap);
                            if(dicResultDTO != null &&
                                    dicResultDTO.getResultList() != null &&
                                    dicResultDTO.getResultList().size() > 0){
                                String shopDiscountStr = dicResultDTO.getResultList().get(0).get("shopDiscount");
                                try {
                                    Double shopDiscount = Double.parseDouble(shopDiscountStr);
                                    Double payMoney = Double.parseDouble(payMoneyStr);
                                    if(payMoney > 0){
                                        //获取向商家打款的后两位
                                        Double shopAmount = shopDiscount * payMoney;
                                        BigDecimal bg = new BigDecimal(shopAmount);
                                        shopAmount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        //准备向商家进行打款
                                        Map<String, Object> enterprisePaymentMap = Maps.newHashMap();
                                        enterprisePaymentMap.put("amount", ((int) (shopAmount * 100)) + "");
                                        enterprisePaymentMap.put("openId", shopOpenId);
                                        enterprisePaymentMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                                        enterprisePaymentMap.put("wxPublicNumGhId", "gh_417c90af3488");
                                        enterprisePaymentMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                                         resultMapDTO = wxRedPacketService.enterprisePayment(enterprisePaymentMap);
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                        //TODO 在此处发起模板消息发送
                                    } else {
                                        logger.info("付款用户使用余额进行付全款，不需要向商家进行打钱.");
                                    }
                                } catch (Exception e) {
                                    resultMapDTO.setCode(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_NUM.getNo());
                                    resultMapDTO.setMessage(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_NUM.getMessage());
                                }
                            } else {
                                resultMapDTO.setCode(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_EXIST.getNo());
                                resultMapDTO.setMessage(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_EXIST.getMessage());
                            }
                        } else {
                            resultMapDTO.setCode(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_NULL.getNo());
                            resultMapDTO.setMessage(NewMallCode.SHOP_SHOPDISCOUNTID_IS_NOT_NULL.getMessage());
                        }
                    } else {
                        resultMapDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
                        resultMapDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }



    public ResultMapDTO purchaseProductInMiniProgram(Map<String, Object> paramMap) throws Exception {
        logger.info("在【service】中购买商品-purchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, Object> resultMap = Maps.newHashMap();
        String productId = paramMap.get("productId") != null ? paramMap.get("productId").toString() : "";       //商品ID
        String productNumStr = paramMap.get("productNum") != null ? paramMap.get("productNum").toString() : "";       //商品数量
        String addressId = paramMap.get("addressId") != null ? paramMap.get("addressId").toString() : "";       //地址ID
        String nonce_str = WXPayUtil.generateUUID();        //生成的随机字符串
        String body = "小程序内发起支付";                     //商品名称
        String out_trade_no = WXPayUtil.generateUUID();     //统一订单编号
        String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "";      //获取发起支付的IP地址
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";
        String orderStatus = "0";
        if (!"".equals(openId) && !"".equals(spbillCreateIp) &&
                !"".equals(productId) && !"".equals(productNumStr) &&
                !"".equals(addressId)) {
            //通过openId获取用户信息
            Map<String, Object> userMap = Maps.newHashMap();
            userMap.put("openId", openId);
            List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(userMap);
            //通过productId获取商品信息
            Map<String, Object> productMap = Maps.newHashMap();
            productMap.put("id", productId);
            List<Map<String, Object>> productList = wxProductDao.getSimpleProductByCondition(productMap);
            if(userList != null && userList.size() > 0
                    && productList != null && productList.size() > 0){
                //获取用户uid，用户积分，用户余额
                String uid = userList.get(0).get("id").toString();
                String userIntegralStr = userList.get(0).get("integral")!=null?userList.get(0).get("integral").toString():"0";
                String userBalanceStr = userList.get(0).get("balance")!=null?userList.get(0).get("balance").toString():"0";
                Double userIntegral = Double.parseDouble(userIntegralStr);
                Double userBalance = Double.parseDouble(userBalanceStr);
                //获取商品所需积分，商品数量，所需金额
                String productIntegralStr = productList.get(0).get("integral")!=null?productList.get(0).get("integral").toString():"0";
                Double productIntegral = Double.parseDouble(productIntegralStr);
                Double productNum = Double.parseDouble(productNumStr);
                String priceStr = productList.get(0).get("price")!=null?productList.get(0).get("price").toString():"0";
                Double price = Double.parseDouble(priceStr);
                price = price * productNum;
                if(userIntegral >= productIntegral){
                    //购买后，用户所剩积分
                    Double newUserIntegral = userIntegral - productIntegral;
                    //购买时，需要支付金额
                    Double payMoneyDouble = 0.0;      //购买商品需要支付费用
                    //购买后，用户所剩余额
                    Double newUserBalance = 0.0;
                    if(userBalance >= price){
                        payMoneyDouble = 0.0;
                        newUserBalance = userBalance - price;
                    } else {
                        payMoneyDouble = price - userBalance;
                        newUserBalance = 0.0;
                    }
                    //用于购买商品更新用户的积分和余额
                    Map<String, String> attachMap = Maps.newHashMap();
                    if(newUserIntegral > 0){
                        attachMap.put("integral", df.format(newUserIntegral));
                    } else {
                        attachMap.put("integral", "0");
                    }
                    if(newUserBalance > 0){
                        attachMap.put("balance", df.format(newUserBalance));
                    } else {
                        attachMap.put("balance", "0");
                    }
                    //判断是否需要付钱
                    boolean isNeedPay = true;
                    if(payMoneyDouble > 0){
                        isNeedPay = true;
                        orderStatus = "0";
                    } else {
                        isNeedPay = false;
                        orderStatus = "1";
                    }
                    if(isNeedPay){
                        //准备获取支付相关的验签等数据
                        payMoneyDouble = Double.parseDouble(df.format(payMoneyDouble) + "");
                        String total_fee = ((int) (payMoneyDouble * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                        logger.info("支付费用(转化前) payMoney = {}" + payMoneyDouble + ", 支付费用(转化后) total_fee = {}" + total_fee);
//                        resultMap = WX_PublicNumberUtil.unifiedOrderForMiniProgram(
//                                nonce_str, body, out_trade_no,
//                                total_fee, spbillCreateIp, openId, JSONObject.toJSONString(attachMap)
//                        );
                        resultMap.put("code", "0");
                        if(resultMap.get("code").toString().equals((NewMallCode.SUCCESS.getNo()+""))){
                            //创建订单，状态设为待支付
                            Map<String, Object> orderMap = Maps.newHashMap();
                            orderMap.put("wxOrderId", out_trade_no);
                            orderMap.put("uid", uid);
                            orderMap.put("productId", productId);
                            orderMap.put("productNum", productNumStr);
                            orderMap.put("addressId", addressId);
                            orderMap.put("useBalanceMonney", userBalance);
                            orderMap.put("payMoney", payMoneyDouble);
                            orderMap.put("payIntegral", productIntegral);
                            orderMap.put("order_type", "purchaseProduct");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                            orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                            orderMap.put("createTime", TimestampUtil.getTimestamp());
                            orderMap.put("updateTime", TimestampUtil.getTimestamp());
                            BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                            resultMapDTO.setCode(addOrderBoolDTO.getCode());
                            resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                        } else {
                            resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                        }
                    } else {
                        //创建订单，状态设为待支付
                        Map<String, Object> orderMap = Maps.newHashMap();
                        orderMap.put("wxOrderId", out_trade_no);
                        orderMap.put("uid", uid);
                        orderMap.put("productId", productId);
                        orderMap.put("productNum", productNumStr);
                        orderMap.put("addressId", addressId);
                        orderMap.put("useBalanceMonney", price);
                        orderMap.put("payMoney", payMoneyDouble);
                        orderMap.put("payIntegral", productIntegral);
                        orderMap.put("order_type", "purchaseProduct");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                        orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                        orderMap.put("createTime", TimestampUtil.getTimestamp());
                        orderMap.put("updateTime", TimestampUtil.getTimestamp());
                        BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                        //更新用户积分和余额信息
                        Map<String, Object> updateMap = Maps.newHashMap();
                        updateMap.put("out_trade_no", out_trade_no);
                        updateMap.put("openId", openId);
                        updateMap.put("attach", JSONObject.toJSONString(attachMap));
                        this.wxPayNotifyForPurchaseProductInMiniProgram(updateMap);
                        //设置返回值
                        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                        resultMapDTO.setCode(addOrderBoolDTO.getCode());
                        resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.ORDER_USER_INTEGRAL_IS_NOT_ENOUGH.getNo());
                    resultMapDTO.setMessage(NewMallCode.ORDER_USER_INTEGRAL_IS_NOT_ENOUGH.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.USER_ID_OR_PRODUCTID_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.USER_ID_OR_PRODUCTID_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_SPBILLCREATEIP_OR_PRODUCTID_OR_PRODUCTNUM_OR_ADDRESSID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_SPBILLCREATEIP_OR_PRODUCTID_OR_PRODUCTNUM_OR_ADDRESSID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中购买商品-purchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 购买商品成功后的回调通知
     * @param paramMap
     * @return
     */
    public ResultMapDTO wxPayNotifyForPurchaseProductInMiniProgram(Map<String, Object> paramMap) {
        logger.info("在【service】中购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String wxOrderId = paramMap.get("out_trade_no") != null ? paramMap.get("out_trade_no").toString() : "";
        String attach = paramMap.get("attach") != null ? paramMap.get("attach").toString() : "";
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";
        if (!"".equals(wxOrderId) && !"".equals(attach)
                && !"".equals(openId)) {
            //修改订单状态为已付款
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            orderMap.put("status", "1");            //订单状态: 0是待支付，1是已支付
            updateNum = wxOrderDao.updateOrder(orderMap);
            if (updateNum != null && updateNum > 0) {
                //更新用户的积分和余额
                Map<String, Object> userMap = Maps.newHashMap();
                userMap.put("openId", openId);
                userMap.putAll(JSONObject.parseObject(attach, Map.class));
                updateNum = wxUserDao.updateUser(userMap);
                if (updateNum != null && updateNum > 0) {
                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());

                    //TODO 在此处发起模板消息发送
                    //TODO 在此处发起模板消息发送
                    //TODO 在此处发起模板消息发送
                    //TODO 在此处发起模板消息发送
                    //TODO 在此处发起模板消息发送

                } else {
                    resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    public BoolDTO addOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String payMoney = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "";
        String orderType = paramMap.get("orderType") != null ? paramMap.get("orderType").toString() : "";
        if (!"".equals(uid) && !"".equals(payMoney)) {
            if("".equals(orderType)){          //订单类型，默认：买单，payTheBill
                paramMap.put("orderType", "payTheBill");
            }
            addNum = wxOrderDao.addOrder(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ORDER_UID_OR_PAYMONEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_UID_OR_PAYMONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中添加订单-addOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }
}