/**
 * Project Name: mq_project
 * File Name: GsonUtils.java
 * Package Name: com.huifenqi.mq.util
 * Date: 2015年11月27日下午5:40:15
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved.
 */
package com.br.newMall.web.utils;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentHelper;

/**
 * 判断字符串类型-工具类
 */
public class StringTypeUtil {

    public static String getType(String string) {
        if (isNumber(string)) {
            return "Number";
        } else if (isJson(string)) {
            return "Json";
        } else if (isXML(string)) {
            return "xml";
        } else {
            return "String";
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            JSONObject.parse(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是xml结构
     */
    public static boolean isXML(String value) {
        try {
            DocumentHelper.parseText(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
