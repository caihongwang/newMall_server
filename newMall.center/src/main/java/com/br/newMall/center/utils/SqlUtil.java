package com.br.newMall.center.utils;

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
public class SqlUtil {

    /**
     * 处理在sql中的like对应的特殊字符处理
     * '：用于包裹搜索条件，需转为\'；
     * %：用于代替任意数目的任意字符，需转换为\%；
     * _：用于代替一个任意字符，需转换为\_；
     * \：转义符号，需转换为\\\\
     *
     * @param paramStr
     * @return
     */
    public static String handleLikeParam(String paramStr) {
        String resultStr = "";
        if (paramStr != null && !"".equals(paramStr)) {
            if (paramStr.contains("\\")) {
                paramStr = paramStr.replace("\\", "\\\\\\\\");
            }
            if (paramStr.contains("'")) {
                paramStr = paramStr.replace("'", "\\'");
            }
            if (paramStr.contains("%")) {
                paramStr = paramStr.replace("%", "\\%");
            }
            if (paramStr.contains("_")) {
                paramStr = paramStr.replace("_", "\\_");
            }
            resultStr = paramStr;
        }
        return resultStr;
    }

}
