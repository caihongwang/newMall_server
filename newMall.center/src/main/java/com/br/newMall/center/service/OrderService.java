package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface OrderService {


    /**
     * 创建统一统一订单
     */
    public ResultMapDTO requestWxPayUnifiedOrder(Map<String, Object> paramMap) throws Exception;
}
