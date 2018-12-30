package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.WX_AddressService;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_AddressDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 地址Service
 * @author caihongwang
 */
@Service
public class WX_AddressServiceImpl implements WX_AddressService {

    private static final Logger logger = LoggerFactory.getLogger(WX_AddressServiceImpl.class);

    @Autowired
    private WX_AddressDao wxAddressDao;

    @Autowired
    private WX_DicService wxDicService;

    /**
     * 默认获取中国省份列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getProvinceList(Map<String, Object> paramMap) {
        logger.info("在【service】中默认获取中国省份列表-getProvinceList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "province";
        if(!"".equals(dicType)){
            paramMap.put("dicType", dicType);
            paramMap.put("dicRemark", "\"parentId\":\"0\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_PROVINCETYPE_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_PROVINCETYPE_IS_NULL.getMessage());
        }
        logger.info("在【service】中默认获取中国省份列表-getProvinceList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据省份ID获取城市列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getCityList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据省份ID获取城市列表-getCityList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "city";
        String provinceId = paramMap.get("provinceId") != null ? paramMap.get("provinceId").toString() : "110000";
        if(!"".equals(dicType) && !"".equals(provinceId)){
            paramMap.put("dicType", dicType);
            paramMap.put("dicRemark", "\"parentId\":\""+provinceId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_CITYTYPE_OR_PROVINCEID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_CITYTYPE_OR_PROVINCEID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据省份ID获取城市列表-getCityList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据城市ID获取地区列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getRegionList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据城市ID获取地区列表-getRegionList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "region";
        String cityId = paramMap.get("cityId") != null ? paramMap.get("cityId").toString() : "110100";
        if(!"".equals(dicType) && !"".equals(cityId)){
            paramMap.put("dicType", dicType);
            paramMap.put("dicRemark", "\"parentId\":\""+cityId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_REGIONTYPE_OR_CITYID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_REGIONTYPE_OR_CITYID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据城市ID获取地区列表-getRegionList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据地区ID获取街道列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getStreetList(Map<String, Object> paramMap) {
        logger.info("在【service】中根据地区ID获取地区列表-getStreetList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "street";
        String regionId = paramMap.get("regionId") != null ? paramMap.get("regionId").toString() : "110108";
        if(!"".equals(dicType) && !"".equals(regionId)){
            paramMap.put("dicType", dicType);
            paramMap.put("dicRemark", "\"parentId\":\""+regionId+"\"");
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_REGIONTYPE_OR_REGIONID_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_REGIONTYPE_OR_REGIONID_IS_NULL.getMessage());
        }
        logger.info("在【service】中根据地区ID获取地区列表-getStreetList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }


    /**
     * 添加地址
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addAddress(Map<String, Object> paramMap) {
        logger.info("在【service】中添加地址-addAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String name = paramMap.get("name") != null ? paramMap.get("name").toString() : "";
        String phone = paramMap.get("phone") != null ? paramMap.get("phone").toString() : "";
        String provinceId = paramMap.get("provinceId") != null ? paramMap.get("provinceId").toString() : "";
        String provinceName = paramMap.get("provinceName") != null ? paramMap.get("provinceName").toString() : "";
        String cityId = paramMap.get("cityId") != null ? paramMap.get("cityId").toString() : "";
        String cityName = paramMap.get("cityName") != null ? paramMap.get("cityName").toString() : "";
        String regionId = paramMap.get("regionId") != null ? paramMap.get("regionId").toString() : "";
        String regionName = paramMap.get("regionName") != null ? paramMap.get("regionName").toString() : "";
        String detailAddress = paramMap.get("detailAddress") != null ? paramMap.get("detailAddress").toString() : "";
        String isDefaultAddress = paramMap.get("isDefaultAddress") != null ? paramMap.get("detailAddress").toString() : "0";
        if("0".equals(isDefaultAddress)){
            paramMap.put("isDefaultAddress", "0");      //设置默认地址
        }
        paramMap.put("status", "0");
        if (!"".equals(uid) && !"".equals(name)
                && !"".equals(phone) && !"".equals(detailAddress)
                    && !"".equals(provinceId) && !"".equals(provinceName)
                        && !"".equals(cityId) && !"".equals(cityName)
                            && !"".equals(regionId) && !"".equals(regionName)) {
            addNum = wxAddressDao.addAddress(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ADDRESS_UID_OR_NAME_OR_PHONE_OR_PROVINCEID_OR_PROVINCENAME_OR_CITYID_OR_CITYNAME_OR_REGIONID_OR_REGIONNAME_OR_STREETID_OR_STREETNAME_OR_DETAILADDRESS_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ADDRESS_UID_OR_NAME_OR_PHONE_OR_PROVINCEID_OR_PROVINCENAME_OR_CITYID_OR_CITYNAME_OR_REGIONID_OR_REGIONNAME_OR_STREETID_OR_STREETNAME_OR_DETAILADDRESS_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中添加地址-addAddress,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除地址
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteAddress(Map<String, Object> paramMap) {
        logger.info("在【service】中删除地址-deleteAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxAddressDao.deleteAddress(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ADDRESS_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ADDRESS_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中删除地址-deleteAddress,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改地址
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateAddress(Map<String, Object> paramMap) {
        logger.info("在【service】中修改地址-updateAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxAddressDao.updateAddress(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ADDRESS_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ADDRESS_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中修改地址-updateAddress,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 设置默认地址
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO setDefaultAddress(Map<String, Object> paramMap) {
        logger.info("在【service】中设置默认地址-setDefaultAddress,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(id) && !"".equals(id)) {
            updateNum = wxAddressDao.setDefaultAddress(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.ADDRESS_ID_OR_UID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ADDRESS_ID_OR_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中设置默认地址-setDefaultAddress,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的地址信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleAddressByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的地址-getSimpleAddressByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> productStrList = Lists.newArrayList();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if(!"".equals(uid)){
            List<Map<String, Object>> productList = wxAddressDao.getSimpleAddressByCondition(paramMap);
            if (productList != null && productList.size() > 0) {
                productStrList = MapUtil.getStringMapList(productList);
                Integer total = wxAddressDao.getSimpleAddressTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(productStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.ADDRESS_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.ADDRESS_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.ADDRESS_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ADDRESS_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取单一的地址-getSimpleAddressByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
}
