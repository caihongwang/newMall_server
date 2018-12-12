package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.BoolDTO;
import com.br.newMall.api.dto.ResultDTO;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.po.OilStationPO;
import com.br.newMall.center.service.OilStationOperatorService;
import com.br.newMall.center.service.OilStationService;
import com.br.newMall.center.utils.HttpsUtil;
import com.br.newMall.center.utils.ImportExcelUtil;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.dao.OilStationDao;
import com.br.newMall.dao.OilStationOperatorDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 加油站service
 */
@Service
public class OilStationServiceImpl implements OilStationService {

    private static final Logger logger = LoggerFactory.getLogger(OilStationServiceImpl.class);

    @Autowired
    private OilStationDao oilStationDao;

    @Autowired
    private OilStationOperatorDao oilStationOperatorDao;

    @Autowired
    private OilStationOperatorService oilStationOperatorService;

    @Autowired
    HttpsUtil httpsUtil;

    /**
     * 获取加油站列表
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getOilStationList(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, Object>> oilStationList = Lists.newArrayList();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        Double lon = Double.parseDouble(paramMap.get("lon") != null ? paramMap.get("lon").toString() : "0");
        Double lat = Double.parseDouble(paramMap.get("lat") != null ? paramMap.get("lat").toString() : "0");
        Double page = Double.parseDouble(paramMap.get("page") != null ? paramMap.get("page").toString() : "1");           //默认一页查询
        Double r = Double.parseDouble(paramMap.get("r") != null ? paramMap.get("r").toString() : "10000");                //默认10公里范围

        if (!"".equals(lon.toString()) && !"".equals(lat.toString()) &&
                !"".equals(page.toString()) && !"".equals(r.toString())) {
            //1.1网络请求中根据经纬度获取附近的加油站,并更新数据库
            oilStationStrList.addAll(LonLatUtil.getOilStationListByLonLat(lon, lat, page, r));
            if (oilStationStrList != null && oilStationStrList.size() > 0) {
                logger.info("根据经纬度获取附近的加油站, 附近加油站数量为：" + oilStationStrList.size());
            } else {                                //如果通过经纬点10公里范围之内都没有获取到加油站，那么直接获取这座城加油站
                //1.2网络请求中根据经纬度获取这座城市附近的加油站,并更新数据库
                logger.info("根据经纬度获取附近的加油站, 附近加油站数量为：0");
                oilStationStrList.addAll(LonLatUtil.getOilStationListByCity(lon, lat, "", page));
                logger.info("根据经纬度获取这座城市附近的加油站, 这座城市的加油站数量为：" + oilStationStrList.size());
            }
            //2.在数据库中根据经纬度在库里查询 民营加油站 的信息
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.clear();
            paramMap_temp.put("oilStationType", "民营");
            paramMap_temp.put("lon", lon);
            paramMap_temp.put("lat", lat);
            paramMap_temp.put("dis", r / 1000);
            ResultDTO resultDTO_temp = this.getPrivateOilStationByCondition(paramMap_temp);
            List<Map<String, String>> oilStationList_private = resultDTO_temp.getResultList();
            if (oilStationList_private != null && oilStationList_private.size() > 0) {
                //通过循环遍历计算当前经纬度坐标与民营加油站的经纬度坐标之间的距离
                for (Map<String, String> newMall : oilStationList_private) {
                    Double endLat = Double.parseDouble(newMall.get("oilStationLat").toString());
                    Double endLon = Double.parseDouble(newMall.get("oilStationLon").toString());
                    Double distance = LonLatUtil.getDistance(lat, lon, endLat, endLon);
                    newMall.put("oilStationDistance", distance.toString());
                }
                oilStationStrList.addAll(oilStationList_private);
            }
            if (oilStationStrList != null && oilStationStrList.size() > 0) {
                //去除重复加油站
                List<Map<String, String>> oilStationStrList_new = Lists.newArrayList();
                for (int i = 0; i < oilStationStrList.size(); i++) {
                    Map<String, String> oldMap = oilStationStrList.get(i);
                    if (oilStationStrList_new.size() > 0) {
                        boolean isContain = false;
                        for (int j = 0; j < oilStationStrList_new.size(); j++) {
                            Map<String, String> newMap = oilStationStrList_new.get(j);
                            if (newMap.get("oilStationCode").equals(oldMap.get("oilStationCode"))) {
                                for (String key : oldMap.keySet()) {
                                    newMap.put(key, oldMap.get(key));
                                }
                                isContain = true;
                                break;
                            }
                        }
                        if (!isContain) {
                            oilStationStrList_new.add(oldMap);
                        }
                    } else {
                        oilStationStrList_new.add(oldMap);
                    }
                }
                oilStationStrList.clear();
                oilStationStrList.addAll(oilStationStrList_new);
                //对oilStationPriceList进行排序
                Collections.sort(oilStationStrList, new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        Double oilStationDistance_1 = Double.parseDouble(o1.get("oilStationDistance").toString());
                        Double oilStationDistance_2 = Double.parseDouble(o2.get("oilStationDistance").toString());
                        return oilStationDistance_1.compareTo(oilStationDistance_2);
                    }
                });
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(true);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.OIL_QUERY_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.OIL_QUERY_IS_NULL.getMessage());
            }

//            //2.根据城市名称获取整座城市的加油站
//            Map<String, Object> paramMap_temp = Maps.newHashMap();
//            String city = LonLatUtil.getAddressByLonLat(lon, lat, "city");
//            paramMap_temp.put("oilStationAreaName", city);
//            List<Map<String, Object>> oilStationList_city = oilStationDao.getSimpleOilStationByCondition(paramMap_temp);
//            logger.info("当前用户所在城市：" + city + " , 这座城市的加油站数量为：" + (oilStationList_city!=null?oilStationList_city.size():0));
//            logger.info("当前用户所在城市：" + city + " , 这座城市的加油站数量为：" + (oilStationList_city!=null?oilStationList_city.size():0));
//            logger.info("当前用户所在城市：" + city + " , 这座城市的加油站数量为：" + (oilStationList_city!=null?oilStationList_city.size():0));
//            logger.info("当前用户所在城市：" + city + " , 这座城市的加油站数量为：" + (oilStationList_city!=null?oilStationList_city.size():0));
//            logger.info("当前用户所在城市：" + city + " , 这座城市的加油站数量为：" + (oilStationList_city!=null?oilStationList_city.size():0));
//            if(oilStationList_city != null && oilStationList_city.size() > 0){
//                List<Map<String, String>> oilStationStrList_city = Lists.newArrayList();
//                oilStationStrList_city = MapUtil.getStringMapList(oilStationList_city);
//                resultDTO.setResultList(oilStationStrList_city);
//                resultDTO.setSuccess(true);
//                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
//                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
//            } else {
//                resultDTO.setResultList(oilStationStrList);
//                resultDTO.setSuccess(false);
//                resultDTO.setCode(NewMallCode.OIL_QUERY_IS_NULL.getNo());
//                resultDTO.setMessage(NewMallCode.OIL_QUERY_IS_NULL.getMessage());
//            }
        } else {
            resultDTO.setResultList(oilStationStrList);
            resultDTO.setSuccess(false);
            resultDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取加油站列表-getOilStationList,响应-response:" + resultDTO);
        return resultDTO;
    }

    /**
     * 获取民营加油站信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getPrivateOilStationByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        String oilStationName = paramMap.get("oilStationName") != null ? paramMap.get("oilStationName").toString() : "";
        Float lon = paramMap.get("lon") != null ? Float.parseFloat(paramMap.get("lon").toString()) : 0;
        Float lat = paramMap.get("lat") != null ? Float.parseFloat(paramMap.get("lat").toString()) : 0;
        Float dis = paramMap.get("dis") != null ? Float.parseFloat(paramMap.get("dis").toString()) : 1;
        String oilStationType = paramMap.get("oilStationType") != null ? paramMap.get("oilStationType").toString() : "民营";
        if (!"".equals(oilStationName) ||
                (!"".equals(lon) && !"".equals(lat))) {
            //1.现在数据库中查找是否存在金纬度范围之内的加油站
            //先计算查询点的经纬度范围
            double r = LonLatUtil.EARTH_RADIUS;//地球半径千米
            double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lat * Math.PI / 180));
            dlng = dlng * 180 / Math.PI;//角度转为弧度
            double dlat = dis / r;
            dlat = dlat * 180 / Math.PI;
            double minLat = lat - dlat;
            double maxLat = lat + dlat;
            double minLon = lon - dlng;
            double maxLon = lon + dlng;
            paramMap.put("minLon", minLon);
            paramMap.put("maxLon", maxLon);
            paramMap.put("minLat", minLat);
            paramMap.put("maxLat", maxLat);
            paramMap.put("oilStationType", oilStationType);
            List<Map<String, Object>> oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
            if (oilStationList != null && oilStationList.size() > 0) {
                Integer total = oilStationDao.getSimpleOilStationTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                oilStationStrList.addAll(MapUtil.getStringMapList(oilStationList));
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                //使用递归，务必查找到最近的加油站进行展示
                System.out.println("第 " + dis + " 次递归查询【民营加油站】...");
                if (dis == 10) {
                    resultDTO.setResultList(oilStationStrList);
                    resultDTO.setSuccess(false);
                    resultDTO.setCode(NewMallCode.OIL_QUERY_IS_NULL.getNo());
                    resultDTO.setMessage(NewMallCode.OIL_QUERY_IS_NULL.getMessage());
                    return resultDTO;
                }
                dis++;
                paramMap.put("dis", dis);
                ResultDTO resultDTO_temp = getPrivateOilStationByCondition(paramMap);
                resultDTO.setResultList(oilStationStrList);
                resultDTO.getResultList().addAll(resultDTO_temp.getResultList());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setResultList(oilStationStrList);
            resultDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取单一的加油站信息-getPrivateOilStationByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 获取一个加油站信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getOneOilStationByCondition(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        Integer total = 0;
        String oilStationName = paramMap.get("oilStationName") != null ? paramMap.get("oilStationName").toString() : "";
        Float lon = paramMap.get("lon") != null ? Float.parseFloat(paramMap.get("lon").toString()) : 0;
        Float lat = paramMap.get("lat") != null ? Float.parseFloat(paramMap.get("lat").toString()) : 0;
        Float dis = paramMap.get("dis") != null ? Float.parseFloat(paramMap.get("dis").toString()) : 1;
        if (!"".equals(oilStationName) ||
                (!"".equals(lon) && !"".equals(lat))) {
            //先计算查询点的经纬度范围
            double r = LonLatUtil.EARTH_RADIUS;//地球半径千米
            double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lat * Math.PI / 180));
            dlng = dlng * 180 / Math.PI;//角度转为弧度
            double dlat = dis / r;
            dlat = dlat * 180 / Math.PI;
            double minLat = lat - dlat;
            double maxLat = lat + dlat;
            double minLon = lon - dlng;
            double maxLon = lon + dlng;
            paramMap.put("minLat", minLat);
            paramMap.put("maxLat", maxLat);
            paramMap.put("minLon", minLon);
            paramMap.put("maxLon", maxLon);
            paramMap.put("start", 0);
            paramMap.put("size", 1);
            List<Map<String, Object>> oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
            logger.info("第一次获取单个加油站，paramMap = " + JSONObject.toJSONString(paramMap));
            logger.info("第一次获取单个加油站，oilStationList = " + JSONObject.toJSONString(oilStationList));
            if (oilStationList != null && oilStationList.size() > 0) {
                total = oilStationList.size();
            } else {
                //2.通过网络获取该坐标范围之内的
                Map<String, Object> paramMap_temp = Maps.newHashMap();
                paramMap_temp.put("lon", lon);
                paramMap_temp.put("lat", lat);
                paramMap_temp.put("page", "1");
                paramMap_temp.put("r", "10000");
                this.getOilStationList(paramMap_temp);              //该接口一定获取到加油站数据
                oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
                total = oilStationList.size();
                logger.info("第二次获取单个加油站，paramMap = " + JSONObject.toJSONString(paramMap));
                logger.info("第二次获取单个加油站，oilStationList = " + JSONObject.toJSONString(oilStationList));
            }
            System.out.println("==============================================");
            System.out.println("==============================================");
            System.out.println("(oilStationList != null && oilStationList.size() > 0) ----->>> " + (oilStationList != null && oilStationList.size() > 0));
            System.out.println("==============================================");
            System.out.println("==============================================");
            if (oilStationList == null || oilStationList.size() <= 0) {
                paramMap.put("oilStationType", "民营");
                oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
                total = oilStationList.size();
                logger.info("第三次获取单个加油站，paramMap = " + JSONObject.toJSONString(paramMap));
                logger.info("第三次获取单个加油站，oilStationList = " + JSONObject.toJSONString(oilStationList));
            }
            resultMapDTO.setResultListTotal(total);
            resultMap.put("oilStationName", oilStationList.get(0).get("oilStationName").toString());
            resultMap.put("oilStationList", JSONObject.toJSONString(oilStationList));
            resultMapDTO.setResultMap(resultMap);
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取单一的加油站信息-getSimpleOilStationByCondition,结果-result:" + resultMapDTO);
        return resultMapDTO;
    }

    /**
     * 根据经纬度地址获取所处的加油站
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getOilStationByLonLat(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        Float lon = paramMap.get("lon") != null ? Float.parseFloat(paramMap.get("lon").toString()) : 0;
        Float lat = paramMap.get("lat") != null ? Float.parseFloat(paramMap.get("lat").toString()) : 0;
        Float dis = paramMap.get("dis") != null ? Float.parseFloat(paramMap.get("dis").toString()) : 3;
        String oilStationType = paramMap.get("oilStationType") != null ? paramMap.get("oilStationType").toString() : "民营";
        if (!"".equals(lon) && !"".equals(lat) && !"".equals(dis)) {
            //1.现在数据库中查找是否存在金纬度范围之内的加油站
            //先计算查询点的经纬度范围
            double r = LonLatUtil.EARTH_RADIUS;//地球半径千米
            double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lat * Math.PI / 180));
            dlng = dlng * 180 / Math.PI;//角度转为弧度
            double dlat = dis / r;
            dlat = dlat * 180 / Math.PI;
            double minLat = lat - dlat;
            double maxLat = lat + dlat;
            double minLon = lon - dlng;
            double maxLon = lon + dlng;
            paramMap.put("minLon", minLon);
            paramMap.put("maxLon", maxLon);
            paramMap.put("minLat", minLat);
            paramMap.put("maxLat", maxLat);
            paramMap.put("oilStationType", oilStationType);
            List<Map<String, Object>> oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
            if (oilStationList != null && oilStationList.size() > 0) {
                oilStationStrList.addAll(MapUtil.getStringMapList(oilStationList));
                //2.通过循环遍历计算当前经纬度坐标与民营加油站的经纬度坐标之间的距离
                for (Map<String, String> newMall : oilStationStrList) {
                    Double endLat = Double.parseDouble(newMall.get("oilStationLat").toString());
                    Double endLon = Double.parseDouble(newMall.get("oilStationLon").toString());
                    Double distance = LonLatUtil.getDistance(lat, lon, endLat, endLon);
                    newMall.put("oilStationDistance", distance.toString());
                }
                //3.对oilStationPriceList进行排序
                Collections.sort(oilStationStrList, new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        Double oilStationDistance_1 = Double.parseDouble(o1.get("oilStationDistance").toString());
                        Double oilStationDistance_2 = Double.parseDouble(o2.get("oilStationDistance").toString());
                        return oilStationDistance_1.compareTo(oilStationDistance_2);
                    }
                });
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.OIL_QUERY_IS_NULL.getNo());
                resultDTO.setMessage(NewMallCode.OIL_QUERY_IS_NULL.getMessage());
                return resultDTO;
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setResultList(oilStationStrList);
            resultDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中根据经纬度地址获取所处的加油站-getOilStationByLonLat,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 添加或者更新加油站
     *      术语解释:
     *          添加油站：添加不存在的并且有油价信息的加油站
     *          纠正油价：修改当前的加油站油价
     *      不管用户是【添加油站】还是【纠正油价】，第二天均有红包奖励
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addOrUpdateOilStation(Map<String, Object> paramMap) {
        Integer addNum = 0;
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        String oilStationName = paramMap.get("oilStationName") != null ? paramMap.get("oilStationName").toString() : "";
        String oilStationAdress = paramMap.get("oilStationAdress") != null ? paramMap.get("oilStationAdress").toString() : "";
        String oilStationLon = paramMap.get("oilStationLon") != null ? paramMap.get("oilStationLon").toString() : "";
        String oilStationLat = paramMap.get("oilStationLat") != null ? paramMap.get("oilStationLat").toString() : "";
        String oilStationPrice = paramMap.get("oilStationPrice") != null ? paramMap.get("oilStationPrice").toString() : "";
        String oilStationType = paramMap.get("oilStationType") != null ? paramMap.get("oilStationType").toString() : "民营";
        paramMap.put("oilStationType", oilStationType);
        paramMap.put("isManualModify", 1);
        if (!"".equals(oilStationName) && !"".equals(oilStationAdress) &&
                !"".equals(oilStationLat) && !"".equals(oilStationLon) &&
                    !"".equals(oilStationPrice)
        ) {
            String oilStationPosition = oilStationLon + "," + oilStationLat;
            paramMap.put("oilStationPosition", oilStationPosition);
            paramMap.put("operator", "addOilStation");
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            if (!"".equals(oilStationCode)) {              //更新
                paramMap_temp.put("oilStationCode", oilStationCode);
                Integer total = oilStationDao.getSimpleOilStationTotalByCondition(paramMap_temp);
                if (total != null && total <= 0) {
                    paramMap_temp.clear();      //清空参数，重新整理参数
                    Long oilStationCode_l = 0L;
                    Map<String, Object> maxOilStationCodeMap = oilStationDao.getMaxOilStationCode(paramMap_temp);
                    if (maxOilStationCodeMap.size() > 0) {
                        oilStationCode_l = Long.parseLong(maxOilStationCodeMap.get("oilStationCode").toString());
                        oilStationCode_l++;
                        paramMap.put("oilStationCode", oilStationCode_l);
                    }
                    //添加油站
                    logger.info("开始新增 加油站 数据， paramMap = " + JSONObject.toJSONString(paramMap));
                    addNum = oilStationDao.addOilStation(paramMap);
                    if (addNum != null && addNum > 0) {
                        boolDTO.setSuccess(true);
                        boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                        boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                        //新增【添加油站】的操作，便于后期发送红包的参考
                        Map<String, Object> oilStationOperator_paramMap = Maps.newHashMap();
                        oilStationOperator_paramMap.put("status", "0");
                        oilStationOperator_paramMap.put("uid", paramMap.get("uid"));
                        oilStationOperator_paramMap.put("operator", "addOilStation");
                        oilStationOperator_paramMap.put("oilStationCode", oilStationCode_l);
                        oilStationOperatorService.addOilStationOperator(oilStationOperator_paramMap);
                    } else {
                        boolDTO.setSuccess(false);
                        boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                        boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                    }
                } else {
                    logger.info("开始更新 加油站 数据， paramMap = " + JSONObject.toJSONString(paramMap));
                    updateNum = oilStationDao.updateOilStation(paramMap);
                    if (updateNum != null && updateNum > 0) {
                        boolDTO.setCode(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getNo());
                        boolDTO.setMessage(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getMessage());
                        //新增【添加油站】的操作，便于后期发送红包的参考
                        Map<String, Object> oilStationOperator_paramMap = Maps.newHashMap();
                        oilStationOperator_paramMap.put("status", "0");
                        oilStationOperator_paramMap.put("uid", paramMap.get("uid"));
                        oilStationOperator_paramMap.put("operator", "updateOilStation");
                        oilStationOperator_paramMap.put("oilStationCode", oilStationCode);
                        oilStationOperatorService.addOilStationOperator(oilStationOperator_paramMap);
                    } else {
                        boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                        boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                    }
                }
            } else {
                //检测 当前加油站名称 是否存在
                paramMap_temp.clear();      //清空参数，重新整理参数
                paramMap_temp.put("oilStationName", oilStationName);
                paramMap_temp.put("oilStationType", oilStationType);
                Integer total = oilStationDao.getSimpleOilStationTotalByCondition(paramMap_temp);
                if(total != null && total > 0){
                    List<Map<String, Object>> existOilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap_temp);
                    if(existOilStationList != null && existOilStationList.size() > 0){
                        Map<String, Object> existOilStation = existOilStationList.get(0);
                        paramMap.put("oilStationCode", existOilStation.get("oilStationCode"));
                        logger.info("开始更新 加油站 数据， paramMap = " + JSONObject.toJSONString(paramMap));
                        updateNum = oilStationDao.updateOilStation(paramMap);
                        if (updateNum != null && updateNum > 0) {
                            boolDTO.setCode(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getNo());
                            boolDTO.setMessage(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getMessage());
                            //新增【添加油站】的操作，便于后期发送红包的参考
                            Map<String, Object> oilStationOperator_paramMap = Maps.newHashMap();
                            oilStationOperator_paramMap.put("status", "0");
                            oilStationOperator_paramMap.put("uid", paramMap.get("uid"));
                            oilStationOperator_paramMap.put("operator", "updateOilStation");
                            oilStationOperator_paramMap.put("oilStationCode", existOilStation.get("oilStationCode"));
                            oilStationOperatorService.addOilStationOperator(oilStationOperator_paramMap);
                        } else {
                            boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                            boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                        }
                    }
                } else {
                    paramMap.put("oilStationType", "民营");
                    Long oilStationCode_l = 0L;
                    Map<String, Object> maxOilStationCodeMap = oilStationDao.getMaxOilStationCode(paramMap_temp);
                    if (maxOilStationCodeMap.size() > 0) {
                        oilStationCode_l = Long.parseLong(maxOilStationCodeMap.get("oilStationCode").toString());
                        oilStationCode_l++;
                        paramMap.put("oilStationCode", oilStationCode_l);
                    }
                    logger.info("开始新增 加油站 数据， paramMap = " + JSONObject.toJSONString(paramMap));
                    addNum = oilStationDao.addOilStation(paramMap);
                    if (addNum != null && addNum > 0) {
                        boolDTO.setSuccess(true);
                        boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                        boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                        //新增【添加油站】的操作，便于后期发送红包的参考
                        Map<String, Object> oilStationOperator_paramMap = Maps.newHashMap();
                        oilStationOperator_paramMap.put("status", "0");
                        oilStationOperator_paramMap.put("uid", paramMap.get("uid"));
                        oilStationOperator_paramMap.put("operator", "addOilStation");
                        oilStationOperator_paramMap.put("oilStationCode", oilStationCode_l);
                        oilStationOperatorService.addOilStationOperator(oilStationOperator_paramMap);
                    } else {
                        boolDTO.setSuccess(false);
                        boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                        boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                    }
                }
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加加油站-addOilStation,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 添加加油站
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO addOilStation(Map<String, Object> paramMap) {
        Integer addNum = 0;
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        String oilStationName = paramMap.get("oilStationName") != null ? paramMap.get("oilStationName").toString() : "";
        String oilStationAreaSpell = paramMap.get("oilStationAreaSpell") != null ? paramMap.get("oilStationAreaSpell").toString() : "";
        String oilStationAreaName = paramMap.get("oilStationAreaName") != null ? paramMap.get("oilStationAreaName").toString() : "";
        String oilStationAdress = paramMap.get("oilStationAdress") != null ? paramMap.get("oilStationAdress").toString() : "";
        String oilStationBrandName = paramMap.get("oilStationBrandName") != null ? paramMap.get("oilStationBrandName").toString() : "";
        String oilStationType = paramMap.get("oilStationType") != null ? paramMap.get("oilStationType").toString() : "";
        String oilStationDiscount = paramMap.get("oilStationDiscount") != null ? paramMap.get("oilStationDiscount").toString() : "";
        String oilStationExhaust = paramMap.get("oilStationExhaust") != null ? paramMap.get("oilStationExhaust").toString() : "";
        String oilStationPosition = paramMap.get("oilStationPosition") != null ? paramMap.get("oilStationPosition").toString() : "";
        String oilStationLon = paramMap.get("oilStationLon") != null ? paramMap.get("oilStationLon").toString() : "";
        String oilStationLat = paramMap.get("oilStationLat") != null ? paramMap.get("oilStationLat").toString() : "";
        String oilStationPayType = paramMap.get("oilStationPayType") != null ? paramMap.get("oilStationPayType").toString() : "";
        String oilStationPrice = paramMap.get("oilStationPrice") != null ? paramMap.get("oilStationPrice").toString() : "";
        String oilStationDistance = paramMap.get("oilStationDistance") != null ? paramMap.get("oilStationDistance").toString() : "";
        if (
                !"".equals(oilStationCode) && !"".equals(oilStationName) && !"".equals(oilStationAreaSpell) &&
                        !"".equals(oilStationAreaName) && !"".equals(oilStationAdress) && !"".equals(oilStationBrandName) &&
                        !"".equals(oilStationType) && !"".equals(oilStationDiscount) && !"".equals(oilStationExhaust) &&
                        !"".equals(oilStationPosition) && !"".equals(oilStationLon) && !"".equals(oilStationLat) &&
                        !"".equals(oilStationPayType) && !"".equals(oilStationPrice) && !"".equals(oilStationDistance)
                ) {
            Map<String, Object> paramMap_temp = Maps.newHashMap();
            paramMap_temp.put("oilStationCode", oilStationCode);
            paramMap_temp.put("oilStationType", oilStationType);
            Integer total = oilStationDao.getSimpleOilStationTotalByCondition(paramMap_temp);
            if (total != null && total <= 0) {
                addNum = oilStationDao.addOilStation(paramMap);
                if (addNum != null && addNum > 0) {
                    boolDTO.setSuccess(true);
                    boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                    boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
                } else {
                    boolDTO.setSuccess(false);
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
            } else {
                updateNum = oilStationDao.updateOilStation(paramMap);
                if (updateNum != null && updateNum > 0) {
                    boolDTO.setSuccess(true);
                    boolDTO.setCode(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getNo());
                    boolDTO.setMessage(NewMallCode.OIL_STATION_EXIST_AND_UPDATE.getMessage());
                } else {
                    boolDTO.setSuccess(false);
                    boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                    boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
                }
                boolDTO.setSuccess(false);

            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中添加加油站-addOilStation,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 删除加油站
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO deleteOilStation(Map<String, Object> paramMap) {
        Integer deleteNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        if (!"".equals(id) || !"".equals(oilStationCode)) {
            deleteNum = oilStationDao.deleteOilStation(paramMap);
            if (deleteNum != null && deleteNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中删除加油站-deleteOilStation,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 更新加油站
     *
     * @param paramMap
     * @return
     */
    @Override
    public BoolDTO updateOilStation(Map<String, Object> paramMap) {
        Integer updateNum = 0;
        BoolDTO boolDTO = new BoolDTO();
        String id = paramMap.get("id") != null ? paramMap.get("id").toString() : "";
        String oilStationCode = paramMap.get("oilStationCode") != null ? paramMap.get("oilStationCode").toString() : "";
        if (!"".equals(id) || !"".equals(oilStationCode)) {
            updateNum = oilStationDao.updateOilStation(paramMap);
            if (updateNum != null && updateNum > 0) {
                boolDTO.setSuccess(true);
                boolDTO.setCode(NewMallCode.SUCCESS.getNo());
                boolDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                boolDTO.setSuccess(false);
                boolDTO.setCode(NewMallCode.NO_DATA_CHANGE.getNo());
                boolDTO.setMessage(NewMallCode.NO_DATA_CHANGE.getMessage());
            }
        } else {
            boolDTO.setSuccess(false);
            boolDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            boolDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中更新加油站-updateOilStation,结果-result:" + boolDTO);
        return boolDTO;
    }

    /**
     * 获取单一的加油站信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public ResultDTO getSimpleOilStationByCondition(Map<String, Object> paramMap) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> resultMap = Maps.newHashMap();
        List<Map<String, String>> oilStationStrList = Lists.newArrayList();
        String oilStationName = paramMap.get("oilStationName") != null ? paramMap.get("oilStationName").toString() : "";
        Float lon = paramMap.get("lon") != null ? Float.parseFloat(paramMap.get("lon").toString()) : 0;
        Float lat = paramMap.get("lat") != null ? Float.parseFloat(paramMap.get("lat").toString()) : 0;
        Float dis = paramMap.get("dis") != null ? Float.parseFloat(paramMap.get("dis").toString()) : 1;
        if (!"".equals(oilStationName) ||
                (!"".equals(lon) && !"".equals(lat))) {
            //1.现在数据库中查找是否存在金纬度范围之内的加油站
            //先计算查询点的经纬度范围
            double r = LonLatUtil.EARTH_RADIUS;//地球半径千米
            double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lat * Math.PI / 180));
            dlng = dlng * 180 / Math.PI;//角度转为弧度
            double dlat = dis / r;
            dlat = dlat * 180 / Math.PI;
            double minLat = lat - dlat;
            double maxLat = lat + dlat;
            double minLon = lon - dlng;
            double maxLon = lon + dlng;
            paramMap.put("minLon", minLon);
            paramMap.put("maxLon", maxLon);
            paramMap.put("minLat", minLat);
            paramMap.put("maxLat", maxLat);
            List<Map<String, Object>> oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap);
            if (oilStationList != null && oilStationList.size() > 0) {
                Integer total = oilStationDao.getSimpleOilStationTotalByCondition(paramMap);
                resultDTO.setResultListTotal(total);
                oilStationStrList.addAll(MapUtil.getStringMapList(oilStationList));
                resultDTO.setResultList(oilStationStrList);
                resultDTO.setSuccess(false);
                resultDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                //使用递归，务必查找到最近的加油站进行展示
                System.out.println("第 " + dis + " 次递归查询【数据库中加油站】...");
                if (dis == 10) {
                    resultDTO.setResultList(oilStationStrList);
                    resultDTO.setSuccess(false);
                    resultDTO.setCode(NewMallCode.OIL_QUERY_IS_NULL.getNo());
                    resultDTO.setMessage(NewMallCode.OIL_QUERY_IS_NULL.getMessage());
                    return resultDTO;
                }
                dis++;
                paramMap.put("dis", dis);
                ResultDTO resultDTO_temp = getSimpleOilStationByCondition(paramMap);
                resultDTO.setResultList(oilStationStrList);
                resultDTO.getResultList().addAll(resultDTO_temp.getResultList());
            }
        } else {
            resultDTO.setSuccess(false);
            resultDTO.setResultList(oilStationStrList);
            resultDTO.setCode(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getNo());
            resultDTO.setMessage(NewMallCode.OIL_STATION_PARAM_IS_NOT_NULL.getMessage());
        }
        logger.info("在service中获取单一的加油站信息-getSimpleOilStationByCondition,结果-result:" + resultDTO);
        return resultDTO;
    }

    /**
     * 通过excel的方式导入加油站数据
     */
    @Override
    public ResultMapDTO batchImportOilStationByExcel(Map<String, Object> paramMap) throws Exception {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Integer addNum = 0;
        Integer updateNum = 0;
        String destFilePath = paramMap.get("destFilePath") != null ? paramMap.get("destFilePath").toString() : "";
        if ("".equals(destFilePath)) {
            destFilePath = "/Users/caihongwang/Downloads/加油站名单.xlsx";
        }
        if (!"".equals(destFilePath)) {
            ImportExcelUtil<OilStationPO> importExcelUtil = new ImportExcelUtil<>();
            importExcelUtil.read(destFilePath, OilStationPO.class, 0);
            List<OilStationPO> oilStationPOList = importExcelUtil.getData();
            System.out.println("========================开始导入数据===========================");
            if (oilStationPOList != null && oilStationPOList.size() > 0) {
                for (int i = 0; i < oilStationPOList.size(); i++) {
                    paramMap.clear();
                    OilStationPO oilStationPO = oilStationPOList.get(i);
                    if (oilStationPO.getOilStationName() == null || "".equals(oilStationPO.getOilStationName())
                            || oilStationPO.getOilStationAddress() == null || "".equals(oilStationPO.getOilStationAddress())
                            || oilStationPO.getFireControl() == null || "".equals(oilStationPO.getFireControl())) {
                        System.out.println(oilStationPO);
                    }
                    String oilStationCode = 10000000 + i + 1 + "";
                    String oilStationName = oilStationPO.getOilStationName();
                    if (!oilStationName.contains("中")) {
                        String queryAddressUrl = NewMallCode.TENCENT_KEY_WORD_SEARCH +
                                "?key=" + NewMallCode.TENCENT_KEY + "&region=" + "贵州" + "&keyword=" + oilStationName;
                        String addressDataJson = httpsUtil.get(queryAddressUrl, MapUtil.getStringMap(paramMap));
                        logger.info("在service中获取加油站列表-getAddressByLonLat, 从腾讯地图获取地址的结果 : " + addressDataJson);
                        JSONObject addressData_JSONObject = JSONObject.parseObject(addressDataJson);
                        String statusStr = addressData_JSONObject.getString("status");
                        if ("0".equals(statusStr)) {
                            JSONArray data_JSONArray = addressData_JSONObject.getJSONArray("data");
                            if (data_JSONArray.size() > 0) {
                                JSONObject data_JSONObject = data_JSONArray.getJSONObject(0);        //全路径 地址
                                String addressStr = data_JSONObject.getString("address");        //全路径 地址
                                String adcodeStr = data_JSONObject.getString("adcode");        // 邮编
                                String categoryStr = data_JSONObject.getString("category");        // 描述
                                JSONObject location_JSONObject = data_JSONObject.getJSONObject("location");        // 经纬度
                                String lonStr = location_JSONObject.getString("lng");        // 经度
                                String latStr = location_JSONObject.getString("lat");        // 纬度
                                String provinceStr = data_JSONObject.getString("province");        // 省份
                                String cityStr = data_JSONObject.getString("city");        //  城市
                                String districtStr = data_JSONObject.getString("district");        //  区域

                                String oilStationAreaSpell = adcodeStr;
                                String oilStationAreaName = provinceStr + " " + cityStr + " " + districtStr;
                                String oilStationAdress = addressStr;
                                String oilStationBrandName = "民营店";
                                String oilStationType = "民营";
                                String oilStationDiscount = "打折加油站";
                                String oilStationPosition = lonStr + "," + latStr;
                                String oilStationLon = lonStr;
                                String oilStationLat = latStr;
                                String oilStationPayType = categoryStr;
                                String oilStationPrice = "[{\"oilPriceLabel\":\"6.89\",\"oilModelLabel\":\"0\",\"oilNameLabel\":\"柴油\"},{\"oilPriceLabel\":\"7.29\",\"oilModelLabel\":\"92\",\"oilNameLabel\":\"汽油\"},{\"oilPriceLabel\":\"7.89\",\"oilModelLabel\":\"95\",\"oilNameLabel\":\"汽油\"}]";
                                String oilStationDistance = "3537";
                                String oilStationWxPaymentCodeImgUrl = "";

                                paramMap.put("oilStationCode", oilStationCode);
                                paramMap.put("oilStationName", oilStationName);
                                paramMap.put("oilStationAreaSpell", oilStationAreaSpell);
                                paramMap.put("oilStationAreaName", oilStationAreaName);
                                paramMap.put("oilStationAdress", oilStationAdress);
                                paramMap.put("oilStationBrandName", oilStationBrandName);
                                paramMap.put("oilStationType", oilStationType);
                                paramMap.put("oilStationDiscount", oilStationDiscount);
                                paramMap.put("oilStationPosition", oilStationPosition);
                                paramMap.put("oilStationLon", oilStationLon);
                                paramMap.put("oilStationLat", oilStationLat);
                                paramMap.put("oilStationPayType", oilStationPayType);
                                paramMap.put("oilStationPrice", oilStationPrice);
                                paramMap.put("oilStationDistance", oilStationDistance);
                                paramMap.put("oilStationWxPaymentCodeImgUrl", oilStationWxPaymentCodeImgUrl);

                                Map<String, Object> paramMap_temp = Maps.newHashMap();
                                paramMap_temp.put("oilStationName", oilStationName);
                                List<Map<String, Object>> oilStationList = oilStationDao.getSimpleOilStationByCondition(paramMap_temp);
                                if (oilStationList != null && oilStationList.size() > 0) {
                                    String id = oilStationList.get(0).get("id").toString();
                                    paramMap.put("id", id);
                                    updateNum = updateNum + oilStationDao.updateOilStation(paramMap);
                                } else {
                                    addNum = addNum + oilStationDao.addOilStation(paramMap);
                                }
                            } else {
                                System.out.println("未出现在腾讯地图上的加油站：  oilStationName = " + oilStationName);
                            }
                            System.out.println("开始沉睡 5 秒.");
                            Thread.sleep(5000);
                        }
                    } else {
                        System.out.println("三【中】加油站：  oilStationName = " + oilStationName);
                    }
                }
            }
            Map<String, String> resultMap = Maps.newHashMap();
            resultMap.put("addNum", addNum != null ? addNum.toString() : "0");
            resultMap.put("updateNum", updateNum != null ? updateNum.toString() : "0");
            resultMapDTO.setResultMap(resultMap);
            resultMapDTO.setSuccess(true);
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            System.out.println("======================================================");
            System.out.println(resultMapDTO);
        } else {
            resultMapDTO.setSuccess(false);
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        return resultMapDTO;
    }
}