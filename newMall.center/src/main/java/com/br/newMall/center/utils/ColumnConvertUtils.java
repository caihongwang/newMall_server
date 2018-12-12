package com.br.newMall.center.utils;


import com.br.newMall.center.po.ColumnFiledName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhenle.li on 17/3/10.
 */
public class ColumnConvertUtils {

    /**
     * 获取BrColumnName
     *
     * @param clazz
     * @return
     */
    public static List<ColumnFiledName> getColumnName(Class clazz) {
        if (clazz == null) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        List<ColumnFiledName> columnNameList = new ArrayList<>();
        for (Field field : fields) {
            ColumnNameUtils annotation = field.getAnnotation(ColumnNameUtils.class);
            if (annotation != null) {
                String columnName = annotation.value();
                int index = annotation.index();
                String filedName = field.getName();
                ColumnFiledName columnFiledName = new ColumnFiledName();
                columnFiledName.setColumnName(columnName);
                columnFiledName.setFiledName(filedName);
                columnFiledName.setIndex(index);
                columnNameList.add(columnFiledName);
            }
        }
        //按index排序
        Collections.sort(columnNameList, new ColumnFiledAscComparator(true));
        return columnNameList;
    }

    private static class ColumnFiledAscComparator implements Comparator<ColumnFiledName> {
        boolean asc = true;

        public ColumnFiledAscComparator(boolean asc) {
            this.asc = asc;
        }

        @Override
        public int compare(ColumnFiledName o1, ColumnFiledName o2) {
            if (o1 == o2) {
                return 0;
            }
            int index1 = o1.getIndex();
            int index2 = o2.getIndex();
            if (index1 > index2) {
                return asc ? 1 : -1;
            }
            if (index1 < index2) {
                return asc ? -1 : 1;
            }
            return 0;
        }
    }
}
