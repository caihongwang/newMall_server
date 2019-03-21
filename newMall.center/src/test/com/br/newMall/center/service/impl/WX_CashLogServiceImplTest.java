package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.NumberUtil;
import com.br.newMall.dao.WX_CashLogDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2019/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_CashLogServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_CashLogServiceImpl.class);

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_CashLogDao wxCashLogDao;

    @Autowired
    private WX_RedPacketService wxRedPacketService;


    @Test
    public void Test() {
        Map<String, Object> paramMap = Maps.newHashMap();
        List<Double> cashToWxMoneyList = Lists.newArrayList();
        cashToWxMoneyList.add(1.0);cashToWxMoneyList.add(2.3);cashToWxMoneyList.add(3.0);cashToWxMoneyList.add(4.4);cashToWxMoneyList.add(5.3);cashToWxMoneyList.add(6.12);cashToWxMoneyList.add(7.22);
        for (int i = 0; i < cashToWxMoneyList.size(); i++) {
            paramMap.clear();
            Double cashToWxMoney = cashToWxMoneyList.get(i);
            paramMap.put("uid", "1");
            paramMap.put("cashToWxMoney", cashToWxMoney);
            cashBalanceToWx(paramMap);
        }
    }


    /**
     * 提现用户余额到微信零钱
     * @param paramMap
     * @return
     */
    public ResultMapDTO cashBalanceToWx(Map<String, Object> paramMap) {
        logger.info("【service】提现用户余额到微信零钱-cashBalanceToWx,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Map<String, String> resultMap = Maps.newHashMap();
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        String cashToWxMoneyStr = paramMap.get("cashToWxMoney")!=null?paramMap.get("cashToWxMoney").toString():"";
        if(!"".equals(cashToWxMoneyStr) &&
                !"".equals(uid)){
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
                        Double cashToWxMoney = 0.0;             //提现金 额
                        Double cashFee = 0.0;                   //手续费
                        Double newUserBalance = 0.0;            //新的用户余额
                        //提现金额
                        cashToWxMoney = Double.parseDouble(cashToWxMoneyStr);
                        //获取用户openId,余额
                        String openId = userMap.get("openId")!=null?userMap.get("openId").toString():"";
                        String userBalanceStr = userMap.get("balance")!=null?userMap.get("balance").toString():"0";
                        Double userBalance = Double.parseDouble(userBalanceStr);
                        if(cashToWxMoney >= cashMoneyLowerLimit){       //提现余额大于等于提现金额下限才可以提现
                            cashFee = cashToWxMoney * proportion;       //服务费
                            cashFee = NumberUtil.getPointTowNumber(cashFee);
                            newUserBalance = userBalance - (cashToWxMoney + cashFee);  //扣除 提现金额和新的服务费 的新用户余额
                            if(newUserBalance >= 0){      //用户余额大于等于提现金额下限才可以提现
                                cashToWxMoney = NumberUtil.getPointTowNumber(cashToWxMoney);
                                //2.整合发送现金红包的参数
                                Map<String, Object> cashRedPacketParamMap = Maps.newHashMap();
                                String cashRedPacketTotal = ((int) (cashToWxMoney * 100)) + "";
                                cashRedPacketParamMap.put("amount", cashRedPacketTotal);
                                cashRedPacketParamMap.put("openId", openId);
                                cashRedPacketParamMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                                cashRedPacketParamMap.put("wxPublicNumGhId", "gh_97b78683d2c9");
                                cashRedPacketParamMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                                resultMapDTO = wxRedPacketService.enterprisePayment(cashRedPacketParamMap);

                                newUserBalance = NumberUtil.getPointTowNumber(newUserBalance);
                                //3.发送成功，将已发送的红包进行记录，并保存.
                                if (NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()) {
                                    //更新用户的余额
                                    Map<String, Object> userParamMap = Maps.newHashMap();
                                    userParamMap.put("openId", openId);
                                    userParamMap.put("balance", newUserBalance.toString());
                                    wxUserDao.updateUser(userParamMap);
                                    //将抽奖记录表的状态变更为已发放
                                    Map<String, Object> cashLogMap = Maps.newHashMap();
                                    cashLogMap.put("uid", userMap.get("id"));
                                    cashLogMap.put("cashToWxMoney", cashToWxMoney.toString());
                                    cashLogMap.put("cashFee", cashFee.toString());
                                    cashLogMap.put("userBalance", newUserBalance.toString());
                                    cashLogMap.put("remark", "成功提现到微信零钱的金额："+cashToWxMoney+
                                            "元，提现手续费："+cashFee+
                                            "元，现用户余额："+newUserBalance+"元.");
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
                                            "元，原用户余额："+userBalance+"元.详情："+resultMapDTO.getMessage()+".");
                                    cashLogMap.put("status", "0");
                                    wxCashLogDao.addCashLog(cashLogMap);
                                }
                            } else {
                                resultMapDTO.setCode(NewMallCode.CASHLOG_USERBALANCE_MUST_BE_MORE_CASHTOWXMONEY.getNo());
                                resultMapDTO.setMessage(NewMallCode.CASHLOG_USERBALANCE_MUST_BE_MORE_CASHTOWXMONEY.getMessage());
                            }
                        } else {
                            resultMapDTO.setCode(NewMallCode.CASHLOG_CASHTOWXMONEY_MUST_BE_MORE_CASHMONEYLOWERLIMIT.getNo());
                            resultMapDTO.setMessage(NewMallCode.CASHLOG_CASHTOWXMONEY_MUST_BE_MORE_CASHMONEYLOWERLIMIT.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("当前用户 uid = " + paramMap.get("uid") +
                            " ，提现现比例 proportion = "+ proportionStr +
                            " 用户提现时提现比例或者提现金额下限非数字，请联系管理员. e : ", e);
                    resultMapDTO.setCode(NewMallCode.USER_PROPORTION_OR_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getNo());
                    resultMapDTO.setMessage(NewMallCode.USER_PROPORTION_OR_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER.getMessage());
                }
            }
        } else {
            //提现的用户uid或者提现金额不能为空
            resultMapDTO.setCode(NewMallCode.CASHLOG_UID_OR_CASHTOWXMONEY_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage("提现金额不能为空");
        }
        logger.info("【service】提现用户余额到微信零钱-cashBalanceToWx,请求-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}