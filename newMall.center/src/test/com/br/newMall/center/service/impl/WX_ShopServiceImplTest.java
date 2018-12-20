package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_ShopDao;
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
 * Created by caihongwang on 2018/12/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_ShopServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopServiceImpl.class);

    @Autowired
    private WX_ShopDao wxShopDao;


    @Test
    public void TEST() throws Exception {
//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("shopStatus", "1");
//        getSimpleShopByCondition(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
        getShopByCondition(paramMap);
    }

    /**
     * 获取单一的店铺信息
     * @param paramMap
     * @return
     */
    public ResultDTO getSimpleShopByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        paramMap.remove("uid");
        List<Map<String, Object>> dicList = wxShopDao.getSimpleShopByCondition(paramMap);
        if (dicList != null && dicList.size() > 0) {
            dicStrList = MapUtil.getStringMapList(dicList);
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
        logger.info("在【service】中获取单一的店铺-getSimpleShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据条件查询店铺相关信息
     * @param paramMap
     * @return
     */
    public ResultDTO getShopByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中根据条件查询店铺相关信息-getShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        List<Map<String, Object>> dicList = wxShopDao.getShopByCondition(paramMap);
        if (dicList != null && dicList.size() > 0) {
            dicStrList = MapUtil.getStringMapList(dicList);
            Integer total = wxShopDao.getShopTotalByCondition(paramMap);
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
        logger.info("在【service】中根据条件查询店铺相关信息-getShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
}