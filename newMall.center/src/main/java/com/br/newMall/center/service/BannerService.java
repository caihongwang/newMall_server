package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultDTO;

import java.util.Map;

public interface BannerService {

    /**
     * 获取正在活跃的Banner信息
     */
    public ResultDTO getActivityBanner(Map<String, Object> paramMap);

}
