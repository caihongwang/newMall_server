package com.br.newMall.dao;

import java.util.List;
import java.util.Map;

public interface OilStationDao {

    /**
     * 获取最大加油站编码
     */
    Map<String, Object> getMaxOilStationCode(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站信息信息
     */
    List<Map<String, Object>> getSimpleOilStationByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询加油站信息信息总数
     */
    Integer getSimpleOilStationTotalByCondition(Map<String, Object> paramMap);

    /**
     * 根据code查询加油站信息信息总数
     */
    Integer getSimpleOilStationTotalByCode(Map<String, Object> paramMap);

    /**
     * 添加或者修改加油站信息信息
     */
    Integer addOilStation(Map<String, Object> paramMap);

    /**
     * 修改加油站信息信息
     */
    Integer updateOilStation(Map<String, Object> paramMap);

    /**
     * 删除加油站信息信息
     */
    Integer deleteOilStation(Map<String, Object> paramMap);
}
