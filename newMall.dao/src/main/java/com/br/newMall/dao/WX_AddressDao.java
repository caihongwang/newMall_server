package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 地址Dao
 * @author caihongwang
 */
public interface WX_AddressDao {

    /**
     * 设置默认地址
     */
    Integer setDefaultAddress(Map<String, Object> paramMap);

    /**
     * 根据条件查询地址信息
     */
    List<Map<String, Object>> getSimpleAddressByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询地址信息总数
     */
    Integer getSimpleAddressTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改地址信息
     */
    Integer addAddress(Map<String, Object> paramMap);

    /**
     * 修改地址信息
     */
    Integer updateAddress(Map<String, Object> paramMap);

    /**
     * 删除地址信息
     */
    Integer deleteAddress(Map<String, Object> paramMap);
}
