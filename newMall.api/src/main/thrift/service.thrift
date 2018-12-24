namespace java com.br.newMall.api.service

include "dto.thrift"

typedef i16 short
typedef i32 int
typedef i64 long

service CommonHandler {

    dto.ResultMapDTO getDecryptPhone(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO sendTemplateMessageForMiniProgram(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO sendTemplateMessageForWxPublicNumber(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getOpenIdAndSessionKeyForWX(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getSignatureAndJsapiTicketAndNonceStrForWxPublicNumber(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO receviceAndSendCustomMessage(1:int tid, 2:map<string, string> paramMap)

}

service WX_UserHandler {

    dto.ResultMapDTO login(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateUser(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO checkSession(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleUserByCondition(1:int tid, 2:map<string, string> paramMap),

    dto.MessageDTO getCheckVerificationCode(1:int tid, 2:map<string, string> paramMap)

}

service CommentsHandler {

    dto.BoolDTO addComments(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteComments(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateComments(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleCommentsByCondition(1:int tid, 2:map<string, string> paramMap)
}

service WX_DicHandler {

    dto.BoolDTO addDic(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteDic(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateDic(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleDicByCondition(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getMoreDicByCondition(1:int tid, 2:map<string, string> paramMap)
}

service OilStationHandler {

    dto.BoolDTO addOrUpdateOilStation(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getOilStationList(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getOilStationByLonLat(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getOneOilStationByCondition(1:int tid, 2:map<string, string> paramMap)

}

service WX_PayHandler {

    dto.ResultMapDTO unifiedOrderPay(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getOauth(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getToOauthUrl(1:int tid, 2:map<string, string> paramMap),

}

service WX_RedPacketHandler {

    dto.ResultMapDTO getRedPacketQrCode(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getOauth(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getToOauthUrl(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO enterprisePayment(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO sendRedPacket(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO sendGroupRedPacket(1:int tid, 2:map<string, string> paramMap)

}

service BannerHandler {

    dto.ResultDTO getActivityBanner(1:int tid, 2:map<string, string> paramMap)
}

service RedPacketDrawCashHistoryHandler {

    dto.ResultMapDTO getDrawCashMoneyTotal(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getRedPacketDrawCashHistory(1:int tid, 2:map<string, string> paramMap)
}

service RedPacketHistoryHandler {

    dto.ResultMapDTO getAllRedPacketMoneyTotal(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getRedPacketHistoryList(1:int tid, 2:map<string, string> paramMap)
}

service OilStationOperatorHandler {

    dto.ResultMapDTO cashOilStationOperatorRedPacket(1:int tid, 2:map<string, string> paramMap)
}


service WX_CustomMenuHandler {

    dto.ResultMapDTO getCustomMenu(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO createCustomMenu(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO deleteCustomMenu(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO createPersonalMenu(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO deletePersonalMenu(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getCurrentSelfMenuInfo(1:int tid, 2:map<string, string> paramMap)
}

service WX_SourceMaterialHandler {

    dto.ResultMapDTO batchGetMaterial(1:int tid, 2:map<string, string> paramMap)
}

service WX_ProductHandler {

    dto.ResultDTO getProductTypeList(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO addProduct(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteProduct(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateProduct(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleProductByCondition(1:int tid, 2:map<string, string> paramMap)

}

service WX_AddressHandler {

    dto.BoolDTO setDefaultAddress(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getProvinceList(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getCityList(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getRegionList(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getStreetList(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO addAddress(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteAddress(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateAddress(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleAddressByCondition(1:int tid, 2:map<string, string> paramMap)

}

service WX_OrderHandler {

    dto.ResultMapDTO purchaseProductInMiniProgram(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO wxPayNotifyForPurchaseProductInMiniProgram(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO payTheBillInMiniProgram(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO wxPayNotifyForPayTheBillInMiniProgram(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO addOrder(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteOrder(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateOrder(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleOrderByCondition(1:int tid, 2:map<string, string> paramMap)

}

service WX_LuckDrawHandler {

    dto.ResultDTO getLuckDrawProductList(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO addLuckDraw(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteLuckDraw(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateLuckDraw(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleLuckDrawByCondition(1:int tid, 2:map<string, string> paramMap)

}

service WX_ShopHandler {

    dto.BoolDTO addShop(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO deleteShop(1:int tid, 2:map<string, string> paramMap),

    dto.BoolDTO updateShop(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getSimpleShopByCondition(1:int tid, 2:map<string, string> paramMap),

    dto.ResultDTO getShopByCondition(1:int tid, 2:map<string, string> paramMap),

    dto.ResultMapDTO getMiniProgramCode(1:int tid, 2:map<string, string> paramMap)

}




