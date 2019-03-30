package com.br.newMall.center.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.newMall.dao.WX_ProductDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caihongwang on 2019/3/4.
 */
public class SpriderFor7DingdongProductUtilTest {

    public static final Logger logger = LoggerFactory.getLogger(SpriderFor7DingdongProductUtil.class);

    @Autowired
    private WX_ProductDao wxProductDao;

    @Test
    public void Test() {
        Map<String, Object> paramMap = Maps.newHashMap();
        get7DingdongProduct(paramMap);
    }

    /**
     * 获取所有企叮咚的商品
     * @param paramMap
     */
    public void get7DingdongProduct(Map<String, Object> paramMap) {
        List<String> productSqlList = Lists.newArrayList();
        String dingGong7ProductPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/企叮咚商品/";
        //获取当前文件夹下的所有文件
        File dingGong7ProductDir = new File(dingGong7ProductPath);
        if (dingGong7ProductDir.isDirectory()) {
            String[] productCategrylist = dingGong7ProductDir.list();
            for (int i = 0; i < productCategrylist.length; i++) {
                String productCategryPath = dingGong7ProductPath + productCategrylist[i];
                File productCategryFile = new File(productCategryPath);
                if (productCategrylist[i].startsWith(".")){
                    continue;
                }
                if (productCategryFile.isDirectory()) {
                    String productCatoryName = productCategryFile.getName();
                    //删除已有的商品类目,如果存在则删除
                    String currentPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/";
                    File productCatoryFile = new File(currentPath + productCatoryName);
                    if(productCatoryFile.exists()){
                        boolean deleteFlag = FileUtil.deleteDir(productCatoryFile);
                    }
                    String[] productlist = productCategryFile.list();
                    for(String productName : productlist){
                        if (productName.startsWith(".")){
                            continue;
                        }
                        String productPath = productCategryPath + "/" + productName;
                        File productFile = new File(productPath);
                        StringBuilder productJson = new StringBuilder();
                        try{
                            BufferedReader br = new BufferedReader(new FileReader(productFile));//构造一个BufferedReader类来读取文件
                            String s = null;
                            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                                productJson.append(System.lineSeparator()+s);
                            }
                            br.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        JSONObject productJsonObject = JSONObject.parseObject(productJson.toString());
                        JSONArray productData = productJsonObject.getJSONArray("data");
                        List<Map<String, Object>> productList = JSONObject.parseObject(JSONObject.toJSONString(productData), List.class);
                        for(Map<String, Object> productMap : productList){
                            String goodsId = productMap.get("goods_id").toString();
                            getSimpleProduct(productCatoryName, goodsId, productSqlList);
                        }
                    }
                    //将所有的SQL存放到文件中去
                    String allProductSqlPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/insertAllProductSql.txt";
                    try {
                        // 创建文件对象
                        File allProductSqlFile = new File(allProductSqlPath);
                        allProductSqlFile.delete();
                        allProductSqlFile.createNewFile();
                        FileWriter fileWriter = new FileWriter(allProductSqlFile);
                        StringBuffer strBuffer = new StringBuffer();
                        for (String sql : productSqlList) {
                            strBuffer.append(sql);
                        }
                        // 写文件
                        fileWriter.write(strBuffer.toString());
                        // 关闭
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error(productCategryPath + " , 当前路径不是文件夹.");
                }
            }
        } else {
            logger.error(dingGong7ProductPath + " , 当前路径不是文件夹.");
        }
    }

    /**
     * 根据企叮咚商品ID来复制商品信息
     * @param productCatoryName
     * @param goodsId
     * @param insertProductSqlList
     * @return
     */
    public List<String> getSimpleProduct(String productCatoryName, String goodsId, List<String> insertProductSqlList){
        String goodDetailUrl = "http://www.7dingdong.com/goods/addCart?gid="+goodsId;
        String productPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/";
        //创建商品类目,并将商品保存在当前类目的路径下
        productPath = productPath + productCatoryName + "/";
        File productCatoryFile = new File(productPath);
        if(!productCatoryFile.exists() && !productCatoryFile.isDirectory()){
            productCatoryFile.mkdir();
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(goodDetailUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                    .timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取 商品名称
        String title = "";
        Elements titleElements = doc.getElementsByTag("h2");
        for (Element element : titleElements) {
            String className = element.attr("class");
            if ("title".equals(className)) {
                Node titleNode = element.childNode(0);
                title = titleNode.toString();
                break;
            }
        }

        //根据商品名称创建文件夹
        productPath = productPath + title + "/";
        File file = new File(productPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //获取 商品积分
        String integral = "0";
        Elements integralElements = doc.getElementsByTag("strong");
        for (Element element : integralElements) {
            String className = element.attr("class");
            if ("zx_lsj zxl".equals(className)) {
                Node integralNode = element.childNode(0);
                String[] integralArr = integralNode.toString().split("￥");
                integral = integralArr[1];
                break;
            }
        }
        //获取 商品库存
        String stock = "0";
        Elements stockElements = doc.getElementsByTag("em");
        for (Element element : stockElements) {
            String className = element.attr("id");
            if ("spec_stockFic".equals(className)) {
                Node stockNode = element.childNode(0);
                stock = stockNode.toString();
                break;
            }
        }
        //获取 商品头图片链接并下载
        String headImgUrl = "";
        Elements headImgUrlElements = doc.getElementsByTag("img");
        for (Element element : headImgUrlElements) {
            String idName = element.attr("id");
            if ("midimg".equals(idName)) {
                String theImgUrl = element.attr("src");
                String imgNamePath = productPath + "headImg.jpg";
                ImageDownloadUtil.downloadPicture(theImgUrl, imgNamePath);
                headImgUrl = "https://www.91caihongwang.com/resourceOfNewMall/product/" +
                        productCatoryName + "/" + title + "/headImg.jpg";
                break;
            }
        }

        //获取 商品头图片链接
        String describeImgUrl = "";
        List<String> describeImgList = Lists.newArrayList();
        Elements describeImgUrlElements = doc.getElementsByTag("div");
        for (Element element : describeImgUrlElements) {
            String className = element.attr("class");
            if ("sd_item_flash sh_cont con0".equals(className)) {
                List<Node> nodeList = element.childNodes();
                if (nodeList != null && nodeList.size() > 0) {
                    for (int i = 1; i <= nodeList.size(); i++) {
                        Node node = nodeList.get(i - 1);
                        String imgUrl = node.attr("src");
                        if (!"".equals(imgUrl) && !describeImgList.contains(imgUrl)) {
                            String theImgUrl = "http://statics.76sd.com" + imgUrl;
                            String imgNamePath = productPath + i + ".jpg";
                            ImageDownloadUtil.downloadPicture(theImgUrl, imgNamePath);

                            theImgUrl = "https://www.91caihongwang.com/resourceOfNewMall/product/" +
                                    productCatoryName + "/" + title + "/" + i + ".jpg";
                            describeImgList.add(theImgUrl);
                        }
                    }
                }
                describeImgUrl = JSONObject.toJSONString(describeImgList);
                break;
            }
        }
        //整合SQL
        String insertProductSql = "INSERT INTO new_mall.n_product\n" +
                "(title, degist, descript, stock, head_img_url, describe_img_url,\n" +
                " price, integral, category, status, create_time, update_time)\n" +
                "VALUES (\n" +
                "  '" + title + "',\n" +
                "  '" + title + "',\n" +
                "  '" + title + "',\n" +
                "  '" + stock + "',\n" +
                "  '" + headImgUrl + "',\n" +
                "  '" + describeImgUrl + "',\n" +
                "  99999,'" + integral + "', '" + productCatoryName + "', 0, \n" +
                "  '2019-03-01 22:00:00', '2019-03-01 22:00:00');";
        logger.info("商品名称【"+title+"】的图片信息和SQL保存成功.");
        insertProductSqlList.add(insertProductSql);
        return insertProductSqlList;
    }

    /**
     * 更新所有企叮咚的商品
     * @param paramMap
     */
    public void update7DingdongProduct(Map<String, Object> paramMap) {
        List<String> productSqlList = Lists.newArrayList();
        String dingGong7ProductPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/企叮咚商品/";
        //获取当前文件夹下的所有文件
        File dingGong7ProductDir = new File(dingGong7ProductPath);
        if (dingGong7ProductDir.isDirectory()) {
            String[] productCategrylist = dingGong7ProductDir.list();
            for (int i = 0; i < productCategrylist.length; i++) {
                String productCategryPath = dingGong7ProductPath + productCategrylist[i];
                File productCategryFile = new File(productCategryPath);
                if (productCategrylist[i].startsWith(".")){
                    continue;
                }
                if (productCategryFile.isDirectory()) {
                    String productCatoryName = productCategryFile.getName();
                    //删除已有的商品类目,如果存在则删除
                    String currentPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/";
                    File productCatoryFile = new File(currentPath + productCatoryName);
                    if(productCatoryFile.exists()){
                        boolean deleteFlag = FileUtil.deleteDir(productCatoryFile);
                    }
                    String[] productlist = productCategryFile.list();
                    for(String productName : productlist){
                        if (productName.startsWith(".")){
                            continue;
                        }
                        String productPath = productCategryPath + "/" + productName;
                        File productFile = new File(productPath);
                        StringBuilder productJson = new StringBuilder();
                        try{
                            BufferedReader br = new BufferedReader(new FileReader(productFile));//构造一个BufferedReader类来读取文件
                            String s = null;
                            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                                productJson.append(System.lineSeparator()+s);
                            }
                            br.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        JSONObject productJsonObject = JSONObject.parseObject(productJson.toString());
                        JSONArray productData = productJsonObject.getJSONArray("data");
                        List<Map<String, Object>> productList = JSONObject.parseObject(JSONObject.toJSONString(productData), List.class);
                        for(Map<String, Object> productMap : productList){
                            String goodsId = productMap.get("goods_id").toString();
                            updateProduct(productCatoryName, goodsId, productSqlList);
                        }
                    }
                    //将所有的SQL存放到文件中去
                    String allProductSqlPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/updateAllProductSql.txt";
                    try {
                        // 创建文件对象
                        File allProductSqlFile = new File(allProductSqlPath);
                        allProductSqlFile.delete();
                        allProductSqlFile.createNewFile();
                        FileWriter fileWriter = new FileWriter(allProductSqlFile);
                        StringBuffer strBuffer = new StringBuffer();
                        for (String sql : productSqlList) {
                            strBuffer.append(sql);
                        }
                        // 写文件
                        fileWriter.write(strBuffer.toString());
                        // 关闭
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error(productCategryPath + " , 当前路径不是文件夹.");
                }
            }
        } else {
            logger.error(dingGong7ProductPath + " , 当前路径不是文件夹.");
        }
    }

    /**
     * 根据企叮咚商品ID来复制商品信息
     * @param deductionName
     * @param goodsId
     * @param updateProductSqlList
     * @return
     */
    public List<String> updateProduct(String deductionName, String goodsId, List<String> updateProductSqlList){
        String goodDetailUrl = "http://www.7dingdong.com/goods/addCart?gid="+goodsId;
        String productPath = "/opt/newMall_tomcat/webapps/resourceOfNewMall/product/";
        //创建商品类目,并将商品保存在当前类目的路径下
        productPath = productPath + deductionName + "/";
        File productCatoryFile = new File(productPath);
        if(!productCatoryFile.exists() && !productCatoryFile.isDirectory()){
            productCatoryFile.mkdir();
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(goodDetailUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
                    .timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取 商品名称
        String title = "";
        Elements titleElements = doc.getElementsByTag("h2");
        for (Element element : titleElements) {
            String className = element.attr("class");
            if ("title".equals(className)) {
                Node titleNode = element.childNode(0);
                title = titleNode.toString();
                break;
            }
        }

        //根据商品名称创建文件夹
        productPath = productPath + title + "/";
        File file = new File(productPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //获取 商品积分
        String integralStr = "0";
        Elements integralElements = doc.getElementsByTag("strong");
        for (Element element : integralElements) {
            String className = element.attr("class");
            if ("zx_lsj zxl".equals(className)) {
                Node integralNode = element.childNode(0);
                String[] integralArr = integralNode.toString().split("￥");
                integralStr = integralArr[1];
                break;
            }
        }
        Double price = 2.9;
        Double integral = 0.0;
        try {
            integral = Double.parseDouble(integralStr);
        } catch (Exception e) {
            integral = 0.0;
        }
        if("1折以下".equals(deductionName)){            //1折
            price = 2.9;
            integral = integral * 1;
        } else if("1-2折".equals(deductionName)){      //2折
            price = 5.9;
            integral = integral * 2;
        } else if("2-3折".equals(deductionName)){      //3折
            price = 8.9;
            integral = integral * 3;
        } else if("3-4折".equals(deductionName)){      //4折
            price = 11.9;
            integral = integral * 4;
        } else if("4折以上".equals(deductionName)){     //5折
            price = 13.9;
            integral = integral * 5;
        }

        //获取 商品库存
        String stock = "0";
        Elements stockElements = doc.getElementsByTag("em");
        for (Element element : stockElements) {
            String className = element.attr("id");
            if ("spec_stockFic".equals(className)) {
                Node stockNode = element.childNode(0);
                stock = stockNode.toString();
                break;
            }
        }

        Map<String, Object> productParamMap = Maps.newHashMap();
        productParamMap.put("title", title);
        List<Map<String, Object>> pruductList = wxProductDao.getSimpleProductByCondition(productParamMap);
        if(pruductList != null && pruductList.size() > 0){
            // 整合SQL
            String updateProductSql = "update n_product np set " +
                    "stock='"+stock+"', " +
                    "price='"+price+"', " +
                    "integral='"+integral+"', " +
                    "deduction='"+deductionName+"', " +
                    "np.update_time = CURRENT_TIMESTAMP " +
                    "where id = '"+pruductList.get(0).get("id").toString()+"'\n";
            logger.info("商品名称【"+title+"】的图片信息和SQL保存成功.");
            updateProductSqlList.add(updateProductSql);
        }

        return updateProductSqlList;
    }

}