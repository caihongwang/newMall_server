package com.br.newMall.api.code;


/**
 * 给前端的状态码
 *
 * @author caihongwang
 */
public class NewMallCode {

    private int no;
    private String message;

    public NewMallCode() {

    }

    public NewMallCode(int no, String message) {
        this.no = no;
        this.message = message;
    }

    public int getNo() {
        return no;
    }

    public String getMessage() {
        return message;
    }

    //system
    public static NewMallCode SUCCESS = new NewMallCode(0, "成功");
    public static NewMallCode SERVER_INNER_ERROR = new NewMallCode(10001, "服务异常,请稍后重试.");
    public static NewMallCode IS_USER_SESSION_OVERDUE = new NewMallCode(10002, "sessionKey已过期，请重新登录.");
    public static NewMallCode SESSION_VALUE_IS_NULL = new NewMallCode(10002, "sessionValue不存在，请稍后再试，请重新登录.");
    public static NewMallCode SESSION_KEY_IS_NOT_NULL = new NewMallCode(10003, "seesionKey不允许为空");
    public static NewMallCode WX_SERVER_INNER_ERROR = new NewMallCode(10004, "微信服务异常，无法获取openid和session_key.");
    public static NewMallCode WX_SERVER_INNER_ERROR_FOR_ACCESS_TOKEN = new NewMallCode(10004, "微信服务异常，无法获取access_token和expires_in.");
    public static NewMallCode WX_PARAM_IS_NOT_NULL = new NewMallCode(10005, "向微信服务器访问的必要参数不允许为空.");
    public static NewMallCode NO_DATA_CHANGE = new NewMallCode(10009, "没有数据发生更改.");

    //字典
    public static NewMallCode DIC_EXIST = new NewMallCode(30001, "字典已经存在，请修改。");
    public static NewMallCode DIC_TYPE_OR_CODE_OR_NAME_IS_NOT_NULL = new NewMallCode(30002, "字典的类型或者编码或者名称不能为空");
    public static NewMallCode DIC_ID_OR_CODE_IS_NOT_NULL = new NewMallCode(30003, "字典的ID或者编码不能为空");
    public static NewMallCode DIC_LIST_IS_NULL = new NewMallCode(0, "当前字典没有数据.");

    //常量值
    public static final int MSG_EXPIRED_TIME = 60 * 2;    //短信失效时间：2分钟
    public static final String REDIS_PREFIX = "sp:";
    public static final String REDIS_MSG_PREFIX = REDIS_PREFIX + "getVerificationCode:";//财富名片夹的短信在redis中的前缀
    public static final int USER_SESSION_EXPIRED_TIME = 86400;    //session失效时间：1天
    public static final String USER_SESSION_PREFIX = "user:session:";//财富名片夹的短信在redis中的前缀

    //申请财富名片夹--短信验证码
    public static NewMallCode USER_EXIST = new NewMallCode(0, "用户已经存在，请直接使用。");
    public static NewMallCode CARD_ERROR_PHONE_CAPTCHA = new NewMallCode(40001, "手机号或者验证码错误，请重新输入");
    public static NewMallCode PHONE_OR_CAPTCHA_IS_NOT_NULL = new NewMallCode(40002, "手机号或者验证码不能为空");
    public static NewMallCode CODE_IS_NOT_NULL = new NewMallCode(40004, "code不能为空");
    public static NewMallCode UID_COMMENT_IS_NOT_NULL = new NewMallCode(40005, "uid或者评论不能为空");
    public static NewMallCode PHONE_IS_NOT_NULL = new NewMallCode(40006, "手机号不能为空");

    //意见
    public static NewMallCode COMMENTS_LIST_IS_NULL = new NewMallCode(0, "当前用户没有反馈意见.");
    public static NewMallCode COMMENTS_NOT_MORE_200 = new NewMallCode(80001, "反馈意见不能超过200个字.");

    //公共模板
    public static NewMallCode PARAM_IS_NULL = new NewMallCode(60001, "必填参数不允许为空.");
    public static NewMallCode DECRYPT_IS_ERROR = new NewMallCode(60002, "解密手机号失败.");

