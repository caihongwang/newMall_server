package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_ShopService;
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
 * @Description 店铺Handler
 * @author caihongwang
 */
public class WX_ShopHandler implements com.br.newMall.api.service.WX_ShopHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopHandler.class);

    @Autowired
    private WX_ShopService wxShopService;

    /**
     * 获取订单排序类型
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getOrderSortTypeList(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取订单排序类型-getProductTypeList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxShopService.getOrderSortTypeList(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取订单排序类型-getOrderSortTypeList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("【hanlder】获取订单排序类型-getOrderSortTypeList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 添加店铺
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO addShop(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】添加店铺-addShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = wxShopService.addShop(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】添加店铺-addShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】添加店铺-addShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除店铺
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteShop(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】删除店铺-deleteShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxShopService.deleteShop(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】删除店铺-deleteShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】删除店铺-deleteShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改店铺
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateShop(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】修改店铺-updateShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxShopService.updateShop(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】修改店铺-updateShop is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】修改店铺-updateShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的店铺
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleShopByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxShopService.getSimpleShopByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取单一的店铺-getSimpleShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("【hanlder】获取单一的店铺-getSimpleShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据条件查询店铺相关信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getShopByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】根据条件查询店铺相关信息-getShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxShopService.getShopByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】根据条件查询店铺相关信息-getShopByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("【hanlder】根据条件查询店铺相关信息-getShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getMiniProgramCode(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxShopService.getMiniProgramCode(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【hanlder】根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序码-getMiniProgramCode,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
