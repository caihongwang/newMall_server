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
     * 获取所有的点餐订单
     */
    public ResultDTO getAllFoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取待支付的点餐订单
     */
    public ResultDTO getWaitPayFoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取已支付的点餐订单
     */
    public ResultDTO getAlreadyPayFoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取点餐订单详情
     */
    public ResultMapDTO getFoodsOrderDetailById(Map<String, Object> paramMap);

    /**
     * 获取所有的商品订单
     */
    public ResultDTO getAllPayGoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取待支付的商品订单
     */
    public ResultDTO getWaitPayGoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取已支付的商品订单
     */
    public ResultDTO getAlreadyPayGoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取已发货的商品订单
     */
    public ResultDTO getAlreadyDeliverGoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取已完成的商品订单
     */
    public ResultDTO getCompletedGoodsOrder(Map<String, Object> paramMap);

    /**
     * 对商品订单进行确认收货
     */
    public BoolDTO confirmReceiptGoodsOrder(Map<String, Object> paramMap);

    /**
     * 获取商品订单详情
     * @param paramMap
     * @return
     */
    public ResultMapDTO getGoodsOrderDetailById(Map<String, Object> paramMap);

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