    //用户
    public static NewMallCode USER_IS_NULL = new NewMallCode(70001, "用户不存在.");
    public static NewMallCode USER_PHONE_IS_ERROR = new NewMallCode(70004, "用户用户手机号错误，请重新输入.");
    public static NewMallCode USER_CODE_IS_NOT_NULL = new NewMallCode(70006, "用户微信访问的code参数不能为空.");
    public static NewMallCode USER_ID_OR_PRODUCTID_NULL = new NewMallCode(70007, "用户或者商品不存在.");
    public static NewMallCode USER_PROPORTION_OR_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER = new NewMallCode(180004, "用户提现时提现比例或者提现金额下限非数字.");
    public static NewMallCode USER_CASHMONEY_NOT_MORE_CASHMONEYLOWERLIMIT_IS_NOT_NUMBER = new NewMallCode(180004, "用户余额大于等于提现金额下限才可以提现.");


    //加油站
    public static NewMallCode OIL_QUERY_IS_NULL = new NewMallCode(130002, "获取加油站信息为空。");
    public static NewMallCode OIL_ADDRESS_QUERY_IS_NULL = new NewMallCode(130005, "您所处的位置不是加油站，请打赏一点开发小哥哥吧。");
    public static NewMallCode OIL_STATION_EXIST_AND_UPDATE = new NewMallCode(130003, "加油站已存在，并更新。");
    public static NewMallCode OIL_STATION_PARAM_IS_NOT_NULL = new NewMallCode(130004, "加油站必填参数不允许为空,相关参数请查看文档。");

    //微信的form_id
    public static NewMallCode USER_FORM_UID_OR_FORMID_IS_NOT_NULL = new NewMallCode(80001, "用户id或者微信的formId不能为空");
    public static NewMallCode USER_FORM_ID_OR_UID_IS_NOT_NULL = new NewMallCode(80002, "用户id或者uid不能为空");
    public static NewMallCode USER_FORM_LIST_IS_NULL = new NewMallCode(80003, "当前用户对应的formId没有数据.");

    //红包领取
    public static NewMallCode WX_RED_PACKET__ACTIVITY_IS_NOT_EXIST = new NewMallCode(150001, "红包活动不存在");
    public static NewMallCode WX_RED_PACKET__SEND_SUCCESS = new NewMallCode(150002, "红包发送成功");
    public static NewMallCode WX_RED_PACKET__SEND_FAILTURE = new NewMallCode(150003, "红包发送失败");
    public static NewMallCode WX_RED_PACKET__UID_OR_MONEY_IS_NOT_NULL = new NewMallCode(150004, "用户UID或者红包金额不允许为空");
    public static NewMallCode WX_RED_PACKET__HISTORY_IS_NULL = new NewMallCode(150005, "当前用户没有红包领取记录");
    public static NewMallCode RED_PACKET_SEND_IS_ERROR = new NewMallCode(50020, "红包状态发送异常，请联系管理员检查微信公众号相关配置.");
    public static NewMallCode RED_PACKET_SEND_IS_ERROR_BUT_RESPOSE_SUCCESS = new NewMallCode(50020, "发送红包响应正常，但是发送失败.");
    //红包提现
    public static NewMallCode WX_RED_PACKET_DRAW_CASH__ACTIVITY_IS_NOT_EXIST = new NewMallCode(160001, "红包活动不存在");
    public static NewMallCode WX_RED_PACKET_DRAW_CASH__SEND_SUCCESS = new NewMallCode(160002, "红包发送成功");
    public static NewMallCode WX_RED_PACKET_DRAW_CASH__SEND_FAILTURE = new NewMallCode(160003, "红包发送失败");
    public static NewMallCode WX_RED_PACKET_DRAW_CASH__UID_OR_PHONE_OR_MONEY_IS_NOT_NULL = new NewMallCode(160004, "用户uid或者用户phone或者红包金额不允许为空");
    public static NewMallCode WX_RED_PACKET_DRAW_CASH__HISTORY_IS_NULL = new NewMallCode(160005, "当前用户没有红包提现记录");
    public static NewMallCode WX_RED_PACKET_ACTIVITY_IS_NOT_START = new NewMallCode(160006, "红包活动尚未开始");
    public static NewMallCode WX_RED_PACKET_ACTIVITY_IS_END = new NewMallCode(160007, "红包活动已经结束");
    public static NewMallCode WX_RED_PACKET_ACTIVITY_INFO_ERROR = new NewMallCode(160008, "红包活动出现异常，请联系客服.");

