package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 加盟Dao
 * @author caihongwang
 */
public interface WX_LeagueDao {

    /**
     * 根据条件查询加盟信息
     */
    List<Map<String, Object>> getSimpleLeagueByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询加盟信息总数
     */
    Integer getSimpleLeagueTotalByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改加盟信息
     */
    Integer addLeague(Map<String, Object> paramMap);

    /**
     * 修改加盟信息
     */
    Integer updateLeague(Map<String, Object> paramMap);

    /**
     * 删除加盟信息
     */
    Integer deleteLeague(Map<String, Object> paramMap);
}
