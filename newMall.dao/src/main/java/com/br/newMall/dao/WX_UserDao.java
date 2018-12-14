package com.br.newMall.dao;


import java.util.List;
import java.util.Map;

public interface WX_UserDao {

    /**
     * 根据条件查询用户信息
     */
    List<Map<String, Object>> getSimpleUserByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询用户信息总数
     */
    Integer getSimpleUserTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改用户信息
     */
    Integer addUser(Map<String, Object> paramMap);

    /**
     * 修改用户信息
     */
    Integer updateUser(Map<String, Object> paramMap);

    /**
     * 删除用户信息
     */
    Integer deleteUser(Map<String, Object> paramMap);
}
