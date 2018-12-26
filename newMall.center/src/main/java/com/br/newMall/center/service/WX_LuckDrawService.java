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
     * 获取参加过抽奖的商家列表
     * @param paramMap
     * @return
     */
    ResultDTO getAllLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception;

    /**
     * 获取已领取奖励的商家列表
     * @param paramMap
     * @return
     */
    ResultDTO getRecevicedLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception;

    /**
     * 获取待领取奖励的商家列表
     * @param paramMap
     * @return
     */
    ResultDTO getWaitLuckDrawShopByCondition(Map<String, Object> paramMap) throws Exception;

    /**
     * 获取某商家下所有参与过领取奖励的队列
     */
    ResultMapDTO getAllLuckDrawRankByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下已领取奖励的队列
     */
    ResultMapDTO getRecevicedLuckDrawRankByCondition(Map<String, Object> paramMap);

    /**
     * 获取某商家下待领取奖励的队列
     */
    ResultMapDTO getWaitLuckDrawRankByCondition(Map<String, Object> paramMap);

    /**
     * 获取抽奖的产品列表
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
