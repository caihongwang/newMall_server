package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 店铺Dao
 * @author caihongwang
 */
public interface WX_ShopDao {

    /**
     * 根据条件查询店铺信息
     */
    List<Map<String, Object>> getSimpleShopByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询店铺信息总数
     */
    Integer getSimpleShopTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改店铺信息
     */
    Integer addShop(Map<String, Object> paramMap);

    /**
     * 修改店铺信息
     */
    Integer updateShop(Map<String, Object> paramMap);

    /**
     * 删除店铺信息
     */
    Integer deleteShop(Map<String, Object> paramMap);
}
