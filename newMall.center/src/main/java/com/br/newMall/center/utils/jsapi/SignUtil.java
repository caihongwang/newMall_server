package com.br.newMall.center.utils.jsapi;

import com.br.newMall.api.code.NewMallCode;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * Created by caihongwang on 2018/5/29.
 */
public class SignUtil {

    public static void main(String[] args) {
        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "https://www.91caihongwang.com/newMall/common/getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber.do";//url是你请求的一个action或者controller地址，并且方法直接跳转到使用jsapi的jsp界面
        Map<String, String> ret = getSign(url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }

    ;

    public static Map<String, String> getSign(String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String jsapi_ticket = JsapiTicketUtil.getJSApiTicket();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", NewMallCode.WX_PUBLIC_NUMBER_APPID);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
