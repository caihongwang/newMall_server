package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.OilStationMapCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_SourceMaterialService;
import com.br.newMall.center.utils.*;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/12/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class WX_MessageServiceImplTest {


    private static final Logger logger = LoggerFactory.getLogger(WX_MessageServiceImpl.class);

    @Autowired
    private WX_SourceMaterialService wxSourceMaterialService;

    @Autowired
    private CommonService commonService;

    @Test
    public void Test() throws Exception{
        Map<String, Object> paramMap = Maps.newHashMap();
        messageSend(paramMap);
    }

    /**
     * 消息群发
     * @param paramMap
     * @return
     * @throws Exception
     */
    public ResultMapDTO messageSend(Map<String, Object> paramMap) throws Exception {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        logger.info("在service中根据OpenID列表群发-messageSend,请求-paramMap:" + paramMap);
        //默认使用 【油价地图】公众号 来发送
        String appId = paramMap.get("appId")!=null?paramMap.get("appId").toString():"wxf768b49ad0a4630c";
        String secret = paramMap.get("secret")!=null?paramMap.get("secret").toString():"a481dd6bc40c9eec3e57293222e8246f";
        if(!"".equals(appId) && !"".equals(secret)){
            //1.获取所有微信用户openId
            Map<String, Object> followersMap = WX_PublicNumberUtil.getFollowers(null, appId,  secret);
            if(followersMap != null && followersMap.size() > 0){
                JSONObject dataJSONObject = followersMap.get("data")!=null?(JSONObject)followersMap.get("data"):null;
                if(dataJSONObject != null){
                    JSONArray openIdJSONArray = dataJSONObject.get("openid")!=null?
                            (JSONArray)dataJSONObject.get("openid"):null;
                    List<String> openIdList = JSONObject.parseObject(openIdJSONArray.toJSONString(), List.class);
                    //2.获取最新的素材
                    List<Map<String, Object>> sourceMaterialList = WX_PublicNumberUtil.batchGetMaterial(MediaTypeUtil.NEWS, 0, 20);
                    Map<String, Object> sourceMaterialMap = sourceMaterialList.get(0);      //获取最近发布的一篇文章
                    String createTime = sourceMaterialMap.get("createTime")!=null?sourceMaterialMap.get("createTime").toString():"";
                    if(!"".equals(createTime)){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date createDate = sdf.parse(createTime);
                        Date currentDate = new Date();
                        //素材的创建时间与当前时间只有1天之隔才允许群发
                        if((currentDate.getTime() - createDate.getTime()) < 24*60*60*1000){
                            //3.发送模板消息
                            for(String openId : openIdList) {
                                paramMap.clear();//清空参数，重新准备参数
                                Map<String, Object> dataMap = Maps.newHashMap();

                                Map<String, Object> firstMap = Maps.newHashMap();
                                firstMap.put("value", sourceMaterialMap.get("title").toString());
                                dataMap.put("first", firstMap);

                                Map<String, Object> keyword1Map = Maps.newHashMap();
                                keyword1Map.put("value", sourceMaterialMap.get("createTime").toString());
                                dataMap.put("keyword1", keyword1Map);

                                Map<String, Object> keyword2Map = Maps.newHashMap();
                                keyword2Map.put("value", "【油价地图】");
                                dataMap.put("keyword2", keyword2Map);

                                Map<String, Object> keyword3Map = Maps.newHashMap();
                                keyword3Map.put("value", "只为专注油价资讯，为车主省钱.");
                                dataMap.put("keyword3", keyword3Map);

                                Map<String, Object> remarkMap = Maps.newHashMap();
                                remarkMap.put("value", sourceMaterialMap.get("digest").toString());
                                dataMap.put("remark", remarkMap);

                                paramMap.put("data", JSONObject.toJSONString(dataMap));
                                paramMap.put("url", sourceMaterialMap.get("url").toString());

                                paramMap.put("openId", openId);
                                paramMap.put("template_id", "Ns82Wg237bj6iaPlBXyp-wBhfQJAJan7p-qSJklQsMQ");
//                                paramMap.put("template_id", "油价资讯通知");       //待审核
                                commonService.sendTemplateMessageForWxPublicNumber(paramMap);
                            }
                        } else {
                            //发送模板消息（>>>>>>>>>>>>>>文案待定<<<<<<<<<<<<<<）
                        }
                    } else {
                        //发送模板消息（>>>>>>>>>>>>>>文案待定<<<<<<<<<<<<<<）
                    }
                } else {
                    //获取微信公众所有openId
                    resultMapDTO.setCode(OilStationMapCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getNo());
                    resultMapDTO.setMessage(OilStationMapCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getMessage());
                }
            } else {
                //获取微信公众所有openId
                resultMapDTO.setCode(OilStationMapCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(OilStationMapCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(OilStationMapCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(OilStationMapCode.PARAM_IS_NULL.getMessage());        }
        logger.info("在service中根据OpenID列表群发-messageSend,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
