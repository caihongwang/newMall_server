package com.br.newMall.center.quartz;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.*;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.PingYingUtil;
import com.br.newMall.center.utils.TimestampUtil;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.br.newMall.center.utils.LonLatUtil.cityJson;

/**
 * 定时任务
 */
@Component
public class TimeTaskOfQuartz {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TimeTaskOfQuartz.class);

    //使用环境
    @Value("${useEnvironmental}")
    private String useEnvironmental;

    @Autowired
    private DicService dicService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private OilStationService oilStationService;

    @Autowired
    private WX_MessageService wxMessageService;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    @Autowired
    private OilStationOperatorService oilStationOperatorService;

    @Autowired
    private RedPacketHistoryService redPacketHistoryService;

    /**
     * 每天早上09:00，定时发送红包
     * 对小程序(油价地图)上操作【添加油站】和【纠正油价】的用户直接发送红包
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void do_SendRedPacket_For_OilStationMap() {
        //当昨天时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String yesterDateStr = formatter.format(yesterDate);
        //1.整合参数
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("createTime", yesterDateStr);
        ResultDTO resultDTO = oilStationOperatorService.getSimpleOilStationOperatorByCondition(paramMap);
        if (resultDTO.getResultList() != null && resultDTO.getResultList().size() > 0) {
            List<Map<String, String>> oilStationOperatorList = resultDTO.getResultList();
            for (Map<String, String> oilStationOperatorMap : oilStationOperatorList) {
                Object idObj = oilStationOperatorMap.get("id");
                Object uidObj = oilStationOperatorMap.get("uid");
                Object openIdObj = oilStationOperatorMap.get("openId");
                Object redPacketTotalObj = oilStationOperatorMap.get("redPacketTotal");
                if (uidObj != null && openIdObj != null
                        && redPacketTotalObj != null && !"".equals(redPacketTotalObj.toString())
                        && !"0".equals(redPacketTotalObj.toString())) {
                    //2.整合发送红包的参数
                    Map<String, Object> redPacketParamMap = Maps.newHashMap();
                    float redPacketTotalFloat = Float.parseFloat(redPacketTotalObj.toString() != "" ? redPacketTotalObj.toString() : "10");
                    String redPacketTotal = ((int) (redPacketTotalFloat * 100)) + "";
                    redPacketParamMap.put("amount", redPacketTotal);
                    redPacketParamMap.put("openId", openIdObj.toString());
                    redPacketParamMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                    redPacketParamMap.put("wxPublicNumGhId", "gh_417c90af3488");
                    redPacketParamMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                    ResultMapDTO resultMapDTO = wxRedPacketService.enterprisePayment(redPacketParamMap);
                    //3.将加油站操作记录表的状态变更为已处理
                    Map<String, Object> oilStationOperatorMap_updateParam = Maps.newHashMap();
                    oilStationOperatorMap_updateParam.put("id", oilStationOperatorMap.get("id"));
                    oilStationOperatorMap_updateParam.put("status", "1");
                    //4.发送成功，将已发送的红包进行记录，并保存.
                    if (NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()) {
                        //更新加油站操作的红包状态
                        Map<String, Object> paramMap_temp = Maps.newHashMap();
                        paramMap_temp.clear();      //清空参数，重新传参
                        paramMap_temp.put("id", idObj.toString());
                        paramMap_temp.put("status", "1");
                        oilStationOperatorService.updateOilStationOperator(paramMap_temp);
                        //插入红包操作记录
                        paramMap_temp.clear();      //清空参数，重新传参
                        paramMap_temp.put("uid", uidObj.toString());
                        paramMap_temp.put("operatorId", idObj.toString());
                        paramMap_temp.put("redPacketMoney", redPacketTotalObj.toString());
                        paramMap_temp.put("remark", "红包正常发送");
                        paramMap_temp.put("status", "1");
                        redPacketHistoryService.addRedPacketHistory(paramMap_temp);
                    } else {
                        //更新加油站操作的红包状态
                        Map<String, Object> paramMap_temp = Maps.newHashMap();
                        paramMap_temp.clear();      //清空参数，重新传参
                        paramMap_temp.put("id", idObj.toString());
                        paramMap_temp.put("status", "0");
                        oilStationOperatorService.updateOilStationOperator(paramMap_temp);
                        //插入红包操作记录
                        paramMap_temp.clear();      //清空参数，重新传参
                        paramMap_temp.put("uid", uidObj.toString());
                        paramMap_temp.put("operatorId", idObj.toString());
                        paramMap_temp.put("redPacketMoney", redPacketTotalObj.toString());
                        paramMap_temp.put("remark", resultMapDTO.getMessage());
                        paramMap_temp.put("status", "0");
                        redPacketHistoryService.addRedPacketHistory(paramMap_temp);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /**
     * 每天下午17:00，定时油价资讯通知
     */
    @Scheduled(cron = "0 0 17 * * ?")
    public void do_OilPrizeMessage_For_OilStationMap() {
        Map<String, Object> paramMap = Maps.newHashMap();
        try {
            wxMessageService.messageSend(paramMap);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
            logger.error("发送油价资讯通知时异常，e :", e);
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
            logger.error(">>>>>>>>>>>>>>>>>>>发送油价资讯通知时异常<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    /**
     * 每周日下午20点30分45秒执行任务，定时获取最新的全国加油站数据
     * 通过腾讯地图获取所有城市的加油站
     */
    @Scheduled(cron = "45 30 20 ? * SUN")
    public void do_getOilStationByTencetMap_For_OilStationMap() {
        Map<String, Object> dicMap = Maps.newHashMap();
        dicMap.put("dicType", "city");
        ResultDTO cityResultDTO = dicService.getSimpleDicByCondition(dicMap);
        if (cityResultDTO.getResultList() != null
                && cityResultDTO.getResultList().size() > 0) {
            List<Map<String, String>> cityList = cityResultDTO.getResultList();
            for (int j = 0; j < cityList.size(); j++) {
                Map<String, String> cityMap = cityList.get(j);
                String cityName = cityMap.get("cityName");
                try {
                    //当获取当天0点时间
                    Calendar calendar_0 = Calendar.getInstance();
                    calendar_0.setTime(new Date());
                    calendar_0.set(Calendar.HOUR, -12);
                    calendar_0.set(Calendar.MINUTE, 0);
                    calendar_0.set(Calendar.SECOND, 0);
                    Date calendar_0_date = calendar_0.getTime();
                    //当获取当天8点时间
                    Calendar calendar_6 = Calendar.getInstance();
                    calendar_6.setTime(new Date());
                    calendar_6.set(Calendar.HOUR, -6);
                    calendar_6.set(Calendar.MINUTE, 0);
                    calendar_6.set(Calendar.SECOND, 0);
                    Date calendar_6_date = calendar_6.getTime();
                    //获取当前时间
                    Date currentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    logger.info("当获取当天【0点】时间 : " + sdf.format(calendar_0_date));
                    logger.info("当获取当天【当前】时间 : " + sdf.format(currentDate));
                    logger.info("当获取当天【6点】时间 : " + sdf.format(calendar_6_date));
                    if (currentDate.after(calendar_0_date) && currentDate.before(calendar_6_date)) {
                        String keyWord = "加油站";
                        Integer pageSize = 20;
                        Integer pageIndex = 1;
                        String orderby = "_distance";
                        List<Map<String, Object>> oilStationList = LonLatUtil.getDetailAddressByCityAndKeyWord(cityName, keyWord, pageSize, pageIndex, orderby);
                        if (oilStationList.size() > 0) {
                            String oilStationNum = oilStationList.size() + "";
                            //更新字典表中city的加油站数量
                            Map<String, String> dicRemarkMap = Maps.newHashMap();
                            dicRemarkMap.put("cityName", cityMap.get("cityName"));
                            dicRemarkMap.put("cityCode", cityMap.get("cityCode"));
                            dicRemarkMap.put("oilStationNum", oilStationNum);
                            dicMap.clear();
                            dicMap.put("dicRemark", JSONObject.toJSONString(dicRemarkMap));
                            dicMap.put("dicStatus", "1");
                            dicMap.put("id", cityMap.get("id"));
                            dicService.updateDic(dicMap);
                            //对加油站进行入库
                            for (int i = 0; i < oilStationList.size(); i++) {
                                Map<String, Object> newMall = oilStationList.get(i);
                                //准备参数插入
                                Map<String, Object> paramMap = Maps.newHashMap();
                                paramMap.put("uid", "1");
                                paramMap.put("oilStationName", newMall.get("title"));
                                paramMap.put("oilStationAreaSpell", PingYingUtil.getPingYin(newMall.get("city") != null ? newMall.get("city").toString() : ""));
                                paramMap.put("oilStationAreaName",
                                        (newMall.get("province") != null ? newMall.get("province").toString() : "") +
                                                (newMall.get("city") != null ? newMall.get("city").toString() : "") +
                                                (newMall.get("district") != null ? newMall.get("district").toString() : "")
                                );
                                paramMap.put("oilStationAdress", newMall.get("address"));
                                paramMap.put("oilStationBrandName", "民营");
                                paramMap.put("oilStationType", "民营");
                                paramMap.put("oilStationDiscount", newMall.get("category"));
                                paramMap.put("oilStationExhaust", "国Ⅳ");
                                paramMap.put("oilStationPosition",
                                        (newMall.get("lng") != null ? newMall.get("lng").toString() : "") +
                                                (newMall.get("lat") != null ? newMall.get("lat").toString() : "")
                                );
                                paramMap.put("oilStationLon", newMall.get("lng") != null ? newMall.get("lng").toString() : "");
                                paramMap.put("oilStationLat", newMall.get("lat") != null ? newMall.get("lat").toString() : "");
                                paramMap.put("oilStationPayType", "微信，支付宝，银联，现金，赊账等");
                                paramMap.put("oilStationPrice", "[{\"oilModelLabel\":\"0\",\"oilNameLabel\":\"柴油\",\"oilPriceLabel\":\"7.43\"},{\"oilModelLabel\":\"92\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"7.65\"},{\"oilModelLabel\":\"95\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"8.28\"}]");
                                paramMap.put("oilStationDistance", "");
                                paramMap.put("isManualModify", "0");
                                oilStationService.addOrUpdateOilStation(paramMap);
                            }
                        } else {
                            //当获取当天24点时间
                            Calendar calendar_24 = Calendar.getInstance();
                            calendar_24.setTime(new Date());
                            calendar_24.set(Calendar.HOUR, -12);
                            calendar_24.set(Calendar.MINUTE, 0);
                            calendar_24.set(Calendar.SECOND, 0);
                            Date calendar_24_date = calendar_0.getTime();
                            long leftTime = calendar_24_date.getTime() - currentDate.getTime();
                            Thread.sleep(leftTime);
                            logger.info("当前时间不统计腾讯地图上的加油站.");
                        }
                    } else {
                        logger.error("获取当前城市【" + cityName + "】暂时没有加油站，反正我不信...");
                    }
                } catch (Exception e) {
                    logger.error("获取当前城市【" + cityName + "】加油站失败.");
                    continue;
                }
            }
        }
    }

    /**
     * 每天早上11:00，定时提交表单
     * 企业安全管理平台的隐患排查
     */
    @Scheduled(cron = "0 0 16 * * ?")
    public void do_LoginAndOperator_For_EnterpriseSafetyManagementPlatform() {
        logger.info("【定时任务】企业安全管理平台的隐患排查 当前环境 useEnvironmental = " + useEnvironmental);
        if (useEnvironmental != null && "develop".equals(useEnvironmental)) {
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
                String currentDateStr = formatter.format(new Date());
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
                logger.info("用户名密码登陆--->>状态:" + ("302".equals(loginResponseState) ? "登录成功" : "登录失败"));
                logger.info(loginResponseState.toString());
                //准备操作
                for (String operatorNameStr : operatorList) {
                    HttpPost postOperator = new HttpPost(operatorUrl);
                    Header[] headers = loginResponse.getHeaders("Set-Cookie");
                    for (int i = 0; i < headers.length; i++) {
                        postOperator.addHeader(headers[i]);
                    }
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
                    logger.info("用户名密码登陆--->>状态: " + ("302".equals(loginResponseState) ? "登录成功" : "登录失败"));
                    logger.info("操作:【 " + operatorNameStr + " 】--->>状态: " + ("302".equals(loginResponseState) ? "操作成功" : "操作失败"));
                    logger.info(operatorResponseState.toString());
                    //沉睡5秒
                    Thread.sleep(5000);
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

}
