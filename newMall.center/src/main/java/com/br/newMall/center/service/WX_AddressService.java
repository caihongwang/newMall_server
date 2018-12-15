package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;

import java.util.Map;

/**
 * @Description 地址Service
 * @author caihongwang
 */
public interface WX_AddressService {

    /**
     * 默认获取中国省份列表
     */
    ResultDTO getProvinceList(Map<String, Object> paramMap);

    /**
     * 根据省份ID获取城市列表
     */
    ResultDTO getCityList(Map<String, Object> paramMap);

    /**
     * 根据省份ID和城市ID获取地区列表
     */
    ResultDTO getRegionList(Map<String, Object> paramMap);

    /**
     * 根据条件查询地址信息
     */
    ResultDTO getSimpleAddressByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改地址信息
     */
    BoolDTO addAddress(Map<String, Object> paramMap);

    /**
     * 修改地址信息
     */
    BoolDTO updateAddress(Map<String, Object> paramMap);

    /**
     * 删除地址信息
     */
    BoolDTO deleteAddress(Map<String, Object> paramMap);
}
