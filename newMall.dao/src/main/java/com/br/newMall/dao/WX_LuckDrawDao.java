package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 抽奖Dao
 * @author caihongwang
 */
public interface WX_LuckDrawDao {

    /**
     * 获取订单各个状态的数量
     */
    List<Map<String, Object>> getLuckDrawNumByStatus(Map<String, Object> paramMap);

    /**
     * 获取抽过奖励的商家列表
     */
    List<Map<String, Object>> getLuckDrawShopByCondition(Map<String, Object> paramMap);

    /**
     * 获取抽过奖励的商家列表总数
     */
    Integer getLuckDrawShopTotalByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下待领取奖励的队列
     */
    List<Map<String, Object>> getLuckDrawRankByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下待领取奖励的队列
     */
    Integer getLuckDrawRankTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询抽奖信息
     */
    List<Map<String, Object>> getLuckDrawByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询抽奖信息总数
     */
    Integer getLuckDrawTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询抽奖信息
     */
    List<Map<String, Object>> getSimpleLuckDrawByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询抽奖信息总数
     */
    Integer getSimpleLuckDrawTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改抽奖信息
     */
    Integer addLuckDraw(Map<String, Object> paramMap);

    /**
     * 修改抽奖信息
     */
    Integer updateLuckDraw(Map<String, Object> paramMap);

    /**
     * 删除抽奖信息
     */
    Integer deleteLuckDraw(Map<String, Object> paramMap);
}
