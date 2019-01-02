package com.br.newMall.center.utils;

import java.math.BigDecimal;

/**
 * Created by caihongwang on 2018/1/24.
 */
public class NumberUtil {

    /**
     * 获取小数点后两位的double数字
     * @param doubleNumber
     * @return
     */
    public static Double getPointTowNumber(Double doubleNumber) {
        BigDecimal bg = new BigDecimal(doubleNumber);
        doubleNumber = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return doubleNumber;
    }

}
