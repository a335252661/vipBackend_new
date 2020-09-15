//package utils;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.File;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//
///**
// * @author by cld
// * @date 2020/3/22  10:52
// * @description:
// */
//public class MailSendHelp {
//    private static final String EMAIL_OWNER_ADDR_HOST = "smtp.163.com"; //smtp.163.com  smtp.aliyun.com  smtp.qq.com
//    private static final String EMAIL_OWNER_ADDR = "chengliudegg@163.com";
//    private static final String EMAIL_OWNER_ADDR_PASS = "acld7758258";
//
//    public static  Session getMailObj(){
//        Properties prop =  new Properties();
//        MimeMessage mailObj  =null;
//        Session session = null;
//        try {
//            prop.put("mail.host", EMAIL_OWNER_ADDR_HOST);
//            prop.put("mail.transport.protocol", "smtp");
//            prop.put("mail.smtp.auth", "true");
//            //如果不加下面的这行代码 windows下正常，linux环境下发送失败，解决：http://www.cnblogs.com/Harold-Hua/p/7029117.html
//            prop.setProperty("mail.smtp.ssl.enable", "true");
//            //使用java发送邮件5步骤
//            //1.创建sesssion
//             session = Session.getInstance(prop);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return session;
//
//    }
//
//    /**
//     *
//     * @param subject  邮件主题
//     * @param sendHtml 邮件内容
//     * @param toUser   收件人 ，可以设置多个收件人，逗号隔开
//     * @param ccUser    抄送人
//     * @param fileStrlist 附件位置
//     */
//    public static void doSendHtmlEmail(String subject, String sendHtml,
//                                       String toUser, String ccUser, List<String> fileStrlist){
//        Transport ts = null;
//        try {
//            Session session = MailSendHelp.getMailObj();
//            //开启session的调试模式，可以查看当前邮件发送状态
////            session.setDebug(true);
//            //2.通过session获取Transport对象（发送邮件的核心API）
//            ts= session.getTransport();
//            //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，
//            ts.connect(EMAIL_OWNER_ADDR, EMAIL_OWNER_ADDR_PASS);
//            MimeMessage mailObj = new MimeMessage(session);
//            //设置发件人
//            mailObj.setFrom(new InternetAddress(EMAIL_OWNER_ADDR));
//            // 设置多个收件人地址
//            if(null != toUser && !toUser.isEmpty()){
////                @SuppressWarnings("static-access")
//                InternetAddress[] internetAddressTo = new InternetAddress().parse(toUser);
//                mailObj.setRecipients(Message.RecipientType.TO, internetAddressTo);
//            }
//
//            // 设置多个抄送地址
//            if(null != ccUser && !ccUser.isEmpty()){
//                @SuppressWarnings("static-access")
//                InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
//                mailObj.setRecipients(Message.RecipientType.CC, internetAddressCC);
//            }
//
//            // 设置多个密送地址
////            if(null != bccUser && !bccUser.isEmpty()){
////                @SuppressWarnings("static-access")
////                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
////                message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
////            }
//
//            // 发送日期
//            mailObj.setSentDate(new Date());
//            // 邮件主题
//            mailObj.setSubject(subject);
//            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
//            Multipart multipart = new MimeMultipart();
//            // 添加邮件正文
//            BodyPart contentPart = new MimeBodyPart();
//            contentPart.setContent(sendHtml, "text/html;charset=utf-8");
//            multipart.addBodyPart(contentPart);
//
//            BodyPart attachmentBodyPart = null;
//            // 添加附件的内容
//            if (null != fileStrlist && fileStrlist.size() != 0) {
//                for (String str : fileStrlist) {
//                    attachmentBodyPart = new MimeBodyPart();
//                    File file = new File(str);
//                    DataSource source = new FileDataSource(new File(str));
//                    attachmentBodyPart.setDataHandler(new DataHandler(source));
//                    //MimeUtility.encodeWord可以避免文件名乱码
//                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
//                    multipart.addBodyPart(attachmentBodyPart);
//                }
//            }
//
//            // 将multipart对象放到message中
//            mailObj.setContent(multipart);
//
//            // 保存邮件
//            mailObj.saveChanges();
//            // 发送
//            ts.sendMessage(mailObj, mailObj.getAllRecipients());
//            System.out.println("发送成功！");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (ts != null) {
//                try {
//                    ts.close();
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    public static void main(String[] args) {
////        List<String> fileStrlist = Arrays.asList("F:\\Screenshots\\comparator比较器.jpg",
////               "F:\\Screenshots\\foreach输出.png" );
////        MailSendHelp.doSendHtmlEmail("邮件测试" ,
////                "spring boot 邮件测试",
////                "chengliudegg@163.com",
////               "chengliudegg@163.com,335252661@qq.com",
////                fileStrlist);
//
//        MailSendHelp.doSendHtmlEmail("cld_all_data 表更新完成" ,
//                "您好：<br/>  " +
//                        "cld_temp_data跟cld_all_data 表更新完成，您可以从这两张表取数",
//                "chengliudegg@163.com",
//                "chengliudegg@163.com,335252661@qq.com",
//                null);
//
////        System.out.println("你好");
//
//    }
//
//}