    //加油站操作
    public static NewMallCode OIL_STATION_OPERATOR_ID_IS_NOT_NULL = new NewMallCode(170001, "加油站操作ID不允许为空.");
    public static NewMallCode OIL_STATION_OPERATOR_UID_OILSTATIONCODE_OPERATOR_IS_NOT_NULL = new NewMallCode(170002, "加油站操作UID,加油站编码，操作名称不允许为空.");
    public static NewMallCode OIL_STATION_OPERATOR_LIST_IS_NULL = new NewMallCode(170003, "当前用户没有加油站操作.");
    public static NewMallCode OIL_STATION_OPERATOR_IS_EXIST = new NewMallCode(170004, "当前用户的加油站操作已存在.");
    public static NewMallCode OIL_STATION_OPERATOR_ID_OR_UID_OR_OPERATOR_IS_NOT_NULL = new NewMallCode(170005, "当前操作的id或者uid不允许为空.");
    public static NewMallCode OIL_STATION_OPERATOR_RED_PACKET_IS_NOT_EXIST_OR_CASHED = new NewMallCode(170006, "当前红包已不存在，或许已被领取.");
    public static NewMallCode OIL_STATION_OPERATOR_ID_OR_UID_IS_NOT_NULL_AND_REDPACKETTOTAL_SHOULD_LARGER_0 = new NewMallCode(170007, "uid或者openId不允许为空，同事红包金额必须大于0.");
    public static NewMallCode CURRENT_PUBLIC_NUMBER_OPENID_IS_NOT_NULL = new NewMallCode(170008, "当前公众号没有粉丝，发送消息无效.");

    //商品
    public static NewMallCode PRODUCT_TITLE_OR_DEGIST_OR_STOCK_OR_HEADIMGURL_OR_DESCRIBEIMGURL_OR_PRICE_IS_NOT_NULL = new NewMallCode(180001, "商品标题或者简介或者库存或者预览图或者详图或者价格不允许为空.");
    public static NewMallCode PRODUCT_EXIST = new NewMallCode(180002, "商品已经存在.");
    public static NewMallCode PRODUCT_ID_IS_NOT_NULL = new NewMallCode(180003, "商品ID不允许为空.");
    public static NewMallCode PRODUCT_LIST_IS_NULL = new NewMallCode(180004, "没有更多商品.");
    public static NewMallCode PRODUCT_TYPE_IS_NULL = new NewMallCode(180005, "商品的类型不允许为空.");

    //地址
    public static NewMallCode ADDRESS_UID_OR_NAME_OR_PHONE_OR_PROVINCEID_OR_PROVINCENAME_OR_CITYID_OR_CITYNAME_OR_REGIONID_OR_REGIONNAME_OR_STREETID_OR_STREETNAME_OR_DETAILADDRESS_IS_NOT_NULL = new NewMallCode(190001, "地址的uid或者收货姓名或者收货电话或者省份ID或者城市ID或者城市名称或者地区ID或者地区名称或者街道ID或者街道名称或者详细地址不允许为空.");
    public static NewMallCode ADDRESS_EXIST = new NewMallCode(190002, "地址已经存在.");
    public static NewMallCode ADDRESS_ID_IS_NOT_NULL = new NewMallCode(190003, "地址ID不允许为空.");
    public static NewMallCode ADDRESS_LIST_IS_NULL = new NewMallCode(190004, "没有更多地址.");
    public static NewMallCode ADDRESS_PROVINCETYPE_IS_NULL = new NewMallCode(190005, "地址的省份类型不允许为空.");
    public static NewMallCode ADDRESS_CITYTYPE_OR_PROVINCEID_IS_NULL = new NewMallCode(190006, "地址的城市类型或者省份ID不允许为空.");
    public static NewMallCode ADDRESS_REGIONTYPE_OR_CITYID_IS_NULL = new NewMallCode(190007, "地址的区域类型或者城市ID不允许为空.");
    public static NewMallCode ADDRESS_REGIONTYPE_OR_REGIONID_IS_NULL = new NewMallCode(190008, "地址的区域类型或者区域ID不允许为空.");
    public static NewMallCode ADDRESS_UID_IS_NOT_NULL = new NewMallCode(190009, "地址UID不允许为空.");
    public static NewMallCode ADDRESS_ID_OR_UID_IS_NOT_NULL = new NewMallCode(190003, "地址ID或者UID不允许为空.");


