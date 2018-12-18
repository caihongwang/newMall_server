package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.OilStationService;
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
 * 加油站Handler
 */
public class OilStationHandler implements com.br.newMall.api.service.OilStationHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(OilStationHandler.class);

    @Autowired
    private OilStationService oilStationService;

    @Override
    public BoolDTO addOrUpdateOilStation(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中添加或者更新加油站-addOrUpdateOilStation,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = oilStationService.addOrUpdateOilStation(objectParamMap);
            } catch (Exception e) {
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中添加或者更新加油站-addOrUpdateOilStation is addOilStation, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中添加或者更新加油站-addOrUpdateOilStation,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public ResultDTO getOilStationList(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取加油站列表-getOilStationList,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = oilStationService.getOilStationList(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取加油站列表-getOilStationList is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取加油站列表-getOilStationList,响应-response:" + resultDTO);
        return resultDTO;
    }

    @Override
    public ResultMapDTO getOneOilStationByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取单一加油站信息-getSimpleOilStationByCondition,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = oilStationService.getOneOilStationByCondition(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取单一加油站信息-getSimpleOilStationByCondition is error, paramMap : " + paramMap + ", e : " + e);
                e.printStackTrace();
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取单一加油站信息-getSimpleOilStationByCondition,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    @Override
    public ResultDTO getOilStationByLonLat(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中根据经纬度地址获取所处的加油站-getOilStationByLonLat,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = oilStationService.getOilStationByLonLat(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中根据经纬度地址获取所处的加油站-getOilStationByLonLat is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中根据经纬度地址获取所处的加油站-getOilStationByLonLat,响应-response:" + resultDTO);
        return resultDTO;
    }
}
