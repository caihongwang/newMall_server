package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.MessageDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.service.WX_UserService;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.br.newMall.dao.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 用户Service
 * @author caihongwang
 */
@Service
public class WX_UserServiceImpl implements WX_UserService {

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
    private WX_AddressDao wxAddressDao;

    @Autowired
    private HttpsUtil httpsUtil;

    /**
     * 登录(首次微信授权，则使用openId创建用户)
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO login(Map<String, Object> paramMap) {
        logger.info("【service】登录(首次微信授权，则使用openId创建用户)-login,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer addNum = 0;
        Map<String, String> resultMap = Maps.newHashMap();
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        //获得维系授权后的用户信息并解析
        String userInfoStr = paramMap.get("userInfo") != null ? paramMap.get("userInfo").toString() : "";
        if (!"".equals(userInfoStr)) {
            Map<String, Object> userInfoMap = JSONObject.parseObject(userInfoStr, Map.class);
            paramMap.putAll(userInfoMap);
        }
        String code = paramMap.get("code") != null ? paramMap.get("code").toString() : "";
        if (!"".equals(code)) {
            //TODO 后续优化方向，将appId和secret的信息放到字典表中
            //向微信服务器发送请求，获取小程序的用户openId和seesion_key
            paramMap.put("appId", NewMallCode.WX_MINI_PROGRAM_APPID);
            paramMap.put("secret", NewMallCode.WX_MINI_PROGRAM_SECRET);
            Map<String, Object> wx_resultMap = WX_PublicNumberUtil.getMiniOpenIdAndSessionKeyForWX(paramMap);
            if (wx_resultMap.size() > 0) {
                //获取session,如果没有则创一个有效的session
                String session_key = wx_resultMap.get("session_key").toString();
                String openid = wx_resultMap.get("openid").toString();
                if (openid != null && !"".equals(openid.toString())) {
                    //1.判断用户这个用户是否已经存在,但是也要判断这个用户是否还在会话范围之内
                    Map<String, Object> paramMap_temp = Maps.newHashMap();
                    paramMap_temp.put("openId", openid);
                    List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(paramMap_temp);
                    if (userList != null && userList.size() > 0) {        //存在，则更新
                        String uid = userList.get(0).get("id").toString();
                        String key = paramMap.get("sessionKey") != null ? paramMap.get("sessionKey").toString() : "";
                        if (!"".equals(key)) {                //再次登录存在seesionKey
                            Map<String, Object> tempMap = Maps.newHashMap();
                            tempMap.put("sessionKey", key);
                            BoolDTO boolDTO = this.checkSession(tempMap);           //检测用户的是否还在会话范围之内
                            if (boolDTO.getCode() != NewMallCode.SUCCESS.getNo()) {         //如果不在会话范围之内，则将他
                                //获取session,如果没有则创一个有效的session
                                Map<String, Object> sessionMap = Maps.newHashMap();
                                sessionMap.put("key", key);
                                sessionMap.put("session_key", session_key);
                                this.setSession(sessionMap);
                                //设置回传参数
                                resultMap.put("sessionKey", key);
                                resultMap.put("uid", uid);
                                resultMapDTO.setResultMap(resultMap);
                                resultMapDTO.setCode(NewMallCode.USER_EXIST.getNo());
                                resultMapDTO.setMessage(NewMallCode.USER_EXIST.getMessage());
                            } else {
                                //设置回传参数
                                resultMap.put("sessionKey", key);
                                resultMap.put("uid", uid);
                                resultMapDTO.setResultMap(resultMap);
                                resultMapDTO.setCode(NewMallCode.USER_EXIST.getNo());
                                resultMapDTO.setMessage(NewMallCode.USER_EXIST.getMessage());
                            }
                        } else {                        //在次登录没有sessionKey则创建sessionKey并保存redius
                            key = UUID.randomUUID().toString();          //将key转化成uuid
                            //设置session
                            Map<String, Object> sessionMap = Maps.newHashMap();
                            sessionMap.put("key", key);
                            sessionMap.put("session_key", session_key);
                            this.setSession(sessionMap);
                            //设置回传参数
                            resultMap.put("sessionKey", key);
                            resultMap.put("uid", uid);
                            resultMapDTO.setResultMap(resultMap);
                            resultMapDTO.setCode(NewMallCode.USER_EXIST.getNo());
                            resultMapDTO.setMessage(NewMallCode.USER_EXIST.getMessage());
                        }
                    } else {                                            //不存在，则添加
                        //3.添加用户
                        paramMap.put("openId", openid);
                        paramMap.put("balance", "0");           //余额为0
                        paramMap.put("integral", "0");          //积分为0
                        paramMap.put("grayStatus", "0");        //灰度用户状态，0是正常登陆用户，1是不需要登陆的用户
                        paramMap.put("userSource", "miniProgram");//微信用户来源，miniProgram是小程序用户，publicNumber是公众号用户
                        paramMap.put("autoCashToWxFlag", "0");  //自动将余额提现到微信零钱，0：否，1：是
                        addNum = wxUserDao.addUser(paramMap);
                        //创建session
                        if (addNum != null && addNum > 0) {
                            //获取session,如果没有则创一个有效的session
                            List<Map<String, Object>> userList_temp = wxUserDao.getSimpleUserByCondition(paramMap);
                            if (userList_temp != null && userList_temp.size() > 0) {
                                //设置session参数
                                String uid = userList_temp.get(0).get("id").toString();
                                String key = UUID.randomUUID().toString();          //将key转化成uuid
                                Map<String, Object> sessionMap = Maps.newHashMap();
                                sessionMap.put("key", key);
                                sessionMap.put("session_key", session_key);
                                this.setSession(sessionMap);
                                //设置回传参数
                                resultMap.put("sessionKey", key);
                                resultMap.put("uid", uid);
                                resultMapDTO.setResultMap(resultMap);
                                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                            } else {
                                resultMapDTO.setResultMap(resultMap);
                                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                            }
                        } else {
                            resultMapDTO.setResultMap(resultMap);
                            resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                            resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                        }
                    }
                } else {
                    resultMapDTO.setResultMap(resultMap);
                    resultMapDTO.setCode(NewMallCode.WX_SERVER_INNER_ERROR.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_SERVER_INNER_ERROR.getMessage());
                }
            } else {
                resultMapDTO.setResultMap(resultMap);
                resultMapDTO.setCode(NewMallCode.WX_PARAM_IS_NOT_NULL.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_PARAM_IS_NOT_NULL.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.CODE_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.CODE_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】登录(首次微信授权，则使用openId创建用户)-login,响应-resultMapDTO = {}", JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }


    /**
     * 设置用户余额是否自动提现
     * @param paramMap
     * @return
     */
    @Override
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

