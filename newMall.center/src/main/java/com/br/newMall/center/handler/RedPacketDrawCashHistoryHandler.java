package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.RedPacketDrawCashHistoryService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 红包提现Handler
 */
public class RedPacketDrawCashHistoryHandler implements com.br.newMall.api.service.RedPacketDrawCashHistoryHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(RedPacketDrawCashHistoryHandler.class);

    @Autowired
    private RedPacketDrawCashHistoryService redPacketDrawCashHistoryService;


    @Override
    public ResultMapDTO getDrawCashMoneyTotal(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取已提现红包总额-getDrawCashMoneyTotal,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = redPacketDrawCashHistoryService.getDrawCashMoneyTotal(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取已提现红包总额-getDrawCashMoneyTotal is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取已提现红包总额-getDrawCashMoneyTotal,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    @Override
    public ResultDTO getRedPacketDrawCashHistory(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取红包提现记录-getRedPacketDrawCashHistory,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = redPacketDrawCashHistoryService.getRedPacketDrawCashHistory(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取红包提现记录-getRedPacketDrawCashHistory is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取红包提现记录-getRedPacketDrawCashHistory,响应-response:" + resultDTO);
        return resultDTO;
    }

}
