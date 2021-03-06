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
//        paramMap.put("uid", "1");
//        paramMap.put("foodsId", "14,7,11");
//        paramMap.put("wxOrderId", "5253ec8cbd1e49d48f503eea8f6a7c38");
//        paramMap.put("shopId", "1");
//        paramMap.put("payMoney", "5");
//        paramMap.put("payIntegral", "2");
//        paramMap.put("payBalance", "2");
//        paramMap.put("useBalanceFlag", "true");
//        paramMap.put("useIntegralFlag", "false");
//        paramMap.put("spbillCreateIp", "https://www.91caihongwang.com");
//        this.payTheBillInMiniProgram(paramMap);

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

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        getAllPayGoodsOrder(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("orderId", "9");
//        confirmReceiptGoodsOrder(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", "1");
        paramMap.put("productId", "148");
        paramMap.put("productNum", "1");
        paramMap.put("addressId", "1");
        paramMap.put("useBalanceFlag", "true");
        paramMap.put("payBalance", "5.00");
        paramMap.put("useIntegralFlag", "true");
        paramMap.put("payIntegral", "11.11");
        paramMap.put("spbillCreateIp", "https://www.91caihongwang.com");
        this.purchaseProductInMiniProgram(paramMap);
    }



    /**
     * 购买商品
     * @param paramMap
     * @return
     * @throws Exception
     */
    public ResultMapDTO purchaseProductInMiniProgram(Map<String, Object> paramMap) throws Exception {
        logger.info("【service】购买商品-purchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("dealFlag", false);       //默认交易状态为失败
        resultMap.put("isLuckDrawFlag", false); //默认不允许抽奖
        //用户uid
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";       //商品ID
        //微信订单ID
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        //用于抵扣的积分
        String payIntegralStr = paramMap.get("payIntegral") != null ? paramMap.get("payIntegral").toString() : "0";
        //用于抵扣的余额
        String payBalanceStr = paramMap.get("payBalance") != null ? paramMap.get("payBalance").toString() : "0";
        //商品ID
        String productId = paramMap.get("productId") != null ? paramMap.get("productId").toString() : "";       //商品ID
        //商品数量
        String productNumStr = paramMap.get("productNum") != null ? paramMap.get("productNum").toString() : "";       //商品数量
        //商品价格
        String productPriceStr = paramMap.get("productPrice") != null ? paramMap.get("productPrice").toString() : "";       //商品价格
        //商品价格
        String productIntegralStr = paramMap.get("productIntegral") != null ? paramMap.get("productIntegral").toString() : "";       //商品价格
        //地址ID
        String addressId = paramMap.get("addressId") != null ? paramMap.get("addressId").toString() : "";       //地址ID
        //是否使用余额抵扣标志
        Boolean useBalanceFlag = paramMap.get("useBalanceFlag") != null ? Boolean.parseBoolean(paramMap.get("useBalanceFlag").toString()) : false;
        //是否使用积分抵扣标志
        Boolean useIntegralFlag = paramMap.get("useIntegralFlag") != null ? Boolean.parseBoolean(paramMap.get("useIntegralFlag").toString()) : false;
        //交易时的商品详情
        String transactionProductDetailJson = paramMap.get("transactionProductDetail") != null ? paramMap.get("transactionProductDetail").toString() : "";
        //生成的随机字符串,微信用于校验
        String nonce_str = WXPayUtil.generateUUID();
        //商品名称
        String body = "在平台的积分商城购买商品";
        //统一订单编号,即微信订单号
        String out_trade_no = WXPayUtil.generateUUID();
        //发起支付的IP地址
        String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "";      //获取发起支付的IP地址
        //默认订单状态为待支付
        String orderStatus = "0";
        if (!"".equals(uid) && !"".equals(spbillCreateIp) &&
                !"".equals(productId) && !"".equals(productNumStr) &&
                !"".equals(addressId)) {
            //通过openId获取用户信息
            Map<String, Object> userMap = Maps.newHashMap();
            userMap.put("id", uid);
            List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(userMap);
            //通过productId获取商品信息
            Map<String, Object> productMap = Maps.newHashMap();
            productMap.put("id", productId);
            List<Map<String, Object>> productList = wxProductDao.getSimpleProductByCondition(productMap);
            if(userList != null && userList.size() > 0
                    && productList != null && productList.size() > 0){
                //获取用户openId
                String openId = userList.get(0).get("openId").toString();
                //获取用户积分
                String userIntegralStr = userList.get(0).get("integral")!=null?userList.get(0).get("integral").toString():"0";
                Double userIntegral = Double.parseDouble(userIntegralStr);
                //获取用户余额
                String userBalanceStr = userList.get(0).get("balance")!=null?userList.get(0).get("balance").toString():"0";
                Double userBalance = Double.parseDouble(userBalanceStr);
                //商品名称
                body = body + "【" + productList.get(0).get("title") + "】";
                //获取商品的现有库存
                String stockStr = productList.get(0).get("stock")!=null?productList.get(0).get("stock").toString():"0";
                Double stock = Double.parseDouble(stockStr);
                //用户即将购买的商品数量
                Double productNum = Double.parseDouble(productNumStr);
                //获取商品所需单价积分
                if("".equals(productIntegralStr)){     //当是在订单列表中进行支付时，productPriceStr存在值.
                    productIntegralStr = productList.get(0).get("integral")!=null?productList.get(0).get("integral").toString():"0";
                }
                Double productIntegral = Double.parseDouble(productIntegralStr);
                //获取商品所需单价金额
                if("".equals(productPriceStr)){     //当是在订单列表中进行支付时，productPriceStr存在值.
                    productPriceStr = productList.get(0).get("price")!=null?productList.get(0).get("price").toString():"0";
                }
                Double productPrice = Double.parseDouble(productPriceStr);
                //用户即将抵扣的余额
                Double payBalance = Double.parseDouble(payBalanceStr);
                //用户即将抵扣的积分
                Double payIntegral = Double.parseDouble(payIntegralStr);
                if(stock >= productNum){
                    //所需支付的总金额 和 所需支付的总积分
                    Double allProductPrice = productPrice * productNum;
                    Double allProductIntegral = productIntegral * productNum;
                    payIntegral = allProductIntegral;       //用户即将抵扣的积分 以后端服务计算出来的积分为准
                    if(userIntegral >= allProductIntegral){//用户的积分必须大于购买商品的总积分
                        //实际支付金额
                        Double actualPayMoney = allProductPrice;
                        //用户最新余额
                        Double newUserBalance = userBalance;
                        //用户最新积分
                        Double newUserIntegral = userIntegral - allProductIntegral;
                        //总支付金额
                        Double allPayAmount = allProductPrice;
                        //购买后，商品所剩的库存
                        Double newStock = stock - productNum;
                        if(useBalanceFlag){     //使用余额进行支付
                            if(userBalance >= payBalance){  //用户的余额大于用户即将抵扣的余额，才可以进行抵扣
                                actualPayMoney = allProductPrice - payBalance;
                                newUserBalance = userBalance - payBalance;
                                payBalance = payBalance;    //使用余额抵扣
                            } else {                        //用户余额不够，则不扣余额，全额支付
                                actualPayMoney = allProductPrice;
                                newUserBalance = userBalance;
                                payBalance = 0.0;           //未使用余额抵扣
                            }
                        } else {               //不使用余额进行支付，则全额支付
                            actualPayMoney = allProductPrice;
                            newUserBalance = userBalance;
                            payBalance = 0.0;               //未使用余额抵扣
                        }
                        //用于购买商品更新用户的积分和余额
                        Map<String, String> attachMap = Maps.newHashMap();
                        attachMap.put("integral", NumberUtil.getPointTowNumber(newUserIntegral).toString());
                        attachMap.put("balance", NumberUtil.getPointTowNumber(newUserBalance).toString());
                        attachMap.put("stock", NumberUtil.getPointTowNumber(newStock).toString());
                        attachMap.put("productId", productId);
                        attachMap.put("wxOrderId", wxOrderId);

                        //判断是否需要付钱
                        boolean isNeedPay = true;
                        if(actualPayMoney > 0){     //需要支付现金
                            isNeedPay = true;
                            orderStatus = "0";
                        } else {                    //余额已抵扣，不需要载支付现金
                            isNeedPay = false;
                            orderStatus = "1";
                        }
                        logger.info(
                                " 用户 uid : {}", uid,
                                " , 购买商品 productId : {}", productId,
                                " , 消费总额 : {}", allPayAmount,
                                " , 实际支付 : {}", actualPayMoney,
                                " , 积分消耗 : {}", allProductIntegral,
                                " , 是否使用余额抵扣 : {}", useBalanceFlag,
                                " , 抵扣余额 : {}", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0",
                                " , 是否使用积分抵扣 : {}", useIntegralFlag,
                                " , 抵扣积分 : {}", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0"
                        );
                        if(isNeedPay){
                            //准备获取支付相关的验签等数据
                            actualPayMoney = NumberUtil.getPointTowNumber(actualPayMoney);
                            String totalMoney = ((int) (actualPayMoney * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                            resultMap.putAll(WX_PublicNumberUtil.unifiedOrderForMiniProgram(
                                    nonce_str, body, out_trade_no,
                                    totalMoney, spbillCreateIp, NewMallCode.WX_PAY_NOTIFY_URL_wxPayNotifyForPurchaseProductInMiniProgram,
                                    openId, JSONObject.toJSONString(attachMap)
                            ));
                            if(resultMap.get("code").toString().equals((NewMallCode.SUCCESS.getNo()+""))){
                                //创建订单，状态设为待支付
                                Map<String, Object> orderMap = Maps.newHashMap();
                                orderMap.put("uid", uid);
                                orderMap.put("orderType", "purchaseProduct");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                                orderMap.put("productId", productId);
                                orderMap.put("productNum", productNumStr);
                                orderMap.put("transactionProductDetail", transactionProductDetailJson);
                                orderMap.put("addressId", addressId);
                                orderMap.put("allPayAmount", allPayAmount);
                                orderMap.put("payMoney", actualPayMoney);
                                orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                                orderMap.put("useIntegralNum",  useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                                orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                                orderMap.put("createTime", TimestampUtil.getTimestamp());
                                orderMap.put("updateTime", TimestampUtil.getTimestamp());
                                if(!"".equals(wxOrderId)){
                                    orderMap.put("wxOrderId", wxOrderId);
                                    BoolDTO updateOrderBoolDTO = this.updateOrder(orderMap);
                                    resultMapDTO.setCode(updateOrderBoolDTO.getCode());
                                    resultMapDTO.setMessage(updateOrderBoolDTO.getMessage());
                                } else {
                                    orderMap.put("wxOrderId", out_trade_no);
                                    BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                                    resultMapDTO.setCode(addOrderBoolDTO.getCode());
                                    resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                                }
                                //设置返回值
                                resultMap.put("dealFlag", true);
                            } else {
                                resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                                resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                            }
                        } else {
                            //创建订单，状态设为待支付
                            Map<String, Object> orderMap = Maps.newHashMap();
                            orderMap.put("uid", uid);
                            orderMap.put("orderType", "purchaseProduct");   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                            orderMap.put("productId", productId);
                            orderMap.put("productNum", productNumStr);
                            orderMap.put("transactionProductDetail", transactionProductDetailJson);
                            orderMap.put("addressId", addressId);
                            orderMap.put("allPayAmount", allPayAmount);
                            orderMap.put("payMoney", actualPayMoney);
                            orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                            orderMap.put("useIntegralNum", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                            orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                            orderMap.put("createTime", TimestampUtil.getTimestamp());
                            orderMap.put("updateTime", TimestampUtil.getTimestamp());
                            if(!"".equals(wxOrderId)){
                                orderMap.put("wxOrderId", wxOrderId);
                                BoolDTO updateOrderBoolDTO = this.updateOrder(orderMap);
                                resultMapDTO.setCode(updateOrderBoolDTO.getCode());
                                resultMapDTO.setMessage(updateOrderBoolDTO.getMessage());
                            } else {
                                orderMap.put("wxOrderId", out_trade_no);
                                BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                                resultMapDTO.setCode(addOrderBoolDTO.getCode());
                                resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                            }
                            //设置返回值
                            resultMap.put("dealFlag", true);
                            //更新用户积分和余额信息
                            Map<String, Object> updateMap = Maps.newHashMap();
                            if(!"".equals(wxOrderId)){
                                updateMap.put("wxOrderId", wxOrderId);
                            } else {
                                updateMap.put("wxOrderId", out_trade_no);
                            }
                            updateMap.put("openId", openId);
                            updateMap.put("attach", JSONObject.toJSONString(attachMap));
                            this.wxPayNotifyForPurchaseProductInMiniProgram(updateMap);
                        }
                    } else {
                        resultMapDTO.setCode(NewMallCode.ORDER_USER_INTEGRAL_IS_NOT_ENOUGH.getNo());
                        resultMapDTO.setMessage(NewMallCode.ORDER_USER_INTEGRAL_IS_NOT_ENOUGH.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.ORDER_PRODUCT_STOCK_IS_NOT_ENOUGH.getNo());
                    resultMapDTO.setMessage(NewMallCode.ORDER_PRODUCT_STOCK_IS_NOT_ENOUGH.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.USER_ID_OR_PRODUCTID_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.USER_ID_OR_PRODUCTID_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_SPBILLCREATEIP_OR_PRODUCTID_OR_PRODUCTNUM_OR_ADDRESSID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_SPBILLCREATEIP_OR_PRODUCTID_OR_PRODUCTNUM_OR_ADDRESSID_IS_NOT_NULL.getMessage());
        }
        //购买商品不允许进行抽奖
        resultMap.put("isLuckDrawFlag", false);
        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
        logger.info("【service】购买商品-purchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 购买商品成功后的回调通知
     * @param paramMap
     * @return
     */
    public ResultMapDTO wxPayNotifyForPurchaseProductInMiniProgram(Map<String, Object> paramMap) {
        logger.info("【service】购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String attach = paramMap.get("attach") != null ? paramMap.get("attach").toString() : "";
        String openId = paramMap.get("openid") != null ? paramMap.get("openid").toString() : "";
        Map<String, String> attachMap = JSONObject.parseObject(attach, Map.class);
        String wxOrderId = attachMap.get("wxOrderId");
        if (!"".equals(wxOrderId) && !"".equals(attach)
                && !"".equals(openId)) {
            //修改订单状态为已付款
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            //获取订单信息
            List<Map<String, Object>> orderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(orderList != null && orderList.size() > 0){
                String status = orderList.get(0).get("status").toString();
                if("0".equals(status)){     //只对待支付的订单在付款成功后变更为已支付
                    orderMap.clear();
                    orderMap.put("wxOrderId", wxOrderId);
                    orderMap.put("status", "1");            //订单状态: 0是待支付，1是已支付
                    updateNum = wxOrderDao.updateOrder(orderMap);
                    if (updateNum != null && updateNum > 0) {
                        String integral = attachMap.get("integral");
                        String balance = attachMap.get("balance");
                        String stock = attachMap.get("stock");
                        String productId = attachMap.get("productId");
                        //更新用户的积分和余额
                        Map<String, Object> userMap = Maps.newHashMap();
                        userMap.put("openId", openId);
                        userMap.put("integral", integral);
                        userMap.put("balance", balance);
                        updateNum = wxUserDao.updateUser(userMap);
                        if (updateNum != null && updateNum > 0) {
                            //更新商品的库存
                            Map<String, Object> productMap = Maps.newHashMap();
                            productMap.put("id", productId);
                            productMap.put("stock", stock);
                            updateNum = wxProductDao.updateProduct(productMap);
                            if (updateNum != null && updateNum > 0) {
                                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
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
                                resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                                resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
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
                    resultMapDTO.setCode(NewMallCode.ORDER_STATUS_IS_NOT_WAIT_PAY_STATUS.getNo());
                    resultMapDTO.setMessage(NewMallCode.ORDER_STATUS_IS_NOT_WAIT_PAY_STATUS.getMessage());
                }
            } else {
                //订单不存在
                resultMapDTO.setCode(NewMallCode.ORDER_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }


    /**
     * 直接买单或者点餐付款
     * @param paramMap
     * @return
     * @throws Exception
     */
    public ResultMapDTO payTheBillInMiniProgram(Map<String, Object> paramMap) throws Exception {
        logger.info("【service】买单-payTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("dealFlag", false);       //默认交易状态为失败
        resultMap.put("isLuckDrawFlag", false); //默认不允许抽奖
        //在点餐历史列表中重新支付
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        //支付的金额
        String payMoneyStr = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "0";
        //用于抵扣的积分
        String payIntegralStr = paramMap.get("payIntegral") != null ? paramMap.get("payIntegral").toString() : "0";
        //用于抵扣的余额
        String payBalanceStr = paramMap.get("payBalance") != null ? paramMap.get("payBalance").toString() : "0";
        //付款用户的uid
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        //收钱商家的店铺名称
        String shopTitle = paramMap.get("shopTitle") != null ? paramMap.get("shopTitle").toString() : "";
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
        if(!"".equals(shopTitle)){
            body = "向 " + shopTitle + " 商家付款买单";
        }
        //点餐的食物IDs
        String foodsId = paramMap.get("foodsId") != null ? paramMap.get("foodsId").toString() : "";
        //订单类型
        String orderType = "payTheBill";
        if(!"".equals(foodsId)){
            orderType = "payTheBillForMenu";
        }
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
                                //判断 准备抵扣的积分是否 超过最高可抵扣的 积分量
                                Double integralDeductionNum = 0.2;
                                Map<String, Object> dicParamMap = Maps.newHashMap();
                                dicParamMap.put("dicType", "deduction");
                                dicParamMap.put("dicCode", "integralDeduction");
                                ResultDTO dicResultDTO = wxDicService.getSimpleDicByCondition(dicParamMap);
                                if(dicResultDTO != null && dicResultDTO.getResultList() != null
                                        && dicResultDTO.getResultList().size() > 0){
                                    Map<String, String> dicMap = dicResultDTO.getResultList().get(0);
                                    String deductionNumStr = dicMap.get("deductionNum");
                                    integralDeductionNum = Double.parseDouble(deductionNumStr);
                                    integralDeductionNum = NumberUtil.getPointTowNumber(integralDeductionNum);
                                } else {
                                    integralDeductionNum = 0.2;
                                }
                                Double integralDeductionMoney = payMoney * integralDeductionNum;
                                if(payIntegral > integralDeductionMoney){
                                    logger.warn("可能遭受外部链接攻击，准备抵扣的积分是 " + payIntegral + " 个，但是最高可抵扣的积分是 " + integralDeductionMoney + " 个.");
                                    logger.warn("已按照最高可抵扣积分进行抵扣，不会被薅羊毛了.");
                                    payIntegral = integralDeductionMoney;
                                }
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
                                //判断 准备抵扣的积分是否 超过最高可抵扣的 积分量
                                Double balanceDeductionNum = 0.2;
                                Map<String, Object> dicParamMap = Maps.newHashMap();
                                dicParamMap.put("dicType", "deduction");
                                dicParamMap.put("dicCode", "balanceDeduction");
                                ResultDTO dicResultDTO = wxDicService.getSimpleDicByCondition(dicParamMap);
                                if(dicResultDTO != null && dicResultDTO.getResultList() != null
                                        && dicResultDTO.getResultList().size() > 0){
                                    Map<String, String> dicMap = dicResultDTO.getResultList().get(0);
                                    String deductionNumStr = dicMap.get("deductionNum");
                                    balanceDeductionNum = Double.parseDouble(deductionNumStr);
                                    balanceDeductionNum = NumberUtil.getPointTowNumber(balanceDeductionNum);
                                } else {
                                    balanceDeductionNum = 0.2;
                                }
                                Double balanceDeductionMoney = payMoney * balanceDeductionNum;
                                if(payBalance > balanceDeductionMoney){
                                    logger.warn("可能遭受外部链接攻击，准备抵扣的余额是 " + payBalance + " 元，但是最高可抵扣的余额是 " + balanceDeductionMoney + " 元.");
                                    logger.warn("已按照最高可抵扣余额进行抵扣，不会被薅羊毛了.");
                                    payBalance = balanceDeductionMoney;
                                }
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
                    //用于购买商品更新付款用户的积分和余额,同时将店铺ID传递过去，便于给店铺的商家打钱
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
                    if(!"".equals(wxOrderId)){
                        resultMap.put("out_trade_no", wxOrderId);
                    } else {
                        resultMap.put("out_trade_no", out_trade_no);
                    }
                    if(isNeedPay){
                        //准备获取支付相关的验签等数据
                        actualPayMoney = NumberUtil.getPointTowNumber(actualPayMoney);
                        String total_fee = ((int) (actualPayMoney * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                        logger.info("支付费用(转化前) payMoney = {}" + actualPayMoney + ", 支付费用(转化后) total_fee = {}" + total_fee);
                        resultMap.putAll(WX_PublicNumberUtil.unifiedOrderForMiniProgram(
                                nonce_str, body, out_trade_no,
                                total_fee, spbillCreateIp, NewMallCode.WX_PAY_NOTIFY_URL_wxPayNotifyForPayTheBillInMiniProgram,
                                openId, JSONObject.toJSONString(attachMap)
                        ));
                        //=====================暂时============================
                        //=====================暂时============================
                        //=====================暂时============================
                        resultMap.put("code", NewMallCode.SUCCESS.getNo());
                        //=====================暂时============================
                        //=====================暂时============================
                        //=====================暂时============================
                        if(resultMap.get("code").toString().equals((NewMallCode.SUCCESS.getNo()+""))){
                            //创建订单，状态设为待支付
                            Map<String, Object> orderMap = Maps.newHashMap();
                            orderMap.put("uid", uid);
                            orderMap.put("orderType", orderType);   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                            //点餐信息
                            orderMap.put("foodsId", paramMap.get("foodsId"));
                            orderMap.put("foodsNum", paramMap.get("foodsNum"));
                            orderMap.put("transactionFoodsDetail", paramMap.get("transactionFoodsDetail"));
                            orderMap.put("remark", paramMap.get("remark"));
                            orderMap.put("shopId", shopId);
                            orderMap.put("allPayAmount", allPayAmount);
                            orderMap.put("payMoney", actualPayMoney);
                            orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                            orderMap.put("useIntegralNum", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                            orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                            orderMap.put("createTime", TimestampUtil.getTimestamp());
                            orderMap.put("updateTime", TimestampUtil.getTimestamp());
                            if(!"".equals(wxOrderId)){
                                orderMap.put("wxOrderId", wxOrderId);
                                BoolDTO updateOrderBoolDTO = this.updateOrder(orderMap);
                                //设置返回值
                                resultMap.put("dealFlag", true);
                                resultMap.put("isLuckDrawFlag", true); //可以抽奖
                                resultMapDTO.setCode(updateOrderBoolDTO.getCode());
                                resultMapDTO.setMessage(updateOrderBoolDTO.getMessage());
                            } else {
                                orderMap.put("wxOrderId", out_trade_no);
                                BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                                //设置返回值
                                resultMap.put("dealFlag", true);
                                resultMap.put("isLuckDrawFlag", true); //可以抽奖
                                resultMapDTO.setCode(addOrderBoolDTO.getCode());
                                resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                            }
                        } else {
                            resultMap.put("dealFlag", false);
                            resultMap.put("isLuckDrawFlag", false); //不可以抽奖
                            resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                        }
                    } else {
                        //创建订单，状态设为已支付
                        Map<String, Object> orderMap = Maps.newHashMap();
                        orderMap.put("uid", uid);
                        orderMap.put("wxOrderId", out_trade_no);
                        orderMap.put("orderType", orderType);   //订单类型：买单，payTheBill；购买商品：purchaseProduct
                        orderMap.put("shopId", shopId);

                        //点餐信息
                        orderMap.put("foodsId", paramMap.get("foodsId"));
                        orderMap.put("foodsNum", paramMap.get("foodsNum"));
                        orderMap.put("transactionFoodsDetail", paramMap.get("transactionFoodsDetail"));
                        orderMap.put("remark", paramMap.get("remark"));

                        orderMap.put("allPayAmount", allPayAmount);
                        orderMap.put("payMoney", actualPayMoney);
                        orderMap.put("useBalanceMonney", useBalanceFlag?NumberUtil.getPointTowNumber(payBalance):"0.0");
                        orderMap.put("useIntegralNum", useIntegralFlag?NumberUtil.getPointTowNumber(payIntegral):"0.0");
                        orderMap.put("status", orderStatus);                //订单状态: 0是待支付，1是已支付
                        orderMap.put("createTime", TimestampUtil.getTimestamp());
                        orderMap.put("updateTime", TimestampUtil.getTimestamp());
                        if(!"".equals(wxOrderId)){
                            orderMap.put("wxOrderId", wxOrderId);
                            BoolDTO updateOrderBoolDTO = this.updateOrder(orderMap);
                            resultMapDTO.setCode(updateOrderBoolDTO.getCode());
                            resultMapDTO.setMessage(updateOrderBoolDTO.getMessage());
                        } else {
                            orderMap.put("wxOrderId", out_trade_no);
                            BoolDTO addOrderBoolDTO = this.addOrder(orderMap);
                            resultMapDTO.setCode(addOrderBoolDTO.getCode());
                            resultMapDTO.setMessage(addOrderBoolDTO.getMessage());
                        }
                        //更新用户积分和余额信息
                        Map<String, Object> updateMap = Maps.newHashMap();
                        if(!"".equals(wxOrderId)){
                            orderMap.put("out_trade_no", wxOrderId);
                        } else {
                            orderMap.put("out_trade_no", out_trade_no);
                        }
                        updateMap.put("openId", openId);
                        updateMap.put("attach", JSONObject.toJSONString(attachMap));
                        this.wxPayNotifyForPayTheBillInMiniProgram(updateMap);
                        //设置返回值
                        resultMap.put("dealFlag", true);
                        resultMap.put("isLuckDrawFlag", true); //可以抽奖
                        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
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

        //只要使用余额或者积分进行支付均布允许进行抽奖
        if(useBalanceFlag || useIntegralFlag){
            resultMap.put("isLuckDrawFlag", false);
        } else {
            resultMap.put("isLuckDrawFlag", true);
        }
        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
        logger.info("【service】买单-payTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 直接买单或者点餐付款成功后的回调通知
     * @param paramMap
     * @return
     */
    public ResultMapDTO wxPayNotifyForPayTheBillInMiniProgram(Map<String, Object> paramMap) {
        logger.info("【service】买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String wxOrderId = paramMap.get("out_trade_no") != null ? paramMap.get("out_trade_no").toString() : "";
        String attach = paramMap.get("attach") != null ? paramMap.get("attach").toString() : "";
        String openId = paramMap.get("openid") != null ? paramMap.get("openid").toString() : "";
        if (!"".equals(wxOrderId) && !"".equals(attach)
                && !"".equals(openId)) {
            //修改订单状态为已付款
            Map<String, Object> orderMap = Maps.newHashMap();
            orderMap.put("wxOrderId", wxOrderId);
            //获取订单信息
            List<Map<String, Object>> orderList = wxOrderDao.getSimpleOrderByCondition(orderMap);
            if(orderList != null && orderList.size() > 0){
                String status = orderList.get(0).get("status").toString();
                if("0".equals(status)){     //只对待支付的订单在付款成功后变更为已支付
                    orderMap.clear();
                    orderMap.put("wxOrderId", wxOrderId);
                    orderMap.put("status", "1");            //订单状态: 0是待支付，1是已支付
                    updateNum = wxOrderDao.updateOrder(orderMap);
                    if (updateNum != null && updateNum > 0) {
                        Map<String, String> attachMap = JSONObject.parseObject(attach, Map.class);
                        String balance = attachMap.get("balance");
                        String integral = attachMap.get("integral");
                        String shopId = attachMap.get("shopId");
                        String payMoneyStr = attachMap.get("payMoney");      //付款用户支付的金额
                        //更新用户和余额
                        Map<String, Object> userMap = Maps.newHashMap();
                        userMap.put("openId", openId);
                        userMap.put("balance", balance);
                        userMap.put("integral", integral);
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
                                                enterprisePaymentMap.put("wxPublicNumGhId", "gh_97b78683d2c9");
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
                    resultMapDTO.setCode(NewMallCode.ORDER_STATUS_IS_NOT_WAIT_PAY_STATUS.getNo());
                    resultMapDTO.setMessage(NewMallCode.ORDER_STATUS_IS_NOT_WAIT_PAY_STATUS.getMessage());
                }
            } else {
                //订单不存在
                resultMapDTO.setCode(NewMallCode.ORDER_WXORDERID_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_WXORDERID_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取所有的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getAllPayGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】获取所有的商品订单-getWaitPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> orderStrList = Lists.newArrayList();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(uid)){
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
        logger.info("【service】获取所有的商品订单-getWaitPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 对商品订单进行确认收货
     * @param paramMap
     * @return
     */
    public BoolDTO confirmReceiptGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】对商品订单进行确认收货-confirmReceiptGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】对商品订单进行确认收货-confirmReceiptGoodsOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取待支付的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getWaitPayGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】获取待支付的商品订单-getWaitPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】获取待支付的商品订单-getWaitPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已支付的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getAlreadyPayGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】获取已支付的商品订单-getAlreadyPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】获取已支付的商品订单-getAlreadyPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已发货的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getAlreadyDeliverGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】获取已发货的商品订单-getAlreadyDeliverGoods,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】获取已发货的商品订单-getAlreadyDeliverGoods,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已完成的商品订单
     * @param paramMap
     * @return
     */
    public ResultDTO getCompletedGoodsOrder(Map<String, Object> paramMap) {
        logger.info("【service】获取已完成的商品订单-getCompletedGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】获取已完成的商品订单-getCompletedGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 修改订单
     * @param paramMap
     * @return
     */
    public BoolDTO updateOrder(Map<String, Object> paramMap) {
        logger.info("【service】修改订单-updateOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String wxOrderId = paramMap.get("wxOrderId") != null ? paramMap.get("wxOrderId").toString() : "";
        if (!"".equals(id) || !"".equals(wxOrderId)) {
            updateNum = wxOrderDao.updateOrder(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ORDER_ID_OR_WXORDERID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_OR_WXORDERID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改订单-updateOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }


    /**
     * 添加订单
     * @param paramMap
     * @return
     */
    public BoolDTO addOrder(Map<String, Object> paramMap) {
        logger.info("【service】添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
        logger.info("【service】添加订单-addOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

}