package helps;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author by cld
 * @date 2020/4/19  11:29
 * @description:   commons-email-1.2.jar    mail.jar
 */
public class MailSend16Help {

    // smtp.163.com 220.181.12.11
    private static final String EMAIL_OWNER_ADDR_HOST = "220.181.12.11"; //smtp.163.com  smtp.aliyun.com  smtp.qq.com
    private static final String EMAIL_OWNER_ADDR = "chengliudegg@163.com";
    private static final String EMAIL_OWNER_ADDR_PASS = "acld7758258";

    public static void doSendHtmlEmail(String subject,
                                       String sendHtml,
                                       String  toUser,
                                       String ccUser,
                                       List<String> fileStrlist){

        try {
            HtmlEmail email = new HtmlEmail();
            // 配置信息
            email.setHostName(EMAIL_OWNER_ADDR_HOST);
//            email.setFrom(EMAIL_OWNER_ADDR,"发件人名字");
            email.setFrom(EMAIL_OWNER_ADDR,EMAIL_OWNER_ADDR);

            email.setAuthentication(EMAIL_OWNER_ADDR,EMAIL_OWNER_ADDR_PASS);

            email.setCharset("GBK");
            email.setSubject(subject);
            email.setHtmlMsg(sendHtml);

            // 添加附件的内容
//            final BodyPart bodyPart = createBodyPart();
            if (null != fileStrlist && fileStrlist.size() != 0) {
                List<EmailAttachment> atts = new ArrayList<EmailAttachment>();
                for (String str : fileStrlist) {
                    //添加附件
                    EmailAttachment att = new EmailAttachment();
                    att.setPath(str);

                    if(str.contains("\\")){
                        String[] split = str.split("\\\\");
                        att.setName(// 解决附件名乱码
                                MimeUtility.encodeText(split[split.length-1]));
                    }else if(str.contains("/")){
                        String[] split = str.split("/");
                        att.setName(// 解决附件名乱码
                                MimeUtility.encodeText(split[split.length-1]));
                    }
                    atts.add(att);
                }

                if (null != atts && atts.size() > 0) {
                    for (int i = 0; i < atts.size(); i++) {
                        email.attach(atts.get(i));
                    }
                }

            }

            // 收件人
            String[] split = toUser.split(",");
            for (int i = 0; i < split.length; i++) {
                email.addTo(split[i]);
            }

            // 抄送人
            String[] split2 = ccUser.split(",");
            for (int i = 0; i < split2.length; i++) {
                email.addCc(split2[i]);
            }

            //邮件模板 密送人
//            List<String> bccAddress = mailInfo.getBccAddress();
//            if (null != bccAddress && bccAddress.size() > 0) {
//                for (int i = 0; i < bccAddress.size(); i++) {
//                    email.addBcc(ccAddress.get(i));
//                }
//            }
            email.send();
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String> fileStrlist = Arrays.asList("D:\\file_temp\\11111\\BILL_IOT_ACCT_ITEM_20210115.021.001" );
        MailSend16Help.doSendHtmlEmail("邮件测试" ,
                "内容：<h1>test,测试</h1>",
                "chengliudegg@163.com",
                "chengliudegg@163.com,335252661@qq.com",
                fileStrlist);

    }

}
