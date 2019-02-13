package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_FoodService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_FoodDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 食物Service
 * @author caihongwang
 */
@Service
public class WX_FoodServiceImpl implements WX_FoodService {

    private static final Logger logger = LoggerFactory.getLogger(WX_FoodServiceImpl.class);

    @Autowired
    private WX_FoodDao wxFoodDao;

    /**
     * 添加食物
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addFood(Map<String, Object> paramMap) {
        logger.info("【service】添加食物-addFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        String foodTypeTitle = paramMap.get("foodTypeTitle") != null ? paramMap.get("foodTypeTitle").toString() : "";
        String foodTitle = paramMap.get("foodTitle") != null ? paramMap.get("foodTitle").toString() : "";
        String foodPrice = paramMap.get("foodPrice") != null ? paramMap.get("foodPrice").toString() : "";
        String foodHeadImgUrl = paramMap.get("foodHeadImgUrl") != null ? paramMap.get("foodHeadImgUrl").toString() : "";
        String foodDescribeImgUrl = paramMap.get("foodDescribeImgUrl") != null ? paramMap.get("foodDescribeImgUrl").toString() : "";
        String foodOptions = paramMap.get("foodOptions") != null ? paramMap.get("foodOptions").toString() : "";
        String remark = paramMap.get("remark") != null ? paramMap.get("remark").toString() : "";
        if (!"".equals(shopId) && !"".equals(foodTypeTitle)
                && !"".equals(foodTitle) && !"".equals(foodPrice)
                && !"".equals(foodHeadImgUrl) && !"".equals(foodDescribeImgUrl)
                && !"".equals(foodOptions)) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("foodTitle", foodTitle);
            paramMap_temp.put("shopId", shopId);
            Integer total = wxFoodDao.getSimpleFoodTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                addNum = wxFoodDao.addFood(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                boolDTO.setCode(NewMallCode.FOOD_EXIST.getNo());
                boolDTO.setMessage(NewMallCode.FOOD_EXIST.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.FOOD_SHOPID_FOODTYPETITLE_FOODTITLE_FOODPRICE_FOODHEADIMGURL_FOODDESCRIBEIMGURL_FOODOPTIONS_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.FOOD_SHOPID_FOODTYPETITLE_FOODTITLE_FOODPRICE_FOODHEADIMGURL_FOODDESCRIBEIMGURL_FOODOPTIONS_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】添加食物-addFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除食物
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteFood(Map<String, Object> paramMap) {
        logger.info("【service】删除食物-deleteFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String status = paramMap.get("status") != null ? paramMap.get("status").toString() : "1";
        if (!"".equals(id) || !"".equals(status)) {
            deleteNum = wxFoodDao.deleteFood(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.FOOD_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.FOOD_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】删除食物-deleteFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改食物
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateFood(Map<String, Object> paramMap) {
        logger.info("【service】修改食物-updateFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        if (!"".equals(id) || !"".equals(shopId)) {
            updateNum = wxFoodDao.updateFood(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.FOOD_ID_OR_SHOPID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.FOOD_ID_OR_SHOPID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改食物-updateFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的食物信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleFoodByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的食物-getSimpleFoodByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        if (!"".equals(id) || !"".equals(shopId)) {
            List<Map<String, Object>> foodList = wxFoodDao.getSimpleFoodByCondition(paramMap);
            if (foodList != null && foodList.size() > 0) {
                dicStrList = MapUtil.getStringMapList(foodList);
                Integer total = wxFoodDao.getSimpleFoodTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(dicStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.FOOD_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.FOOD_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.FOOD_ID_OR_SHOPID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.FOOD_ID_OR_SHOPID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取单一的食物-getSimpleFoodByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取菜单食物列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getMenuByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取菜单食物列表-getMenuByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        if (!"".equals(shopId)) {
            List<Map<String, Object>> foodList = wxFoodDao.getMenuByCondition(paramMap);
            if (foodList != null && foodList.size() > 0) {
                for (Map<String, Object> foodMap : foodList) {
                    List<Map<String, Object>> menuList = Lists.newArrayList();
                    menuList.clear();
                    String foodTypeTitle = foodMap.get("foodTypeTitle")!=null?foodMap.get("foodTypeTitle").toString():"";
                    if(!"".equals(foodTypeTitle)){
                        if(!resultMap.containsKey(foodTypeTitle)){
                            menuList.add(foodMap);
                        } else {
                            Map<String, Object> tempMap = resultMap.get(foodTypeTitle) != null ?
                                    (Map<String, Object>)resultMap.get(foodTypeTitle) : Maps.newHashMap();
                            String menuListStr = tempMap.get("menuList") != null ?
                                    tempMap.get("menuList").toString() : "";
                            menuList = JSONObject.parseObject(menuListStr, List.class);
                            menuList.add(foodMap);
                        }
                        Map<String, Object> tempMap = Maps.newHashMap();
                        tempMap.put("foodTypeTitle", foodTypeTitle);
                        tempMap.put("menuList", menuList);
                        resultMap.put(foodTypeTitle, JSONObject.toJSONString(tempMap));
                    } else {
                        Map<String, Object> tempMap = resultMap.get("其他") != null ?
                                (Map<String, Object>)resultMap.get("其他") : Maps.newHashMap();
                        String menuListStr = tempMap.get("menuList") != null ?
                                tempMap.get("menuList").toString() : "";
                        menuList = JSONObject.parseObject(menuListStr, List.class);
                        menuList.add(foodMap);
                        tempMap.put("foodTypeTitle", "其他");
                        tempMap.put("menuList", menuList);
                        resultMap.put("其他", JSONObject.toJSONString(tempMap));
                    }
                }
                Integer total = wxFoodDao.getMenuTotalByCondition(paramMap);
                resultMapDTO.setResultListTotal(total);
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.FOOD_LIST_IS_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.FOOD_LIST_IS_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.FOOD_SHOPID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.FOOD_SHOPID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取菜单食物列表-getMenuByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
