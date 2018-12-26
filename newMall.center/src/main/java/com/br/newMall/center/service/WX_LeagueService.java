package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 加盟Service
 * @author caihongwang
 */
public interface WX_LeagueService {

    /**
     * 获取加盟类型列表
     */
    ResultDTO getLeagueTypeList(Map<String, Object> paramMap);

    /**
     * 根据条件查询加盟信息
     */
    ResultDTO getSimpleLeagueByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改加盟信息
     */
    BoolDTO addLeague(Map<String, Object> paramMap);

    /**
     * 修改加盟信息
     */
    BoolDTO updateLeague(Map<String, Object> paramMap);

    /**
     * 删除加盟信息
     */
    BoolDTO deleteLeague(Map<String, Object> paramMap);
}
