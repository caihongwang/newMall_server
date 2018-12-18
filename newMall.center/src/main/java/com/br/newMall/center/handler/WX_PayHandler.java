package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_PayService;
import com.br.newMall.center.utils.MapUtil;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 红包Handler
 */
public class WX_PayHandler implements com.br.newMall.api.service.WX_PayHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_PayHandler.class);

    @Autowired
    private WX_PayService wx_PayService;

    /**
     * 获取Oauth
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getOauth(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取oauth-getOauth,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_PayService.getOauth(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取oauth-getOauth is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取oauth-getOauth,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取Oauth的url
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getToOauthUrl(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取oauth的url-getToOauthUrl,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wx_PayService.getToOauthUrl(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取oauth的url-getToOauthUrl is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取oauth的url-getToOauthUrl,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 企业付款，直达微信零钱
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO unifiedOrderPay(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中统一订单支付请求-unifiedOrderPay,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wx_PayService.unifiedOrderPay(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中统一订单支付请求-unifiedOrderPay is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中统一订单支付请求-unifiedOrderPay,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }
}
