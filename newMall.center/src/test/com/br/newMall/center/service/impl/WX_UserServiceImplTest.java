package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.dao.WX_CashLogDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_UserServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_UserServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_CashLogDao wxCashLogDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    @Autowired
    private HttpsUtil httpsUtil;

    @Test
    public void Test() throws Exception{
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", "1");
//        paramMap.put("cashToWxMoney", "1");
        cashBalanceToWx(paramMap);
    }

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