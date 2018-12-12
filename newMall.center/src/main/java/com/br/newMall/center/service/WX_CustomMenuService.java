package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

/**
 * 自定义菜单
 */
public interface WX_CustomMenuService {

  /**
   * 获取公众号自定义菜单
   */
  public ResultMapDTO getCustomMenu(Map<String, Object> paramMap);

  /**
   * 创建公众号自定义菜单
   */
  public ResultMapDTO createCustomMenu(Map<String, Object> paramMap);

  /**
   * 删除公众号自定义菜单
   */
  public ResultMapDTO deleteCustomMenu(Map<String, Object> paramMap);

  /**
   * 创建公众号个性化菜单
   */
  public ResultMapDTO createPersonalMenu(Map<String, Object> paramMap);

  /**
   * 删除公众号个性化菜单
   */
  public ResultMapDTO deletePersonalMenu(Map<String, Object> paramMap);

  /**
   * 获取公众号自定义菜单配置接口
   * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置
   */
  public ResultMapDTO getCurrentSelfMenuInfo(Map<String, Object> paramMap);

}
