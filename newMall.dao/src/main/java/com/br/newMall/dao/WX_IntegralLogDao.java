package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 积分日志Dao
 * @author caihongwang
 */
public interface WX_IntegralLogDao {

    /**
     * 根据条件查询积分日志信息
     */
    List<Map<String, Object>> getSimpleIntegralLogByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询积分日志信息总数
     */
    Integer getSimpleIntegralLogTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改积分日志信息
     */
    Integer addIntegralLog(Map<String, Object> paramMap);

    /**
     * 修改积分日志信息
     */
    Integer updateIntegralLog(Map<String, Object> paramMap);

    /**
     * 删除积分日志信息
     */
    Integer deleteIntegralLog(Map<String, Object> paramMap);
}
