package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Random;

public class TencentHttpUtils {

    public static JSONObject doPost(String strUrl, String payload)
            throws IOException, JSONException {

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        URL url = new URL(strUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // set header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        // 注意，对于json格式的请求，需要设置content-type为application/json
        connection.setRequestProperty("content-type", "application/json");

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.connect();

        // POST请求
        DataOutputStream out = new DataOutputStream(
                connection.getOutputStream());

        out.write(payload.getBytes("utf-8"));
        out.flush();
        out.close();

        // 读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String lines;
        StringBuilder resp = new StringBuilder("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            resp.append(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();

        return JSON.parseObject(resp.toString());

    }

    public static int genSign(String appId, String secretKey,
                              long expired, StringBuffer mySign) throws Exception {
        if (empty(secretKey)) {
            return -1;
        }
        String HMAC_SHA1 = "HmacSHA1";
        long now = System.currentTimeMillis() / 1000;
        int rdm = Math.abs(new Random().nextInt());
        String plain_text = "a=" + appId + "&e=" + expired + "&t=" + now + "&r=" + rdm;

        byte[] bin = hashHmac(plain_text, secretKey);

        byte[] all = new byte[bin.length + plain_text.getBytes().length];
        System.arraycopy(bin, 0, all, 0, bin.length);
        System.arraycopy(plain_text.getBytes(), 0, all, bin.length, plain_text.getBytes().length);

        mySign.append(Base64Util.encode(all));

        return 0;
    }

    private static byte[] hashHmac(String plain_text, String accessKey) {

        try {
            return HMACSHA1.getSignature(plain_text, accessKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean empty(String s) {
        return s == null || s.trim().equals("") || s.trim().equals("null");
    }
}