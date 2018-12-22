package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_OrderHandler;
import com.br.newMall.web.utils.HttpUtil;
import com.br.newMall.web.utils.PayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 订单Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxOrder", produces = "application/json;charset=utf-8")
public class WX_OrderController {

    private static final Logger logger = LoggerFactory.getLogger(WX_OrderController.class);

    @Autowired
    private WX_OrderHandler.Client wxOrderHandler;

    /**
     * 购买商品
     * @param request
     * @return
     */
    @RequestMapping("/purchaseProductInMiniProgram")
    @ResponseBody
    public Map<String, Object> purchaseProductInMiniProgram(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        //获取本机的ip地址
        paramMap.put("spbillCreateIp", HttpUtil.getIpAddr(request));
        logger.info("在【controller】中购买商品-purchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxOrderHandler.payTheBillInMiniProgram(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中买单-purchaseProductInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中购买商品-purchaseProductInMiniProgram,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 购买商品成功后的回调通知，注：此接口不对外开放
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/wxPayNotifyForPurchaseProductInMiniProgram")
    @ResponseBody
    public void wxPayNotifyForPurchaseProductInMiniProgram(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        //参数讲解：
            //01.appid                  微信分配的小程序ID
            //02.mch_id                 微信支付分配的商户号
            //03.device_info            微信支付分配的终端设备号
            //04.nonce_str              随机字符串，不长于32位
            //05.sign                   签名
            //06.sign_type              签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
            //07.result_code            SUCCESS/FAIL
            //08.err_code               错误返回的信息描述
            //09.err_code_des           错误返回的信息描述
            //10.openid                 用户在商户appid下的唯一标识
            //11.is_subscribe           用户是否关注公众账号，Y-关注，N-未关注
            //12.trade_type             JSAPI、NATIVE、APP
            //13.bank_type              银行类型，采用字符串类型的银行标识，银行类型见银行列表
            //14.total_fee              订单总金额，单位为分
            //15.settlement_total_fee   应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
            //16.fee_type               货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
            //17.cash_fee               现金支付金额订单现金支付金额，详见支付金额
            //18.cash_fee_type          货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
            //19.coupon_fee             代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
            //20.coupon_count           代金券使用数量
            //21.coupon_type_$n         CASH--充值代金券 NO_CASH---非充值代金券 并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
            //22.coupon_id_$n           代金券ID,$n为下标，从0开始编号
            //23.coupon_fee_$n          单个代金券支付金额,$n为下标，从0开始编号
            //24.transaction_id         微信支付订单号
            //25.fee_type               货币类型
            //26.out_trade_no           商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
            //27.attach                 商家数据包，原样返回
            //28.time_end               支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        logger.info("在【controller】中购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //SB微信返回的xml格式
        String notityXml = sb.toString();
        String resXml = "";
        paramMap.clear();
        paramMap = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) paramMap.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确，确定是不是当前小程序对应的商户发起的订单
            if (PayUtil.verify(PayUtil.createLinkString(paramMap), (String) paramMap.get("sign"), NewMallCode.WX_PAY_API_SECRET, "utf-8")) {
                /**此处添加自己的业务逻辑代码start**/
                ResultMapDTO resultMapDTO = wxOrderHandler.wxPayNotifyForPurchaseProductInMiniProgram(0, paramMap);
                /**此处添加自己的业务逻辑代码end**/
            }
            resultMap.putAll(paramMap);
        } else {
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", "报文为空");
        }
        logger.info("在【controller】中购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return;
    }

    /**
     * 买单
     * @param request
     * @return
     */
    @RequestMapping("/payTheBillInMiniProgram")
    @ResponseBody
    public Map<String, Object> payTheBillInMiniProgram(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        //获取本机的ip地址
        paramMap.put("spbillCreateIp", HttpUtil.getIpAddr(request));
        logger.info("在【controller】中买单-payTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            ResultMapDTO resultMapDTO = wxOrderHandler.payTheBillInMiniProgram(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中买单-payTheBillInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中买单-payTheBillInMiniProgram,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 买单成功后的回调通知，注：此接口不对外开放
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/wxPayNotifyForPayTheBillInMiniProgram")
    @ResponseBody
    public void wxPayNotifyForPayTheBillInMiniProgram(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        //参数讲解：
            //01.appid                  微信分配的小程序ID
            //02.mch_id                 微信支付分配的商户号
            //03.device_info            微信支付分配的终端设备号
            //04.nonce_str              随机字符串，不长于32位
            //05.sign                   签名
            //06.sign_type              签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
            //07.result_code            SUCCESS/FAIL
            //08.err_code               错误返回的信息描述
            //09.err_code_des           错误返回的信息描述
            //10.openid                 用户在商户appid下的唯一标识
            //11.is_subscribe           用户是否关注公众账号，Y-关注，N-未关注
            //12.trade_type             JSAPI、NATIVE、APP
            //13.bank_type              银行类型，采用字符串类型的银行标识，银行类型见银行列表
            //14.total_fee              订单总金额，单位为分
            //15.settlement_total_fee   应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
            //16.fee_type               货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
            //17.cash_fee               现金支付金额订单现金支付金额，详见支付金额
            //18.cash_fee_type          货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
            //19.coupon_fee             代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
            //20.coupon_count           代金券使用数量
            //21.coupon_type_$n         CASH--充值代金券 NO_CASH---非充值代金券 并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
            //22.coupon_id_$n           代金券ID,$n为下标，从0开始编号
            //23.coupon_fee_$n          单个代金券支付金额,$n为下标，从0开始编号
            //24.transaction_id         微信支付订单号
            //25.fee_type               货币类型
            //26.out_trade_no           商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
            //27.attach                 商家数据包，原样返回
            //28.time_end               支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        logger.info("在【controller】中买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //SB微信返回的xml格式
        String notityXml = sb.toString();
        String resXml = "";
        paramMap.clear();
        paramMap = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) paramMap.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确，确定是不是当前小程序对应的商户发起的订单
            if (PayUtil.verify(PayUtil.createLinkString(paramMap), (String) paramMap.get("sign"), NewMallCode.WX_PAY_API_SECRET, "utf-8")) {
                /**此处添加自己的业务逻辑代码start**/
                ResultMapDTO resultMapDTO = wxOrderHandler.wxPayNotifyForPayTheBillInMiniProgram(0, paramMap);
                /**此处添加自己的业务逻辑代码end**/
            }
            resultMap.putAll(paramMap);
        } else {
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", "报文为空");
        }
        logger.info("在【controller】中买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return;
    }

    /**
     * 添加订单
     * @param request
     * @return
     */
    @RequestMapping("/addOrder")
    @ResponseBody
    public Map<String, Object> addOrder(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxOrderHandler.addOrder(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中添加订单-addOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中添加订单-addOrder,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除订单
     * @param request
     * @return
     */
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public Map<String, Object> deleteOrder(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中删除订单-deleteOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxOrderHandler.deleteOrder(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中删除订单-deleteOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中删除订单-deleteOrder,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改订单
     * @param request
     * @return
     */
    @RequestMapping("/updateOrder")
    @ResponseBody
    public Map<String, Object> updateOrder(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中修改订单-updateOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxOrderHandler.updateOrder(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中修改订单-updateOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中修改订单-updateOrder,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的订单
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleOrderByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleOrderByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取单一的订单-getSimpleOrderByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
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
            ResultDTO resultDTO = wxOrderHandler.getSimpleOrderByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取单一的订单-getSimpleOrderByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取单一的订单-getSimpleOrderByCondition,响应-resultMap = {}", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
