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
    }

}