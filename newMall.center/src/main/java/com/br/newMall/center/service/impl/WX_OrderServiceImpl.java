package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.WX_OrderService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_OrderDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 订单Service
 * @author caihongwang
 */
@Service
public class WX_OrderServiceImpl implements WX_OrderService {

    private static final Logger logger = LoggerFactory.getLogger(WX_OrderServiceImpl.class);

    @Autowired
    private WX_OrderDao wxOrderDao;

    /**
     * 添加订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中添加订单-addOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String payMoney = paramMap.get("payMoney") != null ? paramMap.get("payMoney").toString() : "";
        if (!"".equals(uid) && !"".equals(payMoney)) {
            addNum = wxOrderDao.addOrder(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.ORDER_UID_OR_PAYMONEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_UID_OR_PAYMONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中添加订单-addOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中删除订单-deleteOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxOrderDao.deleteOrder(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.ORDER_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中删除订单-deleteOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改订单
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateOrder(Map<String, Object> paramMap) {
        logger.info("在【service】中修改订单-updateOrder,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxOrderDao.updateOrder(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.ORDER_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.ORDER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中修改订单-updateOrder,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的订单信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleOrderByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的订单-getSimpleOrderByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> productStrList = Lists.newArrayList();
        List<Map<String, Object>> productList = wxOrderDao.getSimpleOrderByCondition(paramMap);
        if (productList != null && productList.size() > 0) {
            productStrList = MapUtil.getStringMapList(productList);
            Integer total = wxOrderDao.getSimpleOrderTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(productStrList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.ORDER_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.ORDER_LIST_IS_NULL.getMessage());
        }
        logger.info("在【service】中获取单一的订单-getSimpleOrderByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
