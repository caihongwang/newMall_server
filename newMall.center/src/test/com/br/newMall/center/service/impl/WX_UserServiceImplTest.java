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
        paramMap.put("autoCashToWxFlag", "0");
        checkUserAutoCashBalance(paramMap);
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