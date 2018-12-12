package com.br.newMall.center.service;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zhenle.li on 17/5/18.
 */
public abstract class BaseConvert {
    public String getDateString(Date date) {
        return getDateFormat().format(date);
    }

    public abstract DateFormat getDateFormat();
}
