package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface WX_DicDao {

    /**
     * 根据条件查询字典信息
     */
    List<Map<String, Object>> getSimpleDicByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询字典信息总数
     */
    Integer getSimpleDicTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改字典信息
     */
    Integer addDic(Map<String, Object> paramMap);

    /**
     * 修改字典信息
     */
    Integer updateDic(Map<String, Object> paramMap);

    /**
     * 删除字典信息
     */
    Integer deleteDic(Map<String, Object> paramMap);
}
