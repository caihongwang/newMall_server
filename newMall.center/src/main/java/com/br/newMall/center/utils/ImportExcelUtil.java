package com.br.newMall.center.utils;

import com.br.newMall.center.service.BaseConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 导入excel文件
 *
 * @param <T>
 */
public class ImportExcelUtil<T> {
    private int totalRows = 0;
    private int totalCells = 0;
    private String errorInfo;
    private List<T> data;
    private BaseConvert baseConvert;
    private volatile boolean readed = false;
    private int bufSize = 1000000;//一次读取的字节长度

    public ImportExcelUtil() {
        this.data = new ArrayList<>();
    }

    public ImportExcelUtil(BaseConvert baseConvert) {
        data = new ArrayList<>();
        this.baseConvert = baseConvert;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public void setTotalCells(int totalCells) {
        this.totalCells = totalCells;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<T> getData() {
        return this.data;
    }

    private boolean validateExcel(String filePath) {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if (filePath == null || !(PoiUtils.isExcel2003(filePath) || PoiUtils.isExcel2007(filePath))) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /** 检查文件是否存在 */
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    public void read(String filePath, Class<T> clazz, int sheet) {
        readed = true;
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            boolean flag = validateExcel(filePath);
            if (!flag) {
                if (PoiUtils.isCSV(filePath)) {
                    readCSV(filePath, clazz);
                } else {
                    System.out.println("暂不支持该格式");
                }
                return;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (PoiUtils.isExcel2007(filePath)) {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            realRead(is, isExcel2003, clazz, sheet);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public void read(String filePath, Class<T> clazz) {
        read(filePath, clazz, -1);
    }

    private void realRead(InputStream inputStream, boolean isExcel2003, Class<T> clazz, int sheet) {
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            int sheets = wb.getNumberOfSheets();
            if (sheet != -1 && sheet > sheets - 1) {
                errorInfo = "sheet out of size";
                return;
            }
            if (sheet != -1) {
                read(wb, sheet, clazz);
            } else {
                for (int i = 0; i < sheets; i++) {
                    read(wb, i, clazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(Workbook wb, int index, Class<T> clazz) {
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(index);
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        Map<Integer, Method> columnMethod = new HashMap<>();
//        Method isSuccessMethod = null;
//        Method getLoanDateMethod = null;
//        try {
//            isSuccessMethod = clazz.getDeclaredMethod("isSuccess");
//            getLoanDateMethod = clazz.getDeclaredMethod("getLoanDate");
//        } catch (NoSuchMethodException e) {
//            System.out.println("获取检查方法失败");
//        }

        /** 循环Excel的行 */
        DecimalFormat decimalFormat = new DecimalFormat("#");
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for (int c = 0; c < this.getTotalCells(); c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    // 以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date date = cell.getDateCellValue();
                                cellValue = baseConvert.getDateString(date);
                            } else {
                                //手机号
                                cellValue = decimalFormat.format(cell.getNumericCellValue());
                            }
                            break;
                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }
                rowLst.add(StringUtils.trim(cellValue));
            }
            /** 保存第r行的第c列 */
            boolean isNull = true;
            for (String str : rowLst) {
                if (StringUtils.isNotEmpty(str)) {
                    isNull = false;
                }
            }
            if (isNull) {
                continue;
            }
            if (r == 0) {
                //表头
                for (int i = 0, size = rowLst.size(); i < size; i++) {
                    String value = rowLst.get(i);
                    Method method = getMethodByColumnName(value, clazz);
                    columnMethod.put(i, method);
                }
            } else {
                try {
                    T instance = clazz.newInstance();
                    for (int i = 0, size = rowLst.size(); i < size; i++) {
                        Method method = columnMethod.get(i);
                        if (method != null) {
                            method.invoke(instance, rowLst.get(i));
                        }
                    }
//                    boolean isSuccess = (Boolean) isSuccessMethod.invoke(instance);
//                    if(isSuccess) {
//                        Date loanDate = (Date) getLoanDateMethod.invoke(instance);
//                        if(loanDate != null && loanDate.after(startDate) && loanDate.before(endDate)){
                    data.add(instance);
//                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Method getMethodByColumnName(String columnName, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ColumnNameUtils annotation = field.getAnnotation(ColumnNameUtils.class);
            if (annotation != null) {
                String value = annotation.value();
                if (StringUtils.isNotEmpty(value) && value.equals(columnName)) {
                    String fieldName = field.getName();
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        return clazz.getDeclaredMethod(methodName, new Class[]{field.getType()});
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    private void readCSV(String filePath, Class<T> clazz) {
        try {
            String charset = "GBK";
            if (isUTF8(filePath)) {
                charset = "UTF-8";
            }

            File file = new File(filePath);

            FileChannel fcin = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);

            readFileByLine(clazz, charset, bufSize, fcin, rBuffer);

//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
//            String firstLine = reader.readLine();
//            if (StringUtils.isNotEmpty(firstLine)) {
//                String[] columnList = firstLine.split(",");
//                for (int i = 0, size = columnList.length; i < size; i++) {
//                    Method method = getMethodByColumnName(columnList[i], clazz);
//                    columnMethod.put(i, method);
//                }
//            } else {
//                System.out.println("CSV格式不正确");
//            }

//            String line = null;
//            long num = 0;
//            while ((line = reader.readLine()) != null) {
//                T instance = clazz.newInstance();
//                String[] strings = line.split(",");
//                for (int i = 0, size = strings.length; i < size; i++) {
//                    Method method = columnMethod.get(i);
//                    if (method != null) {
//                        method.invoke(instance, StringUtils.trim(strings[i]));
//                    }
//                }
//                boolean isSuccess = (Boolean) isSuccessMethod.invoke(instance);
//                if(isSuccess) {
//                    Date loanDate = (Date) getLoanDateMethod.invoke(instance);
//                    if(loanDate != null && loanDate.after(startDate) && loanDate.before(endDate)){
//                        this.data.add(instance);
//                    }
//                }
//            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUTF8(String filePath) {
        try {
            InputStream is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b);
            is.close();
            if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void readFileByLine(Class<T> clazz, String charset, int bufSize, FileChannel fcin,
                                ByteBuffer rBuffer) {
        String enter = "\n";
        byte[] lineByte = new byte[0];
        boolean firstLine = true;
        //缓存方法
        Map<Integer, Method> columnMethod = new HashMap<>();

        //你没用
//        Method isSuccessMethod = null;
//        Method getLoanDateMethod = null;
//        try {
//            isSuccessMethod = clazz.getDeclaredMethod("isSuccess");
//            getLoanDateMethod = clazz.getDeclaredMethod("getLoanDate");
//        } catch (NoSuchMethodException e) {
//            System.out.println("获取检查方法失败");
//        }

        try {
            //temp：由于是按固定字节读取，在一次读取中，第一行和最后一行经常是不完整的行，因此定义此变量来存储上次的最后一行和这次的第一行的内容，
            //并将之连接成完成的一行，否则会出现汉字被拆分成2个字节，并被提前转换成字符串而乱码的问题
            byte[] temp = new byte[0];
            while (fcin.read(rBuffer) != -1) {//fcin.read(rBuffer)：从文件管道读取内容到缓冲区(rBuffer)
                int rSize = rBuffer.position();//读取结束后的位置，相当于读取的长度
                byte[] bs = new byte[rSize];//用来存放读取的内容的数组
                rBuffer.rewind();//将position设回0,所以你可以重读Buffer中的所有数据,此处如果不设置,无法使用下面的get方法
                rBuffer.get(bs);//相当于rBuffer.get(bs,0,bs.length())：从position初始位置开始相对读,读bs.length个byte,并写入bs[0]到bs[bs.length-1]的区域
                rBuffer.clear();

                int startNum = 0;
                int LF = 10;//换行符
                int CR = 13;//回车符
                boolean hasLF = false;//是否有换行符
                for (int i = 0; i < rSize; i++) {
                    if (bs[i] == LF) {
                        hasLF = true;
                        int tempNum = temp.length;
                        int lineNum = i - startNum;
                        lineByte = new byte[tempNum + lineNum];//数组大小已经去掉换行符

                        System.arraycopy(temp, 0, lineByte, 0, tempNum);//填充了lineByte[0]~lineByte[tempNum-1]
                        temp = new byte[0];
                        System.arraycopy(bs, startNum, lineByte, tempNum, lineNum);//填充lineByte[tempNum]~lineByte[tempNum+lineNum-1]

                        String line = new String(lineByte, 0, lineByte.length - 1, charset);//一行完整的字符串(过滤了换行和回车)
                        //获取到line
                        if (firstLine) {
                            firstLine = false;
                            String[] columnList = line.split(",");
                            for (int fline = 0, size = columnList.length; fline < size; fline++) {
                                Method method = getMethodByColumnName(columnList[fline], clazz);
                                columnMethod.put(fline, method);
                            }
                        } else {
                            handleLine(clazz, line, columnMethod);
                        }
                        //过滤回车符和换行符
                        if (i + 1 < rSize && bs[i + 1] == CR) {
                            startNum = i + 2;
                        } else {
                            startNum = i + 1;
                        }
                    }
                }
                if (hasLF) {
                    temp = new byte[bs.length - startNum];
                    System.arraycopy(bs, startNum, temp, 0, temp.length);
                } else {//兼容单次读取的内容不足一行的情况
                    byte[] toTemp = new byte[temp.length + bs.length];
                    System.arraycopy(temp, 0, toTemp, 0, temp.length);
                    System.arraycopy(bs, 0, toTemp, temp.length, bs.length);
                    temp = toTemp;
                }
            }
            if (temp != null && temp.length > 0) {//兼容文件最后一行没有换行的情况
                String line = new String(temp, 0, temp.length, charset);
                handleLine(clazz, line, columnMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLine(Class<T> clazz, String line, Map<Integer, Method> columnMethod) {
        try {
            T instance = clazz.newInstance();
            String[] strings = line.split(",");
            for (int index = 0, size = strings.length; index < size; index++) {
                Method method = columnMethod.get(index);
                if (method != null) {
                    method.invoke(instance, StringUtils.trim(strings[index]));
                }
            }
//            boolean isSuccess = (Boolean) isSuccessMethod.invoke(instance);
//            if(isSuccess) {
//                Date loanDate = (Date) getLoanDateMethod.invoke(instance);
//                if(loanDate != null && loanDate.after(startDate) && loanDate.before(endDate)){
            this.data.add(instance);
//                }
//            }
        } catch (Exception e) {
            System.out.println("处理每行数据出错");
        }
    }

    @Override
    public String toString() {
        return "ImportExcelUtil{" +
                "totalRows=" + totalRows +
                ", totalCells=" + totalCells +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }
}
