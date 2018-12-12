package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.DicService;
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
 * 字典Handler
 */
public class DicHandler implements com.br.newMall.api.service.DicHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(DicHandler.class);

    @Autowired
    private DicService dicService;

    @Override
    public BoolDTO addDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中添加字典-addDic,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = dicService.addDic(objectParamMap);
            } catch (Exception e) {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中添加字典-addDic is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中添加字典-addDic,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public BoolDTO deleteDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中删除字典-deleteDic,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = dicService.deleteDic(objectParamMap);
        } catch (Exception e) {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中删除字典-deleteDic is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中删除字典-deleteDic,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public BoolDTO updateDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中修改字典-updateDic,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = dicService.updateDic(objectParamMap);
        } catch (Exception e) {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中修改字典-updateDic is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中修改字典-updateDic,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public ResultDTO getSimpleDicByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取单一的字典-getSimpleDicByCondition,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = dicService.getSimpleDicByCondition(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取单一的字典-getSimpleDicByCondition is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取单一的字典-getSimpleDicByCondition,响应-response:" + resultDTO);
        return resultDTO;
    }

    @Override
    public ResultMapDTO getMoreDicByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取多个的字典-getMoreDicByCondition,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = dicService.getMoreDicByCondition(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取多个的字典-getMoreDicByCondition is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取多个的字典-getMoreDicByCondition,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
