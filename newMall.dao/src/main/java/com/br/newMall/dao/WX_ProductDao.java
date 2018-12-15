package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 商品Dao
 * @author caihongwang
 */
public interface WX_ProductDao {

    /**
     * 根据条件查询商品信息
     */
    List<Map<String, Object>> getSimpleProductByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询商品信息总数
     */
    Integer getSimpleProductTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改商品信息
     */
    Integer addProduct(Map<String, Object> paramMap);

    /**
     * 修改商品信息
     */
    Integer updateProduct(Map<String, Object> paramMap);

    /**
     * 删除商品信息
     */
    Integer deleteProduct(Map<String, Object> paramMap);
}
