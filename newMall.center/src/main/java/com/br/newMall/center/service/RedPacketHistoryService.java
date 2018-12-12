package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface RedPacketHistoryService {

    /**
     * 获取已领取红包总额
     */
    ResultMapDTO getAllRedPacketMoneyTotal(Map<String, Object> paramMap);

    /**
     * 红包领取记录
     */
    ResultDTO getRedPacketHistoryList(Map<String, Object> paramMap);

    /**
     * 根据条件查询红包领取记录
     */
    ResultDTO getSimpleRedPacketHistoryByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改红包领取记录
     */
    BoolDTO addRedPacketHistory(Map<String, Object> paramMap);

    /**
     * 修改红包领取记录
     */
    BoolDTO updateRedPacketHistory(Map<String, Object> paramMap);

    /**
     * 删除红包领取记录
     */
    BoolDTO deleteRedPacketHistory(Map<String, Object> paramMap);
}
