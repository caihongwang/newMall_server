package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface DicService {

    /**
     * 根据条件查询字典信息
     */
    ResultDTO getSimpleDicByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询字典信息(支持同时查询多个字典)
     */
    ResultMapDTO getMoreDicByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改字典信息
     */
    BoolDTO addDic(Map<String, Object> paramMap);

    /**
     * 修改字典信息
     */
    BoolDTO updateDic(Map<String, Object> paramMap);

    /**
     * 删除字典信息
     */
    BoolDTO deleteDic(Map<String, Object> paramMap);
}
