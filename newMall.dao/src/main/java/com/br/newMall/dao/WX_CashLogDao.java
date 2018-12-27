package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 提现日志Dao
 * @author caihongwang
 */
public interface WX_CashLogDao {

    /**
     * 根据条件查询提现日志信息
     */
    List<Map<String, Object>> getSimpleCashLogByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询提现日志信息总数
     */
    Integer getSimpleCashLogTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改提现日志信息
     */
    Integer addCashLog(Map<String, Object> paramMap);

    /**
     * 修改提现日志信息
     */
    Integer updateCashLog(Map<String, Object> paramMap);

    /**
     * 删除提现日志信息
     */
    Integer deleteCashLog(Map<String, Object> paramMap);
}
