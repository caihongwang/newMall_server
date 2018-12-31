package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.WX_CashLogDao;
import com.br.newMall.dao.WX_LeagueDao;
import com.br.newMall.dao.WX_OrderDao;
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
    private WX_OrderDao wxOrderDao;

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
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("uid", "1");
//        paramMap.put("cashToWxMoney", "1");
//        cashBalanceToWx(paramMap);
        getUserBaseInfo(paramMap);
    }

    public ResultMapDTO getUserBaseInfo(Map<String, Object> paramMap) {
        logger.info("在【service】中获取用户的基本信息-getUserBaseInfo,响应-paramMap = {}", JSONObject.toJSONString(paramMap));
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
                resultMap.put("balance", balance);
                resultMap.put("integral", integral);
            } else {
                resultMap.put("balance", "0.00");
                resultMap.put("integral", "0.00");
            }
            //获取用户待奖励的订单数量，已获得奖励的订单数量
            List<Map<String, Object>> orderList = wxOrderDao.getOrderNumByStatus(paramMap);
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
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.USER_ID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.USER_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在【service】中获取用户的基本信息-getUserBaseInfo,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}