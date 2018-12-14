package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.br.common.utils.DateUtil;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信公众号 工具类
 */
public class WX_PublicNumberUtil {

    private static final Logger logger = LoggerFactory.getLogger(WX_PublicNumberUtil.class);
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private static String getSnsApiUserInfo_uri = "https://api.weixin.qq.com/sns/userinfo";
    private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
    private static String authorize_uri = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static String accessToken_url = "https://api.weixin.qq.com/cgi-bin/token";
    private static String getCgiBinUserInfo_uri = "https://api.weixin.qq.com/cgi-bin/user/info";
    private static String sendRedPack_uri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    private static String sendGroupRedPack_uri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";
    private static String transfer_uri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    private static String qrTicket_uri = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    private static String qrCode_uri = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    //----------------------------->>>>>>>>自定义菜单<<<<<<<<-----------------------------
    //自定义菜单创建接口
    private static String createMenu_uri = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
    //自定义菜单查询接口
    private static String getMenu_uri = "https://api.weixin.qq.com/cgi-bin/menu/get";
    //自定义菜单删除接口
    private static String deleteMenu_uri = "https://api.weixin.qq.com/cgi-bin/menu/delete";
    //个性化菜单接口 新增和删除
    private static String addConditional_uri = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=";
    private static String delConditional_url = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=";
    //获取自定义菜单配置接口
    private static String getCurrentSelfMenuInfo_uri = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info";
    //----------------------------->>>>>>>>素材管理<<<<<<<<-----------------------------
    //新增临时素材URL
    private static String upload_media_url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=";
    //获取临时素材URL
    private static String get_media_url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=";
    //新增其他类型永久素材URL
    private static String add_material_url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";
    //新增永久图文素材
    private static String add_news_url = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";
    //获取永久素材
    private static String get_material_url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";
    //删除永久素材
    private static String del_material_url = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";
    //修改永久图文素材
    private static String update_news_url = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";
    //获取素材总数
    private static String get_materialcount_url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";
    //获取素材列表
    private static String batchget_material_url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";
    //获取所有粉丝的openId
    private static String getFollowers_uri = "https://api.weixin.qq.com/cgi-bin/user/get";
    //图文，文本的消息发送
    private static String messageSend_uri = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

