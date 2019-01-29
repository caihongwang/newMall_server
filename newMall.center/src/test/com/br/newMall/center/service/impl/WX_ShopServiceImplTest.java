package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.br.newMall.dao.WX_ShopDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_ShopServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopServiceImpl.class);

    @Autowired
    private WX_ShopDao wxShopDao;

    @Value("${newMall.shop.miniProgramCode}")
    private String shopMiniProgramCodePath;


    @Test
    public void TEST() throws Exception {
//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("shopStatus", "1");
//        paramMap.put("currentLon", "116.40717");
//        paramMap.put("currentLat", "39.90469");
//        paramMap.put("dis", "1000");
//        paramMap.put("orderSortType", "shopOrderAmount");
//        getSimpleShopByCondition(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
        paramMap.put("currentLon", "116");
        paramMap.put("currentLat", "39");
        paramMap.put("dis", "1000");
        paramMap.put("orderSortType", "shopOrderAmount");
        getShopByCondition(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        paramMap.put("page", "pages/tabBar/todayOilPrice/todayOilPrice");
//        getMiniProgramCode(paramMap);
    }

    public ResultDTO getShopByCondition(Map<String, Object> paramMap) {
        logger.info("【service】根据条件查询店铺相关信息-getShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> shopStrList = Lists.newArrayList();
        //店铺排序顺序，默认距离
        String orderSortType = paramMap.get("orderSortType") != null ? paramMap.get("orderSortType").toString() : "shopDistance";
        Double currentLon = Double.parseDouble(paramMap.get("currentLon") != null ? paramMap.get("currentLon").toString() : "0");
        Double currentLat = Double.parseDouble(paramMap.get("currentLat") != null ? paramMap.get("currentLat").toString() : "0");
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "0";
        paramMap.put("shopStatus", "1");    //店铺表_状态，0是带签约，1是已签约
        if(!"".equals(shopId)){
            List<Map<String, Object>> shopList = wxShopDao.getShopByCondition(paramMap);
            if (shopList != null && shopList.size() > 0) {
                for (Map<String, Object> shopMap : shopList) {
                    //对距离进行计算
                    Double endLon = 0.0;
                    Double endLat = 0.0;
                    Double distance = 0.0;
                    try {
                        endLon = Double.parseDouble(shopMap.get("shopLon").toString());
                    } catch (Exception e) {
                        endLon = 0.0;
                    }
                    try {
                        endLat = Double.parseDouble(shopMap.get("shopLat").toString());
                    } catch (Exception e) {
                        endLat = 0.0;
                    }
                    if(currentLon > 0 && currentLat > 0
                            && endLon > 0 && endLat > 0){
                        distance = LonLatUtil.getDistance(currentLat, currentLon, endLat, endLon);
                    } else {
                        distance = 0.0;
                    }
                    if(distance > 0){
                        shopMap.put("shopDistance", distance.toString());
                    } else {
                        shopMap.put("shopDistance", "未知");
                    }
                    //对营业时间进行计算
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    Date currentTime = new Date();
                    Date shopBusinessHoursStartTime = new Date();
                    Date shopBusinessHoursEndTime = new Date();
                    String shopBusinessHoursStartTimeStr = shopMap.get("shopBusinessHoursStartTime")!=null?shopMap.get("shopBusinessHoursStartTime").toString():"00:00:00";
                    String shopBusinessHoursEndTimeStr = shopMap.get("shopBusinessHoursEndTime")!=null?shopMap.get("shopBusinessHoursEndTime").toString():"23:59:59";
                    try {
                        shopBusinessHoursStartTime = sdf.parse(shopBusinessHoursStartTimeStr);
                        shopBusinessHoursEndTime = sdf.parse(shopBusinessHoursEndTimeStr);
                        sdf = new SimpleDateFormat("HH:mm");
                        shopMap.put("shopBusinessHours",
                                sdf.format(shopBusinessHoursStartTime)
                                        + " - " +
                                        sdf.format(shopBusinessHoursEndTime)
                        );
                        if(shopBusinessHoursStartTime.before(currentTime) &&
                                shopBusinessHoursEndTime.after(currentTime)){
                            shopMap.put("shopBusinessStatus", "营业中");
                        } else {
                            shopMap.put("shopBusinessStatus", "休息中");
                        }
                    } catch (Exception e) {
                        shopMap.put("shopBusinessHours", "00:00 - 23:59");
                        shopMap.put("shopBusinessStatus", "营业中");
                        logger.error("当前店家的营业时间有误，默认为全天营业中，e : ", e);
                    }
                }
                shopStrList = MapUtil.getStringMapList(shopList);
                if("shopDistance".equals(orderSortType)){
                    //对shopStrList按照距离进行排序
                    Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> s1, Map<String, String> s2) {
                            Double shopDistance_1 = 0.0;
                            Double shopDistance_2 = 0.0;
                            try {
                                shopDistance_1 = Double.parseDouble(s1.get("shopDistance").toString());
                            } catch (Exception e) {
                                shopDistance_1 = 0.0;
                            }
                            try {
                                shopDistance_2 = Double.parseDouble(s2.get("shopDistance").toString());                    } catch (Exception e) {
                                shopDistance_1 = 0.0;
                            }
                            return shopDistance_1.compareTo(shopDistance_2);
                        }
                    });
                } else if("shopOrderAmount".equals(orderSortType)){
                    //对shopStrList按照订单量进行排序
                    Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> s1, Map<String, String> s2) {
                            Double shopOrderAmount_1 = 0.0;
                            Double shopOrderAmount_2 = 0.0;
                            try {
                                shopOrderAmount_1 = Double.parseDouble(s1.get("shopOrderAmount").toString());
                            } catch (Exception e) {
                                shopOrderAmount_1 = 0.0;
                            }
                            try {
                                shopOrderAmount_2 = Double.parseDouble(s2.get("shopOrderAmount").toString());
                            } catch (Exception e) {
                                shopOrderAmount_2 = 0.0;
                            }
                            return shopOrderAmount_2.compareTo(shopOrderAmount_1);
                        }
                    });
                } else {
                    //对shopStrList按照距离进行排序
                    Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> s1, Map<String, String> s2) {
                            Double shopDistance_1 = 0.0;
                            Double shopDistance_2 = 0.0;
                            try {
                                shopDistance_1 = Double.parseDouble(s1.get("shopDistance").toString());
                            } catch (Exception e) {
                                shopDistance_1 = 0.0;
                            }
                            try {
                                shopDistance_2 = Double.parseDouble(s2.get("shopDistance").toString());                    } catch (Exception e) {
                                shopDistance_1 = 0.0;
                            }
                            return shopDistance_1.compareTo(shopDistance_2);
                        }
                    });
                }
                Integer total = wxShopDao.getShopTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(shopStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SHOP_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.SHOP_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】根据条件查询店铺相关信息-getShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultDTO getSimpleShopByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> shopStrList = Lists.newArrayList();
//        paramMap.remove("uid");
        //店铺排序顺序，默认距离
        String orderSortType = paramMap.get("orderSortType") != null ? paramMap.get("orderSortType").toString() : "shopDistance";
        Double currentLon = Double.parseDouble(paramMap.get("currentLon") != null ? paramMap.get("currentLon").toString() : "0");
        Double currentLat = Double.parseDouble(paramMap.get("currentLat") != null ? paramMap.get("currentLat").toString() : "0");
        Double dis = Double.parseDouble(paramMap.get("dis") != null ? paramMap.get("dis").toString() : "100");    //默认100公里范围
        paramMap.put("shopStatus", "1");    //店铺表_状态，0是带签约，1是已签约
        if(currentLon > 0 && currentLat > 0){
            //先计算查询点的经纬度范围
            double r = LonLatUtil.EARTH_RADIUS;//地球半径千米
            double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(currentLat * Math.PI / 180));
            dlng = dlng * 180 / Math.PI;//角度转为弧度
            double dlat = dis / r;
            dlat = dlat * 180 / Math.PI;
            double minLat = currentLat - dlat;
            double maxLat = currentLat + dlat;
            double maxLon = currentLon + dlng;
            double minLon = currentLon - dlng;
            paramMap.put("minLon", minLon);
            paramMap.put("maxLon", maxLon);
            paramMap.put("minLat", minLat);
            paramMap.put("maxLat", maxLat);
        }
        List<Map<String, Object>> shopList = wxShopDao.getSimpleShopByCondition(paramMap);
        if (shopList != null && shopList.size() > 0) {
            //计算每个商家距离用户当前位置的距离
            for (Map<String, Object> shopMap : shopList) {
                Double endLat = Double.parseDouble(shopMap.get("shopLat").toString());
                Double endLon = Double.parseDouble(shopMap.get("shopLon").toString());
                Double distance = 0.0;
                if(currentLon > 0 && currentLat > 0){
                    distance = LonLatUtil.getDistance(currentLat, currentLon, endLat, endLon);
                } else {
                    distance = 0.0;
                }
                if(distance > 0){
                    shopMap.put("shopDistance", distance.toString());
                } else {
                    shopMap.put("shopDistance", "未知");
                }
            }
            shopStrList = MapUtil.getStringMapList(shopList);
            if("shopDistance".equals(orderSortType)){
                //对shopStrList按照距离进行排序
                Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> s1, Map<String, String> s2) {
                        Double shopDistance_1 = 0.0;
                        Double shopDistance_2 = 0.0;
                        try {
                            shopDistance_1 = Double.parseDouble(s1.get("shopDistance").toString());
                        } catch (Exception e) {
                            shopDistance_1 = 0.0;
                        }
                        try {
                            shopDistance_2 = Double.parseDouble(s2.get("shopDistance").toString());                    } catch (Exception e) {
                            shopDistance_1 = 0.0;
                        }
                        return shopDistance_1.compareTo(shopDistance_2);
                    }
                });
            } else if("shopOrderAmount".equals(orderSortType)){
                //对shopStrList按照订单量进行排序
                Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> s1, Map<String, String> s2) {
                        Double shopOrderAmount_1 = 0.0;
                        Double shopOrderAmount_2 = 0.0;
                        try {
                            shopOrderAmount_1 = Double.parseDouble(s1.get("shopOrderAmount").toString());
                        } catch (Exception e) {
                            shopOrderAmount_1 = 0.0;
                        }
                        try {
                            shopOrderAmount_2 = Double.parseDouble(s2.get("shopOrderAmount").toString());
                        } catch (Exception e) {
                            shopOrderAmount_2 = 0.0;
                        }
                        return shopOrderAmount_1.compareTo(shopOrderAmount_2);
                    }
                });
            } else {
                //对shopStrList按照距离进行排序
                Collections.sort(shopStrList, new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> s1, Map<String, String> s2) {
                        Double shopDistance_1 = 0.0;
                        Double shopDistance_2 = 0.0;
                        try {
                            shopDistance_1 = Double.parseDouble(s1.get("shopDistance").toString());
                        } catch (Exception e) {
                            shopDistance_1 = 0.0;
                        }
                        try {
                            shopDistance_2 = Double.parseDouble(s2.get("shopDistance").toString());                    } catch (Exception e) {
                            shopDistance_1 = 0.0;
                        }
                        return shopDistance_1.compareTo(shopDistance_2);
                    }
                });
            }

            Integer total = wxShopDao.getSimpleShopTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(shopStrList);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.SHOP_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.SHOP_LIST_IS_NULL.getMessage());
        }
        logger.info("【service】获取单一的店铺-getSimpleShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultMapDTO getMiniProgramCode(Map<String, Object> paramMap) {
        logger.info("【service】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,响应-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String page = paramMap.get("page")!=null?paramMap.get("page").toString():"";
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        String nickName = paramMap.get("nickName")!=null?paramMap.get("nickName").toString():"";
        String shopTitle = paramMap.get("shopTitle")!=null?paramMap.get("shopTitle").toString():"";
        if ( (!"".equals(uid) || !"".equals(nickName) || !"".equals(shopTitle))
                && !"".equals(page) && !"".equals(shopMiniProgramCodePath)) {
            String shopId = "";
            Map<String, Object> shopMap = Maps.newHashMap();
            shopMap.put("shopTitle", shopTitle);
            shopMap.put("nickName", nickName);
            shopMap.put("uid", uid);
            List<Map<String, Object>> shopList = wxShopDao.getShopByCondition(shopMap);
            if(shopList != null && shopList.size() > 0){
                shopId = shopList.get(0).get("shopId").toString();
            }
            if(!"".equals(shopTitle)){
                shopMiniProgramCodePath = shopMiniProgramCodePath + "/" + shopTitle + "/";
            } else if(!"".equals(nickName)){
                shopMiniProgramCodePath = shopMiniProgramCodePath + "/" + nickName + "/";
            } else if(!"".equals(uid)){
                shopMiniProgramCodePath = shopMiniProgramCodePath + "/" + uid + "/";
            }
            if(!"".equals(shopId)){
                String scene = "shopId=" + shopId;
                resultMap = WX_PublicNumberUtil.getMiniProgramCode(
                        NewMallCode.WX_MINI_PROGRAM_APPID,
                        NewMallCode.WX_MINI_PROGRAM_SECRET,
                        page,
                        scene,
                        shopMiniProgramCodePath);
                if(resultMap != null && resultMap.size() > 0){
                    resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                    resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.SHOP_UID_NICKNAME_SHOPTITLE_IS_NOT_EXIST_SHOP.getNo());
                resultMapDTO.setMessage(NewMallCode.SHOP_UID_NICKNAME_SHOPTITLE_IS_NOT_EXIST_SHOP.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.SHOP_UID_NICKNAME_SHOPTITLE_PAGE_SCENE_FILEPATH_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.SHOP_UID_NICKNAME_SHOPTITLE_PAGE_SCENE_FILEPATH_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}