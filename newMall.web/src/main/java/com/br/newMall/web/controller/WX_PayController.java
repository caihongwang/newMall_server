package com.br.newMall.web.controller;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_PayHandler;
import com.br.newMall.web.utils.HttpUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx_Pay", produces = "application/json;charset=utf-8")
public class WX_PayController {

    private static final Logger logger = LoggerFactory.getLogger(WX_PayController.class);

    @Autowired
    private WX_PayHandler.Client wx_PayHandler;

    @RequestMapping("/getToOauthUrl")
    public String getToOauthUrl(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取oauth的url-getToOauthUrl,请求-paramMap:" + paramMap);
        String toOauthUrl = "https://www.91caihongwang.com/newMall/wx_Pay/getOauth.do";
        try {
            ResultMapDTO resultMapDTO = wx_PayHandler.getToOauthUrl(0, paramMap);
            Map<String, String> dataMap = resultMapDTO.getResultMap();
            toOauthUrl = dataMap.get("toOauthUrl");
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", dataMap);
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取oauth的url-getToOauthUrl is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取oauth的url-getToOauthUrl,响应-response:" + resultMap);
        logger.info("在controller中获取oauth的url-getToOauthUrl,重定向的链接toOauthUrl = " + toOauthUrl);
        //从定向到微信服务器，然后从微信服务器跳转到getOauth
        return "redirect:" + toOauthUrl;//重定向;
    }

    @RequestMapping("/getOauth")
    public String getOauth(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取oauth-getOauth,请求-paramMap:" + paramMap);
        String paymentUrl = "https://www.91caihongwang.com/newMall/payment.html";
        try {
            ResultMapDTO resultMapDTO = wx_PayHandler.getOauth(0, paramMap);
            Map<String, String> dataMap = resultMapDTO.getResultMap();
            paymentUrl = dataMap.get("paymentUrl");
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取oauth-getOauth is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        //整合参数
        logger.info("在controller中获取oauth-getOauth,重定向的付款链接，整合之前 paymentUrl = " + paymentUrl);
        try{
            Map<String, String> paramMap_temp = Maps.newHashMap();
            String lon = paramMap.get("lon")!=null?paramMap.get("lon").toString():"";
            String lat = paramMap.get("lat")!=null?paramMap.get("lat").toString():"";
            String oilStationName = paramMap.get("oilStationName")!=null?paramMap.get("oilStationName").toString():"";
            String oilStationWxPaymentCodeImgUrl = paramMap.get("oilStationWxPaymentCodeImgUrl")!=null?paramMap.get("oilStationWxPaymentCodeImgUrl").toString():"";
            if(!"".equals(lon)){
                paramMap_temp.put("lon", lon);
            }
            if(!"".equals(lat)){
                paramMap_temp.put("lat", lat);
            }
            if(!"".equals(oilStationName)){
                logger.info("油站编码之前： oilStationName = " + oilStationName);
                oilStationName = URLEncoder.encode(oilStationName);
                logger.info("油站编码之后： oilStationName = " + oilStationName);
                paramMap_temp.put("oilStationName", oilStationName);
            }
            if(!"".equals(oilStationWxPaymentCodeImgUrl)){
                paramMap_temp.put("oilStationWxPaymentCodeImgUrl", oilStationWxPaymentCodeImgUrl);
            }
            if(paymentUrl.endsWith("?")){
                paymentUrl = paymentUrl.substring(0, paymentUrl.length() - 1);
            }
            if (paramMap_temp != null && !paramMap_temp.isEmpty()) {
                paymentUrl = paymentUrl + "&";
                Map.Entry entry;
                for (Iterator i$ = paramMap_temp.entrySet().iterator(); i$.hasNext(); paymentUrl = paymentUrl + (String) entry.getKey() + "=" + URLDecoder.decode((String) entry.getValue(), "UTF-8") + "&") {
                    entry = (Map.Entry) i$.next();
                }
                paymentUrl = paymentUrl.substring(0, paymentUrl.length() - 1);
            } else {
                paymentUrl = paymentUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("在controller中获取oauth-getOauth,重定向的付款链接，整合之后 paymentUrl = " + paymentUrl);
        logger.info("在controller中获取oauth-getOauth,响应-response:" + resultMap);
        return "redirect:" + paymentUrl;//重定向;
    }

    @RequestMapping("/unifiedOrderPay")
    @ResponseBody
    public Map<String, Object> unifiedOrderPay(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中统一订单支付请求-unifiedOrderPay,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_PayHandler.unifiedOrderPay(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中统一订单支付请求-unifiedOrderPay is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中统一订单支付请求-unifiedOrderPay,响应-response:" + resultMap);
        return resultMap;
    }
}
