package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.WX_IntegralLogService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_IntegralLogDao;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 积分日志Service
 * @author caihongwang
 */
@Service
public class WX_IntegralLogServiceImpl implements WX_IntegralLogService {

    private static final Logger logger = LoggerFactory.getLogger(WX_IntegralLogServiceImpl.class);

    @Autowired
    private WX_IntegralLogDao wxIntegralLogDao;
    
    /**
     * 添加积分日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addIntegralLog(Map<String, Object> paramMap) {
        logger.info("【service】添加积分日志-addIntegralLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String exchangeToUserIntegral = paramMap.get("exchangeToUserIntegral") != null ? paramMap.get("exchangeToUserIntegral").toString() : "";
        String userIntegral = paramMap.get("userIntegral") != null ? paramMap.get("userIntegral").toString() : "";
        if (!"".equals(uid) && !"".equals(exchangeToUserIntegral)
                && !"".equals(userIntegral)) {
            addNum = wxIntegralLogDao.addIntegralLog(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.INTEGRALLOG_UID_OR_EXCHANGETOUSERINTEGRAL_OR_USERINTEGRAL_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.INTEGRALLOG_UID_OR_EXCHANGETOUSERINTEGRAL_OR_USERINTEGRAL_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】添加积分日志-addIntegralLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除积分日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteIntegralLog(Map<String, Object> paramMap) {
        logger.info("【service】删除积分日志-deleteIntegralLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxIntegralLogDao.deleteIntegralLog(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.INTEGRALLOG_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.INTEGRALLOG_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】删除积分日志-deleteIntegralLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改积分日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateIntegralLog(Map<String, Object> paramMap) {
        logger.info("【service】修改积分日志-updateIntegralLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxIntegralLogDao.updateIntegralLog(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.INTEGRALLOG_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.INTEGRALLOG_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改积分日志-updateIntegralLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的积分日志信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleIntegralLogByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的积分日志-getSimpleIntegralLogByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> cashLogStrList = Lists.newArrayList();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(uid)) {
            List<Map<String, Object>> cashLogList = wxIntegralLogDao.getSimpleIntegralLogByCondition(paramMap);
            if (cashLogList != null && cashLogList.size() > 0) {
                cashLogStrList = MapUtil.getStringMapList(cashLogList);
                Integer total = wxIntegralLogDao.getSimpleIntegralLogTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(cashLogStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.INTEGRALLOG_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.INTEGRALLOG_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.INTEGRALLOG_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.INTEGRALLOG_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取单一的积分日志-getSimpleIntegralLogByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
