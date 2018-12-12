package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.center.service.OilStationService;
import com.br.newMall.dao.OilStationDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.util.*;

/**
 * 经纬度的工具类
 */
public class LonLatUtil {

    public static double EARTH_RADIUS = 6371.393;

    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static final Logger logger = LoggerFactory.getLogger(LonLatUtil.class);

    public static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:center-context.xml");
    public static OilStationService oilStationService = context.getBean("oilStationServiceImpl", OilStationService.class);
    public static OilStationDao oilStationDao = context.getBean("oilStationDao", OilStationDao.class);

    public static Integer getDetailAddressNum = 300000;
    public static Integer callDetailAddressNum = 0;

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
            String host = NewMallCode.A_LI_YUN_HOST;
            String path = NewMallCode.A_LI_YUN_PATH_BY_CITY;
            String method = NewMallCode.A_LI_YUN_METHOD;
            String appcode = NewMallCode.A_LI_YUN_APP_CODE;
            Map<String, String> headers = Maps.newHashMap();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + NewMallCode.A_LI_YUN_APP_CODE);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", NewMallCode.A_LI_YUN_CONTENT_TYPE);
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
                        Map<String, Object> newMall = Maps.newHashMap();
                        String oilStationCode = dataMap.get("id") != null ? dataMap.get("id").toString() : "";
                        newMall.put("oilStationCode", oilStationCode);
                        String oilStationName = dataMap.get("name") != null ? dataMap.get("name").toString() : "";
                        newMall.put("oilStationName", oilStationName);
                        String oilStationAreaSpell = dataMap.get("area") != null ? dataMap.get("area").toString() : "";
                        newMall.put("oilStationAreaSpell", oilStationAreaSpell);
                        String oilStationAreaName = dataMap.get("areaname") != null ? dataMap.get("areaname").toString() : "";
                        newMall.put("oilStationAreaName", oilStationAreaName);
                        String oilStationAdress = dataMap.get("address") != null ? dataMap.get("address").toString() : "";
                        newMall.put("oilStationAdress", oilStationAdress);
                        String oilStationBrandName = dataMap.get("brandname") != null ? dataMap.get("brandname").toString() : "";
                        newMall.put("oilStationBrandName", oilStationBrandName);
                        String oilStationType = dataMap.get("type") != null ? dataMap.get("type").toString() : "";
                        newMall.put("oilStationType", oilStationType);
                        String oilStationDiscount = dataMap.get("discount") != null ? dataMap.get("discount").toString() : "";
                        newMall.put("oilStationDiscount", oilStationDiscount);
                        String oilStationExhaust = dataMap.get("exhaust") != null ? dataMap.get("exhaust").toString() : "";
                        newMall.put("oilStationExhaust", oilStationExhaust);
                        String oilStationPosition = dataMap.get("position") != null ? dataMap.get("position").toString() : "";
                        newMall.put("oilStationPosition", oilStationPosition);
                        //获取到坐标默认是百度地图坐标需要转换为腾讯地图坐标
                        Double oilStationLon = Double.parseDouble(dataMap.get("lon") != null ? dataMap.get("lon").toString() : "0");
                        Double oilStationLat = Double.parseDouble(dataMap.get("lat") != null ? dataMap.get("lat").toString() : "0");
                        LonLatUtil.map_bd2tx(oilStationLon, oilStationLat);
                        newMall.put("oilStationLon", oilStationLon);
                        newMall.put("oilStationLat", oilStationLat);
                        String oilStationPayType = dataMap.get("fwlsmc") != null ? dataMap.get("fwlsmc").toString() : "";
                        newMall.put("oilStationPayType", oilStationPayType);
                        String oilStationDistance = dataMap.get("distance") != null ? dataMap.get("distance").toString() : "0";
                        newMall.put("oilStationDistance", oilStationDistance);
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
                        newMall.put("oilStationPrice", JSONObject.toJSONString(allPriceList));

                        Object createTime = TimestampUtil.getTimestamp();
                        newMall.put("createTime", createTime);
                        Object updateTime = TimestampUtil.getTimestamp();
                        newMall.put("updateTime", updateTime);
                        newMall.put("isManualModify", 0);


                        //检测是否已经存在,并且允许自动更新
                        Map<String, Object> paramMapTemp = Maps.newHashMap();
                        paramMapTemp.put("oilStationCode", oilStationCode);
                        List<Map<String, Object>> exist_oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMapTemp);
                        if(exist_oilStationList != null && exist_oilStationList.size() > 0){
                            Object isManualModifyObj = exist_oilStationList.get(0).get("isManualModify");
                            if(isManualModifyObj != null && "0".equals(isManualModifyObj.toString())){
                                //将从网络获取到的加油站数据进行更新
                                oilStationService.updateOilStation(newMall);
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,更新成功，当前加油站数据默认【自动更新】数据模式.");
                                oilStationList.add(newMall);
                            } else {
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,已存在，但是已被某个用户手动更改过，当前加油站数据已变为【手动更新】数据模式.");
                                oilStationList.add(exist_oilStationList.get(0));
                            }
                        } else {
                            //将从网络获取到的加油站数据进行入库
                            oilStationService.addOilStation(newMall);
                            logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByCity,加油站名称：【" + oilStationName + "】,新增成功，当前加油站数据默认【自动更新】数据模式.");
                            oilStationList.add(newMall);
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
        String host = NewMallCode.A_LI_YUN_HOST;
        String path = NewMallCode.A_LI_YUN_PATH_BY_LONLAT;
        String method = NewMallCode.A_LI_YUN_METHOD;
        String appcode = NewMallCode.A_LI_YUN_APP_CODE;
        Map<String, String> headers = Maps.newHashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + NewMallCode.A_LI_YUN_APP_CODE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", NewMallCode.A_LI_YUN_CONTENT_TYPE);
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
                        Map<String, Object> newMall = Maps.newHashMap();
                        String oilStationCode = dataMap.get("id") != null ? dataMap.get("id").toString() : "";
                        newMall.put("oilStationCode", oilStationCode);
                        String oilStationName = dataMap.get("name") != null ? dataMap.get("name").toString() : "";
                        newMall.put("oilStationName", oilStationName);
                        String oilStationAreaSpell = dataMap.get("area") != null ? dataMap.get("area").toString() : "";
                        newMall.put("oilStationAreaSpell", oilStationAreaSpell);
                        String oilStationAreaName = dataMap.get("areaname") != null ? dataMap.get("areaname").toString() : "";
                        newMall.put("oilStationAreaName", oilStationAreaName);
                        String oilStationAdress = dataMap.get("address") != null ? dataMap.get("address").toString() : "";
                        newMall.put("oilStationAdress", oilStationAdress);
                        String oilStationBrandName = dataMap.get("brandname") != null ? dataMap.get("brandname").toString() : "";
                        newMall.put("oilStationBrandName", oilStationBrandName);
                        String oilStationType = dataMap.get("type") != null ? dataMap.get("type").toString() : "";
                        newMall.put("oilStationType", oilStationType);
                        String oilStationDiscount = dataMap.get("discount") != null ? dataMap.get("discount").toString() : "";
                        newMall.put("oilStationDiscount", oilStationDiscount);
                        String oilStationExhaust = dataMap.get("exhaust") != null ? dataMap.get("exhaust").toString() : "";
                        newMall.put("oilStationExhaust", oilStationExhaust);
                        String oilStationPosition = dataMap.get("position") != null ? dataMap.get("position").toString() : "";
                        newMall.put("oilStationPosition", oilStationPosition);
                        //获取到坐标默认是百度地图坐标需要转换为腾讯地图坐标
                        Double oilStationLon = Double.parseDouble(dataMap.get("lon") != null ? dataMap.get("lon").toString() : "0");
                        Double oilStationLat = Double.parseDouble(dataMap.get("lat") != null ? dataMap.get("lat").toString() : "0");
                        LonLatUtil.map_bd2tx(oilStationLon, oilStationLat);
                        newMall.put("oilStationLon", oilStationLon);
                        newMall.put("oilStationLat", oilStationLat);
                        String oilStationPayType = dataMap.get("fwlsmc") != null ? dataMap.get("fwlsmc").toString() : "";
                        newMall.put("oilStationPayType", oilStationPayType);
                        String oilStationDistance = dataMap.get("distance") != null ? dataMap.get("distance").toString() : "0";
                        newMall.put("oilStationDistance", oilStationDistance);
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
                        newMall.put("oilStationPrice", JSONObject.toJSONString(allPriceList));

                        Object createTime = TimestampUtil.getTimestamp();
                        newMall.put("createTime", createTime);
                        Object updateTime = TimestampUtil.getTimestamp();
                        newMall.put("updateTime", updateTime);
                        newMall.put("isManualModify", 0);


                        //检测是否已经存在,并且允许自动更新
                        Map<String, Object> paramMapTemp = Maps.newHashMap();
                        paramMapTemp.put("oilStationCode", oilStationCode);
                        List<Map<String, Object>> exist_oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMapTemp);
                        if(exist_oilStationList != null && exist_oilStationList.size() > 0){
                            Object isManualModifyObj = exist_oilStationList.get(0).get("isManualModify");
                            if(isManualModifyObj != null && "0".equals(isManualModifyObj.toString())){
                                //将从网络获取到的加油站数据进行更新
                                oilStationService.updateOilStation(newMall);
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByLonLat,加油站名称：【" + oilStationName + "】,更新成功，当前加油站数据默认【自动更新】数据模式.");
                                oilStationList.add(newMall);
                            } else {
                                logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByLonLat,加油站名称：【" + oilStationName + "】,已存在，但是已被某个用户手动更改过，当前加油站数据已变为【手动更新】数据模式.");
                                oilStationList.add(exist_oilStationList.get(0));
                            }
                        } else {
                            //将从网络获取到的加油站数据进行入库
                            oilStationService.addOilStation(newMall);
                            logger.info("在LonLatUtil中在线根据城市名称获取加油站列表-getOilStationListByLonLat,加油站名称：【" + oilStationName + "】,新增成功，当前加油站数据默认【自动更新】数据模式.");
                            oilStationList.add(newMall);
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
     * 逆地址解析(坐标位置描述)
     * @param lon
     * @param lat
     * @return
     */
    public static String getAddressByLonLat(Double lon, Double lat, String addressStr) {
        String resultStr = "";
        String host = NewMallCode.TENCENT_HOST;
        String path = NewMallCode.TENCENT_PATH_GET_ADDR;
        String method = NewMallCode.TENCENT_METHOD;
        Map<String, String> headers = Maps.newHashMap();
        Map<String, String> querys = Maps.newHashMap();
        querys.put("location", lat.toString() + "," + lon.toString());
        querys.put("get_poi", "1");
        querys.put("key", NewMallCode.TENCENT_KEY);
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


    /**
     * 根据【关键词名称】获取【详细地址】
     * 地址解析(地址转坐标)
     * @param keyWord
     * @return
     */
    public static Map<String, Object> getDetailAddressByKeyWord(String keyWord) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String host = NewMallCode.TENCENT_HOST;
        String path = NewMallCode.TENCENT_PATH_GET_ADDR;
        String method = NewMallCode.TENCENT_METHOD;
        Map<String, String> headers = Maps.newHashMap();
        Map<String, String> querys = Maps.newHashMap();
        querys.put("address", keyWord);
        querys.put("key", NewMallCode.TENCENT_KEY);
        try {
            HttpResponse response = ALiYunHttpUtils.doGet(host, path, method, headers, querys);
            StatusLine responseState = response.getStatusLine();
            logger.info("在LonLatUtil中根据关键词获取详细地址-getDetailAddressByKeyWord, 从腾讯地图获取地址的状态 : " + responseState);
            logger.info("在LonLatUtil中根据关键词获取详细地址-getDetailAddressByKeyWord, 从腾讯地图获取地址的Headers : " + response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            String resJsonStr = "";
            if (entity != null) {
                resJsonStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("在LonLatUtil中根据关键词获取详细地址-getDetailAddressByKeyWord, 从腾讯地图获取地址的结果 : " + resJsonStr);
            JSONObject resJSONObject = JSONObject.parseObject(resJsonStr);
            String statusStr = resJSONObject.getString("status");
            if ("0".equals(statusStr)) {
                JSONObject resultJSONObject = resJSONObject.getJSONObject("result");
                //名称
                Object title = resultJSONObject.getString("title");
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
                Object city = null;
                Object district = null;
                Object street = null;
                Object street_number = null;
                JSONObject addressComponentsJSONObject = resultJSONObject.getJSONObject("address_components");
                if(addressComponentsJSONObject != null && addressComponentsJSONObject.size() > 0){
                    province = addressComponentsJSONObject.getString("province");
                    city = addressComponentsJSONObject.getString("city");
                    district = addressComponentsJSONObject.getString("district");
                    street = addressComponentsJSONObject.getString("street");
                    street_number = addressComponentsJSONObject.getString("street_number");
                }
                //相似度
                Object similarity = resultJSONObject.getString("similarity");
                //偏离
                Object deviation = resultJSONObject.getString("deviation");
                //可靠性
                Object reliability = resultJSONObject.getString("reliability");
                //水平
                Object level = resultJSONObject.getString("level");
                //整合响应结果
                resultMap.put("title", title);
                resultMap.put("lng", lng);
                resultMap.put("lat", lat);
                resultMap.put("province", province);
                resultMap.put("city", city);
                resultMap.put("district", district);
                resultMap.put("street", street);
                resultMap.put("street_number", street_number);
                resultMap.put("similarity", similarity);
                resultMap.put("deviation", deviation);
                resultMap.put("reliability", reliability);
                resultMap.put("level", level);
            }
        } catch (Exception e) {
            logger.error("在LonLatUtil中根据关键词获取详细地址-getDetailAddressByKeyWord is error 解析结果失败, e : " + e);
        }
        logger.info("在LonLatUtil中根据关键词获取详细地址-getDetailAddressByKeyWord,结果-resultMap:" + JSONObject.toJSONString(resultMap));
        return resultMap;
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
        String host = NewMallCode.TENCENT_HOST;
        String path = NewMallCode.TENCENT_PATH_GET_SEARCH;
        String method = NewMallCode.TENCENT_METHOD;
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
        querys.put("key", NewMallCode.TENCENT_KEY);
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
                    if(countNum > 0 && ((countNum - pageSize*pageIndex) > 0)){
                        pageIndex++;
                        if(callDetailAddressNum <= getDetailAddressNum){    //此key每秒50次.每天总量30W. 规则：每次请求沉睡5秒
                            Thread.sleep(5000);
                            callDetailAddressNum++;
                        } else {                                            //此key已达每天请求上线.  沉睡到第二天
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DATE, 1);
                            calendar.set(Calendar.HOUR, -12);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            long tomorrowTime = calendar.getTimeInMillis();
                            long currentTime = System.currentTimeMillis();
                            long leftTime = tomorrowTime - currentTime;
                            Thread.sleep(leftTime);
                            callDetailAddressNum = 0;
                        }
                        logger.info("根据【城市】和【关键词名称】获取【所属城市的关键词详细地址】, 第 >>> " + callDetailAddressNum + " <<< 次");
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
        return resultMapList;
    }



//    public static String provinceAndCityJson = "{\n" +
//            "    \"info\":{\n" +
//            "        \"type\":8,\n" +
//            "        \"error\":0,\n" +
//            "        \"request_id\":\"1830811811466b10c1809c21172b4fae2fd055aa072b\",\n" +
//            "        \"time\":0,\n" +
//            "        \"query\":\"加油站\",\n" +
//            "        \"what_query\":\"加油站\",\n" +
//            "        \"hint\":0,\n" +
//            "        \"qcnum\":0,\n" +
//            "        \"qctype\":-1,\n" +
//            "        \"left_query\":\"\"\n" +
//            "    },\n" +
//            "    \"detail\":{\n" +
//            "        \"result\":[\n" +
//            "            {\n" +
//            "                \"acode\":310000,\n" +
//            "                \"ccode\":803,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"上海市\",\n" +
//            "                \"cnum\":117\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":320500,\n" +
//            "                \"ccode\":868,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"苏州市\",\n" +
//            "                \"cnum\":94\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":441900,\n" +
//            "                \"ccode\":2090,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"东莞市\",\n" +
//            "                \"cnum\":86\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":510100,\n" +
//            "                \"ccode\":2301,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"成都市\",\n" +
//            "                \"cnum\":82\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":110000,\n" +
//            "                \"ccode\":2,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"北京市\",\n" +
//            "                \"cnum\":79\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":500000,\n" +
//            "                \"ccode\":2258,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"重庆市\",\n" +
//            "                \"cnum\":70\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":120000,\n" +
//            "                \"ccode\":22,\n" +
//            "                \"cities\":[\n" +
//            "\n" +
//            "                ],\n" +
//            "                \"cname\":\"天津市\",\n" +
//            "                \"cnum\":44\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":440000,\n" +
//            "                \"ccode\":1965,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":440100,\n" +
//            "                        \"ccode\":1966,\n" +
//            "                        \"cname\":\"广州市\",\n" +
//            "                        \"cnum\":63\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440600,\n" +
//            "                        \"ccode\":2009,\n" +
//            "                        \"cname\":\"佛山市\",\n" +
//            "                        \"cnum\":47\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440300,\n" +
//            "                        \"ccode\":1990,\n" +
//            "                        \"cname\":\"深圳市\",\n" +
//            "                        \"cnum\":45\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440900,\n" +
//            "                        \"ccode\":2033,\n" +
//            "                        \"cname\":\"茂名市\",\n" +
//            "                        \"cnum\":36\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441300,\n" +
//            "                        \"ccode\":2049,\n" +
//            "                        \"cname\":\"惠州市\",\n" +
//            "                        \"cnum\":35\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440700,\n" +
//            "                        \"ccode\":2015,\n" +
//            "                        \"cname\":\"江门市\",\n" +
//            "                        \"cnum\":34\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":442000,\n" +
//            "                        \"ccode\":2091,\n" +
//            "                        \"cname\":\"中山市\",\n" +
//            "                        \"cnum\":34\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440800,\n" +
//            "                        \"ccode\":2023,\n" +
//            "                        \"cname\":\"湛江市\",\n" +
//            "                        \"cnum\":27\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":445300,\n" +
//            "                        \"ccode\":2102,\n" +
//            "                        \"cname\":\"云浮市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":445200,\n" +
//            "                        \"ccode\":2096,\n" +
//            "                        \"cname\":\"揭阳市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441800,\n" +
//            "                        \"ccode\":2081,\n" +
//            "                        \"cname\":\"清远市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441600,\n" +
//            "                        \"ccode\":2069,\n" +
//            "                        \"cname\":\"河源市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441200,\n" +
//            "                        \"ccode\":2040,\n" +
//            "                        \"cname\":\"肇庆市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440500,\n" +
//            "                        \"ccode\":2001,\n" +
//            "                        \"cname\":\"汕头市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441400,\n" +
//            "                        \"ccode\":2055,\n" +
//            "                        \"cname\":\"梅州市\",\n" +
//            "                        \"cnum\":20\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440200,\n" +
//            "                        \"ccode\":1979,\n" +
//            "                        \"cname\":\"韶关市\",\n" +
//            "                        \"cnum\":15\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":445100,\n" +
//            "                        \"ccode\":2092,\n" +
//            "                        \"cname\":\"潮州市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441500,\n" +
//            "                        \"ccode\":2064,\n" +
//            "                        \"cname\":\"汕尾市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":441700,\n" +
//            "                        \"ccode\":2076,\n" +
//            "                        \"cname\":\"阳江市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":440400,\n" +
//            "                        \"ccode\":1997,\n" +
//            "                        \"cname\":\"珠海市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"广东省\",\n" +
//            "                \"cnum\":533\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":330000,\n" +
//            "                \"ccode\":945,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":330100,\n" +
//            "                        \"ccode\":946,\n" +
//            "                        \"cname\":\"杭州市\",\n" +
//            "                        \"cnum\":75\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330300,\n" +
//            "                        \"ccode\":972,\n" +
//            "                        \"cname\":\"温州市\",\n" +
//            "                        \"cnum\":54\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330200,\n" +
//            "                        \"ccode\":960,\n" +
//            "                        \"cname\":\"宁波市\",\n" +
//            "                        \"cnum\":52\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":331000,\n" +
//            "                        \"ccode\":1027,\n" +
//            "                        \"cname\":\"台州市\",\n" +
//            "                        \"cnum\":49\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330700,\n" +
//            "                        \"ccode\":1005,\n" +
//            "                        \"cname\":\"金华市\",\n" +
//            "                        \"cnum\":34\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330400,\n" +
//            "                        \"ccode\":984,\n" +
//            "                        \"cname\":\"嘉兴市\",\n" +
//            "                        \"cnum\":28\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330500,\n" +
//            "                        \"ccode\":992,\n" +
//            "                        \"cname\":\"湖州市\",\n" +
//            "                        \"cnum\":27\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330600,\n" +
//            "                        \"ccode\":998,\n" +
//            "                        \"cname\":\"绍兴市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330800,\n" +
//            "                        \"ccode\":1015,\n" +
//            "                        \"cname\":\"衢州市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":331100,\n" +
//            "                        \"ccode\":1037,\n" +
//            "                        \"cname\":\"丽水市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":330900,\n" +
//            "                        \"ccode\":1022,\n" +
//            "                        \"cname\":\"舟山市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"浙江省\",\n" +
//            "                \"cnum\":362\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":320000,\n" +
//            "                \"ccode\":824,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":320100,\n" +
//            "                        \"ccode\":825,\n" +
//            "                        \"cname\":\"南京市\",\n" +
//            "                        \"cnum\":38\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320200,\n" +
//            "                        \"ccode\":839,\n" +
//            "                        \"cname\":\"无锡市\",\n" +
//            "                        \"cnum\":35\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320900,\n" +
//            "                        \"ccode\":907,\n" +
//            "                        \"cname\":\"盐城市\",\n" +
//            "                        \"cnum\":32\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320600,\n" +
//            "                        \"ccode\":880,\n" +
//            "                        \"cname\":\"南通市\",\n" +
//            "                        \"cnum\":31\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320400,\n" +
//            "                        \"ccode\":860,\n" +
//            "                        \"cname\":\"常州市\",\n" +
//            "                        \"cnum\":29\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320300,\n" +
//            "                        \"ccode\":848,\n" +
//            "                        \"cname\":\"徐州市\",\n" +
//            "                        \"cnum\":19\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":321300,\n" +
//            "                        \"ccode\":939,\n" +
//            "                        \"cname\":\"宿迁市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":321200,\n" +
//            "                        \"ccode\":932,\n" +
//            "                        \"cname\":\"泰州市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320700,\n" +
//            "                        \"ccode\":890,\n" +
//            "                        \"cname\":\"连云港市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":321000,\n" +
//            "                        \"ccode\":917,\n" +
//            "                        \"cname\":\"扬州市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":320800,\n" +
//            "                        \"ccode\":898,\n" +
//            "                        \"cname\":\"淮安市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":321100,\n" +
//            "                        \"ccode\":925,\n" +
//            "                        \"cname\":\"镇江市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"江苏省\",\n" +
//            "                \"cnum\":244\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":450000,\n" +
//            "                \"ccode\":2108,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":450100,\n" +
//            "                        \"ccode\":2109,\n" +
//            "                        \"cname\":\"南宁市\",\n" +
//            "                        \"cnum\":28\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450900,\n" +
//            "                        \"ccode\":2180,\n" +
//            "                        \"cname\":\"玉林市\",\n" +
//            "                        \"cnum\":24\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":451200,\n" +
//            "                        \"ccode\":2205,\n" +
//            "                        \"cname\":\"河池市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450300,\n" +
//            "                        \"ccode\":2133,\n" +
//            "                        \"cname\":\"桂林市\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450800,\n" +
//            "                        \"ccode\":2174,\n" +
//            "                        \"cname\":\"贵港市\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450400,\n" +
//            "                        \"ccode\":2151,\n" +
//            "                        \"cname\":\"梧州市\",\n" +
//            "                        \"cnum\":16\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450200,\n" +
//            "                        \"ccode\":2122,\n" +
//            "                        \"cname\":\"柳州市\",\n" +
//            "                        \"cnum\":15\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450700,\n" +
//            "                        \"ccode\":2169,\n" +
//            "                        \"cname\":\"钦州市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":451300,\n" +
//            "                        \"ccode\":2217,\n" +
//            "                        \"cname\":\"来宾市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":451100,\n" +
//            "                        \"ccode\":2200,\n" +
//            "                        \"cname\":\"贺州市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":451000,\n" +
//            "                        \"ccode\":2187,\n" +
//            "                        \"cname\":\"百色市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":451400,\n" +
//            "                        \"ccode\":2224,\n" +
//            "                        \"cname\":\"崇左市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450500,\n" +
//            "                        \"ccode\":2159,\n" +
//            "                        \"cname\":\"北海市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":450600,\n" +
//            "                        \"ccode\":2164,\n" +
//            "                        \"cname\":\"防城港市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"广西壮族自治区\",\n" +
//            "                \"cnum\":185\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":370000,\n" +
//            "                \"ccode\":1376,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":370200,\n" +
//            "                        \"ccode\":1388,\n" +
//            "                        \"cname\":\"青岛市\",\n" +
//            "                        \"cnum\":34\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371300,\n" +
//            "                        \"ccode\":1482,\n" +
//            "                        \"cname\":\"临沂市\",\n" +
//            "                        \"cnum\":31\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370700,\n" +
//            "                        \"ccode\":1436,\n" +
//            "                        \"cname\":\"潍坊市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371700,\n" +
//            "                        \"ccode\":1524,\n" +
//            "                        \"cname\":\"菏泽市\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370600,\n" +
//            "                        \"ccode\":1423,\n" +
//            "                        \"cname\":\"烟台市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371500,\n" +
//            "                        \"ccode\":1507,\n" +
//            "                        \"cname\":\"聊城市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371100,\n" +
//            "                        \"ccode\":1474,\n" +
//            "                        \"cname\":\"日照市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370100,\n" +
//            "                        \"ccode\":1377,\n" +
//            "                        \"cname\":\"济南市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370400,\n" +
//            "                        \"ccode\":1410,\n" +
//            "                        \"cname\":\"枣庄市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371600,\n" +
//            "                        \"ccode\":1516,\n" +
//            "                        \"cname\":\"滨州市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370300,\n" +
//            "                        \"ccode\":1401,\n" +
//            "                        \"cname\":\"淄博市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370800,\n" +
//            "                        \"ccode\":1449,\n" +
//            "                        \"cname\":\"济宁市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370900,\n" +
//            "                        \"ccode\":1462,\n" +
//            "                        \"cname\":\"泰安市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":370500,\n" +
//            "                        \"ccode\":1417,\n" +
//            "                        \"cname\":\"东营市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371000,\n" +
//            "                        \"ccode\":1469,\n" +
//            "                        \"cname\":\"威海市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371200,\n" +
//            "                        \"ccode\":1479,\n" +
//            "                        \"cname\":\"莱芜市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":371400,\n" +
//            "                        \"ccode\":1495,\n" +
//            "                        \"cname\":\"德州市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"山东省\",\n" +
//            "                \"cnum\":183\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":430000,\n" +
//            "                \"ccode\":1828,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":430100,\n" +
//            "                        \"ccode\":1829,\n" +
//            "                        \"cname\":\"长沙市\",\n" +
//            "                        \"cnum\":42\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430500,\n" +
//            "                        \"ccode\":1868,\n" +
//            "                        \"cname\":\"邵阳市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430600,\n" +
//            "                        \"ccode\":1881,\n" +
//            "                        \"cname\":\"岳阳市\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430900,\n" +
//            "                        \"ccode\":1906,\n" +
//            "                        \"cname\":\"益阳市\",\n" +
//            "                        \"cnum\":15\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":431100,\n" +
//            "                        \"ccode\":1925,\n" +
//            "                        \"cname\":\"永州市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430700,\n" +
//            "                        \"ccode\":1891,\n" +
//            "                        \"cname\":\"常德市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430200,\n" +
//            "                        \"ccode\":1839,\n" +
//            "                        \"cname\":\"株洲市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":431300,\n" +
//            "                        \"ccode\":1950,\n" +
//            "                        \"cname\":\"娄底市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":431000,\n" +
//            "                        \"ccode\":1913,\n" +
//            "                        \"cname\":\"郴州市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430300,\n" +
//            "                        \"ccode\":1849,\n" +
//            "                        \"cname\":\"湘潭市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":431200,\n" +
//            "                        \"ccode\":1937,\n" +
//            "                        \"cname\":\"怀化市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430400,\n" +
//            "                        \"ccode\":1855,\n" +
//            "                        \"cname\":\"衡阳市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":433100,\n" +
//            "                        \"ccode\":1956,\n" +
//            "                        \"cname\":\"湘西土家族苗族自治州\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":430800,\n" +
//            "                        \"ccode\":1901,\n" +
//            "                        \"cname\":\"张家界市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"湖南省\",\n" +
//            "                \"cnum\":179\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":410000,\n" +
//            "                \"ccode\":1534,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":410100,\n" +
//            "                        \"ccode\":1535,\n" +
//            "                        \"cname\":\"郑州市\",\n" +
//            "                        \"cnum\":35\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410300,\n" +
//            "                        \"ccode\":1559,\n" +
//            "                        \"cname\":\"洛阳市\",\n" +
//            "                        \"cnum\":20\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411300,\n" +
//            "                        \"ccode\":1653,\n" +
//            "                        \"cname\":\"南阳市\",\n" +
//            "                        \"cnum\":16\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411500,\n" +
//            "                        \"ccode\":1677,\n" +
//            "                        \"cname\":\"信阳市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411600,\n" +
//            "                        \"ccode\":1688,\n" +
//            "                        \"cname\":\"周口市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411400,\n" +
//            "                        \"ccode\":1667,\n" +
//            "                        \"cname\":\"商丘市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410700,\n" +
//            "                        \"ccode\":1602,\n" +
//            "                        \"cname\":\"新乡市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411000,\n" +
//            "                        \"ccode\":1633,\n" +
//            "                        \"cname\":\"许昌市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411700,\n" +
//            "                        \"ccode\":1699,\n" +
//            "                        \"cname\":\"驻马店市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410500,\n" +
//            "                        \"ccode\":1586,\n" +
//            "                        \"cname\":\"安阳市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410800,\n" +
//            "                        \"ccode\":1615,\n" +
//            "                        \"cname\":\"焦作市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410200,\n" +
//            "                        \"ccode\":1548,\n" +
//            "                        \"cname\":\"开封市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410400,\n" +
//            "                        \"ccode\":1575,\n" +
//            "                        \"cname\":\"平顶山市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410900,\n" +
//            "                        \"ccode\":1626,\n" +
//            "                        \"cname\":\"濮阳市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411100,\n" +
//            "                        \"ccode\":1640,\n" +
//            "                        \"cname\":\"漯河市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":411200,\n" +
//            "                        \"ccode\":1646,\n" +
//            "                        \"cname\":\"三门峡市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":410600,\n" +
//            "                        \"ccode\":1596,\n" +
//            "                        \"cname\":\"鹤壁市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"河南省\",\n" +
//            "                \"cnum\":166\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":340000,\n" +
//            "                \"ccode\":1047,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":341200,\n" +
//            "                        \"ccode\":1123,\n" +
//            "                        \"cname\":\"阜阳市\",\n" +
//            "                        \"cnum\":26\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340100,\n" +
//            "                        \"ccode\":1048,\n" +
//            "                        \"cname\":\"合肥市\",\n" +
//            "                        \"cnum\":19\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341100,\n" +
//            "                        \"ccode\":1114,\n" +
//            "                        \"cname\":\"滁州市\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341500,\n" +
//            "                        \"ccode\":1144,\n" +
//            "                        \"cname\":\"六安市\",\n" +
//            "                        \"cnum\":15\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341300,\n" +
//            "                        \"ccode\":1132,\n" +
//            "                        \"cname\":\"宿州市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341600,\n" +
//            "                        \"ccode\":1152,\n" +
//            "                        \"cname\":\"亳州市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340300,\n" +
//            "                        \"ccode\":1064,\n" +
//            "                        \"cname\":\"蚌埠市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340800,\n" +
//            "                        \"ccode\":1094,\n" +
//            "                        \"cname\":\"安庆市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341800,\n" +
//            "                        \"ccode\":1162,\n" +
//            "                        \"cname\":\"宣城市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340500,\n" +
//            "                        \"ccode\":1079,\n" +
//            "                        \"cname\":\"马鞍山市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340200,\n" +
//            "                        \"ccode\":1056,\n" +
//            "                        \"cname\":\"芜湖市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340400,\n" +
//            "                        \"ccode\":1072,\n" +
//            "                        \"cname\":\"淮南市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341700,\n" +
//            "                        \"ccode\":1157,\n" +
//            "                        \"cname\":\"池州市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":340600,\n" +
//            "                        \"ccode\":1084,\n" +
//            "                        \"cname\":\"淮北市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":341000,\n" +
//            "                        \"ccode\":1106,\n" +
//            "                        \"cname\":\"黄山市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"安徽省\",\n" +
//            "                \"cnum\":160\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":360000,\n" +
//            "                \"ccode\":1265,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":360700,\n" +
//            "                        \"ccode\":1307,\n" +
//            "                        \"cname\":\"赣州市\",\n" +
//            "                        \"cnum\":42\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360100,\n" +
//            "                        \"ccode\":1266,\n" +
//            "                        \"cname\":\"南昌市\",\n" +
//            "                        \"cnum\":26\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":361100,\n" +
//            "                        \"ccode\":1363,\n" +
//            "                        \"cname\":\"上饶市\",\n" +
//            "                        \"cnum\":15\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360900,\n" +
//            "                        \"ccode\":1340,\n" +
//            "                        \"cname\":\"宜春市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360800,\n" +
//            "                        \"ccode\":1326,\n" +
//            "                        \"cname\":\"吉安市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":361000,\n" +
//            "                        \"ccode\":1351,\n" +
//            "                        \"cname\":\"抚州市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360400,\n" +
//            "                        \"ccode\":1287,\n" +
//            "                        \"cname\":\"九江市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360200,\n" +
//            "                        \"ccode\":1276,\n" +
//            "                        \"cname\":\"景德镇市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360300,\n" +
//            "                        \"ccode\":1281,\n" +
//            "                        \"cname\":\"萍乡市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360600,\n" +
//            "                        \"ccode\":1303,\n" +
//            "                        \"cname\":\"鹰潭市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":360500,\n" +
//            "                        \"ccode\":1300,\n" +
//            "                        \"cname\":\"新余市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"江西省\",\n" +
//            "                \"cnum\":141\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":130000,\n" +
//            "                \"ccode\":43,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":130100,\n" +
//            "                        \"ccode\":44,\n" +
//            "                        \"cname\":\"石家庄市\",\n" +
//            "                        \"cnum\":28\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130200,\n" +
//            "                        \"ccode\":68,\n" +
//            "                        \"cname\":\"唐山市\",\n" +
//            "                        \"cnum\":21\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130500,\n" +
//            "                        \"ccode\":111,\n" +
//            "                        \"cname\":\"邢台市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130600,\n" +
//            "                        \"ccode\":131,\n" +
//            "                        \"cname\":\"保定市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130900,\n" +
//            "                        \"ccode\":187,\n" +
//            "                        \"cname\":\"沧州市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":131100,\n" +
//            "                        \"ccode\":215,\n" +
//            "                        \"cname\":\"衡水市\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":131000,\n" +
//            "                        \"ccode\":204,\n" +
//            "                        \"cname\":\"廊坊市\",\n" +
//            "                        \"cnum\":12\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130300,\n" +
//            "                        \"ccode\":83,\n" +
//            "                        \"cname\":\"秦皇岛市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130400,\n" +
//            "                        \"ccode\":91,\n" +
//            "                        \"cname\":\"邯郸市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130700,\n" +
//            "                        \"ccode\":157,\n" +
//            "                        \"cname\":\"张家口市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":130800,\n" +
//            "                        \"ccode\":175,\n" +
//            "                        \"cname\":\"承德市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"河北省\",\n" +
//            "                \"cnum\":135\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":530000,\n" +
//            "                \"ccode\":2601,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":530100,\n" +
//            "                        \"ccode\":2602,\n" +
//            "                        \"cname\":\"昆明市\",\n" +
//            "                        \"cnum\":36\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":532500,\n" +
//            "                        \"ccode\":2692,\n" +
//            "                        \"cname\":\"红河哈尼族彝族自治州\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530800,\n" +
//            "                        \"ccode\":2661,\n" +
//            "                        \"cname\":\"普洱市\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":532900,\n" +
//            "                        \"ccode\":2719,\n" +
//            "                        \"cname\":\"大理白族自治州\",\n" +
//            "                        \"cnum\":11\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530600,\n" +
//            "                        \"ccode\":2643,\n" +
//            "                        \"cname\":\"昭通市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530400,\n" +
//            "                        \"ccode\":2627,\n" +
//            "                        \"cname\":\"玉溪市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530900,\n" +
//            "                        \"ccode\":2672,\n" +
//            "                        \"cname\":\"临沧市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":532600,\n" +
//            "                        \"ccode\":2706,\n" +
//            "                        \"cname\":\"文山壮族苗族自治州\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530500,\n" +
//            "                        \"ccode\":2637,\n" +
//            "                        \"cname\":\"保山市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530300,\n" +
//            "                        \"ccode\":2617,\n" +
//            "                        \"cname\":\"曲靖市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":532300,\n" +
//            "                        \"ccode\":2681,\n" +
//            "                        \"cname\":\"楚雄彝族自治州\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":530700,\n" +
//            "                        \"ccode\":2655,\n" +
//            "                        \"cname\":\"丽江市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":533300,\n" +
//            "                        \"ccode\":2738,\n" +
//            "                        \"cname\":\"怒江傈僳族自治州\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":533100,\n" +
//            "                        \"ccode\":2732,\n" +
//            "                        \"cname\":\"德宏傣族景颇族自治州\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":533400,\n" +
//            "                        \"ccode\":2743,\n" +
//            "                        \"cname\":\"迪庆藏族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"云南省\",\n" +
//            "                \"cnum\":124\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":520000,\n" +
//            "                \"ccode\":2503,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":522300,\n" +
//            "                        \"ccode\":2553,\n" +
//            "                        \"cname\":\"黔西南布依族苗族自治州\",\n" +
//            "                        \"cnum\":19\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520100,\n" +
//            "                        \"ccode\":2504,\n" +
//            "                        \"cname\":\"贵阳市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520500,\n" +
//            "                        \"ccode\":3382,\n" +
//            "                        \"cname\":\"毕节市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520300,\n" +
//            "                        \"ccode\":2520,\n" +
//            "                        \"cname\":\"遵义市\",\n" +
//            "                        \"cnum\":18\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":522700,\n" +
//            "                        \"ccode\":2588,\n" +
//            "                        \"cname\":\"黔南布依族苗族自治州\",\n" +
//            "                        \"cnum\":17\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":522600,\n" +
//            "                        \"ccode\":2571,\n" +
//            "                        \"cname\":\"黔东南苗族侗族自治州\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520400,\n" +
//            "                        \"ccode\":2535,\n" +
//            "                        \"cname\":\"安顺市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520200,\n" +
//            "                        \"ccode\":2515,\n" +
//            "                        \"cname\":\"六盘水市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":520600,\n" +
//            "                        \"ccode\":3391,\n" +
//            "                        \"cname\":\"铜仁市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"贵州省\",\n" +
//            "                \"cnum\":118\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":420000,\n" +
//            "                \"ccode\":1711,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":420100,\n" +
//            "                        \"ccode\":1712,\n" +
//            "                        \"cname\":\"武汉市\",\n" +
//            "                        \"cnum\":37\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":422800,\n" +
//            "                        \"ccode\":1815,\n" +
//            "                        \"cname\":\"恩施土家族苗族自治州\",\n" +
//            "                        \"cnum\":13\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420500,\n" +
//            "                        \"ccode\":1742,\n" +
//            "                        \"cname\":\"宜昌市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420600,\n" +
//            "                        \"ccode\":1756,\n" +
//            "                        \"cname\":\"襄阳市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":421100,\n" +
//            "                        \"ccode\":1793,\n" +
//            "                        \"cname\":\"黄冈市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":421000,\n" +
//            "                        \"ccode\":1784,\n" +
//            "                        \"cname\":\"荆州市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420800,\n" +
//            "                        \"ccode\":1770,\n" +
//            "                        \"cname\":\"荆门市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":421300,\n" +
//            "                        \"ccode\":1811,\n" +
//            "                        \"cname\":\"随州市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":429004,\n" +
//            "                        \"ccode\":1824,\n" +
//            "                        \"cname\":\"仙桃市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":421200,\n" +
//            "                        \"ccode\":1804,\n" +
//            "                        \"cname\":\"咸宁市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420200,\n" +
//            "                        \"ccode\":1726,\n" +
//            "                        \"cname\":\"黄石市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420900,\n" +
//            "                        \"ccode\":1776,\n" +
//            "                        \"cname\":\"孝感市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420300,\n" +
//            "                        \"ccode\":1733,\n" +
//            "                        \"cname\":\"十堰市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":420700,\n" +
//            "                        \"ccode\":1766,\n" +
//            "                        \"cname\":\"鄂州市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":429006,\n" +
//            "                        \"ccode\":1826,\n" +
//            "                        \"cname\":\"天门市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":429005,\n" +
//            "                        \"ccode\":1825,\n" +
//            "                        \"cname\":\"潜江市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"湖北省\",\n" +
//            "                \"cnum\":117\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":350000,\n" +
//            "                \"ccode\":1170,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":350500,\n" +
//            "                        \"ccode\":1211,\n" +
//            "                        \"cname\":\"泉州市\",\n" +
//            "                        \"cnum\":29\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350100,\n" +
//            "                        \"ccode\":1171,\n" +
//            "                        \"cname\":\"福州市\",\n" +
//            "                        \"cnum\":21\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350200,\n" +
//            "                        \"ccode\":1185,\n" +
//            "                        \"cname\":\"厦门市\",\n" +
//            "                        \"cnum\":16\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350600,\n" +
//            "                        \"ccode\":1224,\n" +
//            "                        \"cname\":\"漳州市\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350300,\n" +
//            "                        \"ccode\":1192,\n" +
//            "                        \"cname\":\"莆田市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350800,\n" +
//            "                        \"ccode\":1247,\n" +
//            "                        \"cname\":\"龙岩市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350900,\n" +
//            "                        \"ccode\":1255,\n" +
//            "                        \"cname\":\"宁德市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350700,\n" +
//            "                        \"ccode\":1236,\n" +
//            "                        \"cname\":\"南平市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":350400,\n" +
//            "                        \"ccode\":1198,\n" +
//            "                        \"cname\":\"三明市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"福建省\",\n" +
//            "                \"cnum\":93\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":210000,\n" +
//            "                \"ccode\":472,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":210100,\n" +
//            "                        \"ccode\":473,\n" +
//            "                        \"cname\":\"沈阳市\",\n" +
//            "                        \"cnum\":33\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210200,\n" +
//            "                        \"ccode\":487,\n" +
//            "                        \"cname\":\"大连市\",\n" +
//            "                        \"cnum\":14\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210400,\n" +
//            "                        \"ccode\":506,\n" +
//            "                        \"cname\":\"抚顺市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":211200,\n" +
//            "                        \"ccode\":564,\n" +
//            "                        \"cname\":\"铁岭市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":211100,\n" +
//            "                        \"ccode\":559,\n" +
//            "                        \"cname\":\"盘锦市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210300,\n" +
//            "                        \"ccode\":498,\n" +
//            "                        \"cname\":\"鞍山市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210600,\n" +
//            "                        \"ccode\":521,\n" +
//            "                        \"cname\":\"丹东市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210700,\n" +
//            "                        \"ccode\":528,\n" +
//            "                        \"cname\":\"锦州市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":211000,\n" +
//            "                        \"ccode\":551,\n" +
//            "                        \"cname\":\"辽阳市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210900,\n" +
//            "                        \"ccode\":543,\n" +
//            "                        \"cname\":\"阜新市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":211400,\n" +
//            "                        \"ccode\":580,\n" +
//            "                        \"cname\":\"葫芦岛市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210800,\n" +
//            "                        \"ccode\":536,\n" +
//            "                        \"cname\":\"营口市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":211300,\n" +
//            "                        \"ccode\":572,\n" +
//            "                        \"cname\":\"朝阳市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":210500,\n" +
//            "                        \"ccode\":514,\n" +
//            "                        \"cname\":\"本溪市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"辽宁省\",\n" +
//            "                \"cnum\":88\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":510000,\n" +
//            "                \"ccode\":2300,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":513300,\n" +
//            "                        \"ccode\":2466,\n" +
//            "                        \"cname\":\"甘孜藏族自治州\",\n" +
//            "                        \"cnum\":10\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511500,\n" +
//            "                        \"ccode\":2408,\n" +
//            "                        \"cname\":\"宜宾市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":510600,\n" +
//            "                        \"ccode\":2342,\n" +
//            "                        \"cname\":\"德阳市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":510500,\n" +
//            "                        \"ccode\":2334,\n" +
//            "                        \"cname\":\"泸州市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":513400,\n" +
//            "                        \"ccode\":2485,\n" +
//            "                        \"cname\":\"凉山彝族自治州\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":510700,\n" +
//            "                        \"ccode\":2349,\n" +
//            "                        \"cname\":\"绵阳市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511300,\n" +
//            "                        \"ccode\":2391,\n" +
//            "                        \"cname\":\"南充市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":513200,\n" +
//            "                        \"ccode\":2452,\n" +
//            "                        \"cname\":\"阿坝藏族羌族自治州\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511800,\n" +
//            "                        \"ccode\":2433,\n" +
//            "                        \"cname\":\"雅安市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":510300,\n" +
//            "                        \"ccode\":2321,\n" +
//            "                        \"cname\":\"自贡市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511900,\n" +
//            "                        \"ccode\":2442,\n" +
//            "                        \"cname\":\"巴中市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511000,\n" +
//            "                        \"ccode\":2373,\n" +
//            "                        \"cname\":\"内江市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":512000,\n" +
//            "                        \"ccode\":2447,\n" +
//            "                        \"cname\":\"资阳市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511600,\n" +
//            "                        \"ccode\":2419,\n" +
//            "                        \"cname\":\"广安市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511400,\n" +
//            "                        \"ccode\":2401,\n" +
//            "                        \"cname\":\"眉山市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511100,\n" +
//            "                        \"ccode\":2379,\n" +
//            "                        \"cname\":\"乐山市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":511700,\n" +
//            "                        \"ccode\":2425,\n" +
//            "                        \"cname\":\"达州市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"四川省\",\n" +
//            "                \"cnum\":74\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":610000,\n" +
//            "                \"ccode\":2828,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":610100,\n" +
//            "                        \"ccode\":2829,\n" +
//            "                        \"cname\":\"西安市\",\n" +
//            "                        \"cnum\":22\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610800,\n" +
//            "                        \"ccode\":2914,\n" +
//            "                        \"cname\":\"榆林市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610400,\n" +
//            "                        \"ccode\":2861,\n" +
//            "                        \"cname\":\"咸阳市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610500,\n" +
//            "                        \"ccode\":2876,\n" +
//            "                        \"cname\":\"渭南市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610600,\n" +
//            "                        \"ccode\":2888,\n" +
//            "                        \"cname\":\"延安市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610900,\n" +
//            "                        \"ccode\":2927,\n" +
//            "                        \"cname\":\"安康市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610700,\n" +
//            "                        \"ccode\":2902,\n" +
//            "                        \"cname\":\"汉中市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":611000,\n" +
//            "                        \"ccode\":2938,\n" +
//            "                        \"cname\":\"商洛市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610300,\n" +
//            "                        \"ccode\":2848,\n" +
//            "                        \"cname\":\"宝鸡市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":610200,\n" +
//            "                        \"ccode\":2843,\n" +
//            "                        \"cname\":\"铜川市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"陕西省\",\n" +
//            "                \"cnum\":60\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":220000,\n" +
//            "                \"ccode\":587,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":220100,\n" +
//            "                        \"ccode\":588,\n" +
//            "                        \"cname\":\"长春市\",\n" +
//            "                        \"cnum\":30\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":220300,\n" +
//            "                        \"ccode\":609,\n" +
//            "                        \"cname\":\"四平市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":220200,\n" +
//            "                        \"ccode\":599,\n" +
//            "                        \"cname\":\"吉林市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":220800,\n" +
//            "                        \"ccode\":642,\n" +
//            "                        \"cname\":\"白城市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":222400,\n" +
//            "                        \"ccode\":648,\n" +
//            "                        \"cname\":\"延边朝鲜族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":220700,\n" +
//            "                        \"ccode\":636,\n" +
//            "                        \"cname\":\"松原市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"吉林省\",\n" +
//            "                \"cnum\":43\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":230000,\n" +
//            "                \"ccode\":657,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":230100,\n" +
//            "                        \"ccode\":658,\n" +
//            "                        \"cname\":\"哈尔滨市\",\n" +
//            "                        \"cnum\":21\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230600,\n" +
//            "                        \"ccode\":722,\n" +
//            "                        \"cname\":\"大庆市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230200,\n" +
//            "                        \"ccode\":677,\n" +
//            "                        \"cname\":\"齐齐哈尔市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230400,\n" +
//            "                        \"ccode\":704,\n" +
//            "                        \"cname\":\"鹤岗市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230700,\n" +
//            "                        \"ccode\":732,\n" +
//            "                        \"cname\":\"伊春市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230300,\n" +
//            "                        \"ccode\":694,\n" +
//            "                        \"cname\":\"鸡西市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":230800,\n" +
//            "                        \"ccode\":750,\n" +
//            "                        \"cname\":\"佳木斯市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"黑龙江省\",\n" +
//            "                \"cnum\":39\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":620000,\n" +
//            "                \"ccode\":2946,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":621000,\n" +
//            "                        \"ccode\":3002,\n" +
//            "                        \"cname\":\"庆阳市\",\n" +
//            "                        \"cnum\":9\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620500,\n" +
//            "                        \"ccode\":2966,\n" +
//            "                        \"cname\":\"天水市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":621200,\n" +
//            "                        \"ccode\":3019,\n" +
//            "                        \"cname\":\"陇南市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620100,\n" +
//            "                        \"ccode\":2947,\n" +
//            "                        \"cname\":\"兰州市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620900,\n" +
//            "                        \"ccode\":2994,\n" +
//            "                        \"cname\":\"酒泉市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620600,\n" +
//            "                        \"ccode\":2974,\n" +
//            "                        \"cname\":\"武威市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":621100,\n" +
//            "                        \"ccode\":3011,\n" +
//            "                        \"cname\":\"定西市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620400,\n" +
//            "                        \"ccode\":2960,\n" +
//            "                        \"cname\":\"白银市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":620800,\n" +
//            "                        \"ccode\":2986,\n" +
//            "                        \"cname\":\"平凉市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":623000,\n" +
//            "                        \"ccode\":3038,\n" +
//            "                        \"cname\":\"甘南藏族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":622900,\n" +
//            "                        \"ccode\":3029,\n" +
//            "                        \"cname\":\"临夏回族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"甘肃省\",\n" +
//            "                \"cnum\":39\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":140000,\n" +
//            "                \"ccode\":227,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":141000,\n" +
//            "                        \"ccode\":326,\n" +
//            "                        \"cname\":\"临汾市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140100,\n" +
//            "                        \"ccode\":228,\n" +
//            "                        \"cname\":\"太原市\",\n" +
//            "                        \"cnum\":7\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140200,\n" +
//            "                        \"ccode\":239,\n" +
//            "                        \"cname\":\"大同市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140700,\n" +
//            "                        \"ccode\":285,\n" +
//            "                        \"cname\":\"晋中市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140800,\n" +
//            "                        \"ccode\":297,\n" +
//            "                        \"cname\":\"运城市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140900,\n" +
//            "                        \"ccode\":311,\n" +
//            "                        \"cname\":\"忻州市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140500,\n" +
//            "                        \"ccode\":271,\n" +
//            "                        \"cname\":\"晋城市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140600,\n" +
//            "                        \"ccode\":278,\n" +
//            "                        \"cname\":\"朔州市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":141100,\n" +
//            "                        \"ccode\":344,\n" +
//            "                        \"cname\":\"吕梁市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":140400,\n" +
//            "                        \"ccode\":257,\n" +
//            "                        \"cname\":\"长治市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"山西省\",\n" +
//            "                \"cnum\":36\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":460000,\n" +
//            "                \"ccode\":2232,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":460100,\n" +
//            "                        \"ccode\":2233,\n" +
//            "                        \"cname\":\"海口市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":460400,\n" +
//            "                        \"ccode\":3356,\n" +
//            "                        \"cname\":\"儋州市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":460200,\n" +
//            "                        \"ccode\":2238,\n" +
//            "                        \"cname\":\"三亚市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469005,\n" +
//            "                        \"ccode\":2242,\n" +
//            "                        \"cname\":\"文昌市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469006,\n" +
//            "                        \"ccode\":2243,\n" +
//            "                        \"cname\":\"万宁市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469022,\n" +
//            "                        \"ccode\":2246,\n" +
//            "                        \"cname\":\"屯昌县\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469024,\n" +
//            "                        \"ccode\":2248,\n" +
//            "                        \"cname\":\"临高县\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469026,\n" +
//            "                        \"ccode\":2250,\n" +
//            "                        \"cname\":\"昌江黎族自治县\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469028,\n" +
//            "                        \"ccode\":2252,\n" +
//            "                        \"cname\":\"陵水黎族自治县\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469025,\n" +
//            "                        \"ccode\":2249,\n" +
//            "                        \"cname\":\"白沙黎族自治县\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469027,\n" +
//            "                        \"ccode\":2251,\n" +
//            "                        \"cname\":\"乐东黎族自治县\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469029,\n" +
//            "                        \"ccode\":2253,\n" +
//            "                        \"cname\":\"保亭黎族苗族自治县\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469030,\n" +
//            "                        \"ccode\":2254,\n" +
//            "                        \"cname\":\"琼中黎族苗族自治县\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469002,\n" +
//            "                        \"ccode\":2240,\n" +
//            "                        \"cname\":\"琼海市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469021,\n" +
//            "                        \"ccode\":2245,\n" +
//            "                        \"cname\":\"定安县\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":469007,\n" +
//            "                        \"ccode\":2244,\n" +
//            "                        \"cname\":\"东方市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"海南省\",\n" +
//            "                \"cnum\":28\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":150000,\n" +
//            "                \"ccode\":358,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":150400,\n" +
//            "                        \"ccode\":383,\n" +
//            "                        \"cname\":\"赤峰市\",\n" +
//            "                        \"cnum\":6\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150500,\n" +
//            "                        \"ccode\":396,\n" +
//            "                        \"cname\":\"通辽市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150100,\n" +
//            "                        \"ccode\":359,\n" +
//            "                        \"cname\":\"呼和浩特市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150600,\n" +
//            "                        \"ccode\":405,\n" +
//            "                        \"cname\":\"鄂尔多斯市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150900,\n" +
//            "                        \"ccode\":436,\n" +
//            "                        \"cname\":\"乌兰察布市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":152900,\n" +
//            "                        \"ccode\":468,\n" +
//            "                        \"cname\":\"阿拉善盟\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150200,\n" +
//            "                        \"ccode\":369,\n" +
//            "                        \"cname\":\"包头市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150700,\n" +
//            "                        \"ccode\":414,\n" +
//            "                        \"cname\":\"呼伦贝尔市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":150800,\n" +
//            "                        \"ccode\":428,\n" +
//            "                        \"cname\":\"巴彦淖尔市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":152200,\n" +
//            "                        \"ccode\":448,\n" +
//            "                        \"cname\":\"兴安盟\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"内蒙古自治区\",\n" +
//            "                \"cnum\":28\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":640000,\n" +
//            "                \"ccode\":3099,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":640500,\n" +
//            "                        \"ccode\":3122,\n" +
//            "                        \"cname\":\"中卫市\",\n" +
//            "                        \"cnum\":8\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":640300,\n" +
//            "                        \"ccode\":3111,\n" +
//            "                        \"cname\":\"吴忠市\",\n" +
//            "                        \"cnum\":5\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":640100,\n" +
//            "                        \"ccode\":3100,\n" +
//            "                        \"cname\":\"银川市\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":640400,\n" +
//            "                        \"ccode\":3116,\n" +
//            "                        \"cname\":\"固原市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"宁夏回族自治区\",\n" +
//            "                \"cnum\":20\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":650000,\n" +
//            "                \"ccode\":3126,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":652300,\n" +
//            "                        \"ccode\":3149,\n" +
//            "                        \"cname\":\"昌吉回族自治州\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":650100,\n" +
//            "                        \"ccode\":3127,\n" +
//            "                        \"cname\":\"乌鲁木齐市\",\n" +
//            "                        \"cnum\":3\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":650200,\n" +
//            "                        \"ccode\":3136,\n" +
//            "                        \"cname\":\"克拉玛依市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":652900,\n" +
//            "                        \"ccode\":3171,\n" +
//            "                        \"cname\":\"阿克苏地区\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":652800,\n" +
//            "                        \"ccode\":3161,\n" +
//            "                        \"cname\":\"巴音郭楞蒙古自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":654300,\n" +
//            "                        \"ccode\":3227,\n" +
//            "                        \"cname\":\"阿勒泰地区\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"新疆维吾尔自治区\",\n" +
//            "                \"cnum\":11\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":630000,\n" +
//            "                \"ccode\":3047,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":632800,\n" +
//            "                        \"ccode\":3093,\n" +
//            "                        \"cname\":\"海西蒙古族藏族自治州\",\n" +
//            "                        \"cnum\":4\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":630100,\n" +
//            "                        \"ccode\":3048,\n" +
//            "                        \"cname\":\"西宁市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":632600,\n" +
//            "                        \"ccode\":3079,\n" +
//            "                        \"cname\":\"果洛藏族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":632500,\n" +
//            "                        \"ccode\":3073,\n" +
//            "                        \"cname\":\"海南藏族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":632200,\n" +
//            "                        \"ccode\":3063,\n" +
//            "                        \"cname\":\"海北藏族自治州\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":630200,\n" +
//            "                        \"ccode\":3467,\n" +
//            "                        \"cname\":\"海东市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"青海省\",\n" +
//            "                \"cnum\":10\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"acode\":540000,\n" +
//            "                \"ccode\":2747,\n" +
//            "                \"cities\":[\n" +
//            "                    {\n" +
//            "                        \"acode\":540100,\n" +
//            "                        \"ccode\":2748,\n" +
//            "                        \"cname\":\"拉萨市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":540300,\n" +
//            "                        \"ccode\":3429,\n" +
//            "                        \"cname\":\"昌都市\",\n" +
//            "                        \"cnum\":2\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":540200,\n" +
//            "                        \"ccode\":3410,\n" +
//            "                        \"cname\":\"日喀则市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":540400,\n" +
//            "                        \"ccode\":3441,\n" +
//            "                        \"cname\":\"林芝市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":540500,\n" +
//            "                        \"ccode\":3449,\n" +
//            "                        \"cname\":\"山南市\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    },\n" +
//            "                    {\n" +
//            "                        \"acode\":542500,\n" +
//            "                        \"ccode\":2812,\n" +
//            "                        \"cname\":\"阿里地区\",\n" +
//            "                        \"cnum\":1\n" +
//            "                    }\n" +
//            "                ],\n" +
//            "                \"cname\":\"西藏自治区\",\n" +
//            "                \"cnum\":8\n" +
//            "            }\n" +
//            "        ],\n" +
//            "        \"city\":{\n" +
//            "            \"ccode\":\"1\",\n" +
//            "            \"acode\":\"0\",\n" +
//            "            \"cname\":\"null\",\n" +
//            "            \"ctype\":0,\n" +
//            "            \"level\":4,\n" +
//            "            \"pointx\":11628630.04,\n" +
//            "            \"pointy\":4098769.14,\n" +
//            "            \"geotype\":0,\n" +
//            "            \"path\":[\n" +
//            "                {\n" +
//            "                    \"acode\":0,\n" +
//            "                    \"ccode\":1,\n" +
//            "                    \"cname\":\"中国\"\n" +
//            "                }\n" +
//            "            ]\n" +
//            "        },\n" +
//            "        \"area\":{\n" +
//            "            \"acode\":0,\n" +
//            "            \"ccode\":1,\n" +
//            "            \"cname\":\"null\"\n" +
//            "        },\n" +
//            "        \"cnull\":0,\n" +
//            "        \"backlink\":{\n" +
//            "            \"type\":0,\n" +
//            "            \"acode\":0,\n" +
//            "            \"ccode\":1,\n" +
//            "            \"cname\":\"中国\",\n" +
//            "            \"query\":\"加油站\"\n" +
//            "        }\n" +
//            "    }\n" +
//            "}\n" +
//            "\n";

    public static String cityJson = "[\n" +
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