package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;

import java.util.Map;

/**
 * @Description 余额日志Service
 * @author caihongwang
 */
public interface WX_BalanceLogService {

    /**
     * 根据条件查询余额日志信息
     */
    ResultDTO getSimpleBalanceLogByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改余额日志信息
     */
    BoolDTO addBalanceLog(Map<String, Object> paramMap);

    /**
     * 修改余额日志信息
     */
    BoolDTO updateBalanceLog(Map<String, Object> paramMap);

    /**
     * 删除余额日志信息
     */
    BoolDTO deleteBalanceLog(Map<String, Object> paramMap);
}
