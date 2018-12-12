package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.google.common.collect.Maps;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信公众号 工具类
 */
public class WX_PayUtil {

    private static final Logger logger = LoggerFactory.getLogger(WX_PayUtil.class);

    private static String CHARSET = "UTF-8";
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
    private static String unifiedOrder_uri = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static String unifiedOrderPay(String appId, String mchId, String body ,
                                         String totalFee, String spbillCreateIp , String notifyUrl,
                                         String tradeType, String openId, String apiSecret) {
        String resultJson = "";
        HttpsUtil httpsUtil = new HttpsUtil();
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        params.put("body", body);
        params.put("out_trade_no", System.currentTimeMillis()+"");
        params.put("total_fee", totalFee);
        params.put("spbill_create_ip", spbillCreateIp);
        params.put("notify_url", notifyUrl);
        params.put("trade_type", tradeType);
        params.put("openid", openId);

        String sign = createSign(params, apiSecret);
        params.put("sign", sign);
        String xmlResult = post(unifiedOrder_uri, null, XmlUtil.toXml(params), null);
        logger.info("统一订单支付请求返回参数：" + xmlResult);
        try{
            Map<String, String> result = XmlUtil.xml2map(xmlResult, false);
            String return_code = result.get("return_code");
            String return_msg = result.get("return_msg");
            if (isBlank(return_code) || !"SUCCESS".equals(return_code)) {
                return resultJson;
            }
            String result_code = result.get("result_code");
            if (isBlank(result_code) || !"SUCCESS".equals(result_code)) {
                return resultJson;
            }
            // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
            String prepay_id = result.get("prepay_id");
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appId", appId);
            packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
            packageParams.put("nonceStr", System.currentTimeMillis() + "");
            packageParams.put("package", "prepay_id=" + prepay_id);
            packageParams.put("signType", "MD5");
            String packageSign = createSign(packageParams, apiSecret);
            packageParams.put("paySign", packageSign);
            resultJson = JSONObject.toJSONString(packageParams);
        } catch (Exception e) {
            logger.info("统一订单支付请求失败，e : " + e.getMessage());
        }
        return resultJson;
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

    public static class TrustAnyTrustManager implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = new TrustManager[]{new TrustAnyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[])null, tm, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL _url = new URL(url);
        SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
        TrustAnyHostnameVerifier trustAnyHostnameVerifier = new TrustAnyHostnameVerifier();
        HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
        if(conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection)conn).setHostnameVerifier(trustAnyHostnameVerifier);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(19000);
        conn.setReadTimeout(19000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if(headers != null && !headers.isEmpty()) {
            Iterator var5 = headers.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var5.next();
                conn.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }
        }

        return conn;
    }

    public static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if(queryParas != null && !queryParas.isEmpty()) {
            StringBuilder sb = new StringBuilder(url);
            boolean isFirst;
            if(url.indexOf(63) == -1) {
                isFirst = true;
                sb.append('?');
            } else {
                isFirst = false;
            }

            String key;
            String value;
            for(Iterator var4 = queryParas.entrySet().iterator(); var4.hasNext(); sb.append(key).append('=').append(value)) {
                Map.Entry<String, String> entry = (Map.Entry)var4.next();
                if(isFirst) {
                    isFirst = false;
                } else {
                    sb.append('&');
                }

                key = (String)entry.getKey();
                value = (String)entry.getValue();
                if(notBlank(value)) {
                    try {
                        value = URLEncoder.encode(value, CHARSET);
                    } catch (UnsupportedEncodingException var9) {
                        throw new RuntimeException(var9);
                    }
                }
            }

            return sb.toString();
        } else {
            return url;
        }
    }

    private static String readResponseString(HttpURLConnection conn) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
            String line = reader.readLine();
            String var4;
            if(line == null) {
                var4 = "";
                return var4;
            } else {
                StringBuilder ret = new StringBuilder();
                ret.append(line);

                while((line = reader.readLine()) != null) {
                    ret.append('\n').append(line);
                }

                var4 = ret.toString();
                return var4;
            }
        } catch (Exception var14) {
            throw new RuntimeException(var14);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException var13) {
                    logger.error(var13.getMessage(), var13);
                }
            }

        }
    }

    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;

        String var11;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), "POST", headers);
            conn.connect();
            if(data != null) {
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes(CHARSET));
                out.flush();
                out.close();
            }

            var11 = readResponseString(conn);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if(conn != null) {
                conn.disconnect();
            }

        }
        return var11;
    }

}
