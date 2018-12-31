package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.MessageDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_UserService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description 用户Handler
 * @author caihongwang
 */
public class WX_UserHandler implements com.br.newMall.api.service.WX_UserHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_UserHandler.class);

    @Autowired
    private WX_UserService userService;

    /**
     * 获取用户的基本信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getUserBaseInfo(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取用户的基本信息-getUserBaseInfo,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = userService.getUserBaseInfo(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在【hanlder】中获取用户的基本信息-getUserBaseInfo is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
        }
        logger.info("在【hanlder】中获取用户的基本信息-getUserBaseInfo,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 登录(首次微信授权，则使用openId创建用户)
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO login(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中登录(首次微信授权，则使用openId创建用户)-login,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = userService.login(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在【hanlder】中登录(首次微信授权，则使用openId创建用户)-login is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
        }
        logger.info("在【hanlder】中登录(首次微信授权，则使用openId创建用户)-login,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 更新用户信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateUser(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中更新用户信息-updateUser,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = userService.updateUser(objectParamMap);
        } catch (Exception e) {
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在【hanlder】中更新用户信息-updateUser is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
        }
        logger.info("在【hanlder】中更新用户信息-updateUser,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 检测用户会话是否过期
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO checkSession(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中检测用户会话是否过期-checkSession,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = userService.checkSession(objectParamMap);
        } catch (Exception e) {
            logger.error("在【hanlder】中检测用户会话是否过期-checkSession is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【hanlder】中检测用户会话是否过期-checkSession,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 校验手机验证码-[暂时未使用]
     */
    @Override
    public MessageDTO getCheckVerificationCode(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中校验手机验证码-getCheckVerificationCode,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        MessageDTO messageDTO = new MessageDTO();
        if (paramMap.size() > 0) {
            Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
            try {
                messageDTO = userService.getCheckVerificationCode(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中校验手机验证码-getCheckVerificationCode is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                messageDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                messageDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            messageDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            messageDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【hanlder】中获取校验手机验证码-getCheckVerificationCode,响应-messageDTO = {}", JSONObject.toJSONString(messageDTO));
        return messageDTO;
    }

    /**
     * 获取单一的用户信息
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleUserByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取单一的用户信息-getSimpleUserByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = userService.getSimpleUserByCondition(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在【hanlder】中获取单一的字典-getSimpleDicByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【hanlder】中获取单一的用户信息-getSimpleUserByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
