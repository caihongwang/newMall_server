package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_LuckDrawService;
import com.br.newMall.center.service.WX_LuckDrawService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description 抽奖信息Handler
 * @author caihongwang
 */
public class WX_LuckDrawHandler implements com.br.newMall.api.service.WX_LuckDrawHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_LuckDrawHandler.class);

    @Autowired
    private WX_LuckDrawService wxLuckDrawService;

    /**
     * 获取抽奖的产品列表
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getLuckDrawProductList(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取抽奖的产品列表-getLuckDrawProductList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxLuckDrawService.getLuckDrawProductList(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中获取抽奖的产品列表-getLuckDrawProductList is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("在【hanlder】中获取抽奖的产品列表-getLuckDrawProductList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 添加抽奖信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO addLuckDraw(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中添加抽奖信息-addLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxLuckDrawService.addLuckDraw(objectParamMap);
            } catch (Exception e) {
                logger.error("在【handler】中添加抽奖信息-addLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【handler】中添加抽奖信息-addLuckDraw,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 删除抽奖信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteLuckDraw(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中删除抽奖信息-deleteLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxLuckDrawService.deleteLuckDraw(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中删除抽奖信息-deleteLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中删除抽奖信息-deleteLuckDraw,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改抽奖信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateLuckDraw(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中修改抽奖信息-updateLuckDraw,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxLuckDrawService.updateLuckDraw(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中修改抽奖信息-updateLuckDraw is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中修改抽奖信息-updateLuckDraw,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的抽奖信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleLuckDrawByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取单一的抽奖信息-getSimpleLuckDrawByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxLuckDrawService.getSimpleLuckDrawByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中获取单一的抽奖信息-getSimpleLuckDrawByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
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
        logger.info("在【hanlder】中获取单一的抽奖信息-getSimpleLuckDrawByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
    
}