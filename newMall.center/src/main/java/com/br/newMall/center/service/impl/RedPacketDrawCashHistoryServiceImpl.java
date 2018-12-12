package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.RedPacketDrawCashHistoryService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.RedPacketDrawCashHistoryDao;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 红包提现记录service
 */
@Service
public class RedPacketDrawCashHistoryServiceImpl implements RedPacketDrawCashHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(RedPacketDrawCashHistoryServiceImpl.class);

    @Autowired
    private RedPacketDrawCashHistoryDao redPacketDrawCashHistoryDao;

    /**
     * 获取已提现红包总额
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getDrawCashMoneyTotal(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String phone = paramMap.get("phone") != null ? paramMap.get("phone").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(phone) || !"".equals(uid)) {
            Map<String, Object> getDrawCashMoneyTotalMap = redPacketDrawCashHistoryDao.getDrawCashMoneyTotal(paramMap);
            if (getDrawCashMoneyTotalMap != null && getDrawCashMoneyTotalMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(getDrawCashMoneyTotalMap));
                resultMapDTO.setSuccess(true);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getMessage());
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取已提现红包总额-getSimpleRedPacketDrawCashHistoryByCondition,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取红包提现历史记录
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getRedPacketDrawCashHistory(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> redPacketDrawCashHistoryStrList = Lists.newArrayList();
        String phone = paramMap.get("phone") != null ? paramMap.get("phone").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(phone) || !"".equals(uid)) {
            List<Map<String, Object>> redPacketDrawCashHistoryList = redPacketDrawCashHistoryDao.getRedPacketDrawCashHistory(paramMap);
            if (redPacketDrawCashHistoryList != null && redPacketDrawCashHistoryList.size() > 0) {
                redPacketDrawCashHistoryStrList = MapUtil.getStringMapList(redPacketDrawCashHistoryList);
                Integer total = redPacketDrawCashHistoryDao.getRedPacketDrawCashHistoryTotal(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(redPacketDrawCashHistoryStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取单一的红包提现记录信息-getSimpleRedPacketDrawCashHistoryByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 添加红包提现记录
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addRedPacketDrawCashHistory(Map<String, Object> paramMap) {
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String redPacketMoney = paramMap.get("redPacketMoney") != null ? paramMap.get("redPacketMoney").toString() : "";
        if (!"".equals(uid) && !"".equals(redPacketMoney)) {
            addNum = redPacketDrawCashHistoryDao.addRedPacketDrawCashHistory(paramMap);
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
            boolDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加红包提现记录-addRedPacketDrawCashHistory,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 删除红包提现记录
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteRedPacketDrawCashHistory(Map<String, Object> paramMap) {
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(id) || !"".equals(uid)) {
            deleteNum = redPacketDrawCashHistoryDao.deleteRedPacketDrawCashHistory(paramMap);
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
            boolDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中删除红包提现记录-deleteRedPacketDrawCashHistory,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 更新红包提现记录
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateRedPacketDrawCashHistory(Map<String, Object> paramMap) {
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(id) || !"".equals(uid)) {
            updateNum = redPacketDrawCashHistoryDao.updateRedPacketDrawCashHistory(paramMap);
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
            boolDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中更新红包提现记录-updateRedPacketDrawCashHistory,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 获取单一的红包提现记录信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleRedPacketDrawCashHistoryByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> redPacketDrawCashHistoryStrList = Lists.newArrayList();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(id) || !"".equals(uid)) {
            List<Map<String, Object>> redPacketDrawCashHistoryList = redPacketDrawCashHistoryDao.getSimpleRedPacketDrawCashHistoryByCondition(paramMap);
            if (redPacketDrawCashHistoryList != null && redPacketDrawCashHistoryList.size() > 0) {
                redPacketDrawCashHistoryStrList = MapUtil.getStringMapList(redPacketDrawCashHistoryList);
                Integer total = redPacketDrawCashHistoryDao.getSimpleRedPacketDrawCashHistoryTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(redPacketDrawCashHistoryStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取单一的红包提现记录信息-getSimpleRedPacketDrawCashHistoryByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }
}
