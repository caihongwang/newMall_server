package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_ShopService;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.br.newMall.dao.WX_ShopDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @Description 店铺Service
 * @author caihongwang
 */
@Service
public class WX_ShopServiceImpl implements WX_ShopService {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopServiceImpl.class);

    @Autowired
    private WX_ShopDao wxShopDao;


    @Value("${newMall.shop.miniProgramCode}")
    private String shopMiniProgramCodePath;

    /**
     * 添加店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addShop(Map<String, Object> paramMap) {
        logger.info("【service】添加店铺-addShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String shopDiscountId = paramMap.get("shopDiscountId") != null ? paramMap.get("shopDiscountId").toString() : "";
        String shopTitle = paramMap.get("shopTitle") != null ? paramMap.get("shopTitle").toString() : "";
        String shopDegist = paramMap.get("shopDegist") != null ? paramMap.get("shopDegist").toString() : "";
        String shopPhone = paramMap.get("shopPhone") != null ? paramMap.get("shopPhone").toString() : "";
        String shopAddress = paramMap.get("shopAddress") != null ? paramMap.get("shopAddress").toString() : "";
        String shopLon = paramMap.get("shopLon") != null ? paramMap.get("shopLon").toString() : "";
        String shopLat = paramMap.get("shopLat") != null ? paramMap.get("shopLat").toString() : "";
        String shopHeadImgUrl = paramMap.get("shopHeadImgUrl") != null ? paramMap.get("shopHeadImgUrl").toString() : "";
        String shopDescribeImgUrl = paramMap.get("shopDescribeImgUrl") != null ? paramMap.get("shopDescribeImgUrl").toString() : "";
        if (!"".equals(shopTitle) && !"".equals(shopDegist)
                && !"".equals(shopPhone) && !"".equals(shopAddress)
                    && !"".equals(shopLon) && !"".equals(shopLat)
                        && !"".equals(shopHeadImgUrl) && !"".equals(shopDescribeImgUrl)
                            && !"".equals(shopDiscountId)) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("shopTitle", shopTitle);
            Integer total = wxShopDao.getSimpleShopTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                paramMap.put("shopStatus", "0");
                addNum = wxShopDao.addShop(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                boolDTO.setCode(NewMallCode.SHOP_EXIST.getNo());
                boolDTO.setMessage(NewMallCode.SHOP_EXIST.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.SHOP_SHOPDISCOUNTID_SHOPTITLE_SHOPDEGIST_SHOPPHONE_SHOPADDRESS_SHOPLON_SHOPLAT_SHOPHEADIMGURL_SHOPDESCRIBEIMGURL_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SHOP_SHOPDISCOUNTID_SHOPTITLE_SHOPDEGIST_SHOPPHONE_SHOPADDRESS_SHOPLON_SHOPLAT_SHOPHEADIMGURL_SHOPDESCRIBEIMGURL_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】添加店铺-addShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteShop(Map<String, Object> paramMap) {
        logger.info("【service】删除店铺-deleteShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxShopDao.deleteShop(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】删除店铺-deleteShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateShop(Map<String, Object> paramMap) {
        logger.info("【service】修改店铺-updateShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxShopDao.updateShop(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改店铺-updateShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的店铺信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleShopByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        paramMap.remove("uid");
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
            dicStrList = MapUtil.getStringMapList(shopList);
            Integer total = wxShopDao.getSimpleShopTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(dicStrList);
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

    /**
     * 根据条件查询店铺相关信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getShopByCondition(Map<String, Object> paramMap) {
        logger.info("【service】根据条件查询店铺相关信息-getShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> shopStrList = Lists.newArrayList();
        Double currentLon = Double.parseDouble(paramMap.get("currentLon") != null ? paramMap.get("currentLon").toString() : "0");
        Double currentLat = Double.parseDouble(paramMap.get("currentLat") != null ? paramMap.get("currentLat").toString() : "0");
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "0";
        paramMap.put("shopStatus", "1");    //店铺表_状态，0是带签约，1是已签约
        if(!"".equals(shopId)){
            List<Map<String, Object>> shopList = wxShopDao.getShopByCondition(paramMap);
            if (shopList != null && shopList.size() > 0) {
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

    /**
     * 根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码
     * @return
     */
    @Override
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
