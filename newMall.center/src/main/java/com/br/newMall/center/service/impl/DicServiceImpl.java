package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.DicService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.DicDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
public class DicServiceImpl implements DicService {

    private static final Logger logger = LoggerFactory.getLogger(DicServiceImpl.class);

    @Autowired
    private DicDao dicDao;

    /**
     * 添加字典
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addDic(Map<String, Object> paramMap) {
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "";
        String dicCode = paramMap.get("dicCode") != null ? paramMap.get("dicCode").toString() : "";
        String dicName = paramMap.get("dicName") != null ? paramMap.get("dicName").toString() : "";
        if (!"".equals(dicType) && !"".equals(dicCode) && !"".equals(dicName)) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("dicCode", dicCode);
            Integer total = dicDao.getSimpleDicTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                addNum = dicDao.addDic(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setSuccess(true);
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setSuccess(false);
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.DIC_EXIST.getNo());
                boolDTO.setMessage(NewMallCode.DIC_EXIST.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.DIC_TYPE_OR_CODE_OR_NAME_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.DIC_TYPE_OR_CODE_OR_NAME_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加字典-addDic,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 删除字典
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteDic(Map<String, Object> paramMap) {
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String dicCode = paramMap.get("dicCode") != null ? paramMap.get("dicCode").toString() : "";
        if (!"".equals(id) || !"".equals(dicCode)) {
            deleteNum = dicDao.deleteDic(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.DIC_ID_OR_CODE_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.DIC_ID_OR_CODE_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中删除字典-deleteDic,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 更新字典
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateDic(Map<String, Object> paramMap) {
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String dicCode = paramMap.get("dicCode") != null ? paramMap.get("dicCode").toString() : "";
        if (!"".equals(id) || !"".equals(dicCode)) {
            updateNum = dicDao.updateDic(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.DIC_ID_OR_CODE_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.DIC_ID_OR_CODE_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中更新字典-updateDic,结果-result:" + boolDTO);
        return boolDTO;
    }


    /**
     * 获取单一的字典信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleDicByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        String dicType = paramMap.get("dicType") != null ? paramMap.get("dicType").toString() : "";
        String dicCode = paramMap.get("dicCode") != null ? paramMap.get("dicCode").toString() : "";
        if (!"".equals(dicType) || !"".equals(dicCode)) {
            List<Map<String, Object>> dicList = dicDao.getSimpleDicByCondition(paramMap);
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
                Integer total = dicDao.getSimpleDicTotalByCondition(paramMap);
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
        logger.info("在service中获取单一的字典信息-getSimpleDicByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 获取单一的字典信息(支持同时查询多个字典)
     * dicTypes可以传送多个参数，使用英文逗号(,)分隔
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getMoreDicByCondition(Map<String, Object> paramMap) {
        Integer total = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, String>> dicStrList = Lists.newArrayList();
        String dicTypeStr = paramMap.get("dicTypes") != null ? paramMap.get("dicTypes").toString() : "";
        if (!"".equals(dicTypeStr)) {
            if (dicTypeStr.contains(",")) {          //同事查询多个字典
                String[] dicTypeArr = dicTypeStr.split(",");
                for (String dicType : dicTypeArr) {
                    paramMap.put("dicType", dicType);
                    List<Map<String, Object>> dicList = dicDao.getSimpleDicByCondition(paramMap);
                    if (dicList != null && dicList.size() > 0) {
                        for (Map<String, Object> dicMap : dicList) {
                            String dicRemark = dicMap.get("dicRemark") != null ? dicMap.get("dicRemark").toString() : "";
                            if (!"".equals(dicRemark)) {
                                Map<String, Object> dicRemarkMap = JSONObject.parseObject(dicRemark, Map.class);
                                dicMap.remove("dicRemark");
                                dicMap.putAll(dicRemarkMap);
                            }
                        }
                        resultMap.put(dicType, JSONObject.toJSONString(dicList));
                        total += dicDao.getSimpleDicTotalByCondition(paramMap);
                    } else {
                        dicList = Lists.newArrayList();
                        resultMap.put("dicType", JSONObject.toJSONString(dicList));
                    }

                }
                resultMapDTO.setResultListTotal(total);
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取多个的字典-getMoreDicByCondition,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

}
