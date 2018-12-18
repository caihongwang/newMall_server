package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.OilStationOperatorService;
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
public class OilStationOperatorHandler implements com.br.newMall.api.service.OilStationOperatorHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(OilStationOperatorHandler.class);

    @Autowired
    private OilStationOperatorService oilStationOperatorService;

    @Override
    public ResultMapDTO cashOilStationOperatorRedPacket(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = oilStationOperatorService.cashOilStationOperatorRedPacket(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket is error, paramMap : " + paramMap + ", e : " + e);
                e.printStackTrace();
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
