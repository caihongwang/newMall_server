package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_CustomMenuService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信公众号自定义菜单service
 */
@Service
public class WX_CustomMenuServiceImpl implements WX_CustomMenuService {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    /**
     * 获取公众号自定义菜单
     * @param paramMap
     */
    @Override
    public ResultMapDTO getCustomMenu(Map<String, Object> paramMap) {
        logger.info("在service中获取公众号自定义菜单-getRedPacketQrCode,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap = WX_PublicNumberUtil.getCustomMenu();
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中获取公众号自定义菜单-getRedPacketQrCode,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 创建公众号自定义菜单
     * @param paramMap
     */
    @Override
    public ResultMapDTO createCustomMenu(Map<String, Object> paramMap) {
        logger.info("在service中创建公众号自定义菜单-createCustomMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String menuStr = paramMap.get("menuStr")!=null?paramMap.get("menuStr").toString():"";
        if("".equals(menuStr)){
            menuStr = "{\n" +
                    "    \"button\":[\n" +
                    "        {\n" +
                    "            \"name\":\"便利商铺\",\n" +
                    "            \"sub_button\":[\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"装逼神器\",\n" +
                    "                    \"url\":\"https://www.91caihongwang.com/newMall/index.html\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"免费WIFI\",\n" +
                    "                    \"url\":\"http://wifi.weixin.qq.com/mbl/connect.xhtml?type=1\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"自动售货机\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/3SL6WEIKwB9Q8dwXEx1gFg\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"type\":\"miniprogram\",\n" +
                    "            \"name\":\"惠生活\",\n" +
                    "            \"url\":\"http://mp.weixin.qq.com\",\n" +
                    "            \"appid\":\"wx54847eda0638538e\",\n" +
                    "            \"pagepath\":\"pages/tabBar/todayOilPrice/todayOilPrice\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\":\"关于我们\",\n" +
                    "            \"sub_button\":[\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"我在这里\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/ifBVk8VBUci1yPCVUug4hg\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"营业执照\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/9OZP7KFScJkXBa9JslGirg\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}\n" +
                    "\n";
        }
        resultMap = WX_PublicNumberUtil.createCustomMenu(menuStr);
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中创建公众号自定义菜单-createCustomMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 删除公众号自定义菜单
     * @param paramMap
     */
    @Override
    public ResultMapDTO deleteCustomMenu(Map<String, Object> paramMap) {
        logger.info("在service中删除公众号自定义菜单-deleteCustomMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap = WX_PublicNumberUtil.deleteCustomMenu();
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中删除公众号自定义菜单-deleteCustomMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 创建公众号个性化菜单
     * @param paramMap
     */
    @Override
    public ResultMapDTO createPersonalMenu(Map<String, Object> paramMap) {
        logger.info("在service中创建公众号个性化菜单-createPersonalMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String menuStr = paramMap.get("menuStr")!=null?paramMap.get("menuStr").toString():"";
        if("".equals(menuStr)){
            menuStr = "{\n" +
                    "    \"button\":[\n" +
                    "        {\n" +
                    "            \"name\":\"便利商铺\",\n" +
                    "            \"sub_button\":[\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"装逼神器\",\n" +
                    "                    \"url\":\"https://www.91caihongwang.com/newMall/index.html\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"免费WIFI\",\n" +
                    "                    \"url\":\"http://wifi.weixin.qq.com/mbl/connect.xhtml?type=1\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"自动售货机\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/3SL6WEIKwB9Q8dwXEx1gFg\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"type\":\"miniprogram\",\n" +
                    "            \"name\":\"惠生活\",\n" +
                    "            \"url\":\"http://mp.weixin.qq.com\",\n" +
                    "            \"appid\":\"wx54847eda0638538e\",\n" +
                    "            \"pagepath\":\"pages/tabBar/todayOilPrice/todayOilPrice\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\":\"关于我们\",\n" +
                    "            \"sub_button\":[\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"我在这里\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/ifBVk8VBUci1yPCVUug4hg\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"type\":\"view\",\n" +
                    "                    \"name\":\"营业执照\",\n" +
                    "                    \"url\":\"https://mp.weixin.qq.com/s/9OZP7KFScJkXBa9JslGirg\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}\n" +
                    "\n";
        }
        resultMap = WX_PublicNumberUtil.createPersonalMenu(menuStr);
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中创建公众号个性化菜单-createPersonalMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 删除公众号个性化菜单
     * @param paramMap
     */
    @Override
    public ResultMapDTO deletePersonalMenu(Map<String, Object> paramMap) {
        logger.info("在service中删除公众号个性化菜单-deletePersonalMenu,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String menuId = "9527";
        resultMap = WX_PublicNumberUtil.deletePersonalMenu(menuId);
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中删除公众号个性化菜单-deletePersonalMenu,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 获取公众号自定义菜单配置接口
     * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
     * @param paramMap
     */
    @Override
    public ResultMapDTO getCurrentSelfMenuInfo(Map<String, Object> paramMap) {
        logger.info("在service中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,请求-paramMap:" + paramMap);
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap = WX_PublicNumberUtil.getCurrentSelfMenuInfo();
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中获取公众号自定义菜单配置接口-getCurrentSelfMenuInfo,响应-response:" + resultMapDTO);
        return resultMapDTO;
    }
}
