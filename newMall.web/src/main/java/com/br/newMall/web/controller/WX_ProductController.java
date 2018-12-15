package com.br.newMall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.WX_ProductHandler;
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

/**
 * @Description 商品Controller
 * @author caihongwang
 */
@Controller
@RequestMapping(value = "/wxProduct", produces = "application/json;charset=utf-8")
public class WX_ProductController {

    private static final Logger logger = LoggerFactory.getLogger(WX_ProductController.class);

    @Autowired
    private WX_ProductHandler.Client wxProductHandler;

    /**
     * 添加商品
     * @param request
     * @return
     */
    @RequestMapping("/addProduct")
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中添加商品-addProduct,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxProductHandler.addProduct(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中添加商品-addProduct is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中添加商品-addProduct,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 删除商品
     * @param request
     * @return
     */
    @RequestMapping("/deleteProduct")
    @ResponseBody
    public Map<String, Object> deleteProduct(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中删除商品-deleteProduct,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxProductHandler.deleteProduct(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中删除商品-deleteProduct is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中删除商品-deleteProduct,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 修改商品
     * @param request
     * @return
     */
    @RequestMapping("/updateProduct")
    @ResponseBody
    public Map<String, Object> updateProduct(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中修改商品-updateProduct,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            BoolDTO boolDTO = wxProductHandler.updateProduct(0, paramMap);
            resultMap.put("success", true);
            resultMap.put("code", boolDTO.getCode());
            resultMap.put("message", boolDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中修改商品-updateProduct is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中修改商品-updateProduct,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取单一的商品
     * @param request
     * @return
     */
    @RequestMapping("/getSimpleProductByCondition")
    @ResponseBody
    public Map<String, Object> getSimpleProductByCondition(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在【controller】中获取单一的商品-getSimpleProductByCondition,请求-paramMap = ", JSONObject.toJSONString(paramMap));
        try {
            ResultDTO resultDTO = wxProductHandler.getSimpleProductByCondition(0, paramMap);
            resultMap.put("recordsFiltered", resultDTO.getResultListTotal());
            resultMap.put("data", resultDTO.getResultList());
            resultMap.put("code", resultDTO.getCode());
            resultMap.put("message", resultDTO.getMessage());
        } catch (Exception e) {
            logger.error("在【controller】中获取单一的商品-getSimpleProductByCondition is error, paramMap : ", JSONObject.toJSONString(paramMap), " , e : ", e);
            resultMap.put("success", false);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【controller】中获取单一的商品-getSimpleProductByCondition,响应-resultMap = ", JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
