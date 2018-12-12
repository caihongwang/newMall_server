package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface RedPacketHistoryDao {

    /**
     * 获取已领取红包总额
     */
    Integer getAllRedPacketMoneyTotal(Map<String, Object> paramMap);

    /**
     * 获取红包领取记录
     */
    List<Map<String, Object>> getRedPacketHistoryList(Map<String, Object> paramMap);

    /**
     * 获取红包领取记录总数
     */
    Integer getRedPacketHistoryTotal(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包领取记录
     */
    List<Map<String, Object>> getSimpleRedPacketHistoryByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包领取记录总数
     */
    Integer getSimpleRedPacketHistoryTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改红包领取记录
     */
    Integer addRedPacketHistory(Map<String, Object> paramMap);

    /**
     * 修改红包领取记录
     */
    Integer updateRedPacketHistory(Map<String, Object> paramMap);

    /**
     * 删除红包领取记录
     */
    Integer deleteRedPacketHistory(Map<String, Object> paramMap);
}
