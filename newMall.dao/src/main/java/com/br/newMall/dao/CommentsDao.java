package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface CommentsDao {

    /**
     * 根据条件查询意见信息
     */
    List<Map<String, Object>> getSimpleCommentsByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询意见信息总数
     */
    Integer getSimpleCommentsTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改意见信息
     */
    Integer addComments(Map<String, Object> paramMap);

    /**
     * 修改意见信息
     */
    Integer updateComments(Map<String, Object> paramMap);

    /**
     * 删除意见信息
     */
    Integer deleteComments(Map<String, Object> paramMap);
}
