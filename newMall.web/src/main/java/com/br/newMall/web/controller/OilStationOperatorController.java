package com.br.newMall.web.controller;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.api.service.OilStationOperatorHandler;
import com.br.newMall.web.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/oilStationOperator", produces = "application/json;charset=utf-8")
public class OilStationOperatorController {

    private static final Logger logger = LoggerFactory.getLogger(OilStationOperatorController.class);

    @Autowired
    private OilStationOperatorHandler.Client oilStationOperatorHandler;

    @RequestMapping("/cashOilStationOperatorRedPacket")
    @ResponseBody
    public Map<String, Object> cashOilStationOperatorRedPacket(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取请求参数能够获取到并解析
        paramMap = HttpUtil.getRequestParams(request);
        logger.info("在controller中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,请求-paramMap:" + paramMap);
        try {
            ResultMapDTO resultMapDTO = oilStationOperatorHandler.cashOilStationOperatorRedPacket(0, paramMap);
            resultMap.put("code", resultMapDTO.getCode());
            resultMap.put("message", resultMapDTO.getMessage());
        } catch (Exception e) {
            logger.error("在controller中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket is error, paramMap : " + paramMap + ", e : " + e);
            resultMap.put("code", NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMap.put("message", NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在controller中领取或者提现加油站操作红包-cashOilStationOperatorRedPacket,响应-response:" + resultMap);
        return resultMap;
    }

}
