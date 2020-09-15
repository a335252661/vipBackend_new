package test;

import helps.MailSend16Help;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/4/28
 */
public class mailTest {
    public static void main(String[] args) {

        System.out.println("youjianceshi");

        MailSend16Help.doSendHtmlEmail("邮件测试" ,
                "内容：<h1>test,测试</h1>",
                "chengliudegg@163.com",
                "chengliudegg@163.com,335252661@qq.com",
                null);
    }
}
