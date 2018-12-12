package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.MessageDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.UserService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 用户Handler
 */
public class UserHandler implements com.br.newMall.api.service.UserHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    private UserService userService;

    /**
     * 更新用户信息
     */
    @Override
    public BoolDTO updateUser(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中更新用户信息-updateUser,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = userService.updateUser(objectParamMap);
        } catch (Exception e) {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中更新用户信息-updateUser is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中更新用户信息-updateUser,响应-response:" + boolDTO);
        return boolDTO;
    }

    /**
     * 添加用户：第一次获得微信授权，则使用openId创建用户
     */
    @Override
    public ResultMapDTO login(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中添加用户-login,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = userService.login(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中添加用户-login is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中添加用户-login,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    @Override
    public BoolDTO checkSession(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中检测用户会话是否过期-checkSession,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = userService.checkSession(objectParamMap);
        } catch (Exception e) {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中检测用户会话是否过期-checkSession is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中检测用户会话是否过期-checkSession,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public ResultDTO getSimpleUserByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取单一的用户信息-getSimpleUserByCondition,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = userService.getSimpleUserByCondition(objectParamMap);
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
        logger.info("在hanlder中获取单一的用户信息-getSimpleUserByCondition,响应-response:" + resultDTO);
        return resultDTO;
    }

    /**
     * 根据手机号校验验证码是否正确,并将这个手机号的创建用户
     */
    @Override
    public MessageDTO getCheckVerificationCode(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中校验手机验证码-getCheckVerificationCode,请求-paramMap:" + paramMap);
        MessageDTO messageDTO = new MessageDTO();
        if (paramMap.size() > 0) {
            Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
            try {
                messageDTO = userService.getCheckVerificationCode(objectParamMap);
            } catch (Exception e) {
                messageDTO.setSuccess(false);
                messageDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                messageDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中校验手机验证码-getCheckVerificationCode is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            messageDTO.setSuccess(false);
            messageDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            messageDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取校验手机验证码-getCheckVerificationCode,响应-response:" + messageDTO);
        return messageDTO;
    }

}