    /**
     * 向微信服务器发送请求，获取小程序的用户openId和seesion_key
     * @param paramMap
     * @return
     */
    public static Map<String, Object> getMiniOpenIdAndSessionKeyForWX(Map<String, Object> paramMap) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> map = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        String appid = paramMap.get("appId").toString();
        String secret = paramMap.get("secret").toString();
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
            logger.info("向微信服务器发送请求，获取小程序的用户openId和seesion_key，res = ", res);
            resultMap = JSON.parseObject(res, Map.class);
        }
        return resultMap;
    }

    /**
     * 获取自定义菜单配置接口
     * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
     * @return
     */
    public static Map<String, Object> getCurrentSelfMenuInfo() {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            String resultJson = httpsUtil.get(getCurrentSelfMenuInfo_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 删除微信公众号的自定义菜单
     * @return
     */
    public static Map<String, Object> deletePersonalMenu(String menuId) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("menuid", menuId);
            String resultJson = httpsUtil.postJson(delConditional_url + accessToken, JSONObject.toJSONString(paramMap));
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 创建微信公众号的个性化菜单
     * @return
     */
    public static Map<String, Object> createPersonalMenu(String menuStr) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            String resultJson = httpsUtil.postJson(addConditional_uri + accessToken, menuStr);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 删除微信公众号的自定义菜单
     * @return
     */
    public static Map<String, Object> deleteCustomMenu() {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            String resultJson = httpsUtil.get(deleteMenu_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 创建微信公众号的自定义菜单
     * @return
     */
    public static Map<String, Object> createCustomMenu(String menuStr) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            String resultJson = httpsUtil.postJson(createMenu_uri + accessToken, menuStr);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取微信公众号的自定义菜单
     * @return
     */
    public static Map<String, Object> getCustomMenu() {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            String resultJson = httpsUtil.get(getMenu_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取所有粉丝openId
     * @param nextOpenid
     * @param appId
     * @param secret
     * @return
     */
    public static Map<String, Object> getFollowers(String nextOpenid, String appId, String secret){
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken(appId, secret);
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            if(nextOpenid != null && !"".equals(nextOpenid)){
                paramMap.put("nextOpenid", nextOpenid);
            }
            String resultJson = httpsUtil.get(getFollowers_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
        }
        return resultMap;
    }
    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     * @param messageJson 消息json字符串
     * @return {ApiResult}
     */
    public static Map<String, Object> messageSend(String messageJson, String appId, String secret) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken(appId, secret);
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            messageSend_uri = messageSend_uri + accessToken;
            String resultJson = httpsUtil.postJson(messageSend_uri, messageJson);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
        }
        return resultMap;
    }

    public static String getRedPacketQrCode(String jsonStr) {
        String qrCodeImg_url = "";
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, Object> ticketMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            String ticketJSon = httpsUtil.postJson(qrTicket_uri + accessToken, jsonStr);
            ticketMap = JSONObject.parseObject(ticketJSon, Map.class);
            String ticket = ticketMap.get("ticket") != null ? ticketMap.get("ticket").toString() : "";
            if (!"".equals(ticket)) {
                qrCodeImg_url = qrCode_uri + ticket;
            } else {
                //获取二维码的ticket失败
                logger.error("获取二维码的ticket失败");
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return qrCodeImg_url;
    }

    public static String getAuthorizeURL(String appId, String redirectUri, String state, boolean snsapiBase) {
        Map<String, String> params = new HashMap();
        params.put("appid", appId);
        params.put("response_type", "code");
        params.put("redirect_uri", redirectUri);
        if (snsapiBase) {
            params.put("scope", "snsapi_base");
        } else {
            params.put("scope", "snsapi_userinfo");
        }
        if (isBlank(state)) {
            params.put("state", "wx#wechat_redirect");
        } else {
            params.put("state", state.concat("#wechat_redirect"));
        }

        String para = packageSign(params, false);
        return authorize_uri + "?" + para;
    }

    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        TreeMap<String, String> sortedParams = new TreeMap(params);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Iterator i$ = sortedParams.entrySet().iterator();

        while (i$.hasNext()) {
            Map.Entry<String, String> param = (Map.Entry) i$.next();
            String value = (String) param.getValue();
            if (!isBlank(value)) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                sb.append((String) param.getKey()).append("=");
                if (urlEncoder) {
                    try {
                        value = urlEncode(value);
                    } catch (UnsupportedEncodingException var9) {
                        ;
                    }
                }

                sb.append(value);
            }
        }

        return sb.toString();
    }

    public static Map<String, Object> getSnsAccessToken(String appId, String secret, String code) {
        final String accessTokenUrl = url.replace("{appid}", appId).replace("{secret}", secret).replace("{code}", code);
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        String resultJson = httpsUtil.get(accessTokenUrl, paramMap);
        resultMap = JSONObject.parseObject(resultJson, Map.class);
        return resultMap;
    }

    public static Map<String, Object> getSnsApiUserInfo(String accessToken, String openId) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        paramMap.put("access_token", accessToken);
        paramMap.put("openid", openId);
        paramMap.put("lang", "zh_CN");
        String resultJson = httpsUtil.get(getSnsApiUserInfo_uri, paramMap);
        resultMap = JSONObject.parseObject(resultJson, Map.class);
        return resultMap;
    }

    public static Map<String, Object> getAccessToken() {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        paramMap.put("appid", NewMallCode.WX_PUBLIC_NUMBER_APPID);
        paramMap.put("secret", NewMallCode.WX_PUBLIC_NUMBER_SECRET);
        paramMap.put("grant_type", "client_credential");
        String resultJson = httpsUtil.get(accessToken_url, paramMap);
        resultMap = JSONObject.parseObject(resultJson, Map.class);
        return resultMap;
    }

    public static Map<String, Object> getAccessToken(String appId, String secret) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        paramMap.put("appid", appId);
        paramMap.put("secret", secret);
        paramMap.put("grant_type", "client_credential");
        String resultJson = httpsUtil.get(accessToken_url, paramMap);
        resultMap = JSONObject.parseObject(resultJson, Map.class);
        return resultMap;
    }

    public static Map<String, Object> getCgiBinUserInfo(String openId) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            paramMap.put("openid", openId);
            paramMap.put("lang", "zh_CN");
            String resultJson = httpsUtil.get(getCgiBinUserInfo_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
        }
        return resultMap;
    }

    public static Map<String, Object> getCgiBinUserInfo(String openId, String appId, String secret) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken(appId, secret);
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            paramMap.put("access_token", accessToken);
            paramMap.put("openid", openId);
            paramMap.put("lang", "zh_CN");
            String resultJson = httpsUtil.get(getCgiBinUserInfo_uri, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
        }
        return resultMap;
    }




    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------
    //-------------------->>>>>>>>微信公众号-素材管理<<<<<<<<--------------------

    /**
     * 新增临时素材
     * @param mediaTypeUtil 上传的临时多媒体文件有格式
     * @param file 需要上传的文件
     * @return ApiResult
     */
    public static Map<String, Object> uploadMedia(MediaTypeUtil mediaTypeUtil, File file) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            upload_media_url = upload_media_url + accessToken + "&type=" + mediaTypeUtil.get();
            try{
                String resultJson = httpsUtil.uploadMedia(upload_media_url, file, null);
                resultMap = JSONObject.parseObject(resultJson, Map.class);
            } catch (Exception e) {
                //获取临时素材失败
                logger.error("获取临时素材失败，" , e);
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取临时素材
     * @param media_id 素材Id
     * @return MediaFile
     */
    public static Map<String, Object> getMedia(String media_id) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            get_media_url = get_media_url + accessToken + "&media_id=" + media_id;
            try{
                MediaFileUtil mediaFileUtil = httpsUtil.download(get_media_url);
                resultMap = JSONObject.parseObject(JSONObject.toJSONString(mediaFileUtil), Map.class);
            } catch (Exception e) {
                //获取临时素材失败
                logger.error("获取临时素材失败，" , e);
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 新增其他类型永久素材
     * @param file 文件
     * @param mediaTypeUtil 素材类型
     * @return ApiResult
     */
    public static Map<String, Object> addMaterial(File file, MediaTypeUtil mediaTypeUtil) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            add_material_url = add_material_url + accessToken;
            try{
                String resultJson = httpsUtil.uploadMedia(add_material_url, file, null);
                resultMap = JSONObject.parseObject(resultJson, Map.class);
            } catch (Exception e) {
                //获取临时素材失败
                logger.error("获取临时素材失败，" , e);
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 新增永久图文素材
     * @param mediaArticles 图文列表
     * @return ApiResult
     */
    public static Map<String, Object> addNews(List<MediaArticlesUtil> mediaArticlesUtilList) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            add_news_url = add_news_url + accessToken;
            paramMap.put("articles", mediaArticlesUtilList);
            String resultJson = httpsUtil.postJson(add_news_url, JSONObject.toJSONString(paramMap));
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取永久素材
     * @param media_id 要获取的素材的media_id
     * @return InputStream 流，考虑到这里可能返回json或file请自行使用IOUtils转换
     */
    public static Map<String, Object> getMaterial(String media_id, String filePath) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            get_material_url = get_material_url + accessToken;
            paramMap.put("media_id", media_id);
            try{
                InputStream inputStream = httpsUtil.downloadMaterial(get_material_url, JSONObject.toJSONString(paramMap));
                File file = new File(filePath);
                IOUtils.toFile(inputStream, file);
                resultMap.put("filePath", filePath);
            } catch (Exception e) {
                //获取临时素材失败
                logger.error("获取临时素材失败，" , e);
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 删除永久素材
     * @param media_id 要获取的素材的media_id
     * @return ApiResult 返回信息
     */
    public static Map<String, Object> delMaterial(String media_id) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            del_material_url = del_material_url + accessToken;
            paramMap.put("media_id", media_id);
            String resultJson = httpsUtil.postJson(del_material_url, JSONObject.toJSONString(paramMap));
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 修改永久图文素材
     * @param media_id 要修改的图文消息的id
     * @param index 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
     * @param mediaArticles 图文素材
     * @return ApiResult 返回信息
     */
    public static Map<String, Object> updateNews(String media_id, Integer index, MediaArticlesUtil mediaArticlesUtil) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            update_news_url = update_news_url + accessToken;
            paramMap.put("media_id", media_id);
            paramMap.put("index", index);
            paramMap.put("articles", mediaArticlesUtil);
            String resultJson = httpsUtil.postJson(update_news_url, JSONObject.toJSONString(paramMap));
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取素材总数
     * @return ApiResult 返回信息
     */
    public static Map<String, Object> getMaterialCount() {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            get_materialcount_url = get_materialcount_url + accessToken;
            String resultJson = httpsUtil.get(get_materialcount_url, paramMap);
            resultMap = JSONObject.parseObject(resultJson, Map.class);
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
        }
        return resultMap;
    }

    /**
     * 获取素材列表
     * @param mediaTypeUtil 素材的类型，图片（image）、视频（video）、语音 （voice）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return ApiResult 返回信息
     */
    public static List<Map<String, Object>> batchGetMaterial(MediaTypeUtil mediaTypeUtil, Integer offset, Integer count) {
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> resultMapList = Lists.newArrayList();
        Map<String, Object> accessTokenMap = getAccessToken();
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            String batchgetMaterialUrl = batchget_material_url + accessToken;
            if(offset < 0){ offset = 0;}
            if(count > 20){ count = 20;}
            if(count < 1){ count = 1;}
            paramMap.put("type", mediaTypeUtil.get());
            paramMap.put("offset", offset);
            paramMap.put("count", count);
            String resultJson = httpsUtil.postJson(batchgetMaterialUrl, JSONObject.toJSONString(paramMap));
            resultMap = JSONObject.parseObject(resultJson, Map.class);
            String totalCountStr = resultMap.get("total_count") != null ? resultMap.get("total_count").toString() : "0";
            if(!"".equals(totalCountStr)){               //code=0表示请求成功
                Integer totalCount = Integer.parseInt(totalCountStr);
                if(totalCount > 0){
                    if(totalCount > 0 && ((totalCount - offset) > 0)){     //确认是否查到了尽头，用来判断是否继续递归
                        String itemJsonStr = resultMap.get("item") != null ? resultMap.get("item").toString() : "";
                        if(!"".equals(itemJsonStr)){
                            List<Map<String, Object>> itemMapList = JSONObject.parseObject(itemJsonStr, List.class);
                            if(itemMapList != null && itemMapList.size() > 0){
                                for(Map<String, Object> itemMap : itemMapList){
                                    Map<String, Object> contentMap= itemMap.get("content") != null ? (Map<String, Object>)itemMap.get("content") : null;
                                    if(contentMap != null && contentMap.size() > 0){
                                        List<Map<String, Object>> newsItemMapList= contentMap.get("news_item") != null ? (List<Map<String, Object>>)contentMap.get("news_item") : null;
                                        if(newsItemMapList != null && newsItemMapList.size() > 0){
                                            for(Map<String, Object> newsItemMap : newsItemMapList){
                                                if(!resultMapList.contains(newsItemMap)){
                                                    String digest = newsItemMap.get("digest")!=null?newsItemMap.get("digest").toString():"";
                                                    if(!"".equals(digest) && digest.length() > 42){
                                                        digest = digest.substring(0, 42) + "...";
                                                        newsItemMap.put("digest", digest);
                                                    }
                                                    String title = newsItemMap.get("title")!=null?newsItemMap.get("title").toString():"";
                                                    if(!"".equals(title) && title.length() > 18){
                                                        title = title.substring(0, 18) + "...";
                                                        newsItemMap.put("title", title);
                                                    }
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    Integer createTime_Integer = contentMap.get("create_time")!=null?(Integer)contentMap.get("create_time"):0;
                                                    if(createTime_Integer > 0){
                                                        String createTime = sdf.format(new Date(Long.valueOf(createTime_Integer+"000")));
                                                        newsItemMap.put("createTime", createTime);
                                                    } else {
                                                        newsItemMap.put("createTime", sdf.format(new Date()));
                                                    }
                                                    Integer updateTime_Integer = contentMap.get("update_time")!=null?(Integer)contentMap.get("update_time"):0;
                                                    if(updateTime_Integer > 0){
                                                        String updateTime = sdf.format(new Date(Long.valueOf(updateTime_Integer+"000")));
                                                        newsItemMap.put("updateTime", updateTime);
                                                    } else {
                                                        newsItemMap.put("updateTime", sdf.format(new Date()));
                                                    }
                                                    resultMapList.add(newsItemMap);
                                                } else {
                                                    continue;
                                                }
                                            }
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            } else {
                                //结束递归
                                return resultMapList;
                            }
                        } else {
                            //结束递归
                            return resultMapList;
                        }
                        offset = offset + 20;
//                        offset = offset + 2;
                        resultMapList.addAll(batchGetMaterial(mediaTypeUtil, offset, count));
                    } else {
                        //停止递归
                        return resultMapList;
                    }
                } else {
                    //停止递归
                    return resultMapList;
                }
            } else {
                //停止递归
                return resultMapList;
            }
        } else {
            //获取access_token失败
            logger.error("获取access_token失败");
            //停止递归
            return resultMapList;
        }
        return resultMapList;
    }

    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------
    //-------------------->>>>>>>>涉及到微信商家的钱，企业付款，发送红包，发送分裂红包<<<<<<<<--------------------

    /**
     * 企业付款，直达微信零钱
     *
     * @param mchAppId       商户账号appid
     * @param partner        商户号
     * @param nonceStr       随机字符串
     * @param partnerTradeNo 商户订单号
     * @param openId         用户openid
     * @param checkName      校验用户姓名选项
     * @param reUserName     收款用户姓名
     * @param amount         金额
     * @param desc           企业付款描述信息
     * @param spbillCreateIp Ip地址: 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP
     * @param paternerKey    商户API秘钥
     * @param certPath       证书地址
     * @return
     */
    public static ResultMapDTO enterprisePayment(
            String mchAppId, String mchId, String nonceStr,
            String partnerTradeNo, String openId, String checkName,
            String reUserName, String amount, String desc,
            String spbillCreateIp, String paternerKey, String certPath) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> params = new HashMap<String, String>();
        params.put("amount", amount);
        params.put("check_name", checkName);
        params.put("desc", desc);
        params.put("mch_appid", mchAppId);
        params.put("mchid", mchId);
        params.put("nonce_str", nonceStr);
        params.put("openid", openId);
        params.put("partner_trade_no", partnerTradeNo);
        params.put("re_user_name", reUserName);
        params.put("spbill_create_ip", spbillCreateIp);
        String sign = createSign(params, paternerKey);
        params.put("sign", sign);
        String xml = XmlUtil.toXml(params);
        System.out.println(xml);
        String xmlResult = postSSL(transfer_uri, xml, certPath, mchId);
        System.out.println(xmlResult);
        try {
            Map<String, String> resultXML = XmlUtil.xml2map(xmlResult.toString(), false);
            String return_code = resultXML.get("return_code");
            String result_code = resultXML.get("result_code");
            String return_msg = resultXML.get("return_msg")!=null?resultXML.get("return_msg").toString():"无";
            String err_code_des = resultXML.get("err_code_des")!=null?resultXML.get("err_code_des").toString():"无";
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                String resultMsg = "红包状态：" + return_msg + " ， 提示：" + err_code_des + "。";
                resultMapDTO.setCode(NewMallCode.RED_PACKET_SEND_IS_ERROR_BUT_RESPOSE_SUCCESS.getNo());
                resultMapDTO.setMessage(resultMsg);
            }
        } catch (Exception e) {
            logger.info("企业付款失败，e : " + e.getMessage());
            resultMapDTO.setCode(NewMallCode.RED_PACKET_SEND_IS_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.RED_PACKET_SEND_IS_ERROR.getMessage());
        }
        return resultMapDTO;
    }

    /**
     * 发送普通红包
     *
     * @param nonceStr    随机字符串
     * @param mchBillNo   商户订单号
     * @param mchId       商户号
     * @param wxAppId     公众账号appid
     * @param sendName    商户名称
     * @param reOpenId    用户openid
     * @param totalAmount 付款金额
     * @param totalNum    红包发放总人数
     * @param wishing     红包祝福语
     * @param clientIp    Ip地址：调用接口的机器Ip地址
     * @param actName     活动名称
     * @param remark      备注
     * @param paternerKey 商户API秘钥
     * @param certPath    证书地址
     * @return
     */
    public static boolean sendRedPack(
            String nonceStr, String mchBillNo, String mchId,
            String wxAppId, String sendName, String reOpenId, String totalAmount,
            String totalNum, String wishing, String clientIp, String actName,
            String remark, String paternerKey, String certPath) {
        Map<String, String> params = new HashMap<String, String>();
        // 随机字符串
        params.put("nonce_str", nonceStr);
        // 商户订单号
        params.put("mch_billno", mchBillNo);
        // 商户号
        params.put("mch_id", mchId);
        // 公众账号ID
        params.put("wxappid", wxAppId);
        // 商户名称
        params.put("send_name", sendName);
        // 用户OPENID
        params.put("re_openid", reOpenId);
        // 付款现金(单位分)
        params.put("total_amount", totalAmount);
        // 红包发放总人数
        params.put("total_num", totalNum);
        // 红包祝福语
        params.put("wishing", wishing);
        // 终端IP
        params.put("client_ip", clientIp);
        // 活动名称
        params.put("act_name", actName);
        // 备注
        params.put("remark", remark);
        //创建签名
        String sign = createSign(params, paternerKey);
        params.put("sign", sign);

        String xmlResult = sendRedPack(params, certPath, mchId);
        try {
            Map<String, String> result = XmlUtil.xml2map(xmlResult, false);
            logger.info(JSONObject.toJSONString(result));
            //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            String return_code = result.get("return_code");
            //业务结果
            String result_code = result.get("result_code");

            if (isBlank(return_code) || !"SUCCESS".equals(return_code)) {
                return false;
            }
            if (notBlank(result_code) && "SUCCESS".equals(result_code)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("发送红包失败，e : " + e.getMessage());
        }
        return false;
    }

    /**
     * 发送裂变红包
     *
     * @param nonceStr    随机字符串
     * @param mchBillNo   商户订单号
     * @param mchId       商户号
     * @param wxAppId     公众账号appid
     * @param sendName    商户名称
     * @param reOpenId    用户openid
     * @param totalAmount 付款金额
     * @param totalNum    红包发放总人数
     * @param amtType     红包金额设置方式
     * @param wishing     红包祝福语
     * @param actName     活动名称
     * @param remark      备注
     * @param paternerKey 商户API秘钥
     * @param certPath    证书地址
     * @return
     */
    public static boolean sendGroupRedPack(
            String nonceStr, String mchBillNo, String mchId,
            String wxAppId, String sendName, String reOpenId, String totalAmount,
            String totalNum, String amtType, String wishing, String actName,
            String remark, String paternerKey, String certPath) {
        Map<String, String> params = new HashMap<String, String>();
        // 随机字符串
        params.put("nonce_str", nonceStr);
        // 商户订单号
        params.put("mch_billno", mchBillNo);
        // 商户号
        params.put("mch_id", mchId);
        // 公众账号ID
        params.put("wxappid", wxAppId);
        // 商户名称
        params.put("send_name", sendName);
        // 用户OPENID
        params.put("re_openid", reOpenId);
        // 付款现金(单位分)
        params.put("total_amount", totalAmount);
        // 红包金额设置方式： ALL_RAND—全部随机,商户指定总金额和红包发放总人数，由微信支付随机计算出各红包金额
        amtType = ((amtType == null) || ("".equals(amtType)))?"ALL_RAND":amtType;
        params.put("amtType", amtType);
        // 红包发放总人数
        params.put("total_num", totalNum);
        // 红包祝福语
        params.put("wishing", wishing);
        // 活动名称
        params.put("act_name", actName);
        // 备注
        params.put("remark", remark);
        //创建签名
        String sign = createSign(params, paternerKey);
        params.put("sign", sign);

        String xmlResult = sendGroupRedPack(params, certPath, mchId);
        try {
            Map<String, String> result = XmlUtil.xml2map(xmlResult, false);
            logger.info(JSONObject.toJSONString(result));
            //此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            String return_code = result.get("return_code");
            //业务结果
            String result_code = result.get("result_code");

            if (isBlank(return_code) || !"SUCCESS".equals(return_code)) {
                return false;
            }
            if (notBlank(result_code) && "SUCCESS".equals(result_code)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("发送裂变红包失败，e : " + e.getMessage());
        }
        return false;
    }

    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------
    //-------------------->>>>>>>>基础使用方法，可以考虑挪到common中去<<<<<<<<--------------------

    public static String sendRedPack(Map<String, String> params, String certPath, String partner) {
        return postSSL(sendRedPack_uri, XmlUtil.toXml(params), certPath, partner);
    }

    public static String sendGroupRedPack(Map<String, String> params, String certPath, String partner) {
        return postSSL(sendGroupRedPack_uri, XmlUtil.toXml(params), certPath, partner);
    }

    public static String postSSL(String url, String data, String certPath, String certPass) {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, certPass.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(kms, (TrustManager[]) null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL _url = new URL(url);
            conn = (HttpsURLConnection) _url.openConnection();
            conn.setConnectTimeout(25000);
            conn.setReadTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
            conn.connect();
            out = conn.getOutputStream();
            out.write(data.getBytes(Charsets.UTF_8));
            out.flush();
            inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String var15 = sb.toString();
            return var15;
        } catch (Exception var19) {
            throw new RuntimeException(var19);
        } finally {
//            IOUtils.closeQuietly(out);
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException var2) {
                ;
            }
//            IOUtils.closeQuietly(reader);
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException var2) {
                ;
            }
//            IOUtils.closeQuietly(inputStream);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var2) {
                ;
            }
            if (conn != null) {
                conn.disconnect();
            }

        }
    }

    public static String createSign(Map<String, String> params, String paternerKey) {
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + paternerKey;
        return md5(stringSignTemp).toUpperCase();
    }

    public static String md5(String srcStr) {
        return hash("MD5", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            ret.append(HEX_DIGITS[bytes[i] >> 4 & 15]);
            ret.append(HEX_DIGITS[bytes[i] & 15]);
        }
        return ret.toString();
    }

    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }

    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            int len = str.length();
            if (len == 0) {
                return true;
            } else {
                int i = 0;

                while (i < len) {
                    switch (str.charAt(i)) {
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ':
                            ++i;
                            break;
                        default:
                            return false;
                    }
                }
                return true;
            }
        }
    }


}
