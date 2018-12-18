package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_PayService;
import com.br.newMall.center.utils.*;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * 公共service
 */
@Service
public class WX_PayServiceImpl implements WX_PayService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    /**
     * 统一订单支付请求
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO unifiedOrderPay(Map<String, Object> paramMap) {
        logger.info("在service中统一订单支付请求-unifiedOrderPay,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String payMoney = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "";
        String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "127.0.0.1";      //获取发起支付的IP地址
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";
        if (!"".equals(openId) && !"".equals(spbillCreateIp)) {
            if ("".equals(payMoney)) {
                resultMapDTO.setCode(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.ORDER_PAY_MONEY_IS_NOT_NULL.getMessage());
            } else {
                // 准备 统一订单支付方式的参数
                // 公众账号ID
                String appId = NewMallCode.WX_PUBLIC_NUMBER_APPID;
                // 微信支付分配的商户号
                String mchId = NewMallCode.WX_PAY_MCH_ID;
                // 商品描述
                String body = "小程序内发起支付";
                // 总金额
                float payMoneyFloat = Float.parseFloat(payMoney != "" ? payMoney : "10");
                String totalFee = ((int) (payMoneyFloat * 100)) + "";
                // 请求IP
                spbillCreateIp = spbillCreateIp;
                // 通知地址
                String notifyUrl = spbillCreateIp + NewMallCode.WX_PAY_NOTIFY_URL_wxPayNotifyForPayTheBillInMiniProgram;
                // 交易类型
                String tradeType = NewMallCode.WX_PAY_TRADE_TYPE;
                // 微信支付商户的api秘钥
                String apiSecret = NewMallCode.WX_PAY_API_SECRET;
                String unifiedOrderPayJson = WX_PayUtil.unifiedOrderPay(
                        appId, mchId, body ,
                        totalFee, spbillCreateIp , notifyUrl,
                        tradeType, openId, apiSecret);
                Map<String, Object> unifiedOrderPayMap = JSONObject.parseObject(unifiedOrderPayJson, Map.class);
                resultMapDTO.setResultMap(MapUtil.getStringMap(unifiedOrderPayMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中统一订单支付请求-unifiedOrderPay,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取oauth
     */
    @Override
    public ResultMapDTO getOauth(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = Maps.newHashMap();
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //用户同意授权，获取code
        String code = paramMap.get("code") != null ? paramMap.get("code").toString() : "";
        String state = paramMap.get("state") != null ? paramMap.get("state").toString() : "";
        if (!"".equals(code) && code != null) {
            String appId = NewMallCode.WX_PUBLIC_NUMBER_APPID;
            String secret = NewMallCode.WX_PUBLIC_NUMBER_SECRET;
            //通过code换取网页授权access_token
            Map<String, Object> snsAccessTokenMap = WX_PublicNumberUtil.getSnsAccessToken(appId, secret, code);
            String accessToken = snsAccessTokenMap.get("access_token") != null ? snsAccessTokenMap.get("access_token").toString() : "";
            String openId = snsAccessTokenMap.get("openid") != null ? snsAccessTokenMap.get("openid").toString() : "";
            if (!"".equals(accessToken) && !"".equals(openId)) {
                //拉取用户信息(需scope为 snsapi_userinfo)
                Map<String, Object> snsApiUserInfoMap = WX_PublicNumberUtil.getSnsApiUserInfo(accessToken, openId);
                logger.info("snsApiUserInfoMap: " + snsApiUserInfoMap);
                Integer errCode = snsApiUserInfoMap.get("errcode") != null ? Integer.parseInt(snsApiUserInfoMap.get("errcode").toString()) : null;
                if ((errCode == null || errCode.intValue() == 0)
                        && snsApiUserInfoMap.size() > 0) {
                    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
                    String country = snsApiUserInfoMap.get("country") != null ? snsApiUserInfoMap.get("country").toString() : "";//国家
                    String province = snsApiUserInfoMap.get("province") != null ? snsApiUserInfoMap.get("province").toString() : "";//省份
                    String city = snsApiUserInfoMap.get("city") != null ? snsApiUserInfoMap.get("city").toString() : "";//城市
                    String wxpOpenId = snsApiUserInfoMap.get("openid") != null ? snsApiUserInfoMap.get("openid").toString() : "";//openid
                    String gender = snsApiUserInfoMap.get("sex") != null ? snsApiUserInfoMap.get("sex").toString() : "";  //性别
                    String nickName = snsApiUserInfoMap.get("nickname") != null ? snsApiUserInfoMap.get("nickname").toString() : "";  //昵称
                    String avatarUrl = snsApiUserInfoMap.get("headimgurl") != null ? snsApiUserInfoMap.get("headimgurl").toString() : "";//头像
                    String language = snsApiUserInfoMap.get("language") != null ? snsApiUserInfoMap.get("language").toString() : "";//语言
                    String privilege = snsApiUserInfoMap.get("privilege") != null ? snsApiUserInfoMap.get("privilege").toString() : "";//特权
                    //获取用户信息判断是否关注
                    Map<String, Object> cgiBinUserInfoMap = WX_PublicNumberUtil.getCgiBinUserInfo(openId);
                    logger.info("cgiBinUserInfoMap = " + cgiBinUserInfoMap);
                    errCode = cgiBinUserInfoMap.get("errcode") != null ? Integer.parseInt(cgiBinUserInfoMap.get("errcode").toString()) : null;
                    if ((errCode == null || errCode.intValue() == 0)
                            && cgiBinUserInfoMap.size() > 0) {
                        //subscribe: 1表示已关注公众号，0表示未关注公众号
                        String wxpAttention = cgiBinUserInfoMap.get("subscribe") != null ? cgiBinUserInfoMap.get("subscribe").toString() : "";
                        //保存用户信息
                        Map<String, Object> userMap = Maps.newHashMap();
                        userMap.put("country", country);
                        userMap.put("province", province);
                        userMap.put("city", city);
                        userMap.put("gender", gender);
                        userMap.put("nickName", nickName);
                        userMap.put("avatarUrl", avatarUrl);
                        userMap.put("language", language);
                        userMap.put("avatarUrl", avatarUrl);
                        userMap.put("wxpAttention", wxpAttention);
                    }
                }
                resultMap.put("openId", openId);
                String paymentUrl = NewMallCode.WX_PAY_DOMAIN + "/payment.html?openId=" + openId;
                resultMap.put("paymentUrl", paymentUrl);
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        return resultMapDTO;
    }

    /**
     * 获取oauth的url
     */
    @Override
    public ResultMapDTO getToOauthUrl(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = Maps.newHashMap();
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String calbackUrl = NewMallCode.WX_PAY_DOMAIN + "/wx_Pay/getOauth.do";
        //准备参数
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
                oilStationName = URLDecoder.decode(oilStationName);
                paramMap_temp.put("oilStationName", oilStationName);
            }
            if(!"".equals(oilStationWxPaymentCodeImgUrl)){
                paramMap_temp.put("oilStationWxPaymentCodeImgUrl", oilStationWxPaymentCodeImgUrl);
            }
            if (paramMap_temp != null && !paramMap_temp.isEmpty()) {
                calbackUrl = calbackUrl + "?";
                Map.Entry entry;
                for (Iterator i$ = paramMap_temp.entrySet().iterator(); i$.hasNext(); calbackUrl = calbackUrl + (String) entry.getKey() + "=" + entry.getValue() + "&") {
                    entry = (Map.Entry) i$.next();
                }
                calbackUrl = calbackUrl.substring(0, calbackUrl.length() - 1);
            } else {
                calbackUrl = calbackUrl;
            }
            calbackUrl = URLEncoder.encode(calbackUrl, "UTF-8");
            String toOauthUrl = WX_PublicNumberUtil.getAuthorizeURL(NewMallCode.WX_PUBLIC_NUMBER_APPID, calbackUrl, "123", false);
            resultMap.put("toOauthUrl", toOauthUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        return resultMapDTO;
    }

}
