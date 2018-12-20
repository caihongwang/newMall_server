package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 店铺Service
 * @author caihongwang
 */
public interface WX_ShopService {


    /**
     * 根据用户uid或者微信昵称或者店铺昵称创建其店铺的小程序吗
     */
    ResultMapDTO getMiniProgramCode(Map<String, Object> paramMap);


    /**
     * 根据条件查询店铺相关信息
     */
    ResultDTO getShopByCondition(Map<String, Object> paramMap);

    /**
     * 根据条件查询店铺信息
     */
    ResultDTO getSimpleShopByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改店铺信息
     */
    BoolDTO addShop(Map<String, Object> paramMap);

    /**
     * 修改店铺信息
     */
    BoolDTO updateShop(Map<String, Object> paramMap);

    /**
     * 删除店铺信息
     */
    BoolDTO deleteShop(Map<String, Object> paramMap);
}
