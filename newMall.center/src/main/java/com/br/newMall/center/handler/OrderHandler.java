package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.OrderService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 订单Handler
 */
public class OrderHandler implements com.br.newMall.api.service.OrderHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @Autowired
    private OrderService orderService;

    @Override
    public ResultMapDTO requestWxPayUnifiedOrder(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中使用统一订单的方式进行下订单，发起微信支付-wxPayUnifiedOrder,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = orderService.requestWxPayUnifiedOrder(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中使用统一订单的方式进行下订单，发起微信支付-wxPayUnifiedOrder is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中使用统一订单的方式进行下订单，发起微信支付-wxPayUnifiedOrder,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