    //订单
    public static NewMallCode ORDER_UID_OR_PAYMONEY_IS_NOT_NULL = new NewMallCode(190001, "订单的uid或者支付金额不允许为空.");
    public static NewMallCode ORDER_ID_IS_NOT_NULL = new NewMallCode(190002, "订单的ID不允许为空.");
    public static NewMallCode ORDER_LIST_IS_NULL = new NewMallCode(190003, "没有更多订单.");
    public static NewMallCode ORDER_RESPONSE_UNIFIEDORDER_IS_ERROR = new NewMallCode(190004, "微信的统一订单方式支付请求失败。");
    public static NewMallCode ORDER_PAY_MONEY_IS_NOT_NULL = new NewMallCode(190005, "支付金额不允许为空，请输入您的金额。");
    public static NewMallCode ORDER_PRODUCTNUM_OR_PRODUCTID_IS_NOT_NULL = new NewMallCode(190006, "购买的商品数量或者商品ID不允许为空。");
    public static NewMallCode ORDER_USER_INTEGRAL_IS_NOT_ENOUGH = new NewMallCode(190007, "用户积分不足。");
    public static NewMallCode ORDER_OPENID_OR_SPBILLCREATEIP_OR_PRODUCTID_OR_PRODUCTNUM_OR_ADDRESSID_IS_NOT_NULL = new NewMallCode(190008, "openId或者访问域名或者商品ID或者商品数量或者地址ID不允许为空。");
    public static NewMallCode ORDER_OPENID_OR_WXORDERID_OR_ATTACH_IS_NOT_NULL = new NewMallCode(190009, "openId或者微信订单号或者附加参数不允许为空.");
    public static NewMallCode ORDER_UID_SHOPID_SPBILLCREATEIP_IS_NOT_NULL = new NewMallCode(190010, "付款用户uid或者店铺id或者交易IP不允许为空.");
    public static NewMallCode ORDER_PRODUCT_STOCK_IS_NOT_ENOUGH = new NewMallCode(190011, "购买的商品库存不足.");

    //抽奖
    public static NewMallCode LUCKDRAW_LUCKDRAWTYPE_IS_NULL = new NewMallCode(200001, "抽奖的产品类型不允许为空.");
    public static NewMallCode LUCKDRAW_UID_OR_WXORDERID_IS_NULL = new NewMallCode(200001, "抽奖的用户uid或者微信订单编号不允许为空.");
    public static NewMallCode LUCKDRAW_WXORDERID_IS_NOT_EXIST = new NewMallCode(200001, "待返现订单不存在或者已被奖励.");
    public static NewMallCode LUCKDRAW_ORDER_IS_NOT_PAYED = new NewMallCode(200001, "抽奖的微信订单还未付款.");
    public static NewMallCode LUCKDRAW_ID_IS_NOT_NULL = new NewMallCode(200001, "抽奖ID不允许为空.");
    public static NewMallCode LUCKDRAW_LIST_IS_NULL = new NewMallCode(0, "没有更多抽奖信息.");
    public static NewMallCode LUCKDRAW_PRODUCT_IS_NULL = new NewMallCode(180004, "没有更多抽奖产品.");
    public static NewMallCode LUCKDRAW_GETPRIZE_IS_FAILED = new NewMallCode(180004, "抽奖失败，再抽一次吧.");
    public static NewMallCode LUCKDRAW_GETPRIZE_HAS_GETED = new NewMallCode(180004, "您已抽过奖。如想再次抽奖，请再交易一笔订单.");
    public static NewMallCode LUCKDRAW_SHOPID_OR_UID_IS_NOT_NULL = new NewMallCode(180004, "您待领取领奖励的商家ID或者用户UID不允许为空.");
    public static NewMallCode LUCKDRAW_UID_IS_NOT_NULL = new NewMallCode(180004, "您待领取领奖励的用户UID不允许为空.");
    public static NewMallCode LUCKDRAW_UPDATE_STATUS_IS_FAILED = new NewMallCode(180004, "转换积分时更新奖励状态失败.");
    public static NewMallCode LUCKDRAW_UPDATE_USER_INEGRAL_IS_FAILED = new NewMallCode(180004, "转换用户积分时更新用户积分失败.");
    public static NewMallCode LUCKDRAW_UPDATE_USER_BANLANCE_IS_FAILED = new NewMallCode(180004, "转换用户余额时更新用户余额失败.");
    public static NewMallCode LUCKDRAW_BALANCE_OR_PAYMONEY_OR_PROPORTION_IS_NOT_NUMBER = new NewMallCode(180004, "转换用户余额时支付金额或者用户余额或者返现比例非数字.");
    public static NewMallCode LUCKDRAW_BALANCE_PROPORTION_IS_NOT_NUMBER = new NewMallCode(180004, "转换用户余额时返现比例不允许为空.");

