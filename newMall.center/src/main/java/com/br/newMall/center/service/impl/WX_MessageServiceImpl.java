package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_MessageService;
import com.br.newMall.center.service.WX_SourceMaterialService;
import com.br.newMall.center.utils.CustomMessageUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.MediaTypeUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号自定义菜单service
 */
@Service
public class WX_MessageServiceImpl implements WX_MessageService {

    private static final Logger logger = LoggerFactory.getLogger(WX_MessageServiceImpl.class);

    @Autowired
    private WX_SourceMaterialService wxSourceMaterialService;

    @Autowired
    private CommonService commonService;

    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     * @param paramMap
     */
    @Override
    public ResultMapDTO messageSend(Map<String, Object> paramMap) throws Exception {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        logger.info("在service中根据OpenID列表群发-messageSend,请求-paramMap:" + paramMap);
        //默认使用 【惠生活】公众号 来发送
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
                                firstMap.put("color", "#2F3451");
                                dataMap.put("first", firstMap);

                                Map<String, Object> keyword1Map = Maps.newHashMap();
                                keyword1Map.put("value", sourceMaterialMap.get("createTime").toString());
                                keyword1Map.put("color", "#2F3451");
                                dataMap.put("keyword1", keyword1Map);

                                Map<String, Object> keyword2Map = Maps.newHashMap();
                                keyword2Map.put("value", "【惠生活】");
                                keyword2Map.put("color", "#2F3451");
                                dataMap.put("keyword2", keyword2Map);

                                Map<String, Object> keyword3Map = Maps.newHashMap();
                                keyword3Map.put("value", "只为专注油价资讯，为车主省钱.");
                                keyword3Map.put("color", "#2F3451");
                                dataMap.put("keyword3", keyword3Map);

                                Map<String, Object> remarkMap = Maps.newHashMap();
                                remarkMap.put("value", sourceMaterialMap.get("digest").toString());
                                remarkMap.put("color", "#2F3451");
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
                    resultMapDTO.setCode(NewMallCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getNo());
                    resultMapDTO.setMessage(NewMallCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getMessage());
                }
            } else {
                //获取微信公众所有openId
                resultMapDTO.setCode(NewMallCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());        }
        logger.info("在service中根据OpenID列表群发-messageSend,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
