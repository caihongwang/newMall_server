package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 食物Dao
 * @author caihongwang
 */
public interface WX_FoodDao {

    /**
     * 根据条件查询菜单食物信息
     */
    List<Map<String, Object>> getMenuByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询菜单食物信息总数
     */
    Integer getMenuTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询食物信息
     */
    List<Map<String, Object>> getSimpleFoodByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询食物信息总数
     */
    Integer getSimpleFoodTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改食物信息
     */
    Integer addFood(Map<String, Object> paramMap);

    /**
     * 修改食物信息
     */
    Integer updateFood(Map<String, Object> paramMap);

    /**
     * 删除食物信息
     */
    Integer deleteFood(Map<String, Object> paramMap);
}
