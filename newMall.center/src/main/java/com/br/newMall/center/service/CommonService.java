package com.br.newMall.center.service;

import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import org.apache.thrift.TException;

import java.util.Map;

public interface CommonService {

    /**
     * 发送公众号的模板消息
     *
     * @param paramMap
     */
    public ResultMapDTO sendTemplateMessageForWxPublicNumber(Map<String, Object> paramMap);

    /**
     * 发送小程序名片的模板消息
     *
     * @param paramMap
     */
    public ResultMapDTO sendTemplateMessageForMiniProgram(Map<String, Object> paramMap);

    /**
     * 获取openId和sessionKey
     */
    public ResultMapDTO getOpenIdAndSessionKeyForWX(Map<String, Object> paramMap);

    /**
     * 获取Signature和JsapiTicket和NonceStr
     */
    public ResultMapDTO getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber(Map<String, Object> paramMap);

    /**
     * 接受小程序端发送过来的消息，同时对特定的消息进行回复小程序的固定客服消息
     *
     * @param paramMap
     * @return
     * @throws TException
     */
    public ResultMapDTO receviceAndSendCustomMessage(Map<String, Object> paramMap);

}