    //店铺
    public static NewMallCode SHOP_EXIST = new NewMallCode(30001, "店铺已经存在，请修改。");
    public static NewMallCode SHOP_SHOPDISCOUNTID_SHOPTITLE_SHOPDEGIST_SHOPPHONE_SHOPADDRESS_SHOPLON_SHOPLAT_SHOPHEADIMGURL_SHOPDESCRIBEIMGURL_IS_NOT_NULL = new NewMallCode(30002, "店铺的折扣ID或者名称或者店铺地址或者店铺经纬度或者店铺头像地址或者店铺描述图片地址不能为空");
    public static NewMallCode SHOP_ID_IS_NOT_NULL = new NewMallCode(30003, "店铺的ID不能为空");
    public static NewMallCode SHOP_LIST_IS_NULL = new NewMallCode(0, "当前店铺没有数据.");
    public static NewMallCode SHOP_UID_IS_NOT_NULL = new NewMallCode(0, "查询店铺的用户UID不能为空.");
    public static NewMallCode SHOP_UID_NICKNAME_SHOPTITLE_PAGE_SCENE_FILEPATH_IS_NOT_NULL = new NewMallCode(0, "店铺小程序码的用户uid或者微信昵称或者店铺名称或者小程序页面或者小程序码存放路径不能为空.");
    public static NewMallCode SHOP_UID_NICKNAME_SHOPTITLE_IS_NOT_EXIST_SHOP = new NewMallCode(0, "店铺小程序码的用户uid或者微信昵称或者店铺名称不存在店铺.");
    public static NewMallCode SHOP_SHOPDISCOUNTID_IS_NOT_NULL = new NewMallCode(30003, "店铺与平台之间的折扣值为空，请联系平台客服人员.");
    public static NewMallCode SHOP_SHOPDISCOUNTID_IS_NOT_EXIST = new NewMallCode(30003, "店铺与平台之间的折扣值不存在，请联系平台客服人员.");
    public static NewMallCode SHOP_SHOPDISCOUNTID_IS_NOT_NUM = new NewMallCode(30003, "店铺与平台之间的折扣值不是数字，请联系平台客服人员.");

    //加盟
    public static NewMallCode LEAGUE_UID_OR_PHONE_OR_NAME_OR_LEAGUETYPECODE_IS_NOT_NULL = new NewMallCode(30002, "加盟的uid或者手机号或者姓名或者加盟类型不能为空");
    public static NewMallCode LEAGUE_ID_IS_NOT_NULL = new NewMallCode(30003, "加盟的ID不能为空");
    public static NewMallCode LEAGUE_TYPE_IS_NULL = new NewMallCode(180005, "加盟类型不允许为空.");
    public static NewMallCode LEAGUE_LIST_IS_NULL = new NewMallCode(0, "当前加盟没有数据.");

    //提现日志
    public static NewMallCode cashlog_EXIST = new NewMallCode(30001, "字典已经存在，请修改。");
    public static NewMallCode CASHLOG_UID_OR_CASHTOWXMONEY_OR_CASHFEE_OR_USERBALANCE_IS_NOT_NULL = new NewMallCode(30002, "提现日志的用户uid或者提现金额或者提现手续费或者用户余额不能为空");
    public static NewMallCode CASHLOG_ID_IS_NOT_NULL = new NewMallCode(30003, "提现日志的ID不能为空");
    public static NewMallCode CASHLOG_UID_IS_NOT_NULL = new NewMallCode(30003, "提现日志的用户UID不能为空");
    public static NewMallCode CASHLOG_LIST_IS_NULL = new NewMallCode(0, "当前体现日志没有数据.");





    //微信小程序
    public static final String WX_MINI_PROGRAM_NAME = "油价地图";     //小程序名称
    public static final String WX_MINI_PROGRAM_GH_ID = "gh_417c90af3488";     //小程序原始ID
    public static final String WX_MINI_PROGRAM_APPID = "wx07cf52be1444e4b7";     //appid
    public static final String WX_MINI_PROGRAM_SECRET = "d6de12032cfe660253b96d5f2868a06c";    //secret
    public static final String WX_MINI_PROGRAM_GRANT_TYPE_FOR_OPENID = "authorization_code";    //grant_type
    public static final String WX_MINI_PROGRAM_GRANT_TYPE_FOR_ACCESS_TOKEN = "client_credential";    //grant_type

