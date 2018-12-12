package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * 加油站操作
 */
public interface OilStationOperatorDao {

    /**
     * 获取红包历史列表
     */
    List<Map<String, Object>> getRedPacketHistoryList(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站操作信息
     */
    List<Map<String, Object>> getSimpleOilStationOperatorByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站操作信息总数
     */
    Integer getSimpleOilStationOperatorTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站操作信息的红包金额
     */
    List<Map<String, Object>> getOilStationOperatorByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站操作的红包总个数
     */
    Integer getOilStationOperatorTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改加油站操作信息
     */
    Integer addOilStationOperator(Map<String, Object> paramMap);

    /**
     * 修改加油站操作信息
     */
    Integer updateOilStationOperator(Map<String, Object> paramMap);

    /**
     * 删除加油站操作信息
     */
    Integer deleteOilStationOperator(Map<String, Object> paramMap);
}
