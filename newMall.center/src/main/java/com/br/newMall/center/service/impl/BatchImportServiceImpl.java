package com.br.newMall.center.service.impl;

import com.br.newMall.center.po.ContactPO;
import com.br.newMall.center.service.BatchImportService;
import com.br.newMall.center.utils.ImportExcelUtil;
import com.br.newMall.center.utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 字典service
 */
@Service
public class BatchImportServiceImpl implements BatchImportService {

    private static final Logger logger = LoggerFactory.getLogger(BatchImportServiceImpl.class);

    @Value("${contact.filepath}")
    private String contactPath;

    /**
     * 将excel通讯录转换为vcf
     * 2018年松桃县大路镇干部职工花名册
     */
    @Override
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
