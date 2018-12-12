package com.br.newMall.center.po;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.center.utils.ColumnNameUtils;

import java.text.DateFormat;
import java.util.Date;

/**
 * 危险化学品单位PO
 */
public class DangerousCompanyPO {

    @ColumnNameUtils("单位名称")
    private String companyName;
    @ColumnNameUtils("单位地址")
    private String companyAddress;
    @ColumnNameUtils("消防管辖")
    private String FireControl;
    @ColumnNameUtils("联系人")
    private String contactPerson;
    @ColumnNameUtils("电话")
    private String phone;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getFireControl() {
        return FireControl;
    }

    public void setFireControl(String fireControl) {
        FireControl = fireControl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DateFormat getDateFormat() {
        return null;
    }

    public String getDateString(Date date) {
        return getDateFormat().format(date);
    }

    @Override
    public String toString() {
        return "危险化学品单位PO(DangerousCompanyPO) = "
                + JSONObject.toJSONString(this);
    }
}
