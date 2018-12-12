package com.br.newMall.center.service.impl;

import com.br.newMall.api.code.OilStationMapCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.po.ContactPO;
import com.br.newMall.center.po.DangerousCompanyPO;
import com.br.newMall.center.service.OilStationService;
import com.br.newMall.center.utils.FileUtil;
import com.br.newMall.center.utils.ImportExcelUtil;
import com.br.newMall.center.utils.LonLatUtil;
import com.br.newMall.center.utils.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2018/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/center-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class BatchImportServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(BatchImportServiceImplTest.class);

    @Value("${contact.filepath}")
    private String contactPath;

    @Value("${excel.filepath}")
    private String excelPath;

    @Autowired
    private OilStationService oilStationService;

    @Test
    public void Test() throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
//        paramMap.put("destFilePath", "/Users/caihongwang/Desktop/2018年松桃县大路镇村干部通讯录.xlsx");
//        paramMap.put("destFilePath", "/Users/caihongwang/Desktop/2018年松桃县大路镇镇干部通讯录.xlsx");
//        convertExcelToVcf(paramMap);

        paramMap.put("destFilePath", "/Users/caihongwang/Desktop/危险化学品单位.xlsx");
        convertExcelToExcel(paramMap);


    }

    public void convertExcelToVcf(Map<String, Object> paramMap) {
        StringBuffer contact_stringBuffer = new StringBuffer();
        String destFilePath = paramMap.get("destFilePath") != null ? paramMap.get("destFilePath").toString() : "";
        if (!"".equals(destFilePath)) {
            ImportExcelUtil<ContactPO> importExcelUtil = new ImportExcelUtil<>();
            importExcelUtil.read(destFilePath, ContactPO.class, 0);
            List<ContactPO> ContactPOList = importExcelUtil.getData();
            if (ContactPOList != null && ContactPOList.size() > 0) {
                //1.整合数据
                for (ContactPO ContactPO : ContactPOList) {
                    try {
                        Map<String, Object> paramMap_ContactPO = MapUtil.objectToMap(ContactPO);
                        String name = paramMap_ContactPO.get("name") != null ? paramMap_ContactPO.get("name").toString() : "";
                        String phone = paramMap_ContactPO.get("phone") != null ? paramMap_ContactPO.get("phone").toString() : "";
                        String job = paramMap_ContactPO.get("job") != null ? paramMap_ContactPO.get("job").toString() : "";
                        String company = paramMap_ContactPO.get("company") != null ? paramMap_ContactPO.get("company").toString() : "";
                        String administrativeDivision = paramMap_ContactPO.get("administrativeDivision") != null ? paramMap_ContactPO.get("administrativeDivision").toString() : "";
                        String jurisdiction = paramMap_ContactPO.get("jurisdiction") != null ? paramMap_ContactPO.get("jurisdiction").toString() : "";
                        if (!"".equals(name) && !"".equals(phone)) {
                            contact_stringBuffer.append("BEGIN:VCARD").append("\n");
                            contact_stringBuffer.append("VERSION:3.0").append("\n");
                            contact_stringBuffer.append("PRODID:-//Apple Inc.//Mac OS X 10.13.2//EN").append("\n");
                            contact_stringBuffer.append("N:;").append(name).append(";;;").append("\n");
                            contact_stringBuffer.append("FN:").append(name).append("\n");
                            contact_stringBuffer.append("NICKNAME:").append(job).append("\n");
                            contact_stringBuffer.append("ORG:").append(administrativeDivision).append("-").append(company).append("\n");
                            contact_stringBuffer.append("TEL;type=HOME;type=VOICE;type=pref:").append(phone).append("\n");
                            contact_stringBuffer.append("item1.ADR;type=pref:;;").append(jurisdiction).append(";;;;中国").append("\n");
                            contact_stringBuffer.append("item1.X-ABLabel:职能辖区").append("\n");
                            contact_stringBuffer.append("item1.X-ABADR:cn").append("\n");
                            contact_stringBuffer.append("END:VCARD").append("\n");
                        }
                    } catch (Exception e) {
                        logger.info("在service中将excel通讯录转换为vcf-convertExcelToVcf,发生错误。 e : " + e);
                        continue;
                    }
                }
                //2.整合数据变成vcf文件
                try {
                    File destFile = new File(destFilePath);
                    String destFileName = destFile.getName();
                    String[] destFileNameArr = destFileName.split("\\.");
                    String vcfFilePath = contactPath + destFileNameArr[0] + ".vcf";
                    File vcfFile = new File(vcfFilePath);
                    if (vcfFile.exists()) {           //如果存在，则创建具有时间戳的vcf
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateStr = sdf.format(date);
                        vcfFilePath = contactPath + destFileNameArr[0] + "-" + currentDateStr + ".vcf";
                        vcfFile = new File(vcfFilePath);
                    }
                    vcfFile.createNewFile();
                    writeFileContent(vcfFilePath, contact_stringBuffer.toString());
                } catch (Exception e) {
                    logger.info("在service中将excel通讯录转换为vcf-convertExcelToVcf,保存文件时发生错误。 e : " + e);
                }
            }
        } else {
            logger.info("请输入Excel通讯录文件的路径");
        }
    }


    /**
     * 将excel进行处理再重新创建excel
     * @param paramMap
     */
    public ResultMapDTO convertExcelToExcel(Map<String, Object> paramMap) {
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        StringBuffer contact_stringBuffer = new StringBuffer();
        String destFilePath = paramMap.get("destFilePath") != null ? paramMap.get("destFilePath").toString() : "";
        if (!"".equals(destFilePath)) {
            List<Map<String, Object>> newDangerousCompanyPOList = Lists.newArrayList();
            ImportExcelUtil<DangerousCompanyPO> importExcelUtil = new ImportExcelUtil<>();
            importExcelUtil.read(destFilePath, DangerousCompanyPO.class, 0);
            List<DangerousCompanyPO> dangerousCompanyPOList = importExcelUtil.getData();
            if (dangerousCompanyPOList != null && dangerousCompanyPOList.size() > 0) {
                //1.整合数据
                for (int i = 0; i < dangerousCompanyPOList.size(); i++) {
                    DangerousCompanyPO dangerousCompanyPO = dangerousCompanyPOList.get(i);
                    try {
                        String companyName = dangerousCompanyPO.getCompanyName();
                        if(companyName != null && !"".equals(companyName)){
                            Map<String, Object> detailAddressMap = LonLatUtil.getDetailAddressByKeyWord(companyName);
                            detailAddressMap.put("companyName", companyName);
                            newDangerousCompanyPOList.add(detailAddressMap);
                        }else {
                            logger.info("在LonLatUtil中将excel进行处理再重新创建excel-companyName不允许为空。");
                            continue;
                        }
                    } catch (Exception e) {
                        logger.info("在LonLatUtil中将excel进行处理再重新创建excel-convertExcelToExcel,发生错误。 e : " + e);
                        continue;
                    }
//                    if(i > 10){
//                        break;
//                    }
                }
                //2.整合数据变成新的excel文件
                if (newDangerousCompanyPOList != null) {
                    String[] excelHeader = {"单位名称", "地图上_单位名称", "经度", "纬度", "经纬度", "省份", "城市", "地区", "街道", "街道号", "详细地址"};
                    int[] excelHeaderWidth = {200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200};
                    try {
                        logger.info("createXLSX begin...");
                        Date currentDate = new Date();          //定时任务在每天下午17点整开始生成报表
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(currentDate);
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        HSSFWorkbook wb = new HSSFWorkbook();
                        HSSFSheet sheet = wb.createSheet("危险化学品单位");
                        HSSFCellStyle style = wb.createCellStyle();
                        // 设置居中样式
                        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
                        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
                        HSSFRow row = sheet.createRow((int) 0);
                        // 设置列宽度（像素）
                        for (int i = 0; i < excelHeaderWidth.length; i++) {
                            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
                        }
                        // 添加表格头
                        for (int i = 0; i < excelHeader.length; i++) {
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(excelHeader[i]);
                            cell.setCellStyle(style);
                        }
                        for (int i = 0; i < newDangerousCompanyPOList.size(); i++) {
                            row = sheet.createRow(i + 1);
                            Map<String, Object> tempMap = newDangerousCompanyPOList.get(i);
                            row.createCell(0).setCellValue(tempMap.get("companyName") != null ? tempMap.get("companyName").toString() : "");       //单位名称
                            row.createCell(1).setCellValue(tempMap.get("title") != null ? tempMap.get("title").toString() : "");       //地图上_单位名称
                            row.createCell(2).setCellValue(tempMap.get("lng") != null ? tempMap.get("lng").toString() : "");       //经度
                            row.createCell(3).setCellValue(tempMap.get("lat") != null ? tempMap.get("lat").toString() : "");       //纬度
                            row.createCell(4).setCellValue(
                                    (tempMap.get("lng") != null ? tempMap.get("lng").toString() : "")
                                    + ","
                                    + (tempMap.get("lat") != null ? tempMap.get("lat").toString() : "")
                                    );       //经纬度
                            row.createCell(5).setCellValue(tempMap.get("province") != null ? tempMap.get("province").toString() : "");       //省份
                            row.createCell(6).setCellValue(tempMap.get("city") != null ? tempMap.get("city").toString() : "");       //城市
                            row.createCell(7).setCellValue(tempMap.get("district") != null ? tempMap.get("district").toString() : "");       //地区
                            row.createCell(8).setCellValue(tempMap.get("street") != null ? tempMap.get("street").toString() : "");       //街道
                            row.createCell(9).setCellValue(tempMap.get("street_number") != null ? tempMap.get("street_number").toString() : "");       //街道号
                            row.createCell(10).setCellValue(
                                    (tempMap.get("province") != null ? tempMap.get("province").toString() : "")
                                    + ","
                                    + (tempMap.get("city") != null ? tempMap.get("city").toString() : "")
                                    + ","
                                    + (tempMap.get("district") != null ? tempMap.get("district").toString() : "")
                                    + ","
                                    + (tempMap.get("street") != null ? tempMap.get("street").toString() : "")
                                    + ","
                                    + (tempMap.get("street_number") != null ? tempMap.get("street_number").toString() : "")
                            );       //详细地址
                        }
                        String exlsFilePath = FileUtil.createFile(wb, excelPath, "xls");
                        resultMapDTO.setCode(OilStationMapCode.SUCCESS.getNo());
                        resultMapDTO.setMessage(exlsFilePath);
                    } catch (Exception e) {
                        logger.error("在LonLatUtil中将excel进行处理再重新创建excel is failed。", e);
                        resultMapDTO.setCode(OilStationMapCode.SERVER_INNER_ERROR.getNo());
                        resultMapDTO.setMessage(OilStationMapCode.SERVER_INNER_ERROR.getMessage());
                    }
                }
                //3.将获取的加油站数据进行入库处理
                if (newDangerousCompanyPOList != null) {
                    for (int i = 0; i < newDangerousCompanyPOList.size(); i++) {
                        Map<String, Object> newDangerousCompanyPO = newDangerousCompanyPOList.get(i);
                        if(newDangerousCompanyPO.get("title") != null && !"".equals(newDangerousCompanyPO.get("title").toString())){
                            paramMap.clear();       //清空参数，重新整理参数
                            paramMap.put("oilStationName", newDangerousCompanyPO.get("title") != null ? newDangerousCompanyPO.get("title").toString() : "");
                            paramMap.put("oilStationAdress",
                                    ((newDangerousCompanyPO.get("province") != null ? newDangerousCompanyPO.get("province").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("city") != null ? newDangerousCompanyPO.get("city").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("district") != null ? newDangerousCompanyPO.get("district").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("street") != null ? newDangerousCompanyPO.get("street").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("street_number") != null ? newDangerousCompanyPO.get("street_number").toString() : ""))
                            );
                            paramMap.put("oilStationLon", newDangerousCompanyPO.get("lng") != null ? newDangerousCompanyPO.get("lng").toString() : "");
                            paramMap.put("oilStationLat", newDangerousCompanyPO.get("lat") != null ? newDangerousCompanyPO.get("lat").toString() : "");
                            paramMap.put("oilStationPrice", "[{\"oilModelLabel\":\"0\",\"oilNameLabel\":\"柴油\",\"oilPriceLabel\":\"7.43\"},{\"oilModelLabel\":\"92\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"7.65\"},{\"oilModelLabel\":\"95\",\"oilNameLabel\":\"汽油\",\"oilPriceLabel\":\"8.28\"}]");
                            paramMap.put("oilStationAreaSpell", "tongren");
                            paramMap.put("oilStationAreaName", (
                                    (newDangerousCompanyPO.get("city") != null ? newDangerousCompanyPO.get("city").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("district") != null ? newDangerousCompanyPO.get("district").toString() : ""))
                            );
                            paramMap.put("oilStationType", "民营");
                            paramMap.put("oilStationBrandName", "民营");
                            paramMap.put("oilStationDiscount", "打折加油站");
                            paramMap.put("oilStationExhaust", "国Ⅳ");
                            paramMap.put("oilStationPosition",
                                    ((newDangerousCompanyPO.get("lng") != null ? newDangerousCompanyPO.get("lng").toString() : "")
                                            + ","
                                            + (newDangerousCompanyPO.get("lat") != null ? newDangerousCompanyPO.get("lat").toString() : ""))
                            );
                            paramMap.put("oilStationPayType", "加油卡,便利店");
                            paramMap.put("isManualModify", 1);
                            oilStationService.addOrUpdateOilStation(paramMap);
                        } else {
                            continue;
                        }
                    }
                }

            }
        } else {
            logger.info("请输入Excel通讯录文件的路径");
            resultMapDTO.setCode(OilStationMapCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(OilStationMapCode.SERVER_INNER_ERROR.getMessage());
        }
        return resultMapDTO;
    }


    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";//新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
}
