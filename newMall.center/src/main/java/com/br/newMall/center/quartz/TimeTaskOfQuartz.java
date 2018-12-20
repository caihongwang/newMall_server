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
    private WX_LuckDrawDao wxLuckDrawDao;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    /**
     * 每隔5秒定时发送红包
     */
//    @Scheduled(cron = "5 * * * * ?")
    public void do_SendRedPacket_For_NewMall() {
        //1.整合参数
        Map<String, Object> luckDrawParamMap = Maps.newHashMap();
        luckDrawParamMap.put("status", "0");         //抽奖状态，0是未发放，1是已发放，2是已删除
        List<Map<String, Object>> luckDrawList = wxLuckDrawDao.getLuckDrawByCondition(luckDrawParamMap);
        if (luckDrawList != null && luckDrawList.size() > 0) {
            for (Map<String, Object> luckDrawMap : luckDrawList) {
                String id = luckDrawMap.get("id")!=null?luckDrawMap.get("id").toString():"";
                String uid = luckDrawMap.get("uid")!=null?luckDrawMap.get("uid").toString():"";
                String openId = luckDrawMap.get("openId")!=null?luckDrawMap.get("openId").toString():"";
                String payMoneyStr = luckDrawMap.get("payMoney")!=null?luckDrawMap.get("payMoney").toString():"";
                String cashBackModeJson = luckDrawMap.get("cashBackModeJson")!=null?luckDrawMap.get("cashBackModeJson").toString():"";
                if (!"".equals(id) && !"".equals(uid) && !"".equals(payMoneyStr)
                        && !"".equals(openId) && !"".equals(cashBackModeJson)) {
                    //对返现模式json进行解析
                    Map<String, String> cashBackModeMap = JSONObject.parseObject(cashBackModeJson, Map.class);
                    String proportionStr = cashBackModeMap.get("proportion");
                    if(!"".equals(proportionStr)){
                        try {
                            //计算待反还的金额
                            Double payMoney = Double.parseDouble(payMoneyStr);
                            Double proportion = Double.parseDouble(proportionStr);
                            Double cashBackMoney = payMoney * proportion;
                            BigDecimal bg = new BigDecimal(cashBackMoney);
                            Double redPacketTotalDouble = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            //2.整合发送红包的参数
                            Map<String, Object> redPacketParamMap = Maps.newHashMap();
                            String redPacketTotal = ((int) (redPacketTotalDouble * 100)) + "";
                            redPacketParamMap.put("amount", redPacketTotal);
                            redPacketParamMap.put("openId", openId);
                            redPacketParamMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                            redPacketParamMap.put("wxPublicNumGhId", "gh_417c90af3488");
                            redPacketParamMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                            ResultMapDTO resultMapDTO = wxRedPacketService.enterprisePayment(redPacketParamMap);
                            //3.发送成功，将已发送的红包进行记录，并保存.
                            if (NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()) {
                                //将抽奖记录表的状态变更为已发放
                                luckDrawParamMap.clear();
                                luckDrawParamMap.put("id", id);
                                luckDrawParamMap.put("status", "1");
                                wxLuckDrawDao.updateLuckDraw(luckDrawParamMap);
                            } else {
                                //将抽奖记录表的状态变更为已发放
                                luckDrawParamMap.clear();
                                luckDrawParamMap.put("id", id);
                                luckDrawParamMap.put("status", "0");
                                wxLuckDrawDao.updateLuckDraw(luckDrawParamMap);
                            }
                        } catch (Exception e) {
                            logger.info("当前用户 uid = " + uid +
                                    " ，订单金额 payMoney = " + payMoneyStr +
                                    " ，返现比例 proportion = "+ proportionStr +
                                    " 不正确(非数字)，请联系管理员.");
                            continue;
                        }
                    } else {
                        //返现比例不存在
                        logger.info("当前用户 uid = " + uid +
                                " ，订单金额 payMoney = " + payMoneyStr +
                                " ，返现比例不存在，请联系管理员.");
                        continue;
                    }
                } else {
                    logger.info("当前用户 uid = " + uid +
                            " 存在垃圾数据，请联系管理员.");
                    continue;
                }
            }
        }
    }
}
