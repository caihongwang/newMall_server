package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.BannerService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_DicDao;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 字典service
 */
@Service
public class BannerServiceImpl implements BannerService {

    private static final Logger logger = LoggerFactory.getLogger(BannerServiceImpl.class);

    @Autowired
    private WX_DicDao wxDicDao;

    /**
     * 获取正在活跃的Banner信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getActivityBanner(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        String dicType = "activityBanner";
        String dicCode = paramMap.get("banner") != null ? paramMap.get("banner").toString() : "";
        if (!"".equals(dicType) || !"".equals(dicCode)) {
            paramMap.put("dicType", dicType);
            paramMap.put("dicCode", dicCode);
            List<Map<String, Object>> dicList = wxDicDao.getSimpleDicByCondition(paramMap);
            if (dicList != null && dicList.size() > 0) {
                for (Map<String, Object> dicMap : dicList) {
                    String dicRemark = dicMap.get("dicRemark") != null ? dicMap.get("dicRemark").toString() : "";
                    if (!"".equals(dicRemark)) {
                        Map<String, Object> dicRemarkMap = JSONObject.parseObject(dicRemark, Map.class);
                        dicMap.remove("dicRemark");
                        dicMap.putAll(dicRemarkMap);
                    }
                }
                dicStrList = MapUtil.getStringMapList(dicList);
                Integer total = wxDicDao.getSimpleDicTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(dicStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.DIC_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.DIC_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取活跃的banner-getActivityBanner,结果-result:" + resultDTO);
        return resultDTO;
    }
}
