package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.WX_DicService;
import com.br.newMall.dao.OilStationOperatorDao;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Map;

/**
 * Created by caihongwang on 2018/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class OilStationOperatorServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(OilStationOperatorServiceImpl.class);

    @Autowired
    private OilStationOperatorDao oilStationOperatorDao;

    @Autowired
    private WX_DicService wxDicService;

    @Test
    public void Test() throws Exception {
        Map<String, Object> oilStationOperator_paramMap = Maps.newHashMap();
        oilStationOperator_paramMap.put("status", "0");
        oilStationOperator_paramMap.put("uid", "1");
        oilStationOperator_paramMap.put("operator", "updateOilStation");
        oilStationOperator_paramMap.put("oilStationCode", "1000001");
        addOilStationOperator(oilStationOperator_paramMap);
    }

    public BoolDTO addOilStationOperator(Map<String, Object> paramMap) {
        Integer addNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String uid = paramMap.get("uid") != null ? paramMap.get("uid").toString() : "";
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        String operator = paramMap.get("operator") != null ? paramMap.get("operator").toString() : "";
        if (!"".equals(uid) && !"".equals(oilStationCode) && !"".equals(operator)) {
            //1.根据operator获取红包金额redPacketTotal
            String redPacketTotal = "0";
            Map<String, Object> dicParamMap = Maps.newHashMap();
            dicParamMap.put("dicCode", operator);
            dicParamMap.put("dicType", "oilStationOperator");
            ResultDTO dicResultDto = wxDicService.getSimpleDicByCondition(dicParamMap);
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
            addNum = oilStationOperatorDao.addOilStationOperator(paramMap);
            if (addNum != null && addNum > 0) {
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setCode(NewMallCode.OIL_STATION_OPERATOR_UID_OILSTATIONCODE_OPERATOR_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.OIL_STATION_OPERATOR_UID_OILSTATIONCODE_OPERATOR_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加对加油站的操作-addOilStationOperator,结果-result:" + boolDTO);
        return boolDTO;
    }

}
