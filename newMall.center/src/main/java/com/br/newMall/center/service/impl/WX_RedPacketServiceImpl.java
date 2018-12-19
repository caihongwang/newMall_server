package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.*;
import com.br.newMall.dao.WX_UserDao;
import com.br.newMall.dao.UserFormMappingDao;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.util.Map;

/**
 * 公共service
 *
 * 红包发送传参  请看 CommonServiceImpl
 */
@Service
public class WX_RedPacketServiceImpl implements WX_RedPacketService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private HttpsUtil httpsUtil;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private UserFormMappingDao userFormMappingDao;

    /**
     * 获取红包二维码
     */
    @Override
    public ResultMapDTO getRedPacketQrCode(Map<String, Object> paramMap) {
        logger.info("在service中获取红包二维码-getRedPacketQrCode,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        BASE64Encoder encoder = new BASE64Encoder();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String phone = paramMap.get("phone") != null ? paramMap.get("phone").toString() : "";
        if (paramMap.size() > 0 && !"".equals(uid) && !"".equals(phone)) {
            //二维码需要带的参数 QR_SCENE：临时二维码  QR_LIMIT_STR_SCENE：永久二维码
            String sceneStr = "{\"uid\": \"2\",\"phone\": \"17701359899\"}";
            sceneStr = encoder.encode(sceneStr.getBytes());
            String paramStr = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\":{\"scene_str\": \"" + sceneStr + "\"}}}";
            String qrCodeImg_url = WX_PublicNumberUtil.getRedPacketQrCode(paramStr);
            resultMap.put("qrCodeImg_url", qrCodeImg_url);
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取红包二维码-getRedPacketQrCode,响应-response:" + resultMapDTO);
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
                        wxUserDao.updateUser(userMap);
                    }
                }
                resultMap.put("openId", openId);
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
        String calbackUrl = NewMallCode.WX_PAY_DOMAIN + "/wx_RedPacket/getOauth.do";
        String toOauthUrl = WX_PublicNumberUtil.getAuthorizeURL(NewMallCode.WX_PUBLIC_NUMBER_APPID, calbackUrl, "111", false);
        resultMap.put("toOauthUrl", toOauthUrl);
        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        return resultMapDTO;
    }


    /**
     * 企业付款，直达微信零钱
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO enterprisePayment(Map<String, Object> paramMap) {
        logger.info("在service中企业付款-enterprisePayment,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //1.先获取红包活动详情
        String wxPublicNumGhId = paramMap.get("wxPublicNumGhId") != null ? paramMap.get("wxPublicNumGhId").toString() : "";
        if (paramMap.size() > 0 && !"".equals(wxPublicNumGhId)) {
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicType", "cashBackActivity");
            dicParamMap.put("dicCode", wxPublicNumGhId);
            ResultDTO dicResultDto = wxDicService.getSimpleDicByCondition(dicParamMap);
            if (dicResultDto.getResultList() != null
                    && dicResultDto.getResultList().size() > 0) {
                paramMap.putAll(dicResultDto.getResultList().get(0));
                // 2.准备参数使用企业付款进行发送红包
                // 商户相关资料
                String mchAppId = paramMap.get("wxPublicNumberAppId").toString();
                // 微信支付分配的商户号
                String mchId = paramMap.get("wxPayMchId").toString();
                //随机字符串
                String nonceStr = System.currentTimeMillis() / 1000 + "";
                // 商户订单号
                String partnerTradeNo = System.currentTimeMillis() + "";
                // 收款用户在wxappid下的openid
                String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "or4Gy0yPbrBJeWAUMDPR7mzUxmvg";
                //校验用户姓名选项：NO_CHECK：不校验真实姓名；FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
                String checkName = paramMap.get("checkName") != null ? paramMap.get("checkName").toString() : "NO_CHECK";
                // 收款用户姓名（可选）使用昵称代替
                String reUserName = paramMap.get("reUserName") != null ? paramMap.get("reUserName").toString() : "caihongwang976499921";
                // 金额 单位：分
                String amount = paramMap.get("amount") != null ? paramMap.get("amount").toString() : "100";
                // 企业付款描述信息
                String desc = paramMap.get("desc") != null ? paramMap.get("desc").toString() : "薅羊毛新口子：蔡老板的公众号撒钱了";
                // 请求IP
                String spbillCreateIp = paramMap.get("spbillCreateIp") != null ? paramMap.get("spbillCreateIp").toString() : "127.0.0.1";
                // 微信证书路径
                String certPath = paramMap.get("wxPayCertPath").toString();
                // API密钥
                String paternerKey = paramMap.get("wxPayApiSecert").toString();
                resultMapDTO = WX_PublicNumberUtil.enterprisePayment(
                        mchAppId, mchId, nonceStr,
                        partnerTradeNo, openId, checkName,
                        reUserName, amount, desc,
                        spbillCreateIp, paternerKey, certPath);
            } else {
                resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中企业付款-enterprisePayment,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送普通红包
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendRedPacket(Map<String, Object> paramMap) {
        logger.info("在service中发送普通红包-sendRedPack,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //1.检查用户的红包金额总数和领取次数
        //@TODO    待做....
        //@TODO    待做....
        //@TODO    待做....
        //2.先获取红包活动详情
        String wxPublicNumGhId = paramMap.get("wxPublicNumGhId") != null ? paramMap.get("wxPublicNumGhId").toString() : "";
        if (paramMap.size() > 0 && !"".equals(wxPublicNumGhId)) {
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicType", "redPacketActivity");
            dicParamMap.put("dicCode", wxPublicNumGhId);
            ResultDTO dicResultDto = wxDicService.getSimpleDicByCondition(dicParamMap);
            if (dicResultDto.getResultList() != null
                    && dicResultDto.getResultList().size() > 0) {
                paramMap.putAll(dicResultDto.getResultList().get(0));
                //3.准备参数开始发送红包
                //随机字符串
                String nonceStr = System.currentTimeMillis() / 1000 + "";
                // 订单号
                String mchBillNo = System.currentTimeMillis() + "";
                // 微信支付分配的商户号
                String mchId = NewMallCode.WX_PAY_MCH_ID;
                //商户相关资料
                String wxAppId = NewMallCode.WX_PUBLIC_NUMBER_APPID;
                //商户名称:红包发送者名称
                String sendName = paramMap.get("sendName") != null ? paramMap.get("sendName").toString() : "【蔡老板】";
                //接受红包的用户openId
                String reOpenId = paramMap.get("reOpenId") != null ? paramMap.get("reOpenId").toString() : "or4Gy0yPbrBJeWAUMDPR7mzUxmvg";
                //付款金额,单位是分
                String totalAmount = paramMap.get("totalAmount") != null ? paramMap.get("totalAmount").toString() : "100";
                //红包发放总人数
                String totalNum = NewMallCode.WX_RED_PACK_NUMBER;
                //红包祝福语
                String wishing = paramMap.get("wishing") != null ? paramMap.get("wishing").toString() : "感谢蔡老板撒钱，祝您加班越精神！";
                //请求IP
                String clientIp = paramMap.get("clientIp") != null ? paramMap.get("clientIp").toString() : "127.0.0.1";
                //活动名称
                String actName = paramMap.get("actName") != null ? paramMap.get("actName").toString() : "【蔡老板撒钱活动】";
                //备注
                String remark = paramMap.get("remark") != null ? paramMap.get("remark").toString() : "分享得多，抢得越多！";
                //API密钥
                String paternerKey = NewMallCode.WX_PAY_API_SECRET;
                //微信证书路径
                String certPath = NewMallCode.WX_PAY_CERT_PATH;
                //发送红包
                boolean isSend = WX_PublicNumberUtil.sendRedPack(
                        nonceStr, mchBillNo, mchId,
                        wxAppId, sendName, reOpenId, totalAmount,
                        totalNum, wishing, clientIp, actName,
                        remark, paternerKey, certPath);
                if (isSend) {
                    resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__SEND_SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__SEND_SUCCESS.getMessage());
                } else {
                    resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__SEND_FAILTURE.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__SEND_FAILTURE.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中发送普通红包-sendRedPack,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送分裂红包
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendGroupRedPacket(Map<String, Object> paramMap) {
        logger.info("在service中发送裂变红包-sendRedPack,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //1.检查用户的红包金额总数和领取次数
        //@TODO    待做....
        //@TODO    待做....
        //@TODO    待做....
        //2.先获取红包活动详情
        String wxPublicNumGhId = paramMap.get("wxPublicNumGhId") != null ? paramMap.get("wxPublicNumGhId").toString() : "";
        if (paramMap.size() > 0 && !"".equals(wxPublicNumGhId)) {
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicType", "redPacketActivity");
            dicParamMap.put("dicCode", wxPublicNumGhId);
            ResultDTO dicResultDto = wxDicService.getSimpleDicByCondition(dicParamMap);
            if (dicResultDto.getResultList() != null
                    && dicResultDto.getResultList().size() > 0) {
                paramMap.putAll(dicResultDto.getResultList().get(0));
                //3.准备参数开始发送红包
                //随机字符串
                String nonceStr = System.currentTimeMillis() / 1000 + "";
                // 订单号
                String mchBillNo = System.currentTimeMillis() + "";
                // 微信支付分配的商户号
                String mchId = NewMallCode.WX_PAY_MCH_ID;
                //商户相关资料
                String wxAppId = NewMallCode.WX_PUBLIC_NUMBER_APPID;
                //商户名称:红包发送者名称
                String sendName = paramMap.get("sendName") != null ? paramMap.get("sendName").toString() : "【蔡老板】";
                //接受红包的用户openId
                String reOpenId = paramMap.get("reOpenId") != null ? paramMap.get("reOpenId").toString() : "or4Gy0yPbrBJeWAUMDPR7mzUxmvg";
                //付款金额,单位是分
                String totalAmount = paramMap.get("totalAmount") != null ? paramMap.get("totalAmount").toString() : "100";
                //红包金额设置方式
                String amtType = "ALL_RAND";
                //红包发放总人数
                String totalNum = NewMallCode.WX_RED_PACK_NUMBER;
                //红包祝福语
                String wishing = paramMap.get("wishing") != null ? paramMap.get("wishing").toString() : "感谢蔡老板撒钱，祝您加班越精神！";
                //活动名称
                String actName = paramMap.get("actName") != null ? paramMap.get("actName").toString() : "【蔡老板撒钱活动】";
                //备注
                String remark = paramMap.get("remark") != null ? paramMap.get("remark").toString() : "分享得多，抢得越多！";
                //API密钥
                String paternerKey = NewMallCode.WX_PAY_API_SECRET;
                //微信证书路径
                String certPath = NewMallCode.WX_PAY_CERT_PATH;
                //发送红包
                boolean isSend = WX_PublicNumberUtil.sendRedPack(
                         nonceStr, mchBillNo, mchId,
                         wxAppId, sendName, reOpenId, totalAmount,
                         totalNum, amtType, wishing, actName,
                         remark, paternerKey, certPath);
                if (isSend) {
                    resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__SEND_SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__SEND_SUCCESS.getMessage());
                } else {
                    resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__SEND_FAILTURE.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__SEND_FAILTURE.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中发送裂变红包-sendRedPack,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }
}
