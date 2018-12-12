package com.br.newMall.center.po;

import com.br.newMall.center.service.BaseConvert;
import com.br.newMall.center.utils.ColumnNameUtils;

import java.text.DateFormat;

/**
 * 百融联系人PO
 */
public class OilStationPO extends BaseConvert {
    @ColumnNameUtils("单位名称")
    private String oilStationName;
    @ColumnNameUtils("单位地址")
    private String oilStationAddress;
    @ColumnNameUtils("消防管辖")
    private String fireControl;

    @Override
    public DateFormat getDateFormat() {
        return null;
    }

    public String getOilStationName() {
        return oilStationName;
    }

    public void setOilStationName(String oilStationName) {
        this.oilStationName = oilStationName;
    }

    public String getOilStationAddress() {
        return oilStationAddress;
    }

    public void setOilStationAddress(String oilStationAddress) {
        this.oilStationAddress = oilStationAddress;
    }

    public String getFireControl() {
        return fireControl;
    }

    public void setFireControl(String fireControl) {
        this.fireControl = fireControl;
    }

    @Override
    public String toString() {
        return "OilStationPO = " +
                "{ oilStationName='" + oilStationName + "\'" +
                " , oilStationAddress='" + oilStationAddress + "\'" +
                " , fireControl='" + fireControl + "\'" +
                " }";
    }
}
