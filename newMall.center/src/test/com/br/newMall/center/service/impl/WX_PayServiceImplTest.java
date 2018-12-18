package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/9/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_PayServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(WX_PayServiceImplTest.class);

    @Test
    public void getToOauthUrl() throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("lon", "116");
        paramMap.put("lat", "39");
        paramMap.put("oilStationName", "%E5%A4%A7%E8%B7%AF%E7%94%B0%E5%9D%9D%E5%8A%A0%E6%B2%B9%E7%AB%99");
        paramMap.put("oilStationWxPaymentCodeImgUrl", "https://www.91caihongwang.com/images/da_lu_tian_ba_jia_you_zhan/wx_payment.jpeg");

        Map<String, Object> resultMap = Maps.newHashMap();
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String calbackUrl = NewMallCode.WX_PAY_DOMAIN + "/wx_Pay/getOauth.do?";
        //准备参数
        try{
            Map<String, String> paramMap_temp = Maps.newHashMap();
            String lon = paramMap.get("lon")!=null?paramMap.get("lon").toString():"";
            String lat = paramMap.get("lat")!=null?paramMap.get("lat").toString():"";
            String oilStationName = paramMap.get("oilStationName")!=null?paramMap.get("oilStationName").toString():"";
            String oilStationWxPaymentCodeImgUrl = paramMap.get("oilStationWxPaymentCodeImgUrl")!=null?paramMap.get("oilStationWxPaymentCodeImgUrl").toString():"";
            if(!"".equals(lon)){
                paramMap_temp.put("lon", lon);
            }
            if(!"".equals(lat)){
                paramMap_temp.put("lat", lat);
            }
            if(!"".equals(oilStationName)){
                oilStationName = URLDecoder.decode(oilStationName);
                paramMap_temp.put("oilStationName", oilStationName);
            }
            if(!"".equals(oilStationWxPaymentCodeImgUrl)){
                paramMap_temp.put("oilStationWxPaymentCodeImgUrl", oilStationWxPaymentCodeImgUrl);
            }
            if (paramMap_temp != null && !paramMap_temp.isEmpty()) {
                if(calbackUrl.endsWith("?")){
                    calbackUrl = calbackUrl;
                } else {
                    calbackUrl = calbackUrl + "?";
                }
                Map.Entry entry;
                for (Iterator i$ = paramMap_temp.entrySet().iterator(); i$.hasNext(); calbackUrl = calbackUrl + (String) entry.getKey() + "=" + entry.getValue() + "&") {
                    entry = (Map.Entry) i$.next();
                }
                calbackUrl = calbackUrl.substring(0, calbackUrl.length() - 1);
            } else {
                calbackUrl = calbackUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        calbackUrl = URLEncoder.encode(calbackUrl, "UTF-8");
        String toOauthUrl = WX_PublicNumberUtil.getAuthorizeURL(NewMallCode.WX_PUBLIC_NUMBER_APPID, calbackUrl, "123", false);
        resultMap.put("toOauthUrl", toOauthUrl);
        resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
        resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
        resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
    }

}
