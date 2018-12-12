package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * 消息管理
 */
public interface WX_MessageService {

  /**
   * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
   */
  public ResultMapDTO messageSend(Map<String, Object> paramMap) throws Exception;

}
