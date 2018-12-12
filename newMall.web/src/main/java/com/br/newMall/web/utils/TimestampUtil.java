package com.br.newMall.web.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Timestamp util.
 */
public class TimestampUtil {

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static Timestamp getTimestamp(long d) {
        return new Timestamp(System.currentTimeMillis() + d);
    }

    /**
     * 将Timestamp转换成格式化的字符串
     *
     * @param timestamp
     * @return
     */
    public static String getFormatTimestamp(Timestamp timestamp) {
        String timestampStr = "";
        if (timestamp != null) {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestampStr = sdf.format(timestamp);
        }
        return timestampStr;
    }

}
