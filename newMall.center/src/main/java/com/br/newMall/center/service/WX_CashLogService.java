package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 提现日志Service
 * @author caihongwang
 */
public interface WX_CashLogService {
    /**
     * 获取提现规则列表
     * @param paramMap
     * @return
     */
    ResultDTO getCashFeeList(Map<String, Object> paramMap);

    /**
     * 提现用户余额到微信零钱
     * @param paramMap
     * @return
     */
    ResultMapDTO cashBalanceToWx(Map<String, Object> paramMap);

    /**
     * 根据条件查询提现日志信息
     */
    ResultDTO getSimpleCashLogByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改提现日志信息
     */
    BoolDTO addCashLog(Map<String, Object> paramMap);

    /**
     * 修改提现日志信息
     */
    BoolDTO updateCashLog(Map<String, Object> paramMap);

    /**
     * 删除提现日志信息
     */
    BoolDTO deleteCashLog(Map<String, Object> paramMap);
}
