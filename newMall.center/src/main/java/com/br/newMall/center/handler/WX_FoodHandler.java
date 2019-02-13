package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_FoodService;
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
 * @Description 食物Handler
 * @author caihongwang
 */
public class WX_FoodHandler implements com.br.newMall.api.service.WX_FoodHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_FoodHandler.class);

    @Autowired
    private WX_FoodService wxFoodService;

    /**
     * 添加食物
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO addFood(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】添加食物-addFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = wxFoodService.addFood(objectParamMap);
            } catch (Exception e) {
                logger.error("【handler】添加食物-addFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【handler】添加食物-addFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除食物
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteFood(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】删除食物-deleteFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxFoodService.deleteFood(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】删除食物-deleteFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】删除食物-deleteFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改食物
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateFood(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【handler】修改食物-updateFood,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxFoodService.updateFood(objectParamMap);
        } catch (Exception e) {
            logger.error("【handler】修改食物-updateFood is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("【handler】修改食物-updateFood,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的食物
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleFoodByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取单一的食物-getSimpleFoodByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxFoodService.getSimpleFoodByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取单一的食物-getSimpleFoodByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("【hanlder】获取单一的食物-getSimpleFoodByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }


    /**
     * 获取菜单食物列表
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getMenuByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("【hanlder】获取菜单食物列表-getMenuByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxFoodService.getMenuByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("【hanlder】获取菜单食物列表-getMenuByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("【hanlder】获取菜单食物列表-getMenuByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }
    
}
