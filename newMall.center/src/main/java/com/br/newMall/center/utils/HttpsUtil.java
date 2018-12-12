package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class HttpsUtil {

    private PoolingHttpClientConnectionManager poolConnManager;

    private SSLConnectionSocketFactory sslsf;

    private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);

    @PostConstruct
    public void init() throws KeyManagementException, NoSuchAlgorithmException {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }
        };
        //先用不做客户端验证
        this.sslsf = new SSLConnectionSocketFactory(SSLContextUtil.createIgnoreVerifySSL("TLSv1.2"), hostnameVerifier);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", this.sslsf)
                .build();

        //初始化连接管理器
        poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // Increase max total connection to 200
        poolConnManager.setMaxTotal(500);
        // Increase default max connection per route to 20
        poolConnManager.setDefaultMaxPerRoute(50);
    }

    //获取连接
    public CloseableHttpClient getConnection() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager).build();
        return httpClient;
    }

    public String post(String url, List<? extends NameValuePair> param) {
        String returnStr = null;
        CloseableHttpClient client = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            httpPost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));

            //配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            logger.info(currentTime + " 开始发送 请求：url {}, \n请求参数: {}", url, JSONObject.toJSONString(param));
//          httpPost.setHeader("prepub", "1");//todo 正式环境一定要去掉
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status + " content : " + resopnse);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(param) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnStr;
    }

    public String post(String url, Map<String, String> param) {
        List<BasicNameValuePair> params = new ArrayList<>();
        if (param != null) {
            for (String key : param.keySet()) {
                params.add(new BasicNameValuePair(key, param.get(key)));
            }
        }
        return this.post(url, params);
    }

    public String get(String url, Map<String, String> params) {
        String returnStr = null;
        HttpGet httpGet = new HttpGet();

        try {
            long currentTime = System.currentTimeMillis();

            String requestUrl = "";
            if (params != null && !params.isEmpty()) {
                Map.Entry entry;
                for (Iterator i$ = params.entrySet().iterator(); i$.hasNext(); requestUrl = requestUrl + (String) entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), "UTF-8") + "&") {
                    entry = (Map.Entry) i$.next();
                }

                requestUrl = url + "?" + requestUrl.substring(0, requestUrl.length() - 1);
            } else {
                requestUrl = url;
            }
            httpGet.setURI(new URI(requestUrl));
            CloseableHttpResponse response = getConnection().execute(httpGet);
            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpGet.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(params) + " Exception" + e.toString());
        }
        return returnStr;
    }

    public byte[] postForByte(String url, List<? extends NameValuePair> param) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            httpPost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));

            //配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            logger.info(currentTime + " 开始发送 请求：url" + url);
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                byte[] res = EntityUtils.toByteArray(entity);
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status);
                return res;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(param) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String postJson(String url, String contentObj) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            StringEntity s = new StringEntity(contentObj, ContentType.create("application/json", "utf-8"));

            httpPost.setEntity(s);

            //配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            logger.info(currentTime + " 开始发送 请求：url" + url);
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 正常接收响应：url" + url + " status=" + status);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 异常接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(contentObj) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String postJson(String url, String contentObj, Map<String, String> paramHeader) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            StringEntity s = new StringEntity(contentObj, ContentType.create("application/json", "utf-8"));

            httpPost.setEntity(s);

            for (String key : paramHeader.keySet()) {
                httpPost.setHeader(key, paramHeader.get(key));
            }

            // 配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            logger.info(currentTime + " 开始发送 请求：url" + url);
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 正常接收响应：url" + url + " status=" + status);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 异常接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(contentObj) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public String postJson(String url, String contentObj, RequestConfig config) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            StringEntity s = new StringEntity(contentObj, ContentType.create("application/json", "utf-8"));

            // 配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(s);
            logger.info(currentTime + " 开始发送 请求：url" + url);
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 正常接收响应：url" + url + " status=" + status);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 异常接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(contentObj) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String post(String url, List<NameValuePair> param, RequestConfig config) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        CloseableHttpResponse response = null;
        try {
            long currentTime = System.currentTimeMillis();
            httpPost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));

            // 配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000).setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);

            logger.info(currentTime + " 开始发送 请求：url" + url);

