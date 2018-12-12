package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;

/**
 * 公共Handler
 */
public class CommonHandler implements com.br.newMall.api.service.CommonHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private CommonService commonService;

    /**
     * 在获取微信手机号时获取解密后手机号
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getDecryptPhone(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中在获取微信手机号时获取解密后手机号-getDecryptPhone,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String encData = paramMap.get("encData") != null ? paramMap.get("encData") : "";
        String iv = paramMap.get("iv") != null ? paramMap.get("iv") : "";
        String sessionKey = paramMap.get("sessionKey") != null ? paramMap.get("sessionKey") : "";
        try (Jedis jedis = jedisPool.getResource()) {
            String wxSessionKey = jedis.get(sessionKey);
            byte[] encrypData = Base64.decodeBase64(encData.getBytes());
            byte[] ivData = Base64.decodeBase64(iv.getBytes());
            byte[] wxSessionKeyData = Base64.decodeBase64(wxSessionKey.getBytes());
            String wxResultData = decrypt(wxSessionKeyData, ivData, encrypData);
            Map<String, String> jsonResultMap = JSONObject.parseObject(wxResultData, Map.class);
            String userPhone = jsonResultMap.get("phoneNumber");
            Map<String, String> resultMap = Maps.newHashMap();
            resultMap.put("userPhone", userPhone);
            resultMapDTO.setSuccess(false);
            resultMapDTO.setResultMap(resultMap);
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.DECRYPT_IS_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.DECRYPT_IS_ERROR.getMessage());
            logger.error("在hanlder中在获取微信手机号时获取解密后手机号-getDecryptPhone is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中在获取微信手机号时获取解密后手机号-getDecryptPhone,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData), "UTF-8");
    }

    /**
     * 发送公众号的模板消息
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendTemplateMessageForWxPublicNumber(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中发送公众号的模板消息-sendTemplateMessageForWxPublicNumber,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = commonService.sendTemplateMessageForWxPublicNumber(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中发送公众号的模板消息-sendTemplateMessageForWxPublicNumber is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中发送公众号的模板消息-sendTemplateMessageForWxPublicNumber,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 发送小程序名片的模板消息
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO sendTemplateMessageForMiniProgram(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中发送小程序名片的模板消息-sendTemplateMessageForMiniProgram,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = commonService.sendTemplateMessageForMiniProgram(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中发送小程序名片的模板消息-sendTemplateMessageForMiniProgram is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中发送小程序名片的模板消息-sendTemplateMessageForMiniProgram,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取小程序的openId和sessionKey
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getOpenIdAndSessionKeyForWX(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取openId和sessionKey-getOpenIdAndSessionKeyForWX,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = commonService.getOpenIdAndSessionKeyForWX(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取openId和sessionKey-getOpenIdAndSessionKeyForWX is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取openId和sessionKey-getOpenIdAndSessionKeyForWX,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    @Override
    public ResultMapDTO getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取SignatureAndJsapiTicketAndNonceStr-getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = commonService.getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber(objectParamMap);
            } catch (Exception e) {
                Map<String, String> resultMap = Maps.newHashMap();
                resultMapDTO.setResultListTotal(0);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取SignatureAndJsapiTicketAndNonceStr-getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取SignatureAndJsapiTicketAndNonceStr-getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO receviceAndSendCustomMessage(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息-receviceAndSendCustomMessage,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultMapDTO = commonService.receviceAndSendCustomMessage(objectParamMap);
            } catch (Exception e) {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息-receviceAndSendCustomMessage is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息-receviceAndSendCustomMessage,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }
}
