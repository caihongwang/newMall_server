package com.br.newMall.center.quartz;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.*;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.PingYingUtil;
import com.br.newMall.center.utils.TimestampUtil;
import com.br.newMall.dao.WX_LuckDrawDao;
import com.br.newMall.dao.WX_UserDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时任务
 */
@Component
public class TimeTaskOfQuartz {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TimeTaskOfQuartz.class);

    //使用环境
    @Value("${useEnvironmental}")
    private String useEnvironmental;

    @Autowired
    private WX_UserDao wxUserDao;

    @Autowired
    private WX_CashLogService wxCashLogService;

    @Autowired
    private WX_LuckDrawService wxLuckDrawService;

    /**
     * 每隔5秒定时发送待奖励的返现
     */
//    @Scheduled(cron = "5 * * * * ?")
    public void do_SendRedPacket_For_NewMall() {
        Map<String, Object> paramMap = Maps.newHashMap();
        try {
            wxLuckDrawService.convertBalance(paramMap);
        } catch (Exception e) {
            logger.error("定时给带奖励的用户返现时异常, e : ", e);
        }
    }

    /**
     * 每隔1小时定时向设置为自动提现的用户 提现到微信余额
     */
//    @Scheduled(cron = "* * 1 * * ?")
    public void do_cashBalanceToWx_For_NewMall() {
        Map<String, Object> userParamMap = Maps.newHashMap();
        userParamMap.put("autoCashToWxFlag", "1");
        wxCashLogService.cashBalanceToWx(userParamMap);
    }
}
