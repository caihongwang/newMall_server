package com.br.newMall.center.po;

import com.br.newMall.center.utils.ColumnNameUtils;

import java.text.DateFormat;
import java.util.Date;

/**
 * 联系人PO
 */
public class ContactPO {

    @ColumnNameUtils("序号")
    private String index;
    @ColumnNameUtils("姓名")
    private String name;
    @ColumnNameUtils("联系电话")
    private String phone;
    @ColumnNameUtils("职务")
    private String job;
    @ColumnNameUtils("单位")
    private String company;
    @ColumnNameUtils("行政区域")
    private String administrativeDivision;
    @ColumnNameUtils("职能辖区")
    private String jurisdiction;

    public String getAdministrativeDivision() {
        return administrativeDivision;
    }

    public void setAdministrativeDivision(String administrativeDivision) {
        this.administrativeDivision = administrativeDivision;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public DateFormat getDateFormat() {
        return null;
    }

    public String getDateString(Date date) {
        return getDateFormat().format(date);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "联系人(Contacts) = {" +
            " 序号(index) = '" + index + '\'' +
            " , 姓名(name) = '" + name + '\'' +
            " , 联系电话(phone) = '" + phone + '\'' +
            " , 职务(job) = '" + job + '\'' +
            " , 单位(company) = '" + company + '\'' +
            " , 行政区域(administrativeDivision) = '" + administrativeDivision + '\'' +
            " , 职务辖区(jurisdiction) = '" + jurisdiction + '\'' +
            " }";
    }
}
