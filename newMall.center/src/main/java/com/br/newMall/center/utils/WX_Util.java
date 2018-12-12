package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信公众号 工具类
 */
public class WX_Util {

    private static final Logger logger = LoggerFactory.getLogger(WX_Util.class);
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private static String getSnsApiUserInfo_uri = "https://api.weixin.qq.com/sns/userinfo";
    private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
    private static String authorize_uri = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static String accessToken_url = "https://api.weixin.qq.com/cgi-bin/token";
    private static String getCgiBinUserInfo_uri = "https://api.weixin.qq.com/cgi-bin/user/info";
    private static String sendRedPack_uri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    private static String transfer_uri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    private static String qrTicket_uri = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    private static String qrCode_uri = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

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

    public static String sendRedPack(Map<String, String> params, String certPath, String partner) {
        return postSSL(sendRedPack_uri, XmlUtil.toXml(params), certPath, partner);
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
