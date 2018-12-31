package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 订单Service
 * @author caihongwang
 */
public interface WX_OrderService {

    /**
     * 获取当前用户的订单信息
     */
    public ResultDTO getOrderByCondition(Map<String, Object> paramMap);

    /**
     * 购买商品
     */
    public ResultMapDTO purchaseProductInMiniProgram(Map<String, Object> paramMap) throws Exception;

    /**
     * 购买商品成功后的回调通知
     */
    public ResultMapDTO wxPayNotifyForPurchaseProductInMiniProgram(Map<String, Object> paramMap) throws Exception;

    /**
     * 买单
     */
    public ResultMapDTO payTheBillInMiniProgram(Map<String, Object> paramMap) throws Exception;

    /**
     * 买单成功后的回调通知
     */
    public ResultMapDTO wxPayNotifyForPayTheBillInMiniProgram(Map<String, Object> paramMap) throws Exception;

    /**
     * 根据条件查询订单信息
     */
    ResultDTO getSimpleOrderByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改订单信息
     */
    BoolDTO addOrder(Map<String, Object> paramMap);

    /**
     * 修改订单信息
     */
    BoolDTO updateOrder(Map<String, Object> paramMap);

    /**
     * 删除订单信息
     */
    BoolDTO deleteOrder(Map<String, Object> paramMap);
}
