package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface CustomMessageHistoryDao {

    /**
     * 根据条件查询客服消息历史记录信息
     */
    List<Map<String, Object>> getSimpleCustomMessageHistoryByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询客服消息历史记录信息总数
     */
    Integer getSimpleCustomMessageHistoryTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改客服消息历史记录信息
     */
    Integer addCustomMessageHistory(Map<String, Object> paramMap);

    /**
     * 修改客服消息历史记录信息
     */
    Integer updateCustomMessageHistory(Map<String, Object> paramMap);

    /**
     * 删除客服消息历史记录信息
     */
    Integer deleteCustomMessageHistory(Map<String, Object> paramMap);
}
