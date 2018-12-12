package com.br.newMall.center.handler;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.CommonService;
import com.br.newMall.center.service.WX_RedPacketService;
import com.br.newMall.center.service.WX_SourceMaterialService;
import com.br.newMall.center.utils.MapUtil;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 红包Handler
 */
public class WX_SourceMaterialHandler implements com.br.newMall.api.service.WX_SourceMaterialHandler.Iface {

    private static final Logger logger = LoggerFactory.getLogger(WX_SourceMaterialHandler.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private WX_SourceMaterialService wx_SourceMaterialService;

    /**
     * 获取红包二维码
     *
     * @param tid
     * @param paramMap
     * @return
     * @throws TException
     */
    @Override
    public ResultMapDTO batchGetMaterial(int tid, Map<String, String> paramMap) throws TException {
        logger.info("在hanlder中获取素材列表-batchGetMaterial,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> objectParamMap = MapUtil.getObjectMap(paramMap);
        try {
            resultMapDTO = wx_SourceMaterialService.batchGetMaterial(objectParamMap);
        } catch (Exception e) {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            logger.error("在hanlder中获取素材列表-batchGetMaterial is error, paramMap : " + paramMap + ", e : " + e);
        }
        logger.info("在hanlder中获取素材列表-batchGetMaterial,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

}
