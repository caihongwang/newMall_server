package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;

import java.util.List;
import java.util.Map;

public interface CommentsService {

    /**
     * 根据条件查询意见信息
     */
    ResultDTO getSimpleCommentsByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改意见信息
     */
    BoolDTO addComments(Map<String, Object> paramMap);

    /**
     * 修改意见信息
     */
    BoolDTO updateComments(Map<String, Object> paramMap);

    /**
     * 删除意见信息
     */
    BoolDTO deleteComments(Map<String, Object> paramMap);
}
