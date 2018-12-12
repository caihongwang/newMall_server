package com.br.oilStationMap.center.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.oilStationMap.api.code.OilStationMapCode;
import com.br.oilStationMap.center.service.OilStationService;
import com.br.oilStationMap.dao.OilStationDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LonLatUtilTest {

    public static double EARTH_RADIUS = 6371.393;

    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static final Logger logger = LoggerFactory.getLogger(LonLatUtil.class);

    public static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:center-context.xml");
    public static OilStationService oilStationService = context.getBean("oilStationServiceImpl", OilStationService.class);
    public static OilStationDao oilStationDao = context.getBean("oilStationDao", OilStationDao.class);

    @Test
    public void Test(){
//        Double lon = 116.29845;
//        Double lat = 39.95933;
//        Double page = 5000.00;
//        Double r = 1.00;
//        String keywords = "北京";
//        getOilStationListByCity(lon, lat, keywords, page);
//        getOilStationListByLonLat(lon, lat, page, r);

        List<Map<String, String>> cityList = JSONObject.parseObject(cityJson, List.class);
        for (int j = 0; j < cityList.size(); j++) {
            Map<String, String> cityMap = cityList.get(j);
//          String city = "铜仁";
            String city = cityMap.get("city");
            String oilStationNum = cityMap.get("oilStationNum")!=null?cityMap.get("oilStationNum").toString():"";
            if(!"".equals(oilStationNum)){
                String keyWord = "加油站";
                Integer pageSize = 20;
                Integer pageIndex = 1;
                String orderby = "_distance";
                List<Map<String, Object>> oilStationList = getDetailAddressByCityAndKeyWord(city, keyWord, pageSize, pageIndex, orderby);
                cityMap.put("oilStationNum", oilStationList.size() + "");
                for (int i = 0; i < oilStationList.size(); i++) {
                    Map<String, Object> oilStationMap = oilStationList.get(i);
                    //准备参数插入
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("uid", "1");
                    paramMap.put("oilStationName", oilStationMap.get("title"));
                    paramMap.put("oilStationAreaSpell", PingYingUtil.getPingYin(oilStationMap.get("city")!=null?oilStationMap.get("city").toString():""));
                    paramMap.put("oilStationAreaName",
                            (oilStationMap.get("province")!=null?oilStationMap.get("province").toString():"") +
                                    (oilStationMap.get("city")!=null?oilStationMap.get("city").toString():"") +
                                    (oilStationMap.get("district")!=null?oilStationMap.get("district").toString():"")
                    );
                    paramMap.put("oilStationAdress", oilStationMap.get("address"));
                    paramMap.put("oilStationBrandName", "民营");
                    paramMap.put("oilStationType", "民营");
                    paramMap.put("oilStationDiscount", oilStationMap.get("category"));
                    paramMap.put("oilStationExhaust", "国Ⅳ");
                    paramMap.put("oilStationPosition",
                            (oilStationMap.get("lng")!=null?oilStationMap.get("lng").toString():"") +
                                    (oilStationMap.get("lat")!=null?oilStationMap.get("lat").toString():"")
                    );
                    paramMap.put("oilStationLon", oilStationMap.get("lng")!=null?oilStationMap.get("lng").toString():"");
                    paramMap.put("oilStationLat", oilStationMap.get("lat")!=null?oilStationMap.get("lat").toString():"");
                    paramMap.put("oilStationPayType", "微信，支付宝，银联，现金，赊账等");
                    paramMap.put("oilStationPrice", "[{\"oilModelLabel\":\"0\",\"oilNameLabel\":\"柴油\",\"oilPriceLabel\":\"7.43\"},{\"oilModelLabel\":\"92\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"7.65\"},{\"oilModelLabel\":\"95\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"8.28\"}]");
                    paramMap.put("oilStationDistance", "");
                    paramMap.put("isManualModify", "1");
                    if("大路田坝加油站".equals(oilStationMap.get("title").toString())){
                        oilStationService.addOrUpdateOilStation(paramMap);
                    } else {
                        oilStationService.addOrUpdateOilStation(paramMap);
                    }
                }
            } else {
                continue;
            }
        }

    }


    /**
     * 根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】
     * 地点搜索,默认使用周边搜索，文档地址：https://lbs.qq.com/webservice_v1/guide-search.html
     *
     * boundary:
     *      示例1，指定地区名称，不自动扩大范围： boundary=region(北京,0)
     *      示例2，周边搜索（圆形范围）：boundary=nearby(39.908491,116.374328,1000)
     *      示例3，矩形区域范围：boundary=rectangle(39.9072,116.3689,39.9149,116.3793)
     * page_size:每页条目数，最大限制为20条
     * page_index:第x页，默认第1页
     * orderby:排序，目前仅周边搜索（boundary=nearby）, 支持按距离由近到远排序，取值：_distance
     *
     * @param city
     * @param keyWord
     * @return
     */
    public static List<Map<String, Object>> getDetailAddressByCityAndKeyWord(String city, String keyWord,
                                                                             Integer pageSize, Integer pageIndex, String orderby) {
        List<Map<String, Object>> resultMapList = Lists.newArrayList();
        String host = OilStationMapCode.TENCENT_HOST;
        String path = OilStationMapCode.TENCENT_PATH_GET_SEARCH;
        String method = OilStationMapCode.TENCENT_METHOD;
        Map<String, String> headers = Maps.newHashMap();
        Map<String, String> querys = Maps.newHashMap();
        if(city.contains("(")){
            querys.put("boundary", city);
        } else {
            querys.put("boundary", "region("+city+",0)");
        }
        querys.put("page_size", pageSize.toString());
        querys.put("page_index", pageIndex.toString());
        querys.put("keyword", keyWord);
        querys.put("orderby", orderby);
        querys.put("key", OilStationMapCode.TENCENT_KEY);
        try {
            HttpResponse response = ALiYunHttpUtils.doGet(host, path, method, headers, querys);
            StatusLine responseState = response.getStatusLine();
            logger.info("在LonLatUtil中根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】-getDetailAddressByCityAndKeyWord, 从腾讯地图获取地址的状态 : " + responseState);
            logger.info("在LonLatUtil中根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】-getDetailAddressByCityAndKeyWord, 从腾讯地图获取地址的Headers : " + response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            String resJsonStr = "";
            if (entity != null) {
                resJsonStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("在LonLatUtil中根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】-getDetailAddressByCityAndKeyWord, 从腾讯地图获取地址的结果 : " + resJsonStr);
            JSONObject resJSONObject = JSONObject.parseObject(resJsonStr);
            String statusStr = resJSONObject.getString("status");       //状态
            if ("0".equals(statusStr)) {
                String count = resJSONObject.getString("count");        //总数
                JSONArray dataJSONArray = resJSONObject.getJSONArray("data");
                if(dataJSONArray != null && dataJSONArray.size() > 0){
                    for(int i=0;i<dataJSONArray.size();i++){
                        JSONObject resultJSONObject = dataJSONArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        //名称
                        Object title = resultJSONObject.getString("title");
                        //地址
                        Object address = resultJSONObject.getString("address");
                        //电话
                        Object tel = resultJSONObject.getString("tel");
                        //类别
                        Object category = resultJSONObject.getString("category");
                        //经纬度
                        Object lng = null;
                        Object lat = null;
                        JSONObject locationJSONObject = resultJSONObject.getJSONObject("location");
                        if(locationJSONObject != null && locationJSONObject.size() > 0){
                            lng = locationJSONObject.getString("lng");
                            lat = locationJSONObject.getString("lat");
                        }
                        //详细地址
                        Object province = null;
                        Object cityObj = null;
                        Object district = null;
                        Object adcode = null;
                        JSONObject adInfoJSONObject = resultJSONObject.getJSONObject("ad_info");
                        if(adInfoJSONObject != null && adInfoJSONObject.size() > 0){
                            province = adInfoJSONObject.getString("province");
                            cityObj = adInfoJSONObject.getString("city");
                            district = adInfoJSONObject.getString("district");
                            adcode = adInfoJSONObject.getString("adcode");
                        }
                        //整合响应结果
                        Map<String, Object> resultMap = Maps.newHashMap();
                        resultMap.put("title", title);
                        resultMap.put("address", address);
                        resultMap.put("tel", tel);
                        resultMap.put("category", category);
                        resultMap.put("lng", lng);
                        resultMap.put("lat", lat);
                        resultMap.put("province", province);
                        resultMap.put("city", city);
                        resultMap.put("district", district);
                        resultMap.put("adcode", adcode);
                        resultMapList.add(resultMap);
                    }
                    Integer countNum = 0;
                    try{
                        countNum = Integer.parseInt(count);
                    } catch (Exception e) {
                        countNum = 0;
                    }

                    logger.info("===========================================");
                    logger.info("===========================================");
                    logger.info("countNum = " + countNum);
                    logger.info("pageIndex = " + pageIndex);
                    logger.info("pageSize*pageIndex = " + pageSize*pageIndex);
                    logger.info("(countNum - pageSize*pageIndex) = " + (countNum - pageSize*pageIndex));
                    logger.info("((countNum - pageSize*pageIndex) > 0) = " + ((countNum - pageSize*pageIndex) > 0));
                    logger.info("当前的 resultMapList.size() = " + resultMapList.size());
                    logger.info("===========================================");
                    logger.info("===========================================");
                    if(countNum > 0 && ((countNum - pageSize*pageIndex) > 0)){
                        pageIndex++;
                        Thread.sleep(2000);     //此key每秒请求量已达到上限,每秒5次.
                        resultMapList.addAll(getDetailAddressByCityAndKeyWord(city, keyWord, pageSize, pageIndex, orderby));
                    } else {
                        //停止递归
                        return resultMapList;
                    }
                } else {
                    //停止递归
                    return resultMapList;
                }
            }
        } catch (Exception e) {
            logger.error("在LonLatUtil中根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】-getDetailAddressByCityAndKeyWord is error 解析结果失败, e : " + e);
        }
        logger.info("在LonLatUtil中根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】-getDetailAddressByCityAndKeyWord,结果-resultMap:" + JSONObject.toJSONString(resultMapList));
        logger.info("===========================================");
        logger.info("===========================================");
        logger.info("resultMapList.size() = " + resultMapList.size());
        logger.info("===========================================");
        logger.info("===========================================");
        return resultMapList;
    }

    /**
     * 根据城市名称获取附近的加油站
     *
     * @param city
     * @param keywords
     * @param page
     * @return
     */
    public static List<Map<String, String>> getOilStationListByCity(Double lon, Double lat, String keywords, Double page) {
        List<Map<String, Object>> oilStationList = Lists.newArrayList();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        try {
            String city = getAddressByLonLat(lon, lat, "city");
            String host = OilStationMapCode.A_LI_YUN_HOST;
            String path = OilStationMapCode.A_LI_YUN_PATH_BY_CITY;
            String method = OilStationMapCode.A_LI_YUN_METHOD;
            String appcode = OilStationMapCode.A_LI_YUN_APP_CODE;
            Map<String, String> headers = Maps.newHashMap();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + OilStationMapCode.A_LI_YUN_APP_CODE);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", OilStationMapCode.A_LI_YUN_CONTENT_TYPE);
            Map<String, String> bodys = Maps.newHashMap();
            Map<String, String> querys = Maps.newHashMap();
            if (!"".equals(page.toString())) {
                querys.put("city", new String(city.getBytes("UTF-8")));
            } else {
                querys.put("city", new String("铜仁市".getBytes("UTF-8")));
            }
            if (!"".equals(keywords)) {
                querys.put("keywords", new String(keywords.getBytes("UTF-8")));
            }
            if (!"".equals(page.toString())) {
                querys.put("page", page.toString());
            } else {
                querys.put("page", "1");
            }

            HttpResponse response = ALiYunHttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            StatusLine responseState = response.getStatusLine();
            logger.info("在service中获取加油站列表-getOilStationListByCity, 通过【城市】获取在线加油站列表请求的状态 : " + responseState);
            logger.info("在service中获取加油站列表-getOilStationListByCity, 通过【城市】获取在线加油站列表请求的Headers : " + response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            String resJsonStr = "";
            if (entity != null) {
                resJsonStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("在service中获取加油站列表-getOilStationListByCity, 通过【城市】获取在线加油站列表请求的结果 : " + resJsonStr);
            JSONObject resJSONObject = JSONObject.parseObject(resJsonStr);
            String statusStr = resJSONObject.getString("status");
            if ("000000".equals(statusStr)) {
                JSONObject resultJSONObject = resJSONObject.getJSONObject("result");
                JSONArray dataValueArray = resultJSONObject.getJSONArray("data");
                List<Map<String, Object>> dataList = JSONObject.parseObject(dataValueArray.toJSONString(), List.class);
                if (dataList != null && dataList.size() > 0) {
                    for (Map<String, Object> dataMap : dataList) {
                        Map<String, Object> oilStationMap = Maps.newHashMap();
                        String oilStationCode = dataMap.get("id") != null ? dataMap.get("id").toString() : "";
                        oilStationMap.put("oilStationCode", oilStationCode);
                        String oilStationName = dataMap.get("name") != null ? dataMap.get("name").toString() : "";
                        oilStationMap.put("oilStationName", oilStationName);
                        String oilStationAreaSpell = dataMap.get("area") != null ? dataMap.get("area").toString() : "";
                        oilStationMap.put("oilStationAreaSpell", oilStationAreaSpell);
                        String oilStationAreaName = dataMap.get("areaname") != null ? dataMap.get("areaname").toString() : "";
                        oilStationMap.put("oilStationAreaName", oilStationAreaName);
                        String oilStationAdress = dataMap.get("address") != null ? dataMap.get("address").toString() : "";
                        oilStationMap.put("oilStationAdress", oilStationAdress);
                        String oilStationBrandName = dataMap.get("brandname") != null ? dataMap.get("brandname").toString() : "";
                        oilStationMap.put("oilStationBrandName", oilStationBrandName);
                        String oilStationType = dataMap.get("type") != null ? dataMap.get("type").toString() : "";
                        oilStationMap.put("oilStationType", oilStationType);
                        String oilStationDiscount = dataMap.get("discount") != null ? dataMap.get("discount").toString() : "";
                        oilStationMap.put("oilStationDiscount", oilStationDiscount);
                        String oilStationExhaust = dataMap.get("exhaust") != null ? dataMap.get("exhaust").toString() : "";
                        oilStationMap.put("oilStationExhaust", oilStationExhaust);
                        String oilStationPosition = dataMap.get("position") != null ? dataMap.get("position").toString() : "";
                        oilStationMap.put("oilStationPosition", oilStationPosition);
                        //获取到坐标默认是百度地图坐标需要转换为腾讯地图坐标
                        Double oilStationLon = Double.parseDouble(dataMap.get("lon") != null ? dataMap.get("lon").toString() : "0");
                        Double oilStationLat = Double.parseDouble(dataMap.get("lat") != null ? dataMap.get("lat").toString() : "0");
                        LonLatUtil.map_bd2tx(oilStationLon, oilStationLat);
                        oilStationMap.put("oilStationLon", oilStationLon);
                        oilStationMap.put("oilStationLat", oilStationLat);
                        String oilStationPayType = dataMap.get("fwlsmc") != null ? dataMap.get("fwlsmc").toString() : "";
                        oilStationMap.put("oilStationPayType", oilStationPayType);
                        String oilStationDistance = dataMap.get("distance") != null ? dataMap.get("distance").toString() : "0";
                        oilStationMap.put("oilStationDistance", oilStationDistance);
                        List<Map<String, Object>> allPriceList = new ArrayList<>();
                        Map<String, Object> priceMap = dataMap.get("price") != null ? (Map<String, Object>) dataMap.get("price") : new HashMap<String, Object>();
                        Map<String, Object> gastPriceMap = dataMap.get("gastprice") != null ? (Map<String, Object>) dataMap.get("gastprice") : new HashMap<String, Object>();
                        List<Map<String, Object>> priceList = parseOilStationPrice(JSONObject.toJSONString(priceMap));
                        List<Map<String, Object>> gastPriceList = parseOilStationPrice(JSONObject.toJSONString(gastPriceMap));
                        if (gastPriceList != null && gastPriceList.size() > 0) {
                            allPriceList.addAll(gastPriceList);
                        }
                        if (priceList != null && priceList.size() > 0) {
                            allPriceList.addAll(priceList);
                        }
                        oilStationMap.put("oilStationPrice", JSONObject.toJSONString(allPriceList));

                        Object createTime = TimestampUtil.getTimestamp();
                        oilStationMap.put("createTime", createTime);
                        Object updateTime = TimestampUtil.getTimestamp();
                        oilStationMap.put("updateTime", updateTime);
                        oilStationMap.put("isManualModify", 0);
                        oilStationList.add(oilStationMap);

                        //检测是否已经存在,并且允许自动更新
                        Map<String, Object> paramMapTemp = Maps.newHashMap();
                        paramMapTemp.put("oilStationCode", oilStationCode);
                        List<Map<String, Object>> exist_oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMapTemp);
                        if(exist_oilStationList != null && exist_oilStationList.size() > 0){
                            Object isManualModifyObj = exist_oilStationList.get(0).get("isManualModify");
                            if(isManualModifyObj != null && "0".equals(isManualModifyObj.toString())){
                                //将从网络获取到的加油站数据进行更新
                                oilStationService.updateOilStation(oilStationMap);
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,更新成功，当前加油站数据默认【自动更新】数据模式.");
                            } else {
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,已存在，但是已被某个用户手动更改过，当前加油站数据已变为【手动更新】数据模式.");
                            }
                        } else {
                            //将从网络获取到的加油站数据进行入库
                            oilStationService.addOilStation(oilStationMap);
                            logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,新增成功，当前加油站数据默认【自动更新】数据模式.");
                        }
                    }
                    oilStationStrList.addAll(MapUtil.getStringMapList(oilStationList));
                    return oilStationStrList;
                }
            }
        } catch (Exception e) {
            logger.error("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity is error 解析结果失败, e : " + e);
        }
        return oilStationStrList;
    }

    /**
     * 根据经纬度获取附近的加油站
     *
     * @param lon
     * @param lat
     * @param page
     * @param r
     * @return
     */
    public static List<Map<String, String>> getOilStationListByLonLat(Double lon, Double lat, Double page, Double r) {
        List<Map<String, Object>> oilStationList = Lists.newArrayList();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        String host = OilStationMapCode.A_LI_YUN_HOST;
        String path = OilStationMapCode.A_LI_YUN_PATH_BY_LONLAT;
        String method = OilStationMapCode.A_LI_YUN_METHOD;
        String appcode = OilStationMapCode.A_LI_YUN_APP_CODE;
        Map<String, String> headers = Maps.newHashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + OilStationMapCode.A_LI_YUN_APP_CODE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", OilStationMapCode.A_LI_YUN_CONTENT_TYPE);
        Map<String, String> bodys = Maps.newHashMap();
        Map<String, String> querys = Maps.newHashMap();
        querys.put("lon", lon.toString());
        querys.put("lat", lat.toString());
        querys.put("page", page.toString());
        querys.put("r", r.toString());
        try {
            HttpResponse response = ALiYunHttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            StatusLine responseState = response.getStatusLine();
            logger.info("在service中获取加油站列表-getOilStationList, 通过【经纬度】获取在线加油站列表请求的状态 : " + responseState);
            logger.info("在service中获取加油站列表-getOilStationList, 通过【经纬度】获取在线加油站列表请求的Headers : " + response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            String resJsonStr = "";
            if (entity != null) {
                resJsonStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("在service中获取加油站列表-getOilStationList, 通过【经纬度】获取在线加油站列表请求的结果 : " + resJsonStr);
            JSONObject resJSONObject = JSONObject.parseObject(resJsonStr);
            String statusStr = resJSONObject.getString("status");
            if ("000000".equals(statusStr)) {
                JSONObject resultJSONObject = resJSONObject.getJSONObject("result");
                JSONArray dataValueArray = resultJSONObject.getJSONArray("data");
                List<Map<String, Object>> dataList = JSONObject.parseObject(dataValueArray.toJSONString(), List.class);
                if (dataList != null && dataList.size() > 0) {
                    for (Map<String, Object> dataMap : dataList) {
                        Map<String, Object> oilStationMap = Maps.newHashMap();
                        String oilStationCode = dataMap.get("id") != null ? dataMap.get("id").toString() : "";
                        oilStationMap.put("oilStationCode", oilStationCode);
                        String oilStationName = dataMap.get("name") != null ? dataMap.get("name").toString() : "";
                        oilStationMap.put("oilStationName", oilStationName);
                        String oilStationAreaSpell = dataMap.get("area") != null ? dataMap.get("area").toString() : "";
                        oilStationMap.put("oilStationAreaSpell", oilStationAreaSpell);
                        String oilStationAreaName = dataMap.get("areaname") != null ? dataMap.get("areaname").toString() : "";
                        oilStationMap.put("oilStationAreaName", oilStationAreaName);
                        String oilStationAdress = dataMap.get("address") != null ? dataMap.get("address").toString() : "";
                        oilStationMap.put("oilStationAdress", oilStationAdress);
                        String oilStationBrandName = dataMap.get("brandname") != null ? dataMap.get("brandname").toString() : "";
                        oilStationMap.put("oilStationBrandName", oilStationBrandName);
                        String oilStationType = dataMap.get("type") != null ? dataMap.get("type").toString() : "";
                        oilStationMap.put("oilStationType", oilStationType);
                        String oilStationDiscount = dataMap.get("discount") != null ? dataMap.get("discount").toString() : "";
                        oilStationMap.put("oilStationDiscount", oilStationDiscount);
                        String oilStationExhaust = dataMap.get("exhaust") != null ? dataMap.get("exhaust").toString() : "";
                        oilStationMap.put("oilStationExhaust", oilStationExhaust);
                        String oilStationPosition = dataMap.get("position") != null ? dataMap.get("position").toString() : "";
                        oilStationMap.put("oilStationPosition", oilStationPosition);
                        //获取到坐标默认是百度地图坐标需要转换为腾讯地图坐标
                        Double oilStationLon = Double.parseDouble(dataMap.get("lon") != null ? dataMap.get("lon").toString() : "0");
                        Double oilStationLat = Double.parseDouble(dataMap.get("lat") != null ? dataMap.get("lat").toString() : "0");
                        LonLatUtil.map_bd2tx(oilStationLon, oilStationLat);
                        oilStationMap.put("oilStationLon", oilStationLon);
                        oilStationMap.put("oilStationLat", oilStationLat);
                        String oilStationPayType = dataMap.get("fwlsmc") != null ? dataMap.get("fwlsmc").toString() : "";
                        oilStationMap.put("oilStationPayType", oilStationPayType);
                        String oilStationDistance = dataMap.get("distance") != null ? dataMap.get("distance").toString() : "0";
                        oilStationMap.put("oilStationDistance", oilStationDistance);
                        List<Map<String, Object>> allPriceList = new ArrayList<>();
                        Map<String, Object> priceMap = dataMap.get("price") != null ? (Map<String, Object>) dataMap.get("price") : new HashMap<String, Object>();
                        Map<String, Object> gastPriceMap = dataMap.get("gastprice") != null ? (Map<String, Object>) dataMap.get("gastprice") : new HashMap<String, Object>();
                        List<Map<String, Object>> priceList = parseOilStationPrice(JSONObject.toJSONString(priceMap));
                        List<Map<String, Object>> gastPriceList = parseOilStationPrice(JSONObject.toJSONString(gastPriceMap));
                        if (gastPriceList != null && gastPriceList.size() > 0) {
                            allPriceList.addAll(gastPriceList);
                        }
                        if (priceList != null && priceList.size() > 0) {
                            allPriceList.addAll(priceList);
                        }
                        oilStationMap.put("oilStationPrice", JSONObject.toJSONString(allPriceList));

                        Object createTime = TimestampUtil.getTimestamp();
                        oilStationMap.put("createTime", createTime);
                        Object updateTime = TimestampUtil.getTimestamp();
                        oilStationMap.put("updateTime", updateTime);
                        oilStationMap.put("isManualModify", 0);
                        oilStationList.add(oilStationMap);

                        //检测是否已经存在,并且允许自动更新
                        Map<String, Object> paramMapTemp = Maps.newHashMap();
                        paramMapTemp.put("oilStationCode", oilStationCode);
                        List<Map<String, Object>> exist_oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMapTemp);
                        if(exist_oilStationList != null && exist_oilStationList.size() > 0){
                            Object isManualModifyObj = exist_oilStationList.get(0).get("isManualModify");
                            if(isManualModifyObj != null && "0".equals(isManualModifyObj.toString())){
                                //将从网络获取到的加油站数据进行更新
                                oilStationService.updateOilStation(oilStationMap);
                            } else {
                                logger.info("加油站名称：【" + oilStationName + "】,已存在，但是已被某个用户手动更改过，当前加油站数据已变为【手动更新】数据模式.");
                            }
                        } else {
                            paramMapTemp.clear();

                            //将从网络获取到的加油站数据进行入库
                            oilStationService.addOilStation(oilStationMap);
                        }
                    }
                    oilStationStrList.addAll(MapUtil.getStringMapList(oilStationList));
                    return oilStationStrList;
                }
            }
            if (oilStationStrList.size() <= 0) {
                System.out.println("第 " + r + " 次递归查询【在线获取加油站】...");
                if (r >= 10000) {
                    return oilStationStrList;
                }
                r = r + 1000;
                oilStationStrList.addAll(getOilStationListByLonLat(lon, lat, page, r));
            }
        } catch (Exception e) {
            logger.error("在LonLatUtil中在线根据经纬度获取加油站列表-getOilStationListByLonLat is error 解析结果失败, e : " + e);
        }
        return oilStationStrList;
    }

    /**
     * 根据经纬度获取当前所处详细地址
     *
     * @param lon
     * @param lat
     * @return
     */
    public static String getAddressByLonLat(Double lon, Double lat, String addressStr) {
        String resultStr = "";
        String host = OilStationMapCode.TENCENT_HOST;
        String path = OilStationMapCode.TENCENT_PATH_GET_ADDR;
        String method = OilStationMapCode.TENCENT_METHOD;
        Map<String, String> headers = Maps.newHashMap();
        Map<String, String> querys = Maps.newHashMap();
        querys.put("location", lat.toString() + "," + lon.toString());
        querys.put("get_poi", "1");
        querys.put("key", OilStationMapCode.TENCENT_KEY);
        try {
            HttpResponse response = ALiYunHttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            StatusLine responseState = response.getStatusLine();
            logger.info("在service中获取加油站列表-getAddressByLonLat, 从腾讯地图获取地址的状态 : " + responseState);
            logger.info("在service中获取加油站列表-getAddressByLonLat, 从腾讯地图获取地址的Headers : " + response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            String resJsonStr = "";
            if (entity != null) {
                resJsonStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("在service中获取加油站列表-getAddressByLonLat, 从腾讯地图获取地址的结果 : " + resJsonStr);
            JSONObject resJSONObject = JSONObject.parseObject(resJsonStr);
            String statusStr = resJSONObject.getString("status");
            if ("0".equals(statusStr)) {
                JSONObject resultJSONObject = resJSONObject.getJSONObject("result");
                JSONObject addressJSONObject = resultJSONObject.getJSONObject("address_component");
                Object nation = addressJSONObject.getString("nation");
                Object province = addressJSONObject.getString("province");
                Object city = addressJSONObject.getString("city");
                Object district = addressJSONObject.getString("district");
                Object street = addressJSONObject.getString("street");
                Object street_number = addressJSONObject.getString("street_number");
                if ("city".equals(addressStr)) {
                    resultStr = city != null ? city.toString() : "";
                }
            }
        } catch (Exception e) {
            logger.error("在LonLatUtil中在线根据经纬度获取加油站列表-getAddressByLonLat is error 解析结果失败, e : " + e);
        }
        return resultStr;
    }

    /**
     * 将加油站的价格进行解析
     *
     * @param oilStationPrice
     * @return
     */
    public static List<Map<String, Object>> parseOilStationPrice(String oilStationPrice) {
        List<Map<String, Object>> oilStationPriceList = Lists.newArrayList();
        if (!"".equals(oilStationPrice)) {
            Map<String, Object> oilStationPriceMap_temp = JSONObject.parseObject(oilStationPrice, Map.class);
            for (Map.Entry<String, Object> entry : oilStationPriceMap_temp.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String[] priceName = key.split("#");
                if (!"".equals(priceName[0])) {
                    String firstNumpriceName = priceName[0].substring(0, 1);
                    Map<String, Object> oilStationPriceMap = Maps.newHashMap();
                    if ("0".equals(firstNumpriceName.toUpperCase())) {                    //普通柴油
                        oilStationPriceMap.put("oilModelLabel", priceName[0]);
                        oilStationPriceMap.put("oilNameLabel", "柴油");
                        oilStationPriceMap.put("oilPriceLabel", value);
                    } else {
                        if ("E".equals(firstNumpriceName.toUpperCase())) {                //乙醇
                            String secondNumpriceName = priceName[0].substring(1, 2);
                            if ("0".equals(secondNumpriceName.toUpperCase())) {           //乙醇柴油
                                oilStationPriceMap.put("oilModelLabel", priceName[0]);
                                oilStationPriceMap.put("oilNameLabel", "乙醇柴油");
                                oilStationPriceMap.put("oilPriceLabel", value);
                            } else {                                                    //乙醇汽油
                                oilStationPriceMap.put("oilModelLabel", priceName[0]);
                                oilStationPriceMap.put("oilNameLabel", "乙醇汽油");
                                oilStationPriceMap.put("oilPriceLabel", value);
                            }
                        } else {                                                        //普通汽油
                            oilStationPriceMap.put("oilModelLabel", priceName[0]);
                            oilStationPriceMap.put("oilNameLabel", "汽油");
                            oilStationPriceMap.put("oilPriceLabel", value);
                        }
                    }
                    if (oilStationPriceMap.size() > 0) {
                        oilStationPriceList.add(oilStationPriceMap);
                    }
                }
            }
        }
        //对oilStationPriceList进行排序
        Collections.sort(oilStationPriceList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer oilModleNum_1 = 0;
                String oilModelLabel_1 = o1.get("oilModelLabel").toString();
                String firstNumpriceName_1 = oilModelLabel_1.substring(0, 1).toUpperCase();
                if (!"E".equals(firstNumpriceName_1)) {
                    oilModleNum_1 = Integer.parseInt(oilModelLabel_1);
                } else {
                    oilModleNum_1 = Integer.parseInt(oilModelLabel_1.substring(1, oilModelLabel_1.length()));
                }

                Integer oilModleNum_2 = 0;
                String oilModelLabel_2 = o2.get("oilModelLabel").toString();
                String firstNumpriceName_2 = oilModelLabel_2.substring(0, 1).toUpperCase();
                if (!"E".equals(firstNumpriceName_2)) {
                    oilModleNum_2 = Integer.parseInt(oilModelLabel_2);
                } else {
                    oilModleNum_2 = Integer.parseInt(oilModelLabel_2.substring(1, oilModelLabel_2.length()));
                }
                return oilModleNum_1.compareTo(oilModleNum_2);
            }
        });
        return oilStationPriceList;
    }


    /**
     * 计算两个经纬度之间的距离
     *
     * @param startLat
     * @param startLon
     * @param endLat
     * @param endLon
     * @return
     */
    public static double getDistance(double startLat, double startLon, double endLat, double endLon) {
        double radLat1 = rad(startLat);
        double radLat2 = rad(endLat);
        double a = radLat1 - radLat2;
        double b = rad(startLon) - rad(endLon);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     * 坐标转换，腾讯地图转换成百度地图坐标
     *
     * @param lat 腾讯纬度
     * @param lon 腾讯经度
     * @return 返回结果：经度,纬度
     */
    public static void map_tx2bd(double lon, double lat) {
        double bd_lat;
        double bd_lon;
        double x_pi = 3.14159265358979324;
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        bd_lon = z * Math.cos(theta) + 0.0065;
        bd_lat = z * Math.sin(theta) + 0.006;
        lon = bd_lon;
        lat = bd_lat;
    }


    /**
     * 坐标转换，百度地图坐标转换成腾讯地图坐标
     *
     * @param lat 百度坐标纬度
     * @param lon 百度坐标经度
     * @return 返回结果：纬度,经度
     */
    public static void map_bd2tx(double lon, double lat) {
        double tx_lat;
        double tx_lon;
        double x_pi = 3.14159265358979324;
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        tx_lon = z * Math.cos(theta);
        tx_lat = z * Math.sin(theta);
        lon = tx_lon;
        lat = tx_lat;
    }


    private String cityJson = "[\n" +
            "  {\n" +
            "    \"city\": \"北京\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"天津\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"石家庄\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"唐山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"秦皇岛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"邯郸\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"邢台\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"保定\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"张家口\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"承德\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"沧州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"廊坊\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"衡水\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"太原\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"大同\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阳泉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"长治\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"晋城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"朔州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"晋中\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"运城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"忻州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"临汾\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"吕梁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"呼和浩特\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"包头\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"乌海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"赤峰\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"通辽\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鄂尔多斯\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"呼伦贝尔\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"巴彦淖尔\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"乌兰察布\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"兴安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"锡林郭勒\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿拉善\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"沈阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"大连\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鞍山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"抚顺\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"本溪\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"丹东\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"锦州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"营口\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阜新\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"辽阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"盘锦\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"铁岭\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"朝阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"葫芦岛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"长春\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"吉林\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"四平\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"辽源\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"通化\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"白山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"松原\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"白城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"延边朝鲜族\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"哈尔滨\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"齐齐哈尔\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鸡西\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鹤岗\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"双鸭山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"大庆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"伊春\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"佳木斯\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"七台河\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"牡丹江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黑河\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"绥化\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"大兴安岭\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"上海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南京\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"无锡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"徐州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"常州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"苏州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南通\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"连云港\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"淮安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"盐城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"扬州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"镇江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"泰州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宿迁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"杭州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宁波\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"温州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"嘉兴\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"湖州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"绍兴\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"金华\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"衢州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"舟山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"台州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"丽水\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"合肥\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"芜湖\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"蚌埠\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"淮南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"马鞍山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"淮北\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"铜陵\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"安庆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黄山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"滁州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阜阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宿州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"六安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"亳州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"池州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宣城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"福州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"厦门\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"莆田\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"三明\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"泉州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"漳州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南平\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"龙岩\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宁德\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"景德镇\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"萍乡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"九江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新余\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鹰潭\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"赣州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"吉安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宜春\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"抚州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"上饶\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"济南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"青岛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"淄博\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"枣庄\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"东营\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"烟台\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"潍坊\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"济宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"泰安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"威海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"日照\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"莱芜\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"临沂\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"德州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"聊城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"滨州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"菏泽\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"郑州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"开封\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"洛阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"平顶山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"安阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鹤壁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新乡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"焦作\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"济源\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"濮阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"许昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"漯河\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"三门峡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"商丘\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"信阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"周口\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"驻马店\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"武汉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黄石\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"十堰\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宜昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"襄阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"鄂州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"荆门\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"孝感\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"荆州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黄冈\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"咸宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"随州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"恩施\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"仙桃\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"潜江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"天门\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"神农架\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"长沙\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"株洲\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"湘潭\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"衡阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"邵阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"岳阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"常德\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"张家界\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"益阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"郴州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"永州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"怀化\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"娄底\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"湘西\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"广州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"韶关\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"深圳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"珠海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"汕头\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"佛山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"江门\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"湛江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"茂名\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"肇庆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"惠州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"梅州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"汕尾\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"河源\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阳江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"清远\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"东莞\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"中山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"东沙\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"潮州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"揭阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"云浮\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"柳州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"桂林\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"梧州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"北海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"防城港\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"钦州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"贵港\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"玉林\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"百色\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"贺州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"河池\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"来宾\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"崇左\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"海口\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"三亚\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"三沙\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"五指山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"琼海\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"儋州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"文昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"万宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"东方\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"定安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"屯昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"澄迈\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"临高\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"白沙\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"昌江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"乐东\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"陵水\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"保亭\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"琼中\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"重庆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"成都\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"自贡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"攀枝花\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"泸州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"德阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"绵阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"广元\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"遂宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"内江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"乐山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南充\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"眉山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宜宾\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"广安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"达州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"雅安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"巴中\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"资阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿坝\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"甘孜\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"凉山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"贵阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"六盘水\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"遵义\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"安顺\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"铜仁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黔西南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"毕节\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黔东南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黔南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"昆明\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"曲靖\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"玉溪\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"保山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"昭通\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"丽江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"普洱\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"临沧\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"楚雄\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"红河\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"文山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"西双版纳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"大理\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"德宏\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"怒江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"迪庆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"拉萨\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"昌都\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"山南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"日喀则\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"那曲\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿里\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"林芝\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"西安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"铜川\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宝鸡\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"咸阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"渭南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"延安\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"汉中\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"榆林\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"安康\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"商洛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"兰州\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"嘉峪关\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"金昌\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"白银\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"天水\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"武威\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"张掖\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"平凉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"酒泉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"庆阳\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"定西\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"陇南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"临夏\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"甘南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"西宁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"海东\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"海北\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"黄南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"海南藏族\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"果洛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"玉树\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"海西\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"银川\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"石嘴山\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"吴忠\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"固原\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"中卫\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"乌鲁木齐\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"克拉玛依\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"吐鲁番\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"哈密\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"昌吉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"博尔塔拉\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"巴音郭楞\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿克苏\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"克孜勒苏柯尔克孜\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"喀什\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"和田\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"伊犁\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"塔城\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿勒泰\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"石河子\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"阿拉尔\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"图木舒克\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"五家渠\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"台北\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"高雄\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"台南\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"台中\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"金门\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"南投\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"基隆\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新竹\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"嘉义\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新北\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"宜兰\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新竹\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"桃园\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"苗栗\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"彰化\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"嘉义\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"云林\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"屏东\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"台东\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"花莲\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"澎湖\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"连江\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"香港岛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"九龙\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"新界\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"澳门半岛\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"离岛\"\n" +
            "  }\n" +
            "]";
}
