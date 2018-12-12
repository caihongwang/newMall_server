package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.center.service.BannerService;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * banner Handler
 */
public class BannerHandler implements com.br.newMall.api.service.BannerHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(BannerHandler.class);

    @Autowired
    private BannerService bannerService;

    @Override
    public ResultDTO getActivityBanner(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取活跃的banner-getActivityBanner,请求-paramMap:" + paramMap);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        if (paramMap.size() > 0) {
            try {
                resultDTO = bannerService.getActivityBanner(objectParamMap);
            } catch (Exception e) {
                List<Map<String, String>> resultList = Lists.newArrayList();
                resultDTO.setResultListTotal(0);
                resultDTO.setResultList(resultList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
                logger.error("在hanlder中获取活跃的banner-getActivityBanner is error, paramMap : " + paramMap + ", e : " + e);
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在hanlder中获取活跃的banner-getActivityBanner,响应-response:" + resultDTO);
        return resultDTO;
    }
}
