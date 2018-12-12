package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_CustomMenuService;
import com.br.newMall.center.utils.MapUtil;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 自定义菜单Handler
 */
public class WX_CustomMenuHandler implements com.br.newMall.api.service.WX_CustomMenuHandler.Iface{

    private static final Logger logger = LoggerFactory.getLogger(WX_CustomMenuHandler.class);

    @Autowired
    private WX_CustomMenuService wx_CustomMenuService;

    /**
     * 获取公众号自定义菜单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getCustomMenu(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取公众号自定义菜单-getCustomMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.createCustomMenu(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取公众号自定义菜单-getCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取公众号自定义菜单-getCustomMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 创建公众号自定义菜单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO createCustomMenu(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中创建公众号自定义菜单-createCustomMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.createCustomMenu(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中创建公众号自定义菜单-createCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中创建公众号自定义菜单-createCustomMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 删除公众号自定义菜单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO deleteCustomMenu(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中删除公众号自定义菜单-deleteCustomMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.deleteCustomMenu(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中删除公众号自定义菜单-deleteCustomMenu is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中删除公众号自定义菜单-deleteCustomMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 创建公众号个性化菜单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO createPersonalMenu(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中创建公众号个性化菜单-createPersonalMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.createPersonalMenu(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中创建公众号个性化菜单-createPersonalMenu is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中创建公众号个性化菜单-createPersonalMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 删除公众号个性化菜单
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO deletePersonalMenu(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中删除公众号个性化菜单-deletePersonalMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.deletePersonalMenu(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中删除公众号个性化菜单-deletePersonalMenu is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中删除公众号个性化菜单-deletePersonalMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取公众号自定义菜单配置接口
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO getCurrentSelfMenuInfo(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_CustomMenuService.getCurrentSelfMenuInfo(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
