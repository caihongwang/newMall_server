package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_OrderService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description 订单Handler
 * @author caihongwang
 */
public class WX_OrderHandler implements com.br.newMall.api.service.WX_OrderHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_OrderHandler.class);

    @Autowired
    private WX_OrderService wxOrderService;

    /**
     * 购买商品
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO purchaseProductInMiniProgram(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】购买商品-purchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxOrderService.purchaseProductInMiniProgram(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】购买商品-purchaseProductInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】购买商品-purchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 购买商品成功后的回调通知
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO wxPayNotifyForPurchaseProductInMiniProgram(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxOrderService.wxPayNotifyForPurchaseProductInMiniProgram(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】购买商品成功后的回调通知-wxPayNotifyForPurchaseProductInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 买单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO payTheBillInMiniProgram(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】买单-payTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxOrderService.payTheBillInMiniProgram(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】买单-payTheBillInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】买单-payTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 买单成功后的回调通知
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO wxPayNotifyForPayTheBillInMiniProgram(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxOrderService.wxPayNotifyForPayTheBillInMiniProgram(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】买单成功后的回调通知-wxPayNotifyForPayTheBillInMiniProgram,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 添加订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO addOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = wxOrderService.addOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】添加订单-addOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】添加订单-addOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】删除订单-deleteOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxOrderService.deleteOrder(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】删除订单-deleteOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】删除订单-deleteOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】修改订单-updateOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxOrderService.updateOrder(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】修改订单-updateOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】修改订单-updateOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleOrderByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取单一的订单-getSimpleOrderByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getSimpleOrderByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取单一的订单-getSimpleOrderByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取单一的订单-getSimpleOrderByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取当前用户的订单信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getOrderByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取当前用户的订单信息-getOrderByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getOrderByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取当前用户的订单信息-getOrderByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取当前用户的订单信息-getOrderByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取所有的商品订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getAllPayGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取所有的商品订单-getAllPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getAllPayGoodsOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取所有的商品订单-getAllPayGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取所有的商品订单-getAllPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取待支付的商品订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getWaitPayGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取待支付的商品订单-getWaitPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getWaitPayGoodsOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取待支付的商品订单-getWaitPayGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取待支付的商品订单-getWaitPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已支付的商品订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getAlreadyPayGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取已支付的商品订单-getAlreadyPayGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getAlreadyPayGoodsOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取已支付的商品订单-getAlreadyPayGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取已支付的商品订单-getAlreadyPayGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已发货的商品订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getAlreadyDeliverGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取已发货的商品订单-getAlreadyDeliverGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getAlreadyDeliverGoodsOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取已发货的商品订单-getAlreadyDeliverGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取已发货的商品订单-getAlreadyDeliverGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取已完成的商品订单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getCompletedGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取已完成的商品订单-getCompletedGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxOrderService.getCompletedGoodsOrder(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取已完成的商品订单-getCompletedGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取已完成的商品订单-getCompletedGoodsOrder,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }


    /**
     * 对商品订单进行确认收货
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO confirmReceiptGoodsOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】对商品订单进行确认收货-confirmReceiptGoodsOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxOrderService.confirmReceiptGoodsOrder(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】对商品订单进行确认收货-confirmReceiptGoodsOrder is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】对商品订单进行确认收货-confirmReceiptGoodsOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取商品订单详情
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getGoodsOrderDetailById(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取商品订单详情-getGoodsOrderDetailById,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxOrderService.getGoodsOrderDetailById(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取商品订单详情-getGoodsOrderDetailById is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】获取商品订单详情-getGoodsOrderDetailById,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }
}
