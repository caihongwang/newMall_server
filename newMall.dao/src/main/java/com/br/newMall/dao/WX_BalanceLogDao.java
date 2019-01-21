package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 余额日志Dao
 * @author caihongwang
 */
public interface WX_BalanceLogDao {

    /**
     * 根据条件查询余额日志信息
     */
    List<Map<String, Object>> getSimpleBalanceLogByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询余额日志信息总数
     */
    Integer getSimpleBalanceLogTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改余额日志信息
     */
    Integer addBalanceLog(Map<String, Object> paramMap);

    /**
     * 修改余额日志信息
     */
    Integer updateBalanceLog(Map<String, Object> paramMap);

    /**
     * 删除余额日志信息
     */
    Integer deleteBalanceLog(Map<String, Object> paramMap);
}
