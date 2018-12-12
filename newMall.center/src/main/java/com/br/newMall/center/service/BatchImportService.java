package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultMapDTO;

import java.util.Map;

public interface BatchImportService {

  /**
   * 将excel通讯录转换为vcf
   */
  void convertExcelToVcf(Map<String, Object> paramMap);
}
