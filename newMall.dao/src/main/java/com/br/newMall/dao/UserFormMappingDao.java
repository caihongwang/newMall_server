package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface UserFormMappingDao {

    /**
     * 获取用户对应的formId(用户在微信中给制定用户发送模板消息)
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getSimpleUserFormMappingByCondition(Map<String, Object> paramMap);

    Integer getSimpleUserFormMappingTotalByCondition(Map<String, Object> paramMap);

    /**
     * 新增用户对应的formId(用户在微信中给制定用户发送模板消息)
     */
    Integer addUserFormMapping(Map<String, Object> paramMap);

    /**
     * 更新用户对应的formId(用户在微信中给制定用户发送模板消息)
     */
    Integer updateUserFormMapping(Map<String, Object> paramMap);

    /**
     * 删除用户对应的formId(用户在微信中给制定用户发送模板消息)
     */
    Integer deleteUserFormMapping(Map<String, Object> paramMap);

}
