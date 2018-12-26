package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 抽奖Dao
 * @author caihongwang
 */
public interface WX_LuckDrawDao {

    /**
     * 获取待领取奖励的商家列表
     */
    List<Map<String, Object>> getWaitGetLuckDrawShopByCondition(Map<String, Object> paramMap);

    /**
     * 获取待领取奖励的商家列表总数
     */
    Integer getWaitGetLuckDrawShopTotalByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下待领取奖励的队列
     */
    List<Map<String, Object>> getWaitGetLuckDrawRankByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下待领取奖励的队列
     */
    Integer getWaitGetLuckDrawRankTotalByCondition(Map<String, Object> paramMap);

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
