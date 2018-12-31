package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 订单Dao
 * @author caihongwang
 */
public interface WX_OrderDao {

    /**
     * 获取订单各个状态的数量
     */
    List<Map<String, Object>> getOrderNumByStatus(Map<String, Object> paramMap);

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