    //微信公众号
    public static final String WX_PUBLIC_NUMBER_APPID = "wxf768b49ad0a4630c";                   //appid
    public static final String WX_PUBLIC_NUMBER_SECRET = "a481dd6bc40c9eec3e57293222e8246f";    //secret

    //微信自定义信息发送，适用于小程序员和公众号
    public static final String WX_CUSTOM_MESSAGE_HOST = "https://api.weixin.qq.com";     //host
    public static final String WX_CUSTOM_MESSAGE_PATH = "/cgi-bin/message/custom/send?access_token=";     //path
    public static final String WX_CUSTOM_MESSAGE_METHOD = "POST";                        //method

    //本服务器域名
    public static final String THE_DOMAIN = "https://www.91caihongwang.com/";        // 域名  线上


    //微信支付
    public static final String WX_PAY_API_SECRET = "Caihongwang52013Caihongwang52014";     //微信支付 api secret
    public static final String WX_PAY_MCH_ID = "1345780701";     //微信支付 商家ID
    public static final String WX_PAY_CERT_PATH = "/Users/caihongwang/Desktop/cert/apiclient_cert.p12";     //微信支付 商家证书
//    public static final String WX_PAY_DOMAIN = "http://172.30.5.91:8080/newMall";        //微信支付 域名  本机
    public static final String WX_PAY_DOMAIN = "https://www.91caihongwang.com/newMall";        //微信支付 域名  线上
    public static final String WX_RED_PACK_NUMBER = "1";     //微信红包总数：1个
    public static final String WX_PAY_NOTIFY_URL_wxPayNotifyForPayTheBillInMiniProgram = "/newMall/wxOrder/wxPayNotifyForPayTheBillInMiniProgram.do";  //支付成功后的服务器回调url
    public static final String WX_PAY_NOTIFY_URL_wxPayNotifyForPurchaseProductInMiniProgram = "/newMall/wxOrder/wxPayNotifyForPurchaseProductInMiniProgram.do";  //购买成功后的服务器回调url

    public static final String WX_PAY_TRADE_TYPE = "JSAPI";  //交易类型

    //阿里常量值
    public static final String A_LI_YUN_HOST = "http://oil.market.alicloudapi.com";     //host
    public static final String A_LI_YUN_PATH = "/oil/local";                          //经纬度path
    public static final String A_LI_YUN_PATH_BY_LONLAT = "/oil/local";                          //经纬度path
    public static final String A_LI_YUN_PATH_BY_CITY = "/oil/region";                            //城市path
    public static final String A_LI_YUN_METHOD = "POST";                                //method
    public static final String A_LI_YUN_APP_CODE = "d725e59af6564dd89f0e6fede24c2427";     //app code
    public static final String A_LI_YUN_CONTENT_TYPE = "application/json; charset=UTF-8";     //Content-Type

    //腾讯常量值   绑定账号QQ:976499921,手机号：17701359899
    public static final String TENCENT_HOST = "https://apis.map.qq.com";     //host
    public static final String TENCENT_PATH_GET_ADDR = "/ws/geocoder/v1/";
    public static final String TENCENT_PATH_GET_SEARCH = "/ws/place/v1/search";
    public static final String TENCENT_METHOD = "GET";                                //method
    public static final String TENCENT_CONTENT_TYPE = "application/json; charset=UTF-8";     //Content-Type
    public static final String TENCENT_KEY = "Z73BZ-ISKC4-WMDU3-DO2UD-KCPIZ-JIFQI";     //key
    public static final String TENCENT_KEY_WORD_SEARCH = "http://apis.map.qq.com/ws/place/v1/suggestion/";     //关键词查找

    public static final String TENCENT_APP_ID = "11641";     //id
    public static final String TENCENT_SECRET_KEY = "g2RT4RVE3hy2YEYfWqYN3wWFys6";     //key
    public static final String TENCENT_URL = "https://openai.qq.com/api/json/ai/GetMultiAI";     //host
    public static final String TENCENT_CvRectDetect = "CvRectDetect";     //区域检测

}