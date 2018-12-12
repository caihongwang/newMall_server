package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.center.service.CommentsService;
import com.br.newMall.center.service.DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.ALiYunHttpUtils;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.center.utils.TimestampUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.br.newMall.dao.CustomMessageHistoryDao;
import com.br.newMall.dao.DicDao;
import com.br.newMall.dao.UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by caihongwang on 2018/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class CommonServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private DicDao dicDao;

    @Autowired
    private DicService dicService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpsUtil httpsUtil;

    @Value("${newMall.tmp.filepath}")
    private String filePath;

    @Autowired
    private CustomMessageHistoryDao customMessageHistoryDao;

    @Autowired
    private WX_RedPacketService wx_RedPacketService;

    @Autowired
    private CommentsService commentsService;

    @Test
    public void Test(){
        //获取accessToken
        Map<String, Object> accessTokenMap =
                WX_PublicNumberUtil.getAccessToken(
                        "wx07cf52be1444e4b7",
                        "d6de12032cfe660253b96d5f2868a06c");
        if (accessTokenMap != null && accessTokenMap.size() > 0) {
            String accessToken = accessTokenMap.get("access_token") != null ? accessTokenMap.get("access_token").toString() : "";
            getTwoDimensionForWX(
                    accessToken,
                    "pages/tabBar/todayOilPrice/todayOilPrice",
                    "uid=1");
        }

    }

    public Map<String, Object> getTwoDimensionForWX(String accessToken, String page, String scene) {
        Map<String, String> map = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        if (!"".equals(accessToken) && !"".equals(page) && !"".equals(scene)) {
            try {
                map.put("page", page);
                map.put("scene", scene);
                Map<String, String> headers = Maps.newHashMap();
                //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
                String method = "POST";
                //根据API的要求，定义相对应的Content-Type
                headers.put("Content-Type", "application/json; charset=UTF-8");
                Map<String, String> querys = Maps.newHashMap();
                String bodys = JSONObject.toJSONString(map);
                String host = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
                HttpResponse res = ALiYunHttpUtils.doPost(host, "", method, headers, querys, bodys);
                logger.info("向微信服务器发送请求获取，获取响应的is {}", res);
                //处理返回的图片
                HttpEntity entity = res.getEntity();
                String resJsonStr = "";
                if (entity != null) {
                    InputStream inStream = entity.getContent();
                    //1.将图片存储到cdn
                    int size;
                    byte[] buffer = new byte[1024 * 1000000];
                    long startTime = System.currentTimeMillis();
                    //判断 文件夹 是否存存在，如果不存在则创建
                    File dirFile = new File(this.filePath);
                    Boolean dirFileFlag = dirFile.exists();
                    if (dirFileFlag == false) {
                        dirFile.mkdir();
                    }
                    String filePath = this.filePath + UUID.randomUUID() + "_" + startTime + ".jpg";
                    OutputStream out = new FileOutputStream(filePath);
                    do {
                        size = inStream.read(buffer);
                        if (size > 0) {
                            out.write(buffer, 0, size);
                        }
                    } while (size > 0);
                    out.flush();
                    out.close();
                    inStream.close();
//                    // 处理fastdfs文件上传
//                    String dfsPath = FastDfsSyncUtil.upload(filePath);
//                    long endTime = System.currentTimeMillis();
//                    logger.info("向微信获取二维码图片， 在服务器本地的地址 : " + filePath + ", 在dfs服务器的地址 : " + dfsPath +
//                            ", 形成二维码及获取上传图片的总时间 : " + (endTime - startTime));
//                    String cdnUrl = dfsPath;
//                    resultMap.put("cardTwoDimensionCodeUrl", dfsPath);
                } else {
                    logger.error("向微信服务器发送请求获取，获取二维码失败，accessToken=" + accessToken + ",page=" + page + ",scene=" + scene);
                }
            } catch (Exception e) {
                logger.error("向微信服务器发送请求获取，获取二维码时报错 is {}", e);
            }
        }
        return resultMap;
    }




    @Test
    public void test() throws Exception {
        Class handlerClass = com.br.newMall.center.handler.WX_CustomMenuHandler.class;
        List<Class> interfaceList = new ArrayList<>();
        Stack<Class> stack = new Stack<>();
        if (handlerClass != null) {
            for (Class handlerInterface : handlerClass.getInterfaces()) {
                stack.push(handlerInterface);
            }
            while (!stack.isEmpty()) {
                Class handlerInterface = stack.pop();               //获取到thrift编译之后handler的Iface接口类
                interfaceList.add(handlerInterface);
                for (Class parentHandlerInterface : handlerInterface.getInterfaces()) {
                    stack.push(parentHandlerInterface);
                }
            }
        }
    }

    @Test
    public void loginAndOperator_For_EnterpriseSafetyManagementPlatform() throws Exception {
        try {
            //1.准备参数
            String username = "caizhiwen";          //用户名
            String password = "caizhiwen";          //密码
            String loginUrl = "http://corp.unicom-ptt.com:8/html/user/login/main";          //登录url
            String operatorUrl = "http://corp.unicom-ptt.com:8/html/danger/add3/add3";      //操作url
            //准备 隐患排查 项目名称
            List<String> operatorList = Lists.newArrayList();
            //第一横排
            operatorList.add("资质证照");
            operatorList.add("安全生产管理机构及人员");
            operatorList.add("安全规章制度");
            operatorList.add("安全教育培训");
            operatorList.add("安全投入");
            operatorList.add("相关方管理");
            //第二横排
            operatorList.add("重大危险源管理");
            operatorList.add("个体防护");
            operatorList.add("职业健康");
            operatorList.add("应急管理");
            operatorList.add("隐患排查治理");
            operatorList.add("事故报告、调查和处理");
            //第三横排
            operatorList.add("作业场所");
            //operatorList.add("设备设施");
            operatorList.add("防护、保险、信号等装置设备");
            operatorList.add("原辅物料、产品");
            operatorList.add("安全技能");
            operatorList.add("作业许可");
            //当前天时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String currentDateStr = formatter.format(new Date());
            //2.开始模拟登陆并操作
            //准备联网的 httpClient
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
            //准备登录
            List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
            valuePairs.add(new BasicNameValuePair("username", username));
            valuePairs.add(new BasicNameValuePair("password", password));
            HttpPost postLogin = new HttpPost(loginUrl);
            postLogin.setEntity(new UrlEncodedFormEntity(valuePairs));
            postLogin.setEntity(new UrlEncodedFormEntity(valuePairs));
            HttpResponse loginResponse = httpClient.execute(postLogin);
            StatusLine loginResponseState = loginResponse.getStatusLine();
            int loginResponseStateCode = loginResponseState.getStatusCode();
            logger.info("用户名密码登陆--->>状态:" + (302 == loginResponseStateCode ? "登录成功" : "登录失败"));
            logger.info(loginResponseState.toString());

            HttpPost postOperator = new HttpPost(operatorUrl);
            Header[] headers = loginResponse.getHeaders("Set-Cookie");
            for (int i = 0; i < headers.length; i++) {
                postOperator.addHeader(headers[i]);
            }
            Date currentDate = formatter.parse("2016-7-18");
            while (true) {
                String currentDateStr = formatter.format(currentDate);
                //准备操作
                for (String operatorNameStr : operatorList) {
                    StringBody check_type = new StringBody(operatorNameStr, Charset.forName("utf-8"));
                    StringBody result = new StringBody("1", Charset.forName("utf-8"));
                    StringBody is_update = new StringBody("1", Charset.forName("utf-8"));
                    StringBody check_time = new StringBody(currentDateStr, Charset.forName("utf-8"));
                    StringBody organ_id = new StringBody("0", Charset.forName("utf-8"));
                    StringBody organ_name = new StringBody("企业本级", Charset.forName("utf-8"));
                    StringBody person_id = new StringBody("ee9de767-622d-43c6-9368-76443a049063", Charset.forName("utf-8"));
                    StringBody person_name = new StringBody("蔡红旺", Charset.forName("utf-8"));
                    StringBody addr = new StringBody("大路田坝加油站", Charset.forName("utf-8"));
                    StringBody duty_unit = new StringBody("企业本级", Charset.forName("utf-8"));
                    StringBody workshop = new StringBody("良好，一切正常.", Charset.forName("utf-8"));
                    StringBody factory = new StringBody("良好，一切正常.", Charset.forName("utf-8"));
                    HttpEntity reqEntity = MultipartEntityBuilder.create()
                            .addPart("check_type", check_type)
                            .addPart("result", result)
                            .addPart("is_update", is_update)
                            .addPart("check_time", check_time)
                            .addPart("organ_id", organ_id)
                            .addPart("organ_name", organ_name)
                            .addPart("person_id", person_id)
                            .addPart("person_name", person_name)
                            .addPart("addr", addr)
                            .addPart("duty_unit", duty_unit)
                            .addPart("workshop", workshop)
                            .addPart("factory", factory)
                            .build();
                    postOperator.setEntity(reqEntity);
                    HttpResponse postOperatorResponse = httpClient.execute(postOperator);
                    StatusLine operatorResponseState = postOperatorResponse.getStatusLine();
                    int operatorResponseStateCode = loginResponseState.getStatusCode();
                    logger.info("操作:【 " + operatorNameStr + " 】--->>状态: " + (302 == operatorResponseStateCode ? "操作成功" : "操作失败"));
                    logger.info(operatorResponseState.toString());
                    //沉睡5秒
                    Thread.sleep(1000);
                }
                logger.info("============================================================");
                logger.info("============================================================");
                logger.info("============================================================");
                logger.info("时间：【 " + currentDateStr + " 】的企业安全管理平台的隐患排查已登记完毕=======");
                logger.info("时间：【 " + currentDateStr + " 】的企业安全管理平台的隐患排查已登记完毕=======");
                logger.info("时间：【 " + currentDateStr + " 】的企业安全管理平台的隐患排查已登记完毕=======");
                logger.info("============================================================");
                logger.info("============================================================");
                logger.info("============================================================");
                //将时间更改为第二天
                currentDate.setDate(currentDate.getDate() + 1);
                currentDate.setHours(10);
                currentDate.setMinutes(30);
                currentDate.setSeconds(0);
                if (currentDate.after(new Date())) {
                    break;
                }
            }
        } catch (Exception e) {
            Map<String, Object> commentsMap = Maps.newHashMap();
            commentsMap.put("comments", e.getMessage());
            commentsMap.put("uid", "1");
            commentsMap.put("createTime", TimestampUtil.getTimestamp());
            commentsMap.put("updateTime", TimestampUtil.getTimestamp());
            commentsService.addComments(commentsMap);
        }
    }


}
