package com.br.newMall.web.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caihongwang on 2018/1/24.
 */
public class MapUtil {
    /**
     * 将map参数中的start,size的value转换成Integer
     * 默认添加 创建时间 和 修改时间  的参数值
     *
     * @param paramMap
     * @return
     */
    public static Map<String, Object> getObjectMap(Map<String, String> paramMap) {
        Map<String, Object> objectParamMap = Maps.newHashMap();
        if (paramMap != null && paramMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    value = EmojiUtil.emojiConvert(value);
                } catch (Exception e) {
                    System.out.println("key = " + key + " , value = " + value);
                }

                Object newValue = new Object();
                if ("start".equals(key) || "size".equals(key)) {
                    objectParamMap.put(key, Integer.parseInt(value));
                } else {
                    objectParamMap.put(key, value);
                }
            }
        }
        //默认为每个参数map添加 创建时间 和 修改时间  的参数值
        objectParamMap.put("createTime", TimestampUtil.getTimestamp());
        objectParamMap.put("updateTime", TimestampUtil.getTimestamp());
        return objectParamMap;
    }

    /**
     * 将map参数中的start,size的value转换成Integer
     *
     * @param paramMap
     * @return
     */
    public static Map<String, String> getStringMap(Map<String, ?> paramMap) {
        Map<String, String> stringParamMap = Maps.newHashMap();
        if (paramMap != null && paramMap.size() > 0) {
            for (Map.Entry<String, ?> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    try {
                        String valueStr = EmojiUtil.emojiRecovery(value != null ? value.toString() : "");
                        stringParamMap.put(key, valueStr);
                    } catch (Exception e) {
                        stringParamMap.put(key, value.toString());
                        System.out.println("key = " + key + " , value = " + value);
                    }
                }
            }
        }
        return stringParamMap;
    }

    /**
     * 将map参数中的start,size的value转换成Integer
     *
     * @param paramMap
     * @return
     */
    public static Map<String, String> getJsonStringMap(Map<String, Object> paramMap) {
        Map<String, String> stringParamMap = Maps.newHashMap();
        if (paramMap != null && paramMap.size() > 0) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    if (value instanceof JSONObject) {
                        String valueStr = EmojiUtil.emojiRecovery((((JSONObject) value).toJSONString()) != null ? (((JSONObject) value).toJSONString()) : "");
                        stringParamMap.put(key, valueStr);
                    } else if (value instanceof JSONArray) {
                        String valueStr = EmojiUtil.emojiRecovery((((JSONArray) value).toJSONString()) != null ? (((JSONArray) value).toJSONString()) : "");
                        stringParamMap.put(key, valueStr);
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(value);
                        stringParamMap.put(key, jsonArray.toJSONString());
                    }
                } catch (Exception e) {
                    System.out.println("key = " + key + " , value = " + value);
                }

            }
        }
        return stringParamMap;
    }

    /**
     * 将list中的map中的Object转换成String
     *
     * @param resultList
     * @return
     */
    public static List<Map<String, String>> getStringMapList(List<Map<String, Object>> resultList) {
        List<Map<String, String>> resultStrList = Lists.newArrayList();
        if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> resultMap : resultList) {
                Map<String, String> tempMap = Maps.newHashMap();
                for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    try {
                        String valueStr = EmojiUtil.emojiRecovery(value != null ? value.toString() : "");
                        tempMap.put(key, valueStr);
                    } catch (Exception e) {
                        tempMap.put(key, value.toString());
                    }
                }
                resultStrList.add(tempMap);
            }
        }
        return resultStrList;
    }

    /**
     * 将对象安装属性名称与属性值形成key-value存储到map中
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }
}