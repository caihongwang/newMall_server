package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface RedPacketDrawCashHistoryService {

    /**
     * 获取已提现红包总额
     */
    public ResultMapDTO getDrawCashMoneyTotal(Map<String, Object> paramMap);

    /**
     * 获取红包提现历史记录
     */
    public ResultDTO getRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包提现记录
     */
    ResultDTO getSimpleRedPacketDrawCashHistoryByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改红包提现记录
     */
    BoolDTO addRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 修改红包提现记录
     */
    BoolDTO updateRedPacketDrawCashHistory(Map<String, Object> paramMap);

    /**
     * 删除红包提现记录
     */
    BoolDTO deleteRedPacketDrawCashHistory(Map<String, Object> paramMap);
}
