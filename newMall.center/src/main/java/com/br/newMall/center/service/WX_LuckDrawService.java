package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 抽奖Service
 * @author caihongwang
 */
public interface WX_LuckDrawService {

    /**
     * 根据抽奖的产品列表
     */
    ResultDTO getLuckDrawProductList(Map<String, Object> paramMap);

    /**
     * 根据条件查询抽奖信息信息
     */
    ResultDTO getSimpleLuckDrawByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改抽奖信息信息
     */
    ResultMapDTO addLuckDraw(Map<String, Object> paramMap);

    /**
     * 修改抽奖信息信息
     */
    BoolDTO updateLuckDraw(Map<String, Object> paramMap);

    /**
     * 删除抽奖信息信息
     */
    BoolDTO deleteLuckDraw(Map<String, Object> paramMap);
}
