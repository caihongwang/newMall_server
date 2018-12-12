package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface RedPacketDrawCashHistoryDao {

    /**
     * 获取已提现红包总额
     */
    Map<String, Object> getDrawCashMoneyTotal(Map<String, Object> paramMap);

    /**
     * 获取红包提现历史记录
     */
    List<Map<String, Object>> getRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 获取红包提现历史记录总数
     */
    Integer getRedPacketDrawCashHistoryTotal(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包提现记录
     */
    List<Map<String, Object>> getSimpleRedPacketDrawCashHistoryByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包提现记录总数
     */
    Integer getSimpleRedPacketDrawCashHistoryTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改红包提现记录
     */
    Integer addRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 修改红包提现记录
     */
    Integer updateRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 删除红包提现记录
     */
    Integer deleteRedPacketDrawCashHistory(Map<String, Object> paramMap);
}
