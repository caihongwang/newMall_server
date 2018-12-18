package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_OrderService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.wxpay.WXPay;
import com.br.newMall.center.utils.wxpay.WXPayConfigImpl;
import com.br.newMall.center.utils.wxpay.WXPayConstants;
import com.br.newMall.center.utils.wxpay.WXPayUtil;
import com.br.newMall.dao.WX_OrderDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 订单Service
 * @author caihongwang
 */
@Service
public class WX_OrderServiceImpl implements WX_OrderService {

    private static final Logger logger = LoggerFactory.getLogger(WX_OrderServiceImpl.class);

    @Autowired
    private WX_OrderDao wxOrderDao;

    /**
     * 买单
     * @param paramMap
     * @return
     * @throws Exception
     */
    @Override
    public ResultMapDTO payTheBillInMiniProgram(Map<String, Object> paramMap) throws Exception {
        logger.info("在【service】中买单-payTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = new HashMap<String, String>();
        String payMoney = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "";
        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
        WXPay wxpay = new WXPay(config);
        String nonce_str = WXPayUtil.generateUUID();        //生成的随机字符串
        String body = "小程序内发起支付";                     //商品名称
        String out_trade_no = WXPayUtil.generateUUID();     //统一订单编号
        String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "";      //获取发起支付的IP地址
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";
        if (!"".equals(openId) && !"".equals(spbillCreateIp)) {
            if ("".equals(payMoney)) {
                resultMapDTO.setCode(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getMessage());
            } else {
                //支付费用，默认一角钱
                float payMoneyFloat = Float.parseFloat(payMoney != "" ? payMoney : "10");
                String total_fee = ((int) (payMoneyFloat * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                logger.info("支付费用(转化前) payMoney = {}" + payMoney + ", 支付费用(转化后) total_fee = {}" + total_fee);
                Map<String, String> packageParams = new HashMap<String, String>();
                packageParams.put("appid", NewMallCode.WX_MINI_PROGRAM_APPID);
                packageParams.put("mch_id", NewMallCode.WX_PAY_MCH_ID);
                packageParams.put("nonce_str", nonce_str);
                packageParams.put("body", body);
                packageParams.put("out_trade_no", out_trade_no);//商户订单号
                packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
                packageParams.put("spbill_create_ip", spbillCreateIp);
                packageParams.put("notify_url", spbillCreateIp + NewMallCode.WX_PAY_NOTIFY_URL);
                packageParams.put("trade_type", NewMallCode.WX_PAY_TRADE_TYPE);
                packageParams.put("openid", openId);
                packageParams.put("sign_type", WXPayConstants.MD5);
                Map<String, String> unifiedOrderResponseMap = wxpay.unifiedOrder(packageParams);            //向微信客户端发送统一订单请求
                logger.info("通过统一下单的方式请求，响应为 unifiedOrderResponseMap = {}" + JSONObject.toJSONString(unifiedOrderResponseMap));
                String return_code = (String) unifiedOrderResponseMap.get("return_code");           //返回状态码
                if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                    String prepay_id = (String) unifiedOrderResponseMap.get("prepay_id");//返回的预付单信息
                    resultMap.put("nonceStr", nonce_str);
                    resultMap.put("package", "prepay_id=" + prepay_id);
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    resultMap.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                    //获取unifiedOrderResponseMap需要的支付验证签名
                    Map<String, String> paramMap_temp = new HashMap<String, String>();
                    paramMap_temp.put("appId", NewMallCode.WX_MINI_PROGRAM_APPID);
                    paramMap_temp.put("timeStamp", timeStamp + "");
                    paramMap_temp.put("nonceStr", nonce_str);
                    paramMap_temp.put("package", "prepay_id=" + prepay_id);
                    paramMap_temp.put("signType", "MD5");
                    //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                    String paySign = WXPayUtil.generateSignature(paramMap_temp, config.getKey(), WXPayConstants.SignType.MD5);
                    resultMap.put("paySign", paySign);
                    resultMap.put("appid", NewMallCode.WX_MINI_PROGRAM_APPID);
                    resultMapDTO.setResultMap(resultMap);
                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                    resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                }
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【service】中买单-payTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 添加订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String payMoney = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "";
        if (!"".equals(uid) && !"".equals(payMoney)) {
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

    /**
     * 删除订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中删除订单-deleteOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxOrderDao.deleteOrder(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ORDER_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中删除订单-deleteOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中修改订单-updateOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxOrderDao.updateOrder(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ORDER_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中修改订单-updateOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的订单信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleOrderByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的订单-getSimpleOrderByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> productStrList = Lists.newArrayList();
        List<Map<String, Object>> productList = wxOrderDao.getSimpleOrderByCondition(paramMap);
        if (productList != null && productList.size() > 0) {
            productStrList = MapUtil.getStringMapList(productList);
            Integer total = wxOrderDao.getSimpleOrderTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(productStrList);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
        }
        logger.info("在【service】中获取单一的订单-getSimpleOrderByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
