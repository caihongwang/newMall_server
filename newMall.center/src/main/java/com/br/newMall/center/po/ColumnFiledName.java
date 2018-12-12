package com.br.newMall.center.po;

public class ColumnFiledName {
    private String columnName;
    private String filedName;
    private int index;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ColumnFiledName{" +
                "columnName='" + columnName + '\'' +
                ", filedName='" + filedName + '\'' +
                ", index=" + index +
                '}';
    }
}
