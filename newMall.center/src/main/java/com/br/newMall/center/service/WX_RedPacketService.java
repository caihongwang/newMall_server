package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface WX_RedPacketService {

    /**
     * 获取红包二维码
     */
    public ResultMapDTO getRedPacketQrCode(Map<String, Object> paramMap);

    /**
     * 获取oauth
     */
    public ResultMapDTO getOauth(Map<String, Object> paramMap);

    /**
     * 获取oauth的url
     */
    public ResultMapDTO getToOauthUrl(Map<String, Object> paramMap);

    /**
     * 企业付款，直达微信零钱
     */
    public ResultMapDTO enterprisePayment(Map<String, Object> paramMap);

    /**
     * 发送普通红包
     */
    public ResultMapDTO sendRedPacket(Map<String, Object> paramMap);

    /**
     * 发送分裂红包
     */
    public ResultMapDTO sendGroupRedPacket(Map<String, Object> paramMap);

}
