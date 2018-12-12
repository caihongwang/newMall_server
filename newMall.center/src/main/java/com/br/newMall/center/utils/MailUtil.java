package com.br.newMall.center.utils;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * caihongwang
 */
public class MailUtil {

    public static void sendMail(String sendAccount, String sendPassword, String smtpHost, String popAccount, String title, String content) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", smtpHost);   // 发件人的邮箱的 SMTP 服务器地址
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        // 3. 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 4. From: 发件人
        message.setFrom(new InternetAddress(sendAccount));

        List list = new ArrayList();
        String[] median = popAccount.split(",");//对输入的多个邮件进行逗号分割
        for (int i = 0; i < median.length; i++) {
            list.add(new InternetAddress(median[i]));
        }
        InternetAddress[] address = (InternetAddress[]) list.toArray(new InternetAddress[list.size()]);
        message.addRecipients(Message.RecipientType.TO, address);
        // 5. Subject: 邮件主题
        message.setSubject(title, "UTF-8");
        // 6. Content: 邮件内容
        message.setContent(content, "text/plain;charset=utf-8");   //生成邮件正文
        // 7. 设置发件时间
        message.setSentDate(new Date());
        // 8. 保存上面的所有设置
        message.saveChanges();
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 9. 使用 邮箱账号 和 密码 连接邮件服务器
        //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(sendAccount, sendPassword);
        // 10. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 11. 关闭连接
        transport.close();
    }
}
