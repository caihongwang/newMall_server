package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_CashLogService;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_CashLogDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description 提现日志Service
 * @author caihongwang
 */
@Service
public class WX_CashLogServiceImpl implements WX_CashLogService {

    private static final Logger logger = LoggerFactory.getLogger(WX_CashLogServiceImpl.class);

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_CashLogDao wxCashLogDao;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    /**
     * 添加提现日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addCashLog(Map<String, Object> paramMap) {
        logger.info("在【service】中添加提现日志-addCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String cashToWxMoney = paramMap.get("cashToWxMoney") != null ? paramMap.get("cashToWxMoney").toString() : "";
        String cashFee = paramMap.get("cashFee") != null ? paramMap.get("cashFee").toString() : "";
        String userBalance = paramMap.get("userBalance") != null ? paramMap.get("userBalance").toString() : "";
        if (!"".equals(uid) && !"".equals(cashToWxMoney)
                && !"".equals(cashFee) && !"".equals(userBalance)) {
            addNum = wxCashLogDao.addCashLog(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.CASHLOG_UID_OR_CASHTOWXMONEY_OR_CASHFEE_OR_USERBALANCE_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.CASHLOG_UID_OR_CASHTOWXMONEY_OR_CASHFEE_OR_USERBALANCE_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中添加提现日志-addCashLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 删除提现日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteCashLog(Map<String, Object> paramMap) {
        logger.info("在【service】中删除提现日志-deleteCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = wxCashLogDao.deleteCashLog(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.CASHLOG_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.CASHLOG_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中删除提现日志-deleteCashLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 修改提现日志
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateCashLog(Map<String, Object> paramMap) {
        logger.info("在【service】中修改提现日志-updateCashLog,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = wxCashLogDao.updateCashLog(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.CASHLOG_ID_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.CASHLOG_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中修改提现日志-updateCashLog,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 获取单一的提现日志信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleCashLogByCondition(Map<String, Object> paramMap) {
        logger.info("在【service】中获取单一的提现日志-getSimpleCashLogByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> cashLogStrList = Lists.newArrayList();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        if (!"".equals(uid)) {
            List<Map<String, Object>> cashLogList = wxCashLogDao.getSimpleCashLogByCondition(paramMap);
            if (cashLogList != null && cashLogList.size() > 0) {
                cashLogStrList = MapUtil.getStringMapList(cashLogList);
                Integer total = wxCashLogDao.getSimpleCashLogTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(cashLogStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.CASHLOG_LIST_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.CASHLOG_LIST_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.CASHLOG_UID_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.CASHLOG_UID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取单一的提现日志-getSimpleCashLogByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }



    /**
     * 提现用户余额到微信零钱
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO cashBalanceToWx(Map<String, Object> paramMap) {
        logger.info("在【service】中提现用户余额到微信零钱-cashBalanceToWx,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Map<String, String> resultMap = Maps.newHashMap();
        String cashToWxMoneyStr = paramMap.get("cashToWxMoney")!=null?paramMap.get("cashToWxMoney").toString():"";
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        List<Map<String, Object>> userMapList = wxUserDao.getSimpleUserByCondition(paramMap);
        Map<String, Object> dicMap = Maps.newHashMap();
        dicMap.put("dicType", "cashFee");
        ResultDTO cashFeeResultDTO = wxDicService.getSimpleDicByCondition(dicMap);
        if(userMapList != null && userMapList.size() > 0
                && cashFeeResultDTO != null && cashFeeResultDTO.getResultList() != null
                && cashFeeResultDTO.getResultList().size() > 0) {
            //提现手续费比例
            List<Map<String, String>> cashFeeList = cashFeeResultDTO.getResultList();
            String proportionStr = cashFeeList.get(0).get("proportion");
            String cashMoneyLowerLimitStr = cashFeeList.get(0).get("cashMoneyLowerLimit");
            try {
                //提现手续费比例
                Double proportion = Double.parseDouble(proportionStr);
                Double cashMoneyLowerLimit = Double.parseDouble(cashMoneyLowerLimitStr);
                for (Map<String, Object> userMap : userMapList) {
                    //获取用户openId,余额
                    String openId = userMap.get("openId")!=null?userMap.get("openId").toString():"";
                    String balanceStr = userMap.get("balance")!=null?userMap.get("balance").toString():"0";
                    Double balance = Double.parseDouble(balanceStr);
                    if(balance >= cashMoneyLowerLimit){            //用户余额大于等于提现金额下限才可以提现
                        Double cashToWxMoney = 0.0;             //提现金额
                        Double cashFee = 0.0;                   //手续费
                        Double userBalance = 0.0;               //用户余额
                        if(!"".equals(cashToWxMoneyStr)){           //提现金额未填写，则提取全部
                            cashToWxMoney = Double.parseDouble(cashToWxMoneyStr);
                            cashFee = cashToWxMoney * proportion;
                            cashToWxMoney = cashToWxMoney - cashFee;
                            userBalance = balance - (cashToWxMoney + cashFee);
                        } else {
                            cashToWxMoney = balance;
                            cashFee = cashToWxMoney * proportion;
                            cashToWxMoney = cashToWxMoney - cashFee;
                            userBalance = 0.0;
                        }
                        BigDecimal bg = new BigDecimal(cashToWxMoney);
                        cashToWxMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        //2.整合发送红包的参数
                        Map<String, Object> redPacketParamMap = Maps.newHashMap();
                        String redPacketTotal = ((int) (cashToWxMoney * 100)) + "";
                        redPacketParamMap.put("amount", redPacketTotal);
                        redPacketParamMap.put("openId", openId);
                        redPacketParamMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                        redPacketParamMap.put("wxPublicNumGhId", "gh_417c90af3488");
                        redPacketParamMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                        resultMapDTO = wxRedPacketService.enterprisePayment(redPacketParamMap);
                        //3.发送成功，将已发送的红包进行记录，并保存.
                        if (NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()) {
                            //更新用户的余额
                            Map<String, Object> userParamMap = Maps.newHashMap();
                            userParamMap.put("openId", openId);
                            userParamMap.put("balance", userBalance.toString());
                            wxUserDao.updateUser(userParamMap);
                            //将抽奖记录表的状态变更为已发放
                            Map<String, Object> cashLogMap = Maps.newHashMap();
                            cashLogMap.put("uid", userMap.get("id"));
                            cashLogMap.put("cashToWxMoney", cashToWxMoney.toString());
                            cashLogMap.put("cashFee", cashFee.toString());
                            cashLogMap.put("userBalance", userBalance.toString());
                            cashLogMap.put("remark", "成功提现到微信零钱的金额："+cashToWxMoney+
                                    "元，提现手续费："+cashFee+
                                    "元，用户余额："+userBalance+"元.");
                            cashLogMap.put("status", "1");
                            wxCashLogDao.addCashLog(cashLogMap);
                        } else {
                            //将抽奖记录表的状态变更为已发放
                            Map<String, Object> cashLogMap = Maps.newHashMap();
                            cashLogMap.put("uid", userMap.get("id"));
                            cashLogMap.put("cashToWxMoney", cashToWxMoney.toString());
                            cashLogMap.put("cashFee", cashFee.toString());
                            cashLogMap.put("userBalance", userBalance.toString());
                            cashLogMap.put("remark", "失败提现到微信零钱的金额："+cashToWxMoney+
                                    "元，提现手续费："+cashFee+
                                    "元，用户余额："+userBalance+"元.详情："+resultMapDTO.getMessage()+".");
                            cashLogMap.put("status", "0");
                            wxCashLogDao.addCashLog(cashLogMap);
                        }
                    } else {
                        resultMapDTO.setCode(NewMallCode.USER_CASHMONEY_NOT_MORE_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getNo());
                        resultMapDTO.setMessage(NewMallCode.USER_CASHMONEY_NOT_MORE_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.info("当前用户 uid = " + paramMap.get("uid") +
                        " ，提现现比例 proportion = "+ proportionStr +
                        " 用户提现时提现比例或者提现金额下限非数字，请联系管理员.");
                resultMapDTO.setCode(NewMallCode.USER_PROPORTION_OR_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getNo());
                resultMapDTO.setMessage(NewMallCode.USER_PROPORTION_OR_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getMessage());
            }
        }
        logger.info("在【service】中提现用户余额到微信零钱-cashBalanceToWx,请求-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
