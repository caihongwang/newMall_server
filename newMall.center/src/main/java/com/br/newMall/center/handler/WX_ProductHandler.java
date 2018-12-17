package com.br.newMall.center.handler;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_ProductService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description 商品Handler
 * @author caihongwang
 */
public class WX_ProductHandler implements com.br.newMall.api.service.WX_ProductHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_ProductHandler.class);

    @Autowired
    private WX_ProductService wxProductService;

    /**
     * 添加商品
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO addProduct(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中添加商品-addProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                boolDTO = wxProductService.addProduct(objectParamMap);
            } catch (Exception e) {
                logger.error("在【handler】中添加商品-addProduct is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【handler】中添加商品-addProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除商品
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO deleteProduct(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中删除商品-deleteProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxProductService.deleteProduct(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中删除商品-deleteProduct is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中删除商品-deleteProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改商品
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public BoolDTO updateProduct(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【handler】中修改商品-updateProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            boolDTO = wxProductService.updateProduct(objectParamMap);
        } catch (Exception e) {
            logger.error("在【handler】中修改商品-updateProduct is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
            boolDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            boolDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在【handler】中修改商品-updateProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的商品
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultDTO getSimpleProductByCondition(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在【hanlder】中获取单一的商品-getSimpleProductByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = wxProductService.getSimpleProductByCondition(objectParamMap);
            } catch (Exception e) {
                logger.error("在【hanlder】中获取单一的商品-getSimpleProductByCondition is error, paramMap : {}", JSONObject.toJSONString(paramMap), " , e : {}", e);
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在【hanlder】中获取单一的商品-getSimpleProductByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }
}