//        httpPost.setHeader("prepub", "1");//todo 正式环境一定要去掉
            response = getConnection().execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            logger.info("接收响应：url" + url + " status=" + status);
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String resopnse = "";
                if (entity != null) {
                    resopnse = EntityUtils.toString(entity, "utf-8");
                }
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status);
                return entity != null ? resopnse : null;
            } else {
                HttpEntity entity = response.getEntity();
                logger.info(currentTime + " 接收响应：url" + url + " status=" + status + " resopnse=" + EntityUtils.toString(entity, "utf-8"));
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpPost.abort();
            logger.error("发送请求异常：url {}, \n请求参数: {}", url, JSONObject.toJSONString(param) + " Exception" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final okhttp3.MediaType CONTENT_TYPE_FORM = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
//    /**
//     * http请求工具 委托
//     * 优先使用OkHttp
//     * 最后使用JFinal HttpKit
//     */
//    private interface HttpDelegate {
//        String get(String url);
//        String get(String url, Map<String, String> queryParas);
//
//        String post(String url, String data);
//        String postSSL(String url, String data, String certPath, String certPass);
//
//        MediaFileUtil download(String url);
//        InputStream download(String url, String params);
//
//        String upload(String url, File file, String params);
//    }
//
//    /**
//     * HttpKit代理 实现
//     */
//    private static class HttpKitDelegate implements HttpDelegate {
//        HttpsUtil httpsUtil = new HttpsUtil();
//        @Override
//        public String get(String url) {
//            return get(url);
//        }
//
//        @Override
//        public String get(String url, Map<String, String> queryParas) {
//            return get(url, queryParas);
//        }
//
//        @Override
//        public String post(String url, String data) {
//            return post(url, data);
//        }
//
//        @Override
//        public String postSSL(String url, String data, String certPath, String certPass) {
//            return postSSL(url, data, certPath, certPass);
//        }
//
//        @Override
//        public MediaFileUtil download(String url) {
//            try {
//                return httpsUtil.download(url);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public InputStream download(String url, String params) {
//            try {
//                return httpsUtil.downloadMaterial(url, params);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Override
//        public String upload(String url, File file, String params) {
//            try {
//                return httpsUtil.uploadMedia(url, file, params);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }


    /**
     * 上传临时素材
     * @param url 图片上传地址
     * @param file 需要上传的文件
     * @return ApiResult
     * @throws IOException
     */
    protected String uploadMedia(String url, File file, String params) throws IOException {
        URL urlGet = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", DEFAULT_USER_AGENT);
        conn.setRequestProperty("Charsert", "UTF-8");
        // 定义数据分隔线
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(conn.getOutputStream());
        // 定义最后数据分隔线
        StringBuilder mediaData = new StringBuilder();
        mediaData.append("--").append(BOUNDARY).append("\r\n");
        mediaData.append("Content-Disposition: form-data;name=\"media\";filename=\""+ file.getName() + "\"\r\n");
        mediaData.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] mediaDatas = mediaData.toString().getBytes();
        out.write(mediaDatas);
        DataInputStream fs = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = fs.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        IOUtils.closeQuietly(fs);
        // 多个文件时，二个文件之间加入这个
        out.write("\r\n".getBytes());
        if (WX_PublicNumberUtil.notBlank(params)) {
            StringBuilder paramData = new StringBuilder();
            paramData.append("--").append(BOUNDARY).append("\r\n");
            paramData.append("Content-Disposition: form-data;name=\"description\";");
            byte[] paramDatas = paramData.toString().getBytes();
            out.write(paramDatas);
            out.write(params.getBytes(Charsets.UTF_8));
        }
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
        out.write(end_data);
        out.flush();
        IOUtils.closeQuietly(out);

        // 定义BufferedReader输入流来读取URL的响应
        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
        String valueString = null;
        StringBuffer bufferRes = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        IOUtils.closeQuietly(in);
        // 关闭连接
        if (conn != null) {
            conn.disconnect();
        }
        return bufferRes.toString();
    }

    /**
     * 获取永久素材
     * @param url 素材地址
     * @return params post参数
     * @return InputStream 流，考虑到这里可能返回json或file
     * @throws IOException
     */
    protected InputStream downloadMaterial(String url, String params) throws IOException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        // 连接超时
        conn.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        conn.setReadTimeout(25000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "Keep-Alive");
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        if (WX_PublicNumberUtil.notBlank(params)) {
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes(Charsets.UTF_8));
            out.flush();
            IOUtils.closeQuietly(out);
        }
        return conn.getInputStream();
    }

    /**
     * 下载素材
     * @param url 素材地址
     * @return MediaFile
     * @throws IOException
     */
    protected MediaFileUtil download(String url) throws IOException {
        MediaFileUtil mediaFile = new MediaFileUtil();
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        // 连接超时
        conn.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        conn.setReadTimeout(25000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();

        if(conn.getContentType().equalsIgnoreCase("text/plain")){
            // 定义BufferedReader输入流来读取URL的响应
            InputStream in = conn.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
            String valueString = null;
            StringBuffer bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null){
                bufferRes.append(valueString);
            }
            read.close();
            IOUtils.closeQuietly(in);
            mediaFile.setError(bufferRes.toString());
        }else{
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String ds = conn.getHeaderField("Content-disposition");
            String fullName = ds.substring(ds.indexOf("filename=\"") + 10, ds.length() - 1);
            String relName = fullName.substring(0, fullName.lastIndexOf("."));
            String suffix = fullName.substring(relName.length()+1);

            mediaFile.setFullName(fullName);
            mediaFile.setFileName(relName);
            mediaFile.setSuffix(suffix);
            mediaFile.setContentLength(conn.getHeaderField("Content-Length"));
            mediaFile.setContentType(conn.getHeaderField("Content-Type"));
            mediaFile.setFileStream(bis);
        }
        return mediaFile;
    }

    // http请求工具代理对象
//    private static HttpDelegate delegate;
//
//    public String upload(String url, File file, String params) {
//        return delegate.upload(url, file, params);
//    }


}
