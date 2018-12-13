package com.br.newMall.center.quartz;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.*;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.PingYingUtil;
import com.br.newMall.center.utils.TimestampUtil;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TimeTaskOfQuartzTest {

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
    private WX_RedPacketService wxRedPacketService;

    @Autowired
    private OilStationOperatorService oilStationOperatorService;

    @Autowired
    private RedPacketHistoryService redPacketHistoryService;

    @Test
    public void Test(){
//        do_SendRedPacket_For_OilStationMap();
        do_getOilStationByTencetMap_For_OilStationMap();
//        insertAllCityDic();
    }


    public void insertAllCityDic(){
        List<Map<String, String>> cityList = JSONObject.parseObject(LonLatUtil.cityJson, List.class);
        for (int j = 0; j < cityList.size(); j++) {
            Map<String, String> cityMap = cityList.get(j);
            Map<String, Object> dicMap = Maps.newHashMap();
            Map<String, Object> dicRemarkMap = Maps.newHashMap();
            dicRemarkMap.put("cityName", cityMap.get("city"));
            dicRemarkMap.put("cityCode", j+100001);
            dicMap.put("dicRemark", JSONObject.toJSONString(dicRemarkMap));
            dicMap.put("dicName", cityMap.get("city"));
            dicMap.put("dicType", "city");
            dicMap.put("dicCode", j+100001);
            dicMap.put("createTime", TimestampUtil.getTimestamp());
            dicMap.put("updateTime", TimestampUtil.getTimestamp());
            dicService.addDic(dicMap);
        }
    }

    public void do_getOilStationByTencetMap_For_OilStationMap() {
        Map<String, Object> dicMap = Maps.newHashMap();
        dicMap.put("dicType", "city");
        ResultDTO cityResultDTO= dicService.getSimpleDicByCondition(dicMap);
        if(cityResultDTO.getResultList() != null
                && cityResultDTO.getResultList().size() > 0){
            List<Map<String, String>> cityList = cityResultDTO.getResultList();
            for (int j = 0; j < cityList.size(); j++) {
                Map<String, String> cityMap = cityList.get(j);
                String cityName = cityMap.get("cityName");
                try{
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
//                    if(currentDate.after(calendar_0_date) && currentDate.before(calendar_6_date)){
                    String keyWord = "加油站";
                    Integer pageSize = 20;
                    Integer pageIndex = 1;
                    String orderby = "_distance";
                    List<Map<String, Object>> oilStationList = LonLatUtil.getDetailAddressByCityAndKeyWord(cityName, keyWord, pageSize, pageIndex, orderby);
                    if("吉林".equals(cityName)){
                        System.out.println("cityName = " + cityName);
                    }
                    if(oilStationList.size() > 0){
                        String oilStationNum = oilStationList.size()+"";
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
                            paramMap.put("oilStationAreaSpell", PingYingUtil.getPingYin(newMall.get("city")!=null?newMall.get("city").toString():""));
                            paramMap.put("oilStationAreaName",
                                    (newMall.get("province")!=null?newMall.get("province").toString():"") +
                                            (newMall.get("city")!=null?newMall.get("city").toString():"") +
                                            (newMall.get("district")!=null?newMall.get("district").toString():"")
                            );
                            paramMap.put("oilStationAdress", newMall.get("address"));
                            paramMap.put("oilStationBrandName", "民营");
                            paramMap.put("oilStationType", "民营");
                            paramMap.put("oilStationDiscount", newMall.get("category"));
                            paramMap.put("oilStationExhaust", "国Ⅳ");
                            paramMap.put("oilStationPosition",
                                    (newMall.get("lng")!=null?newMall.get("lng").toString():"") +
                                            (newMall.get("lat")!=null?newMall.get("lat").toString():"")
                            );
                            paramMap.put("oilStationLon", newMall.get("lng")!=null?newMall.get("lng").toString():"");
                            paramMap.put("oilStationLat", newMall.get("lat")!=null?newMall.get("lat").toString():"");
                            paramMap.put("oilStationPayType", "微信，支付宝，银联，现金，赊账等");
                            paramMap.put("oilStationPrice", "[{\"oilModelLabel\":\"0\",\"oilNameLabel\":\"柴油\",\"oilPriceLabel\":\"7.43\"},{\"oilModelLabel\":\"92\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"7.65\"},{\"oilModelLabel\":\"95\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"8.28\"}]");
                            paramMap.put("oilStationDistance", "");
                            paramMap.put("isManualModify", "0");
                            oilStationService.addOrUpdateOilStation(paramMap);
                        }
//                    } else {
//                        //当获取当天24点时间
//                        Calendar calendar_24 = Calendar.getInstance();
//                        calendar_24.setTime(new Date());
//                        calendar_24.set(Calendar.HOUR, -12);
//                        calendar_24.set(Calendar.MINUTE, 0);
//                        calendar_24.set(Calendar.SECOND, 0);
//                        Date calendar_24_date = calendar_0.getTime();
//                        long leftTime = calendar_24_date.getTime()-currentDate.getTime();
//                        Thread.sleep(leftTime);
//                        logger.info("当前时间不统计腾讯地图上的加油站.");
//                    }
                    } else {
                        logger.error("获取当前城市【"+cityName+"】暂时没有加油站，反正我不信...");
                    }
                } catch (Exception e) {
                    logger.error("获取当前城市【"+cityName+"】加油站失败.");
                    continue;
                }
            }
        }
    }



    public void do_SendRedPacket_For_OilStationMap() {
        //当昨天时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String yesterDateStr = formatter.format(yesterDate);
        //1.整合参数
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("createTime", "2018-11-14");
        ResultDTO resultDTO = oilStationOperatorService.getSimpleOilStationOperatorByCondition(paramMap);
        if(resultDTO.getResultList() != null && resultDTO.getResultList().size() > 0){
            List<Map<String, String>> oilStationOperatorList = resultDTO.getResultList();
            for(Map<String, String> oilStationOperatorMap : oilStationOperatorList){
                Object idObj = oilStationOperatorMap.get("id");
                Object uidObj = oilStationOperatorMap.get("uid");
                Object openIdObj = oilStationOperatorMap.get("openId");
                Object redPacketTotalObj = oilStationOperatorMap.get("redPacketTotal");
                if(uidObj != null && openIdObj != null
                        && redPacketTotalObj != null && !"".equals(redPacketTotalObj.toString())
                        && !"0".equals(redPacketTotalObj.toString())){
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
                    if(NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()){
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

}
