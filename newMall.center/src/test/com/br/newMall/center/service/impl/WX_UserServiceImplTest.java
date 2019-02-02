package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.*;
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
    private WX_OrderDao wxOrderDao;

    @Autowired
    private WX_LuckDrawDao wxLuckDrawDao;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_LeagueDao wxLeagueDao;

    @Autowired
    private WX_CashLogDao wxCashLogDao;

    @Autowired
    private WX_DicService wxDicService;

    @Autowired
    private HttpsUtil httpsUtil;

    @Test
    public void Test() throws Exception{
//        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("uid", "1");
//        paramMap.put("autoCashToWxFlag", "0");
//        checkUserAutoCashBalance(paramMap);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", "1");
        getUserBaseInfo(paramMap);
    }


    /**
     * 获取用户的基本信息
     * @param paramMap
     * @return
     */
    public ResultMapDTO getUserBaseInfo(Map<String, Object> paramMap) {
        logger.info("【service】获取用户的基本信息-getUserBaseInfo,响应-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if (!"".equals(uid)) {
            //获取用户余额，积分
            Map<String, Object> userParamMap = Maps.newHashMap();
            userParamMap.put("id", uid);
            List<Map<String, Object>> userMapList = wxUserDao.getSimpleUserByCondition(userParamMap);
            if(userMapList != null && userMapList.size() > 0){
                Map<String, Object> userMap = userMapList.get(0);
                String balance = userMap.get("balance")!=null?userMap.get("balance").toString():"0.00";
                String integral = userMap.get("integral")!=null?userMap.get("integral").toString():"0.00";
                String nickName = userMap.get("nickName")!=null?userMap.get("nickName").toString():"0.00";
                String avatarUrl = userMap.get("avatarUrl")!=null?userMap.get("avatarUrl").toString():"0.00";
                String userType = userMap.get("userType")!=null?userMap.get("userType").toString():"0.00";
                resultMap.put("balance", balance);
                resultMap.put("integral", integral);
                resultMap.put("nickName", nickName);
                resultMap.put("avatarUrl", avatarUrl);
                resultMap.put("userType", userType);
            } else {
                resultMap.put("balance", "0.00");
                resultMap.put("integral", "0.00");
                resultMap.put("nickName", "未知");
                resultMap.put("avatarUrl", "未知");
                resultMap.put("userType", "普通用户");
            }
            //获取用户待奖励的订单数量，已获得奖励的订单数量
            List<Map<String, Object>> orderList = wxLuckDrawDao.getLuckDrawNumByStatus(paramMap);
            if(orderList != null && orderList.size() > 0){
                Integer allLuckDrawTotal = 0;
                Integer waitLuckDrawTotal = 0;
                Integer recevicedLuckDrawTotal = 0;
                for (Map<String, Object> orderMap : orderList) {
                    String status = orderMap.get("status")!=null?orderMap.get("status").toString():"";
                    String total = orderMap.get("total")!=null?orderMap.get("total").toString():"0";
                    if("0".equals(status)){            //待奖励
                        waitLuckDrawTotal = Integer.parseInt(total);
                    } else if("1".equals(status)){     //已奖励
                        recevicedLuckDrawTotal = Integer.parseInt(total);
                    }
                }
                allLuckDrawTotal = waitLuckDrawTotal + recevicedLuckDrawTotal;
                resultMap.put("allLuckDrawTotal", allLuckDrawTotal);
                resultMap.put("waitLuckDrawTotal", waitLuckDrawTotal);
                resultMap.put("recevicedLuckDrawTotal", recevicedLuckDrawTotal);
            } else {
                resultMap.put("allLuckDrawTotal", "0");
                resultMap.put("waitLuckDrawTotal", "0");
                resultMap.put("recevicedLuckDrawTotal", "0");
            }
            //获取当前加盟的总数
            Map<String, Object> leagueParamMap = Maps.newHashMap();
            Integer allLeagueTotal = wxLeagueDao.getSimpleLeagueTotalByCondition(leagueParamMap);
            if(allLeagueTotal != null){
                resultMap.put("allLeagueTotal", allLeagueTotal);
            } else {
                resultMap.put("allLeagueTotal", "0");
            }
            //获取 积分或者余额可抵扣的情况
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicType", "deduction");
            ResultDTO dicResultDTO = wxDicService.getSimpleDicByCondition(dicParamMap);
            if(dicResultDTO != null && dicResultDTO.getResultList() != null
                    && dicResultDTO.getResultList().size() > 0){
                for(Map<String, String> dicMap : dicResultDTO.getResultList()){
                    if("integralDeduction".equals(dicMap.get("deductionCode"))){
                        resultMap.put("integralDeductionNum", dicMap.get("deductionNum"));
                    } else if("balanceDeduction".equals(dicMap.get("deductionCode"))){
                        resultMap.put("balanceDeductionNum", dicMap.get("deductionNum"));
                    }
                }
            } else {
                resultMap.put("integralDeductionNum", "0.2");
                resultMap.put("balanceDeductionNum", "0.2");
            }
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.USER_ID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.USER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】获取用户的基本信息-getUserBaseInfo,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 设置用户余额是否自动提现
     * @param paramMap
     * @return
     */
    public BoolDTO checkUserAutoCashBalance(Map<String, Object> paramMap) {
        logger.info("【service】设置用户余额是否自动提现-checkUserAutoCashBalance,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        String autoCashToWxFlag = paramMap.get("autoCashToWxFlag")!=null?paramMap.get("autoCashToWxFlag").toString():"";
        if(!"".equals(uid) && !"".equals(autoCashToWxFlag)){
            updateNum = wxUserDao.checkUserAutoCashBalance(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.USER_ID_OR_AUTOCASHTOWXFLAG_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.USER_ID_OR_AUTOCASHTOWXFLAG_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】设置用户余额是否自动提现-checkUserAutoCashBalance,请求-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

}