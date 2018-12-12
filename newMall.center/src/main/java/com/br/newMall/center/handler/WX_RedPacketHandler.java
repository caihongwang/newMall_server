package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.MapUtil;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 红包Handler
 */
public class WX_RedPacketHandler implements com.br.newMall.api.service.WX_RedPacketHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_RedPacketHandler.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private WX_RedPacketService wx_RedPacketService;

    /**
     * 获取红包二维码
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getRedPacketQrCode(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取红包二维码-getRedPacketQrCode,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_RedPacketService.getRedPacketQrCode(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取红包二维码-getRedPacketQrCode is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取红包二维码-getRedPacketQrCode,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

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
            resultMapDTO = wx_RedPacketService.getOauth(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
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
                resultMapDTO = wx_RedPacketService.getToOauthUrl(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取oauth的url-getToOauthUrl is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
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
    public ResultMapDTO enterprisePayment(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中企业付款，直达微信零钱-enterprisePayment,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wx_RedPacketService.enterprisePayment(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中企业付款，直达微信零钱-enterprisePayment is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中企业付款，直达微信零钱-enterprisePayment,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送普通红包
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendRedPacket(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中发送普通红包-sendRedPacket,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wx_RedPacketService.sendRedPacket(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中发送普通红包-sendRedPacket is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中发送普通红包-sendRedPacket,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送分裂红包
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendGroupRedPacket(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中发送分裂红包-sendGroupRedPacket,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = wx_RedPacketService.sendGroupRedPacket(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中发送分裂红包-sendGroupRedPacket is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中发送分裂红包-sendGroupRedPacket,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }
}
