package com.br.newMall.web.controller;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_SourceMaterialHandler;
import com.br.newMall.web.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx_SourceMaterial", produces = "application/json;charset=utf-8")
public class WX_SourceMaterialController {

    private static final Logger logger = LoggerFactory.getLogger(WX_SourceMaterialController.class);

    @Autowired
    private WX_SourceMaterialHandler.Client wx_SourceMaterialHandler;

    @RequestMapping("/batchGetMaterial")
    @ResponseBody
    public Map<String, Object> batchGetMaterial(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取素材列表-batchGetMaterial,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = wx_SourceMaterialHandler.batchGetMaterial(0, paramMap);
            resultMap.put("recordsFiltered", resultMapDTO.getResultListTotal());
            resultMap.put("data", resultMapDTO.getResultMap());
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中获取素材列表-batchGetMaterial is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中获取素材列表-batchGetMaterial,响应-response:" + resultMap);
        return resultMap;
    }


    @RequestMapping("/redirectMaterialUrl")
    @ResponseBody
    public String redirectMaterialUrl(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中获取素材列表-batchGetMaterial,请求-paramMap:" + paramMap);
        String materialUrl = "https://www.91caihongwang.com/newMall/payment.html";
        if(paramMap.get("materialUrl") != null && !"".equals(paramMap.get("materialUrl"))){
            materialUrl = paramMap.get("materialUrl").toString();
        }
        logger.info("在controller中获取素材列表-batchGetMaterial,响应-response:" + resultMap);
        return "redirect:" + materialUrl;//重定向;
    }
}
