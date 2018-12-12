package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.CommentsService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.CommentsDao;
import com.br.newMall.dao.UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 意见service
 */
@Service
public class CommentsServiceImpl implements CommentsService {

    private static final Logger logger = LoggerFactory.getLogger(CommentsServiceImpl.class);

    @Autowired
    private CommentsDao commentsDao;

    @Autowired
    private UserDao userDao;

    /**
     * 添加意见
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addComments(Map<String, Object> paramMap) {
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String comments = paramMap.get("comments") != null ? paramMap.get("comments").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(comments) && !"".equals(uid)) {
            if (comments.length() <= 200) {
                Map<String, Object> paramMap_temp = Maps.newHashMap();
                paramMap_temp.put("id", uid);
                List<Map<String, Object>> userList = userDao.getSimpleUserByCondition(paramMap_temp);
                if (userList != null && userList.size() > 0) {
                    paramMap.put("status", 0);
                    addNum = commentsDao.addComments(paramMap);
                    if (addNum != null && addNum > 0) {
                        boolDTO.setSuccess(true);
                        boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                        boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                    } else {
                        boolDTO.setSuccess(false);
                        boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                        boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                    }
                } else {
                    boolDTO.setSuccess(false);
                    boolDTO.setCode(NewMallCode.USER_IS_NULL.getNo());
                    boolDTO.setMessage(NewMallCode.USER_IS_NULL.getMessage());
                }
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.COMMENTS_NOT_MORE_200.getNo());
                boolDTO.setMessage(NewMallCode.COMMENTS_NOT_MORE_200.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.UID_COMMENT_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.UID_COMMENT_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加意见-addComments,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 删除意见
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteComments(Map<String, Object> paramMap) {
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = commentsDao.deleteComments(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.PHONE_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PHONE_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中删除意见-deleteComments,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 更新意见
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateComments(Map<String, Object> paramMap) {
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = commentsDao.updateComments(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.UID_COMMENT_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.UID_COMMENT_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中更新意见-updateComments,结果-result:" + boolDTO);
        return boolDTO;
    }


    /**
     * 获取单一的意见信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleCommentsByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> commentsStrList = Lists.newArrayList();
        List<Map<String, Object>> commentsList = commentsDao.getSimpleCommentsByCondition(paramMap);
        if (commentsList != null && commentsList.size() > 0) {
            commentsStrList = MapUtil.getStringMapList(commentsList);
            Integer total = commentsDao.getSimpleCommentsTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(commentsStrList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.COMMENTS_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.COMMENTS_LIST_IS_NULL.getMessage());
        }
        logger.info("在service中获取单一的意见信息-getSimpleCommentsByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

}
