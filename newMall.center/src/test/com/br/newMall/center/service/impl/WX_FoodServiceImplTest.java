package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_FoodDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2019/2/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_FoodServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_FoodServiceImpl.class);

    @Autowired
    private WX_FoodDao wxFoodDao;

    @Test
    public void TEST() throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("shopId", "1");
        this.getMenuByCondition(paramMap);
    }

    public ResultMapDTO getMenuByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取菜单食物列表-getMenuByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String shopId = paramMap.get("shopId") != null ? paramMap.get("shopId").toString() : "";
        if (!"".equals(shopId)) {
            List<Map<String, Object>> foodList = wxFoodDao.getMenuByCondition(paramMap);
            if (foodList != null && foodList.size() > 0) {
                for (Map<String, Object> foodMap : foodList) {
                    List<Map<String, Object>> foodTypeTitleList = Lists.newArrayList();
                    foodTypeTitleList.clear();
                    String foodTypeTitle = foodMap.get("foodTypeTitle")!=null?foodMap.get("foodTypeTitle").toString():"";
                    if(!"".equals(foodTypeTitle)){
                        if(!resultMap.containsKey(foodTypeTitle)){
                            foodTypeTitleList.add(foodMap);
                        } else {
                            String foodTypeTitleListStr = resultMap.get(foodTypeTitle) != null ?
                                    resultMap.get(foodTypeTitle).toString() : "";
                            foodTypeTitleList = JSONObject.parseObject(foodTypeTitleListStr, List.class);
                            foodTypeTitleList.add(foodMap);
                        }
                        resultMap.put(foodTypeTitle, JSONObject.toJSONString(foodTypeTitleList));
                    } else {
                        String foodTypeTitleListStr = resultMap.get("其他") != null ?
                                resultMap.get("其他").toString() : "";
                        foodTypeTitleList = JSONObject.parseObject(foodTypeTitleListStr, List.class);
                        foodTypeTitleList.add(foodMap);
                        resultMap.put(foodTypeTitle, JSONObject.toJSONString(foodTypeTitleList));
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