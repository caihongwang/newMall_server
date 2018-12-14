package com.br.newMall.center.service;

import com.br.newMall.api.dto.*;

import java.util.Map;

/**
 * 用户service
 */
public interface WX_UserService {

    /**
     * 添加用户
     *
     * @param paramMap
     * @return
     */
    ResultMapDTO login(Map<String, Object> paramMap);

    /**
     * 设置用户的session
     */
    public BoolDTO setSession(Map<String, Object> paramMap);

    /**
     * 检测用户会话是否过期
     *
     * @param paramMap
     * @return
     */
    BoolDTO checkSession(Map<String, Object> paramMap);

    /**
     * 删除用户
     *
     * @param paramMap
     * @return
     */
    BoolDTO deleteUser(Map<String, Object> paramMap);

    /**
     * 更新用户
     *
     * @param paramMap
     * @return
     */
    BoolDTO updateUser(Map<String, Object> paramMap);

    /**
     * 获取单一的用户信息
     *
     * @param paramMap
     * @return
     */
    ResultDTO getSimpleUserByCondition(Map<String, Object> paramMap);

    /**
     * 根据手机号校验验证码是否正确
     */
    MessageDTO getCheckVerificationCode(Map<String, Object> paramMap);
}
