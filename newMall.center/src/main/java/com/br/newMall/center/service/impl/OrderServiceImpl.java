package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.OrderService;
import com.br.newMall.center.utils.wxpay.WXPay;
import com.br.newMall.center.utils.wxpay.WXPayConfigImpl;
import com.br.newMall.center.utils.wxpay.WXPayConstants;
import com.br.newMall.center.utils.wxpay.WXPayUtil;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单service
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 使用统一订单的方式进行下订单，发起微信支付
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO requestWxPayUnifiedOrder(Map<String, Object> paramMap) throws Exception {
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
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getMessage());
            } else {
                float payMoneyFloat = Float.parseFloat(payMoney != "" ? payMoney : "10");
                String total_fee = ((int) (payMoneyFloat * 100)) + "";                           //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败，默认付款1元
                System.out.println("================================================");
                System.out.println("================================================");
                System.out.println("================================================");
                System.out.println("支付费用 payMoney = " + payMoney);
                System.out.println("支付费用 total_fee = " + total_fee);
                System.out.println("================================================");
                System.out.println("================================================");
                System.out.println("================================================");
                try {
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
                    System.out.println("统一订单返回请求：" + JSONObject.toJSONString(unifiedOrderResponseMap));
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
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的更新订单信息 **/
                        /** 此处添加自己的业务逻辑代码 **/
                        /** 此处添加自己的业务逻辑代码 **/
                        /** 此处添加自己的业务逻辑代码 **/
                        /** 此处添加自己的业务逻辑代码 **/
                        /** 此处添加自己的业务逻辑代码 **/
                        resultMapDTO.setResultMap(resultMap);
                        resultMapDTO.setSuccess(true);
                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        resultMapDTO.setSuccess(false);
                        resultMapDTO.setCode(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getNo());
                        resultMapDTO.setMessage(NewMallCode.ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR.getMessage());
                    }
                } catch (Exception e) {
                    logger.info("在service中使用统一订单的方式进行下订单，发起微信支付-wxPayUnifiedOrder is error, e : " + e);
                    resultMapDTO.setSuccess(false);
                    resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
                    resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
                }
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中使用统一订单的方式进行下订单，发起微信支付-wxPayUnifiedOrder,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

}
