package com.br.newMall.center.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * 文件转化工具
 * Created by caihongwang on 2018/1/31.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建文件
     * @param imgBase64Data   文件的imgBase64Data
     * @param fileSuffix   文件后缀名，默认是jpg
     * @return
     */
    public static String createFile(String imgBase64Data, String currentFileUploadUrl, String fileSuffix) {
        String fileUploadUrl_temp = "";
        FileUtil fileUtil = new FileUtil();
        if(!"".equals(imgBase64Data)){
            if(fileSuffix == null || "".equals(fileSuffix)){
                fileSuffix = "jpg";
            }
            try{
                int size;
                byte[] buffer = new byte[1024 * 1000000];
                long startTime = System.currentTimeMillis();
                //判断 文件夹 是否存存在，如果不存在则创建
                File dirFile = new File(currentFileUploadUrl);
                if (!dirFile.getParentFile().getParentFile().exists()) {
                    dirFile.getParentFile().getParentFile().mkdirs();
                }
                if (!dirFile.getParentFile().exists()) {
                    dirFile.getParentFile().mkdirs();
                }
                if(!dirFile.exists()){
                    dirFile.mkdir();
                }
                fileUploadUrl_temp = currentFileUploadUrl + UUID.randomUUID() + "_" + startTime + "." + fileSuffix;
                buffer = new BASE64Decoder().decodeBuffer(imgBase64Data);
                FileOutputStream out = new FileOutputStream(fileUploadUrl_temp);
                out.write(buffer);
                out.close();
                logger.info("创建本地图片文件，在当前服务器中的本地地址 : " + fileUploadUrl_temp);
            } catch (Exception e) {
                logger.error("创建本地图片文件失败. e : " + e);
            }
        } else {
            logger.error("创建本地图片文件失败. imgBase64Data 不允许为空。");
        }
        return fileUploadUrl_temp;
    }
    /**
     * 创建文件
     * @param wb   文件的Object
     * @param fileSuffix   文件后缀名，默认是jpg
     * @return
     */
    public static String createFile(HSSFWorkbook wb, String currentFileUploadUrl, String fileSuffix) {
        String fileUploadUrl_temp = "";
        FileUtil fileUtil = new FileUtil();
        if(wb != null){
            if(fileSuffix == null || "".equals(fileSuffix)){
                fileSuffix = "xls";
            }
            try{
                int size;
                byte[] buffer = new byte[1024 * 1000000];
                long startTime = System.currentTimeMillis();
                //判断 文件夹 是否存存在，如果不存在则创建
                File dirFile = new File(currentFileUploadUrl);
                if (!dirFile.getParentFile().getParentFile().exists()) {
                    dirFile.getParentFile().getParentFile().mkdirs();
                }
                if (!dirFile.getParentFile().exists()) {
                    dirFile.getParentFile().mkdirs();
                }
                if(!dirFile.exists()){
                    dirFile.mkdir();
                }
                fileUploadUrl_temp = currentFileUploadUrl + UUID.randomUUID() + "_" + startTime + "." + fileSuffix;
                FileOutputStream out = new FileOutputStream(fileUploadUrl_temp);
                wb.write(out);
                out.close();
                logger.info("创建本地exls文件，在当前服务器中的本地地址 : " + fileUploadUrl_temp);
            } catch (Exception e) {
                logger.error("创建本地exls文件失败. e : " + e);
            }
        } else {
            logger.error("创建本地图片文件失败. imgBase64Data 不允许为空。");
        }
        return fileUploadUrl_temp;
    }

    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
