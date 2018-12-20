package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_ShopService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_ShopDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 店铺Service
 * @author caihongwang
 */
@Service
public class WX_ShopServiceImpl implements WX_ShopService {

    private static final Logger logger = LoggerFactory.getLogger(WX_ShopServiceImpl.class);

    @Autowired
    private WX_ShopDao wxShopDao;

    /**
     * 添加店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addShop(Map<String, Object> paramMap) {
        logger.info("在【service】中添加店铺-addShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String shopTitle = paramMap.get("shopTitle") != null ? paramMap.get("shopTitle").toString() : "";
        String shopDegist = paramMap.get("shopDegist") != null ? paramMap.get("shopDegist").toString() : "";
        String shopPhone = paramMap.get("shopPhone") != null ? paramMap.get("shopPhone").toString() : "";
        String shopAddress = paramMap.get("shopAddress") != null ? paramMap.get("shopAddress").toString() : "";
        String shopLon = paramMap.get("shopLon") != null ? paramMap.get("shopLon").toString() : "";
        String shopLat = paramMap.get("shopLat") != null ? paramMap.get("shopLat").toString() : "";
        String shopHeadImgUrl = paramMap.get("shopHeadImgUrl") != null ? paramMap.get("shopHeadImgUrl").toString() : "";
        String shopDescribeImgUrl = paramMap.get("shopDescribeImgUrl") != null ? paramMap.get("shopDescribeImgUrl").toString() : "";
        if (!"".equals(shopTitle) && !"".equals(shopDegist)
                && !"".equals(shopPhone) && !"".equals(shopAddress)
                && !"".equals(shopLon) && !"".equals(shopLat)
                && !"".equals(shopHeadImgUrl) && !"".equals(shopDescribeImgUrl)) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("shopTitle", shopTitle);
            Integer total = wxShopDao.getSimpleShopTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                paramMap.put("shopStatus", "0");
                addNum = wxShopDao.addShop(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                boolDTO.setCode(NewMallCode.SHOP_EXIST.getNo());
                boolDTO.setMessage(NewMallCode.SHOP_EXIST.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.DIC_TYPE_OR_CODE_OR_NAME_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.DIC_TYPE_OR_CODE_OR_NAME_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中添加店铺-addShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteShop(Map<String, Object> paramMap) {
        logger.info("在【service】中删除店铺-deleteShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxShopDao.deleteShop(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中删除店铺-deleteShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改店铺
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateShop(Map<String, Object> paramMap) {
        logger.info("在【service】中修改店铺-updateShop,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxShopDao.updateShop(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.SHOP_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SHOP_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中修改店铺-updateShop,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的店铺信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleShopByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的店铺-getSimpleShopByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        List<Map<String, Object>> dicList = wxShopDao.getSimpleShopByCondition(paramMap);
        if (dicList != null && dicList.size() > 0) {
            dicStrList = MapUtil.getStringMapList(dicList);
            Integer total = wxShopDao.getSimpleShopTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(dicStrList);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setCode(NewMallCode.SHOP_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.SHOP_LIST_IS_NULL.getMessage());
        }
        logger.info("在【service】中获取单一的店铺-getSimpleShopByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }

}
