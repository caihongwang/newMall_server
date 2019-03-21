package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.*;
import com.br.newMall.center.utils.jsapi.SignUtil;
import com.br.newMall.dao.CustomMessageHistoryDao;
import com.br.newMall.dao.WX_DicDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Pattern;

import com.br.newMall.center.utils.MapUtil;
import sun.misc.BASE64Decoder;

/**
 * 公共service
 */
@Service
public class CommonServiceImpl implements CommonService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private WX_DicDao wxDicDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private HttpsUtil httpsUtil;

    @Value("${newMall.tmp.filepath}")
    private String filePath;

    @Autowired
    private CustomMessageHistoryDao customMessageHistoryDao;

    @Autowired
    private WX_RedPacketService wx_RedPacketService;


    /**
     * 获取Signature和JsapiTicket和NonceStr
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = new HashMap<String, String>();
//        resultMap.putAll(SignUtil.getSign("https://www.91caihongwang.com/newMall/wx_sdk.html?uid=1"));
//        resultMap.putAll(SignUtil.getSign("http://127.0.0.1:8080//newMall/wx_sdk.html?uid=1"));
        resultMap.putAll(SignUtil.getSign(paramMap.get("requestPageUrl").toString()));
        if (resultMap.size() > 0) {
            resultMapDTO.setResultMap(resultMap);
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.USER_CODE_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.USER_CODE_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取SignatureAndJsapiTicketAndNonceStr-getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送小程序名片的模板消息
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendTemplateMessageForMiniProgram(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String data = paramMap.get("data") != null ? paramMap.get("data").toString() : "";
        String form_id = paramMap.get("form_id") != null ? paramMap.get("form_id").toString() : "";
        String template_id = paramMap.get("template_id") != null ? paramMap.get("template_id").toString() : "";
        String receiveUid = paramMap.get("receiveUid") != null ? paramMap.get("receiveUid").toString() : "";
        String page = paramMap.get("page") != null ? paramMap.get("page").toString() : "pages/shareOpen/index";
        String firstChar = page.substring(0, 1);
        if ("/".equals(firstChar)) {
            page = page.substring(1, page.length());
        }

        if (!"".equals(data) && !"".equals(form_id)
                && !"".equals(template_id) && !"".equals(page) && !"".equals(receiveUid)) {
            try {
                Map<String, Object> resultMap = WX_PublicNumberUtil.getAccessToken(NewMallCode.WX_MINI_PROGRAM_APPID, NewMallCode.WX_MINI_PROGRAM_SECRET);
                if (resultMap.get("access_token") != null
                        && !"".equals(resultMap.get("access_token").toString())) {
                    String accessToken = resultMap.get("access_token").toString();
                    Map<String, Object> paramMap_temp = Maps.newHashMap();
                    paramMap_temp.put("id", receiveUid);
                    List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(paramMap_temp);
                    if (userList != null && userList.size() > 0) {
                        String openId = userList.get(0).get("openId").toString();
                        paramMap.put("touser", openId);
                        //准备发送模板消息
                        Map<String, String> headers = Maps.newHashMap();
                        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
                        String method = "POST";
                        //根据API的要求，定义相对应的Content-Type
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        Map<String, String> querys = Maps.newHashMap();
                        data = data.replace("\\", "");
                        JSONObject dataJSONObject = JSONObject.parseObject(data);
                        JSONObject paramJSONObject = new JSONObject();
                        for (String key : paramMap.keySet()) {
                            paramJSONObject.put(key, paramMap.get(key));
                        }
                        paramJSONObject.put("data", dataJSONObject);
                        String bodys = paramJSONObject.toJSONString();
                        String host = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
                        HttpResponse response = ALiYunHttpUtils.doPost(host, "", method, headers, querys, bodys);
                        HttpEntity entity = response.getEntity();
                        String res = EntityUtils.toString(entity, "utf-8");
                        logger.info("向微信服务器发送请求获取，获取响应的is {}", res);
                        Map<String, String> resultDataMap = JSONObject.parseObject(res, Map.class);
                        resultMapDTO.setResultMap(resultDataMap);
                        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                        resultMapDTO.setMessage("用户不存在.");
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.WX_SERVER_INNER_ERROR_FOR_ACCESS_TOKEN.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_SERVER_INNER_ERROR_FOR_ACCESS_TOKEN.getMessage());
                }
            } catch (Exception e) {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在service中发送小程序名片的模板消息-sendTemplateMessageForMiniProgram is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }

        logger.info("在service中发送小程序名片的模板消息-sendTemplateMessageForMiniProgram,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }



    /**
     * 发送公众号的模板消息
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendTemplateMessageForWxPublicNumber(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String data = paramMap.get("data") != null ? paramMap.get("data").toString() : "";
        String template_id = paramMap.get("template_id") != null ? paramMap.get("template_id").toString() : "";
        String openId = paramMap.get("openId") != null ? paramMap.get("openId").toString() : "";

        if (!"".equals(data) && !"".equals(template_id)&& !"".equals(openId)) {
            try {
                Map<String, Object> resultMap = WX_PublicNumberUtil.getAccessToken(NewMallCode.WX_PUBLIC_NUMBER_APPID, NewMallCode.WX_PUBLIC_NUMBER_SECRET);
                if (resultMap.get("access_token") != null
                        && !"".equals(resultMap.get("access_token").toString())) {
                    String accessToken = resultMap.get("access_token").toString();
                    paramMap.put("touser", openId);
                    //准备发送模板消息
                    Map<String, String> headers = Maps.newHashMap();
                    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
                    String method = "POST";
                    //根据API的要求，定义相对应的Content-Type
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    Map<String, String> querys = Maps.newHashMap();
                    data = data.replace("\\", "");
                    JSONObject dataJSONObject = JSONObject.parseObject(data);
                    JSONObject paramJSONObject = new JSONObject();
                    for (String key : paramMap.keySet()) {
                        paramJSONObject.put(key, paramMap.get(key));
                    }
                    paramJSONObject.put("data", dataJSONObject);
                    String bodys = paramJSONObject.toJSONString();
                    String host = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
                    HttpResponse response = ALiYunHttpUtils.doPost(host, "", method, headers, querys, bodys);
                    HttpEntity entity = response.getEntity();
                    String res = EntityUtils.toString(entity, "utf-8");
                    logger.info("向微信服务器发送请求获取，获取响应的is {}", res);
                    Map<String, String> resultDataMap = JSONObject.parseObject(res, Map.class);
                    resultMapDTO.setResultMap(resultDataMap);
                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    resultMapDTO.setCode(NewMallCode.WX_SERVER_INNER_ERROR_FOR_ACCESS_TOKEN.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_SERVER_INNER_ERROR_FOR_ACCESS_TOKEN.getMessage());
                }
            } catch (Exception e) {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在service中发送公众号的模板消息-sendTemplateMessageForWxPublicNumber is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }

        logger.info("在service中发送公众号的模板消息-sendTemplateMessageForWxPublicNumber,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取openId和sessionKey
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getOpenIdAndSessionKeyForWX(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = new HashMap<String, String>();
        String code = paramMap.get("code") != null ? paramMap.get("code").toString() : "";
        if (!"".equals(code)) {
            Map<String, String> map = Maps.newHashMap();
            String appid = NewMallCode.WX_MINI_PROGRAM_APPID;
            String secret = NewMallCode.WX_MINI_PROGRAM_SECRET;
            String js_code = paramMap.get("code") != null ? paramMap.get("code").toString() : "";
            String grant_type = NewMallCode.WX_MINI_PROGRAM_GRANT_TYPE_FOR_OPENID;
            if (!"".equals(appid) && !"".equals(secret)
                    && !"".equals(js_code) && !"".equals(grant_type)) {
                map.put("appid", appid);
                map.put("secret", secret);
                map.put("js_code", js_code);
                map.put("grant_type", grant_type);
                String res = httpsUtil.post(
                        "https://api.weixin.qq.com/sns/jscode2session",
                        map);
                logger.info("向微信服务器发送请求，获取响应的is {}", res);
                resultMap = JSON.parseObject(res, Map.class);
            }
            resultMapDTO.setResultMap(resultMap);
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.USER_CODE_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.USER_CODE_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取openId和sessionKey-getOpenIdAndSessionKeyForWX,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO receviceAndSendCustomMessage(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String Content = paramMap.get("Content") != null ? paramMap.get("Content").toString() : "";                       //文本消息内容
        String ToUserName = paramMap.get("ToUserName") != null ? paramMap.get("ToUserName").toString() : "";              //小程序的原始ID
        String FromUserName = paramMap.get("FromUserName") != null ? paramMap.get("FromUserName").toString() : "";        //发送者的openid
        String MsgType = paramMap.get("MsgType") != null ? paramMap.get("MsgType").toString() : "";                       //消息类型：text
        String MsgId = paramMap.get("MsgId") != null ? paramMap.get("MsgId").toString() : "";                             //消息id，64位整型
        String CreateTime = paramMap.get("CreateTime") != null ? paramMap.get("CreateTime").toString() : "";              //消息创建时间(整型）
        // 消息事件Type
        // 1.user_scan_product为打开商品主页事件
        // 2.subscribe为关注公众号事件
        // 3.user_scan_product_enter_session为进入公众号事件
        // 4.user_scan_product_async为异步事件
        // 5.user_scan_product_verify_action为审核结果事件
        String Event = paramMap.get("Event") != null ? paramMap.get("Event").toString() : "";                             //消息事件
        String EventKey = paramMap.get("EventKey") != null ? paramMap.get("EventKey").toString() : "";                             //消息事件

        //保存用户访问客户消息的历史记录
        String customMessage_openid = "";
        String customMessage_miniProgramId = "";
        String customMessage_miniProgramName = "";
        String customMessage_customMessageType = "";
        String customMessage_customMessageCode = "";
        //默认 文本消息
        String customMessageStr = CustomMessageUtil.makeTextCustomMessage(
                FromUserName,
                "<a href=\'http://www.qq.com\' data-miniprogram-appid=\'wx54847eda0638538e' data-miniprogram-path='pages/index/index'>点击跳转【惠生活】小程序</a>"
        );
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        //根据ToUserName获取对应 公众号或者小程序的账号
        Map<String, Object> customMessageAccountParamMap = Maps.newHashMap();
        customMessageAccountParamMap.put("dicType", "customMessageAccount");
        customMessageAccountParamMap.put("dicCode", ToUserName);
        ResultDTO customMessageAccountResultDTO = wxDicService.getSimpleDicByCondition(customMessageAccountParamMap);
        if(customMessageAccountResultDTO != null && customMessageAccountResultDTO.getResultList() != null
                && customMessageAccountResultDTO.getResultList().size() > 0){
            Map<String, String> customMessageAccountMap = customMessageAccountResultDTO.getResultList().get(0);
            //获取将要返回的客服消息的类型
            String customMessageType = customMessageAccountMap.get("customMessageType") != null ? customMessageAccountMap.get("customMessageType").toString() : "";
            String customMessageAccountName = customMessageAccountMap.get("customMessageAccountName") != null ? customMessageAccountMap.get("customMessageAccountName").toString() : "";
            String customMessageAccountAppId = customMessageAccountMap.get("customMessageAccountAppId") != null ? customMessageAccountMap.get("customMessageAccountAppId").toString() : "";
            String customMessageAccountSecret = customMessageAccountMap.get("customMessageAccountSecret") != null ? customMessageAccountMap.get("customMessageAccountSecret").toString() : "";

            //获取二维码中的参数--暂时无用
            String userInfoJson = EventKey.replace("qrscene_", "");
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                logger.error("获取二维码中的参数 , userInfoJson = " + userInfoJson);
                userInfoJson = new String(decoder.decodeBuffer(userInfoJson));
                Map<String, Object> userInfoMap = JSONObject.parseObject(userInfoJson, Map.class);
                paramMap.putAll(userInfoMap);
                //准备 客服消息历史记录 参数
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("userInfoMap : " + JSONObject.toJSONString(userInfoMap));
                logger.info("userInfoMap : " + JSONObject.toJSONString(userInfoMap));
                logger.info("userInfoMap : " + JSONObject.toJSONString(userInfoMap));
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
                logger.info("===============二维码或者小程序码中的参数====================");
            } catch (Exception e) {
                logger.error("获取二维码或者小程序码中的参数失败， ", e);
            }

            if ("user_scan_product".equals(Event)) {                      //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
                //打开商品主页事件
            } else if ("subscribe".equals(Event) || "user_scan_product_enter_session".equals(Event)) {//关注公众号事件 或者 进入公众号事件 都发送红包
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以下是:关注或者进入【公众号为惠生活】<<<<<<--------------
                if ("".equals(Content) && "gh_bcce99ab0079".equals(ToUserName)) {
                    //====================调用发红包,暂时发送文本消息=====================
                    Content = "【惠生活-公众号】提现红包";
                    //获取小程序名片消息内容
                    Map<String, Object> customMessageParamMap = Maps.newHashMap();
                    customMessageParamMap.put("dicType", "newMall_customMessage");
                    customMessageParamMap.put("dicCode", "25");
                    ResultDTO customMessageResultDTO = wxDicService.getSimpleDicByCondition(customMessageParamMap);
                    if(customMessageResultDTO != null && customMessageResultDTO.getResultList() != null
                            && customMessageResultDTO.getResultList().size() > 0) {
                        Map<String, String> customMessageMap = customMessageResultDTO.getResultList().get(0);
                        String title = customMessageMap.get("title")!=null?customMessageMap.get("title").toString():"";
                        String appid = customMessageMap.get("appid")!=null?customMessageMap.get("appid").toString():"";
                        String pagePath = customMessageMap.get("pagePath")!=null?customMessageMap.get("pagePath").toString():"";
                        String thumbMediaId = customMessageMap.get("thumbMediaId")!=null?customMessageMap.get("thumbMediaId").toString():"";
                        if(!"".equals(title) && !"".equals(appid)
                                && !"".equals(pagePath) && !"".equals(thumbMediaId)){
                            customMessageStr = CustomMessageUtil.makeMiniProgramPageCustomMessage(
                                    FromUserName,
                                    title,
                                    appid,
                                    pagePath,
                                    thumbMediaId
                            );
                        } else {
                            customMessageStr = CustomMessageUtil.makeTextCustomMessage(
                                    FromUserName,
                                    "加油站的油价都在这里，赶紧关注吧，秒慢无.。点击跳转<a href=\'http://www.qq.com\' data-miniprogram-appid=\'wx54847eda0638538e\' data-miniprogram-path=\'pages/index/index\'>【惠生活】</a>小程序"
                            );
                        }
                    } else {
                        customMessageStr = CustomMessageUtil.makeTextCustomMessage(
                                FromUserName,
                                "加油站的油价都在这里，赶紧关注吧，秒慢无.。点击跳转<a href=\'http://www.qq.com\' data-miniprogram-appid=\'wx54847eda0638538e\' data-miniprogram-path=\'pages/index/index\'>【惠生活】</a>小程序"
                        );
                    }
                    logger.info("微信客服消息参数 = " + customMessageStr);
                    customMessage_customMessageType = customMessageType;
                    customMessage_customMessageCode = Content;
                    customMessage_openid = FromUserName;
                    customMessage_miniProgramId = customMessageAccountAppId;
                    customMessage_miniProgramName = customMessageAccountName;
                    //2.准备参数发送客服消息
                    String host = NewMallCode.WX_CUSTOM_MESSAGE_HOST;
                    String path = NewMallCode.WX_CUSTOM_MESSAGE_PATH;
                    String method = NewMallCode.WX_CUSTOM_MESSAGE_METHOD;
                    Map<String, String> headers = Maps.newHashMap();
                    Map<String, String> querys = Maps.newHashMap();
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap = WX_PublicNumberUtil.getAccessToken(customMessageAccountAppId, customMessageAccountSecret);
                    if (resultMap.get("access_token") != null &&
                            !"".equals(resultMap.get("access_token").toString())) {
                        String accessToken = resultMap.get("access_token").toString();
                        path = path + accessToken;
                        String bodys = customMessageStr;
                        try {
                            HttpResponse response = ALiYunHttpUtils.doPost(host, path, method, headers, querys, bodys);
                            logger.info("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage,响应 EntityUtils = " + EntityUtils.toString(response.getEntity(), "utf-8"));
                            StatusLine responseState = response.getStatusLine();
                            logger.info("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage, 状态 : " + responseState);
                        } catch (Exception e) {
                            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                            logger.error("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage is error, paramMap : " + paramMap + ", e : " + e);
                        } finally {
                            Map<String, Object> customMessageHistoryMap = Maps.newHashMap();
                            customMessageHistoryMap.put("openId", customMessage_openid);
                            customMessageHistoryMap.put("miniProgramId", customMessage_miniProgramId);
                            customMessageHistoryMap.put("miniProgramName", customMessage_miniProgramName);
                            customMessageHistoryMap.put("customMessageType", customMessage_customMessageType);
                            customMessageHistoryMap.put("customMessageCode", customMessage_customMessageCode);
                            customMessageHistoryMap.put("createTime", TimestampUtil.getTimestamp());
                            customMessageHistoryMap.put("updateTime", TimestampUtil.getTimestamp());
                            customMessageHistoryDao.addCustomMessageHistory(customMessageHistoryMap);
                        }
                        //企业付款和现金红包
//                        paramMap.put("wxPublicNumGhId", ToUserName);
//                        paramMap.put("openId", FromUserName);           //企业付款下的openId
//                        paramMap.put("reOpenId", FromUserName);         //现金红包下的openId
//                        wx_RedPacketService.enterprisePayment(paramMap);
//                            wx_RedPacketService.sendRedPacket(paramMap);      //普通红包
//                            wx_RedPacketService.sendGroupRedPacket(paramMap); //裂变红包
                    } else {
                        logger.info("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage 获取微信access_token失败.");
                    }
                }
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
                //-------------->>>>>>以上是:关注或者进入【公众号为惠生活】<<<<<<--------------
            } else if ("user_scan_product_verify_action".equals(Event)) { //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
                //异步事件
            } else {                    //普通的客服消息
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以下是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                if (!"".equals(Content) && pattern.matcher(Content).matches()) {
                    //根据 客服消息 自动匹配响应图片消息
                    if (!"".equals(customMessageType) && !"".equals(customMessageAccountName)
                            && !"".equals(customMessageAccountAppId) && !"".equals(customMessageAccountSecret)) {
                        //获取1到24之间的随机数
                        Random random = new Random();
                        Integer randomNumber = random.nextInt(24) % (24 - 1 + 1) + 1;
                        Content = randomNumber.toString();
                        //1.根据消息内容从数据库获取对应消息内容
                        Map<String, Object> dicParamMap = Maps.newHashMap();
                        dicParamMap.put("dicType", customMessageType);
                        dicParamMap.put("dicCode", Content);
                        ResultDTO dicResultDTO = wxDicService.getSimpleDicByCondition(dicParamMap);
                        if(dicResultDTO != null && dicResultDTO.getResultList() != null
                                && dicResultDTO.getResultList().size() > 0){
                            Map<String, String> dicMap = dicResultDTO.getResultList().get(0);
                            String imgUrl = dicMap.get("imgUrl").toString();
                            String title = dicMap.get("title").toString();
                            String description = dicMap.get("description").toString();
                            //图片消息
                            customMessageStr = CustomMessageUtil.makeImageCustomMessage(FromUserName, title, description, imgUrl);
                        }
                    }
                    //准备 客服消息历史记录 参数
                    customMessage_customMessageType = customMessageType;
                    customMessage_customMessageCode = Content;
                    customMessage_openid = FromUserName;
                    customMessage_miniProgramId = customMessageAccountAppId;
                    customMessage_miniProgramName = customMessageAccountName;
                    //2.准备参数发送客服消息
                    String host = NewMallCode.WX_CUSTOM_MESSAGE_HOST;
                    String path = NewMallCode.WX_CUSTOM_MESSAGE_PATH;
                    String method = NewMallCode.WX_CUSTOM_MESSAGE_METHOD;
                    Map<String, String> headers = Maps.newHashMap();
                    Map<String, String> querys = Maps.newHashMap();
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap = WX_PublicNumberUtil.getAccessToken(customMessageAccountAppId, customMessageAccountSecret);
                    if (resultMap.get("access_token") != null &&
                            !"".equals(resultMap.get("access_token").toString())) {
                        String accessToken = resultMap.get("access_token").toString();
                        path = path + accessToken;
                        String bodys = customMessageStr;
                        try {
                            HttpResponse response = ALiYunHttpUtils.doPost(host, path, method, headers, querys, bodys);
                            System.out.println(response.toString());
                            System.out.println("EntityUtils = " + EntityUtils.toString(response.getEntity(), "utf-8"));
                            StatusLine responseState = response.getStatusLine();
                            logger.info("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage, 状态 : " + responseState);
                        } catch (Exception e) {
                            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                            logger.error("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage is error, paramMap : " + paramMap + ", e : " + e);
                        } finally {
                            Map<String, Object> customMessageHistoryMap = Maps.newHashMap();
                            customMessageHistoryMap.put("openId", customMessage_openid);
                            customMessageHistoryMap.put("miniProgramId", customMessage_miniProgramId);
                            customMessageHistoryMap.put("miniProgramName", customMessage_miniProgramName);
                            customMessageHistoryMap.put("customMessageType", customMessage_customMessageType);
                            customMessageHistoryMap.put("customMessageCode", customMessage_customMessageCode);
                            customMessageHistoryMap.put("createTime", TimestampUtil.getTimestamp());
                            customMessageHistoryMap.put("updateTime", TimestampUtil.getTimestamp());
                            customMessageHistoryDao.addCustomMessageHistory(customMessageHistoryMap);
                        }
                    } else {
                        logger.info("在service中接受小程序端接受并发送固定消息-receviceAndSendCustomMessage 获取微信access_token失败.");
                    }
                } else {
                    logger.info("有人开始发送垃圾消息过来了，content : " + Content);
                }
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
                //-------------->>>>>>以上是:【小程序为惠生活】的【客服消息】发送数字的自动回复消息<<<<<<--------------
            }
        } else {
            logger.info("公众号或者小程序的原始ID：" + ToUserName + " 不存在.");
        }
        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        resultMapDTO.setResultMap(MapUtil.getStringMap(paramMap));
        return resultMapDTO;
    }
}
