package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.center.service.DicService;
import com.br.newMall.center.service.OilStationOperatorService;
import com.br.newMall.center.service.RedPacketHistoryService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.dao.OilStationOperatorDao;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.UserDao;
import com.google.common.collect.Lists;
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
 * 对加油站的操作service
 */
@Service
public class OilStationOperatorServiceImpl implements OilStationOperatorService {

    private static final Logger logger = LoggerFactory.getLogger(OilStationOperatorServiceImpl.class);

    @Autowired
    private OilStationOperatorDao oilStationOperatorDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DicService dicService;

    @Autowired
    private WX_RedPacketService wxRedPacketService;

    @Autowired
    private RedPacketHistoryService redPacketHistoryService;

    /**
     * 添加对加油站的操作
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO addOilStationOperator(Map<String, Object> paramMap) {
        Integer addNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        String operator = paramMap.get("operator") != null ? paramMap.get("operator").toString() : "";
        if (!"".equals(uid) && !"".equals(oilStationCode) && !"".equals(operator)) {
            //1.根据operator获取红包金额redPacketTotal
            String redPacketTotal = "0";
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicCode", operator);
            dicParamMap.put("dicType", "oilStationOperator");
            ResultDTO dicResultDto = dicService.getSimpleDicByCondition(dicParamMap);
            if (dicResultDto.getResultList() != null
                    && dicResultDto.getResultList().size() > 0) {
                Object redPacketTotalObj = dicResultDto.getResultList().get(0).get("redPacketTotal");
                redPacketTotal = redPacketTotalObj!=null?redPacketTotalObj.toString():"0";
            } else {
                redPacketTotal = "0";
            }
            paramMap.put("status", 0);
            paramMap.put("redPacketTotal", redPacketTotal);
            //2.保存操作
            //2.1查看当前操作在当天是否存在过.如果存在则不记录也就意味着不发红包,不存在则记录
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> paramMapTemp = Maps.newHashMap();
            paramMapTemp.put("uid", uid);
            paramMapTemp.put("operator", operator);
            paramMapTemp.put("oilStationCode", oilStationCode);
            paramMapTemp.put("createTime", formatter.format(new Date()));
            List<Map<String, Object>> exist_oilStationOperatorList = oilStationOperatorDao.getSimpleOilStationOperatorByCondition(paramMapTemp);
            if(exist_oilStationOperatorList != null && exist_oilStationOperatorList.size() > 0){
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_IS_EXIST.getNo());
                resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_IS_EXIST.getMessage());
            } else {
            //2.2记录加油站操作
                addNum = oilStationOperatorDao.addOilStationOperator(paramMap);
                if (addNum != null && addNum > 0) {
                    resultMapDTO.setSuccess(true);
                    resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                    resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    resultMapDTO.setSuccess(false);
                    resultMapDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    resultMapDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_UID_OILSTATIONCODE_OPERATOR_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_UID_OILSTATIONCODE_OPERATOR_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加对加油站的操作-addOilStationOperator,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 删除对加油站的操作
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO deleteOilStationOperator(Map<String, Object> paramMap) {
        Integer deleteNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            deleteNum = oilStationOperatorDao.deleteOilStationOperator(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                resultMapDTO.setSuccess(true);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_ID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中删除对加油站的操作-deleteOilStationOperator,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 更新对加油站的操作
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO updateOilStationOperator(Map<String, Object> paramMap) {
        Integer updateNum = 0;
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        if (!"".equals(id)) {
            updateNum = oilStationOperatorDao.updateOilStationOperator(paramMap);
            if (updateNum != null && updateNum > 0) {
                resultMapDTO.setSuccess(true);
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setSuccess(false);
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_ID_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_ID_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中更新对加油站的操作-updateOilStationOperator,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }


    /**
     * 获取单一的对加油站的操作信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleOilStationOperatorByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> oilStationOperatorStrList = Lists.newArrayList();
        List<Map<String, Object>> oilStationOperatorList = oilStationOperatorDao.getSimpleOilStationOperatorByCondition(paramMap);
        if (oilStationOperatorList != null && oilStationOperatorList.size() > 0) {
            oilStationOperatorStrList = MapUtil.getStringMapList(oilStationOperatorList);
            Integer total = oilStationOperatorDao.getSimpleOilStationOperatorTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(oilStationOperatorStrList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_LIST_IS_NULL.getMessage());
        }
        logger.info("在service中获取单一的对加油站的操作信息-getSimpleOilStationOperatorByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 获取单一的对加油站操作的红包总金额
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getOilStationOperatorByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        List<Map<String, String>> oilStationOperatorStrList = Lists.newArrayList();
        List<Map<String, Object>> oilStationOperatorList = oilStationOperatorDao.getOilStationOperatorByCondition(paramMap);
        if (oilStationOperatorList != null && oilStationOperatorList.size() > 0) {
            oilStationOperatorStrList = MapUtil.getStringMapList(oilStationOperatorList);
            Integer total = oilStationOperatorDao.getOilStationOperatorTotalByCondition(paramMap);
            resultDTO.setResultListTotal(total);
            resultDTO.setResultList(oilStationOperatorStrList);
            resultDTO.setSuccess(true);
            resultDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            List<Map<String, String>> resultList = Lists.newArrayList();
            resultDTO.setResultListTotal(0);
            resultDTO.setResultList(resultList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_LIST_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_LIST_IS_NULL.getMessage());
        }
        logger.info("在service中获取单一的对加油站操作的红包总金额-getOilStationOperatorByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }


    /**
     * 领取或者提现加油站操作红包
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO cashOilStationOperatorRedPacket(Map<String, Object> paramMap) {
        logger.info("在service中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,结果-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        String id = paramMap.get("id")!=null?paramMap.get("id").toString():"";      //加油站操作ID
        String uid = paramMap.get("uid")!=null?paramMap.get("uid").toString():"";
        if(!"".equals(id) && !"".equals(uid)){
            //0.检测活动是否还在进行, 获取【油价地图】的【红包活动】信息
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicType", "redPacketActivity");
            dicParamMap.put("dicCode", "gh_417c90af3488");
            List<Map<String, String>> dicResultMapList = Lists.newArrayList();
            ResultDTO dicResultDTO = dicService.getSimpleDicByCondition(dicParamMap);
            if(dicResultDTO != null && dicResultDTO.getResultList() != null && dicResultDTO.getResultList().size() > 0){
                dicResultMapList = dicResultDTO.getResultList();
                String startTimeStr = dicResultMapList.get(0).get("startTime");
                String endTimeStr = dicResultMapList.get(0).get("endTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(startTimeStr != null && !"".equals(startTimeStr)
                        && endTimeStr != null && !"".equals(endTimeStr)){
                    Date currentTime = new Date();
                    try{
                        Date startTime = simpleDateFormat.parse(startTimeStr);
                        Date endTime = simpleDateFormat.parse(endTimeStr);
                        if(currentTime.after(startTime) && currentTime.before(endTime) ){       //活动进行中
                            paramMap.put("createTime", "");
                            ResultDTO resultDTO = this.getSimpleOilStationOperatorByCondition(paramMap);
                            if(resultDTO.getResultList() != null && resultDTO.getResultList().size() > 0){
                                List<Map<String, String>> oilStationOperatorList = resultDTO.getResultList();
                                Map<String, String> oilStationOperatorMap = oilStationOperatorList.get(0);
                                Object uidObj = oilStationOperatorMap.get("uid");
                                Object openIdObj = oilStationOperatorMap.get("openId");
                                Object redPacketTotalObj = oilStationOperatorMap.get("redPacketTotal");
                                if(uidObj != null && openIdObj != null
                                        && redPacketTotalObj != null && !"".equals(redPacketTotalObj.toString())
                                        && !"0".equals(redPacketTotalObj.toString())){
                                    //2.整合发送红包的参数
                                    Map<String, Object> redPacketParamMap = Maps.newHashMap();
                                    float redPacketTotalFloat = Float.parseFloat(redPacketTotalObj.toString() != "" ? redPacketTotalObj.toString() : "10");
                                    String redPacketTotal = ((int) (redPacketTotalFloat * 100)) + "";
                                    redPacketParamMap.put("amount", redPacketTotal);
                                    redPacketParamMap.put("openId", openIdObj.toString());
                                    redPacketParamMap.put("reUserName", NewMallCode.WX_MINI_PROGRAM_NAME);
                                    redPacketParamMap.put("wxPublicNumGhId", "gh_417c90af3488");
                                    redPacketParamMap.put("desc", NewMallCode.WX_MINI_PROGRAM_NAME + "发红包了，快来看看吧.");
                                    resultMapDTO = wxRedPacketService.enterprisePayment(redPacketParamMap);
                                    //3.将加油站操作记录表的状态变更为已处理
                                    Map<String, Object> oilStationOperatorMap_updateParam = Maps.newHashMap();
                                    oilStationOperatorMap_updateParam.put("id", oilStationOperatorMap.get("id"));
                                    oilStationOperatorMap_updateParam.put("status", "1");
                                    //4.发送成功，将已发送的红包进行记录，并保存.
                                    if(NewMallCode.SUCCESS.getNo() == resultMapDTO.getCode()){
                                        //更新加油站操作的红包状态
                                        paramMap.put("status", "1");
                                        oilStationOperatorDao.updateOilStationOperator(paramMap);
                                        //插入红包操作记录
                                        Map<String, Object> redPacketHistoryMap = Maps.newHashMap();
                                        redPacketHistoryMap.put("uid", uidObj.toString());
                                        redPacketHistoryMap.put("operatorId", id);
                                        redPacketHistoryMap.put("redPacketMoney", redPacketTotalObj.toString());
                                        redPacketHistoryMap.put("remark", "红包正常发送");
                                        redPacketHistoryMap.put("status", "1");
                                        redPacketHistoryService.addRedPacketHistory(redPacketHistoryMap);
                                    } else {
                                        //更新加油站操作的红包状态
                                        paramMap.put("status", "0");
                                        oilStationOperatorDao.updateOilStationOperator(paramMap);
                                        //插入红包操作记录
                                        Map<String, Object> redPacketHistoryMap = Maps.newHashMap();
                                        redPacketHistoryMap.put("uid", uidObj.toString());
                                        redPacketHistoryMap.put("operatorId", id);
                                        redPacketHistoryMap.put("redPacketMoney", redPacketTotalObj.toString());
                                        redPacketHistoryMap.put("remark", resultMapDTO.getMessage());
                                        redPacketHistoryMap.put("status", "0");
                                        redPacketHistoryService.addRedPacketHistory(redPacketHistoryMap);
                                    }
                                } else {
                                    resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_ID_OR_UID_IS_NOT_NULL_AND_REDPACKETTOTAL_SHOULD_LARGER_0.getNo());
                                    resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_ID_OR_UID_IS_NOT_NULL_AND_REDPACKETTOTAL_SHOULD_LARGER_0.getMessage());
                                }
                            } else {
                                resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_RED_PACKET_IS_NOT_EXIST_OR_CASHED.getNo());
                                resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_RED_PACKET_IS_NOT_EXIST_OR_CASHED.getMessage());
                            }
                        } else if(currentTime.before(startTime)){           //活动未开始
                            resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_IS_NOT_START.getNo());
                            resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_IS_NOT_START.getMessage());
                        } else if(currentTime.after(endTime)){              //活动已结束
                            resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_IS_END.getNo());
                            resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_IS_END.getMessage());
                        } else {
                            resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getNo());
                            resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getMessage());
                        }
                    } catch (Exception e) {
                        logger.info("解析红包活动的开始时间和结束时间失败，请联系管理员，查看红包活动心在字典中的数据.");
                        resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getNo());
                        resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getMessage());
                    }
                } else {
                    resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getNo());
                    resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getMessage());
                }
            } else {
                resultMapDTO.setCode(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.WX_RED_PACKET_ACTIVITY_INFO_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_ID_OR_UID_OR_OPERATOR_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_ID_OR_UID_OR_OPERATOR_IS_NOT_NULL.getMessage());
        }

        logger.info("在service中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

}
