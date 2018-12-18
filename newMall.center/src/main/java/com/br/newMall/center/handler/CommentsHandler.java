package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.CommentsService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 意见Handler
 */
public class CommentsHandler implements com.br.newMall.api.service.CommentsHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(CommentsHandler.class);

    @Autowired
    private CommentsService commentsService;

    @Override
    public BoolDTO addComments(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中添加意见-addComments,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = commentsService.addComments(objectParamMap);
        } catch (Exception e) {
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中添加意见-addComments is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中添加意见-addComments,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public BoolDTO deleteComments(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中删除意见-deleteComments,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = commentsService.deleteComments(objectParamMap);
        } catch (Exception e) {
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中删除意见-deleteComments is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中删除意见-deleteComments,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public BoolDTO updateComments(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中修改意见-updateComments,请求-paramMap:" + paramMap);
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = commentsService.updateComments(objectParamMap);
        } catch (Exception e) {
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中修改意见-updateComments is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中修改意见-updateComments,响应-response:" + boolDTO);
        return boolDTO;
    }

    @Override
    public ResultDTO getSimpleCommentsByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取单一的意见-getSimpleCommentsByCondition,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultDTO = commentsService.getSimpleCommentsByCondition(objectParamMap);
        } catch (Exception e) {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取单一的意见-getSimpleCommentsByCondition is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取单一的意见-getSimpleCommentsByCondition,响应-response:" + resultDTO);
        return resultDTO;
    }

}
