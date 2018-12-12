package com.br.newMall.center.utils;

import com.br.newMall.center.po.ColumnFiledName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhenle.li on 17/3/13.
 */
public class PoiUtils {
    public static <T> HSSFWorkbook writeToExcel(String sheetName, List<T> objectList, Class<T> clazz) throws Exception {
        if (CollectionUtils.isNotEmpty(objectList)) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //1、添加sheet
            HSSFSheet hssfSheet = hssfWorkbook.createSheet(sheetName);
            //2、添加表头
            HSSFCellStyle headCellStyle = hssfWorkbook.createCellStyle();
            //3、设置字体
            HSSFFont font = hssfWorkbook.createFont();
            font.setFontHeight((short) 300);
            font.setBold(true);
            headCellStyle.setFont(font);
            headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headCellStyle.setBorderBottom((short) 1);
            headCellStyle.setBorderRight((short) 1);
            headCellStyle.setFillBackgroundColor((short) 20);
            HSSFRow headRow = hssfSheet.createRow(0);
            //4、设置表头值
            HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderRight((short) 1);
            List<ColumnFiledName> columnNameList = ColumnConvertUtils.getColumnName(clazz);
            int columnSize = columnNameList.size();
            for (int i = 0; i < columnSize; i++) {
                HSSFCell headCell = headRow.createCell(i);
                headCell.setCellStyle(headCellStyle);
                headCell.setCellValue(columnNameList.get(i).getColumnName());
                hssfSheet.setColumnWidth(i, 5000);
            }
            for (int i = 0, objectSize = objectList.size(); i < objectSize; i++) {
                T t = objectList.get(i);
                HSSFRow row = hssfSheet.createRow(i + 1);
                //遍历写入每一列
                for (int j = 0; j < columnSize; j++) {
                    ColumnFiledName columnFiledName = columnNameList.get(j);
                    Field declaredField = clazz.getDeclaredField(columnFiledName.getFiledName());
                    declaredField.setAccessible(true);
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    Object obj = declaredField.get(t);
                    obj = obj == null ? "" : obj;
                    cell.setCellValue(obj.toString());
                }
            }
            return hssfWorkbook;
        }
        return null;
    }

    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static boolean isCSV(String filePath) {
        return filePath.matches("^.+\\.(?i)(csv)$");
    }
}
