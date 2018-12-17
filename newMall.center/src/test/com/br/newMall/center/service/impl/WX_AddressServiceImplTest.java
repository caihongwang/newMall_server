package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.dao.WX_AddressDao;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_AddressServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_AddressServiceImpl.class);

    @Autowired
    private WX_AddressDao wxAddressDao;

    @Autowired
    private WX_DicService wxDicService;

    @Test
    public void Test(){
//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("dicType", "province");
//        getProvinceList(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("dicType", "city");
//        paramMap.put("provinceId", "130000");
//        getCityList(paramMap);

//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("dicType", "region");
//        paramMap.put("cityId", "131100");
//        getRegionList(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("dicType", "street");
        paramMap.put("regionId", "131102");
        getStreetList(paramMap);
    }

    public ResultDTO getStreetList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据地区ID获取地区列表-getStreetList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "province";
        String regionId = paramMap.get("regionId") != null ? paramMap.get("regionId").toString() : "province";
        if(!"".equals(dicType) && !"".equals(regionId)){
            paramMap.put("dicRemark", "\"parentId\":\""+regionId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_REGIONTYPE_OR_REGIONID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_REGIONTYPE_OR_REGIONID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据地区ID获取地区列表-getStreetList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultDTO getRegionList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据城市ID获取地区列表-getRegionList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "province";
        String cityId = paramMap.get("cityId") != null ? paramMap.get("cityId").toString() : "province";
        if(!"".equals(dicType) && !"".equals(cityId)){
            paramMap.put("dicRemark", "\"parentId\":\""+cityId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_REGIONTYPE_OR_CITYID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_REGIONTYPE_OR_CITYID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据城市ID获取地区列表-getRegionList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultDTO getCityList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据省份ID获取城市列表-getCityList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "province";
        String provinceId = paramMap.get("provinceId") != null ? paramMap.get("provinceId").toString() : "province";
        if(!"".equals(dicType) && !"".equals(provinceId)){
            paramMap.put("dicRemark", "\"parentId\":\""+provinceId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_CITYTYPE_OR_PROVINCEID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_CITYTYPE_OR_PROVINCEID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据省份ID获取城市列表-getCityList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    public ResultDTO getProvinceList(Map<String, Object> paramMap) {
        logger.info("在【service】中默认获取中国省份列表-getProvinceList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "province";
        if(!"".equals(dicType)){
            paramMap.put("dicRemark", "\"parentId\":\"0\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_PROVINCETYPE_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_PROVINCETYPE_IS_NULL.getMessage());
        }
        logger.info("在【service】中默认获取中国省份列表-getProvinceList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
}