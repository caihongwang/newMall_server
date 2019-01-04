package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_ProductService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_ProductDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 商品Service
 * @author caihongwang
 */
@Service
public class WX_ProductServiceImpl implements WX_ProductService {

    private static final Logger logger = LoggerFactory.getLogger(WX_ProductServiceImpl.class);

    @Autowired
    private WX_ProductDao wxProductDao;

    @Autowired
    private WX_DicService wxDicService;

    /**
     * 获取商品类型列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getProductTypeList(Map<String, Object> paramMap) {
        logger.info("【service】获取商品类型列表-getProductTypeList,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "category";
        if(!"".equals(dicType)){
            paramMap.put("dicType", dicType);
            resultDTO = wxDicService.getSimpleDicByCondition(paramMap);
        } else {
            resultDTO.setCode(NewMallCode.PRODUCT_TYPE_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PRODUCT_TYPE_IS_NULL.getMessage());
        }
        logger.info("【service】获取商品类型列表-getProductTypeList,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

    /**
     * 添加商品
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addProduct(Map<String, Object> paramMap) {
        logger.info("【service】添加商品-addProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String title = paramMap.get("title") != null ? paramMap.get("title").toString() : "";
        String degist = paramMap.get("degist") != null ? paramMap.get("degist").toString() : "";
        String stock = paramMap.get("stock") != null ? paramMap.get("stock").toString() : "";
        String headImgUrl = paramMap.get("headImgUrl") != null ? paramMap.get("headImgUrl").toString() : "";
        String describeImgUrl = paramMap.get("describeImgUrl") != null ? paramMap.get("describeImgUrl").toString() : "";
        String price = paramMap.get("price") != null ? paramMap.get("price").toString() : "";
        if (!"".equals(title) && !"".equals(degist)
                && !"".equals(stock) && !"".equals(headImgUrl)
                    && !"".equals(describeImgUrl) && !"".equals(price)) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("title", title);
            Integer total = wxProductDao.getSimpleProductTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                addNum = wxProductDao.addProduct(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                boolDTO.setCode(NewMallCode.PRODUCT_EXIST.getNo());
                boolDTO.setMessage(NewMallCode.PRODUCT_EXIST.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PRODUCT_TITLE_OR_DEGIST_OR_STOCK_OR_HEADIMGURL_OR_DESCRIBEIMGURL_OR_PRICE_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PRODUCT_TITLE_OR_DEGIST_OR_STOCK_OR_HEADIMGURL_OR_DESCRIBEIMGURL_OR_PRICE_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】添加商品-addProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除商品
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteProduct(Map<String, Object> paramMap) {
        logger.info("【service】删除商品-deleteProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxProductDao.deleteProduct(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PRODUCT_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PRODUCT_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】删除商品-deleteProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改商品
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateProduct(Map<String, Object> paramMap) {
        logger.info("【service】修改商品-updateProduct,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxProductDao.updateProduct(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.PRODUCT_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.PRODUCT_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】修改商品-updateProduct,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的商品信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleProductByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的商品-getSimpleProductByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> productStrList = Lists.newArrayList();
        String category = paramMap.get("category") != null ? paramMap.get("category").toString() : "";
        if(!"".equals(category)){
            paramMap.put("paramMap", "recommend");
        }
        List<Map<String, Object>> productList = wxProductDao.getSimpleProductByCondition(paramMap);
        if (productList != null && productList.size() > 0) {
            productStrList = MapUtil.getStringMapList(productList);
            Integer total = wxProductDao.getSimpleProductTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(productStrList);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.PRODUCT_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PRODUCT_LIST_IS_NULL.getMessage());
        }
        logger.info("【service】获取单一的商品-getSimpleProductByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