    /**
     * 获取用户的基本信息
     * @param paramMap
     * @return
     */
    @Override
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
            //获取用户在商家的交易订单数量
            Integer foodsOrderTotal = wxOrderDao.getFoodsOrderTotalByCondition(paramMap);
            if(foodsOrderTotal != null){
                resultMap.put("foodsOrderTotal", foodsOrderTotal.toString());
            } else {
                resultMap.put("foodsOrderTotal", "0");
            }
            //获取用户在积分商城的交易订单数量
            Integer goodsOrderTotal = wxOrderDao.getGoodsOrderTotalByCondition(paramMap);
            if(foodsOrderTotal != null){
                resultMap.put("goodsOrderTotal", goodsOrderTotal.toString());
            } else {
                resultMap.put("goodsOrderTotal", "0");
            }
            //获取用的收货地址的数量
            Integer addressTotal = wxAddressDao.getSimpleAddressTotalByCondition(paramMap);
            if(addressTotal != null){
                resultMap.put("addressTotal", addressTotal.toString());
            } else {
                resultMap.put("addressTotal", "0");
            }
            //获取用的收货地址的数量
            Integer cashLogTotal = wxCashLogDao.getSimpleCashLogTotalByCondition(paramMap);
            if(addressTotal != null){
                resultMap.put("cashLogTotal", cashLogTotal.toString());
            } else {
                resultMap.put("cashLogTotal", "0");
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
     * 更新用户信息
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateUser(Map<String, Object> paramMap) {
        logger.info("【service】更新用户信息-updateUser,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        //对微信的用户信息进行解析
        String userInfoStr = paramMap.get("userInfo") != null ? paramMap.get("userInfo").toString() : "";
        if (!"".equals(userInfoStr)) {
            Map<String, Object> userInfoMap = JSONObject.parseObject(userInfoStr, Map.class);
            paramMap.putAll(userInfoMap);
        }
        paramMap.put("id", paramMap.get("uid"));
        updateNum = wxUserDao.updateUser(paramMap);
        if (updateNum != null && updateNum > 0) {
            boolDTO.setCode(NewMallCode.SUCCESS.getNo());
            boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
            boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
        }
        logger.info("【service】更新用户信息-updateUser,请求-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 检测用户会话是否过期
     * 获取用户的session
     * userSessionKey还存在对应的value,则说明用户的会话时间还是有效的
     * 如果失效了，对应的value则会被删除
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO checkSession(Map<String, Object> paramMap) {
        logger.info("【service】检测用户会话是否过期-checkSession,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        String userSessionKey = paramMap.get("sessionKey") != null ? paramMap.get("sessionKey").toString() : "";
        if (!"".equals(userSessionKey)) {
            try (Jedis jedis = jedisPool.getResource()) {
                String value = jedis.get(userSessionKey);
                logger.info("当前用户的 sessionKey = " + userSessionKey +
                        " , 用户的 sessionValue = " + value
                );
                if (value != null && !"".equals(value)) {
                    boolDTO.setValue(true);
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setValue(false);
                    boolDTO.setCode(NewMallCode.IS_USER_SESSION_OVERDUE.getNo());
                    boolDTO.setMessage(NewMallCode.IS_USER_SESSION_OVERDUE.getMessage());
                }
            } catch (Exception e) {
                boolDTO.setValue(false);
                boolDTO.setCode(NewMallCode.IS_USER_SESSION_OVERDUE.getNo());
                boolDTO.setMessage(NewMallCode.IS_USER_SESSION_OVERDUE.getMessage());
            }
        } else {
            boolDTO.setValue(false);
            boolDTO.setCode(NewMallCode.SESSION_KEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SESSION_KEY_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】检测用户会话是否过期-checkSession,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 校验手机验证码-[暂时未使用]
     * @param paramMap
     * @return
     */
    @Override
    public MessageDTO getCheckVerificationCode(Map<String, Object> paramMap) {
        logger.info("【service】校验手机验证码-getCheckVerificationCode,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        MessageDTO messageDTO = new MessageDTO();
        String userPhone = paramMap.get("userPhone") != null ? paramMap.get("userPhone").toString() : "";
        String captcha = paramMap.get("captcha") != null ? paramMap.get("captcha").toString() : "";
        //1.校验手机验证码是否正确
        if (!"".equals(userPhone) && !"".equals(captcha)) {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = getKey(userPhone);
                String checkCaptcha = jedis.get(key);
                boolean result = checkCaptcha != null && captcha.trim().equals(checkCaptcha.trim());
                if (result) {
                    jedis.del(key);
                    messageDTO.setCode(NewMallCode.SUCCESS.getNo());
                    messageDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    messageDTO.setCode(NewMallCode.CARD_ERROR_PHONE_CAPTCHA.getNo());
                    messageDTO.setMessage(NewMallCode.CARD_ERROR_PHONE_CAPTCHA.getMessage());
                }
            } catch (Exception e) {
                messageDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                messageDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("【service】校验手机验证码-getCheckVerificationCode is error, paramMap = {}", JSONObject.toJSONString(paramMap), ", e : {}", e);
            }
        } else {
            messageDTO.setCode(NewMallCode.PHONE_OR_CAPTCHA_IS_NOT_NULL.getNo());
            messageDTO.setMessage(NewMallCode.PHONE_OR_CAPTCHA_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】校验手机验证码-getCheckVerificationCode,响应-messageDTO = {}", JSONObject.toJSONString(messageDTO));
        return messageDTO;
    }

    /**
     * 获取单一的用户信息
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleUserByCondition(Map<String, Object> paramMap) {
        logger.info("【service】获取单一的用户信息-getSimpleUserByCondition,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        ResultDTO resultDTO = new ResultDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        List<Map<String, Object>> userList = wxUserDao.getSimpleUserByCondition(paramMap);
        if (!"".equals(uid)) {
            if (userList != null && userList.size() > 0) {
                List<Map<String, String>> userStrList = MapUtil.getStringMapList(userList);
                Integer total = wxUserDao.getSimpleUserTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                resultDTO.setResultList(userStrList);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setCode(NewMallCode.USER_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.USER_IS_NULL.getMessage());
            }
        } else {
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("【service】获取单一的用户信息-getSimpleUserByCondition,响应-resultDTO = {}", JSONObject.toJSONString(resultDTO));
        return resultDTO;
    }


    /**
     * 删除用户
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteUser(Map<String, Object> paramMap) {
        logger.info("【service】删除用户-deleteUser,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        deleteNum = wxUserDao.deleteUser(paramMap);
        if (deleteNum != null && deleteNum > 0) {
            boolDTO.setCode(NewMallCode.SUCCESS.getNo());
            boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
            boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
        }
        logger.info("【service】删除用户-deleteUser,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));

        return boolDTO;
    }


    /**
     * 设置用户的session
     * userSessionKey还存在对应的value,则说明用户的会话时间还是有效的
     * 如果失效了，对应的value则会被删除
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO setSession(Map<String, Object> paramMap) {
        logger.info("【service】设置用户的session-setSession,请求-paramMap = {}", JSONObject.toJSONString(paramMap));
        BoolDTO boolDTO = new BoolDTO();
        String key = paramMap.get("key") != null ? paramMap.get("key").toString() : "";
        String session_key = paramMap.get("session_key") != null ? paramMap.get("session_key").toString() : "";
        if (!"".equals(session_key)) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(key, session_key);        //将session_key作为redius中的value
                jedis.expire(key, NewMallCode.USER_SESSION_EXPIRED_TIME);
                boolDTO.setValue(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } catch (Exception e) {
                boolDTO.setValue(false);
                boolDTO.setCode(NewMallCode.IS_USER_SESSION_OVERDUE.getNo());
                boolDTO.setMessage(NewMallCode.IS_USER_SESSION_OVERDUE.getMessage());
            }
        } else {
            boolDTO.setValue(false);
            boolDTO.setCode(NewMallCode.SESSION_KEY_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.SESSION_KEY_IS_NOT_NULL.getMessage());
        }
        logger.info("【service】设置用户的session-setSession,响应-boolDTO = {}", JSONObject.toJSONString(boolDTO));
        return boolDTO;
    }

    /**
     * 整合在redis中查询的key值
     *
     * @param mobile
     * @return
     */
    private String getKey(String mobile) {
        return NewMallCode.REDIS_MSG_PREFIX + mobile;
    }

    /**
     * 整合在redis中查询的key值--针对用户的会话时间
     *
     * @param session
     * @return
     */
    private String getUserKey(String session) {
        return NewMallCode.USER_SESSION_PREFIX + session;
    }
}
