package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 订单Dao
 * @author caihongwang
 */
public interface WX_OrderDao {

    /**
     * 获取当前用户的商品订单信息
     */
    List<Map<String, Object>> getGoodsOrderByCondition(Map<String, Object> paramMap);

    /**
     * 获取当前用户的商品订单信息总数
     */
    Integer getGoodsOrderTotalByCondition(Map<String, Object> paramMap);

    /**
     * 获取当前用户的订单信息
     */
    List<Map<String, Object>> getOrderByCondition(Map<String, Object> paramMap);

    /**
     * 获取当前用户的订单信息总数
     */
    Integer getOrderTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询订单信息
     */
    List<Map<String, Object>> getSimpleOrderByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询订单信息总数
     */
    Integer getSimpleOrderTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改订单信息
     */
    Integer addOrder(Map<String, Object> paramMap);

    /**
     * 修改订单信息
     */
    Integer updateOrder(Map<String, Object> paramMap);

    /**
     * 删除订单信息
     */
    Integer deleteOrder(Map<String, Object> paramMap);
}
