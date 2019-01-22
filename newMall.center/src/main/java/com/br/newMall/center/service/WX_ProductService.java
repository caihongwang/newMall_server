package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * @Description 商品Service
 * @author caihongwang
 */
public interface WX_ProductService {
    /**
     * 获取商品类型列表
     */
    ResultDTO getProductTypeList(Map<String, Object> paramMap);

    /**
     * 获取商品详情
     */
    ResultMapDTO getProductDetail(Map<String, Object> paramMap);

    /**
     * 获取商品列表
     */
    ResultDTO getProductList(Map<String, Object> paramMap);

    /**
     * 根据条件查询商品信息
     */
    ResultDTO getSimpleProductByCondition(Map<String, Object> paramMap);

    /**
     * 添加或者修改商品信息
     */
    BoolDTO addProduct(Map<String, Object> paramMap);

    /**
     * 修改商品信息
     */
    BoolDTO updateProduct(Map<String, Object> paramMap);

    /**
     * 删除商品信息
     */
    BoolDTO deleteProduct(Map<String, Object> paramMap);
}
