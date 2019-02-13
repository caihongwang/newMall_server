package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 食物Service
 * @author caihongwang
 */
public interface WX_FoodService {

    /**
     * 根据条件查询菜单食物信息
     */
    ResultMapDTO getMenuByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询食物信息
     */
    ResultDTO getSimpleFoodByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改食物信息
     */
    BoolDTO addFood(Map<String, Object> paramMap);

    /**
     * 修改食物信息
     */
    BoolDTO updateFood(Map<String, Object> paramMap);

    /**
     * 删除食物信息
     */
    BoolDTO deleteFood(Map<String, Object> paramMap);
}
