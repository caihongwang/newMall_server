package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
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
 * @Description 字典Handler
 * @author caihongwang
 */
public class WX_DicHandler implements com.br.newMall.api.service.WX_DicHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_DicHandler.class);

    @Autowired
    private WX_DicService wxDicService;

    /**
     * 添加字典
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO addDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中添加字典-addDic,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = wxDicService.addDic(objectParamMap);
            } catch (Exception e) {
                logger.error("在【handler】中添加字典-addDic is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【handler】中添加字典-addDic,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除字典
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中删除字典-deleteDic,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxDicService.deleteDic(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中删除字典-deleteDic is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中删除字典-deleteDic,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改字典
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateDic(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中修改字典-updateDic,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxDicService.updateDic(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中修改字典-updateDic is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中修改字典-updateDic,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的字典
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleDicByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取单一的字典-getSimpleDicByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxDicService.getSimpleDicByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中获取单一的字典-getSimpleDicByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【hanlder】中获取单一的字典-getSimpleDicByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 获取多个的字典
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getMoreDicByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取多个的字典-getMoreDicByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wxDicService.getMoreDicByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中获取多个的字典-getMoreDicByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【hanlder】中获取多个的字典-getMoreDicByCondition,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
