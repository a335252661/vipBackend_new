package test;

import helps.MailSend16Help;

/**
 * @author ������
 * @version 1.0
 * @Description TODO
 * @date 2020/4/28
 */
public class mailTest {
    public static void main(String[] args) {

        System.out.println("youjianceshi");

        MailSend16Help.doSendHtmlEmail("�ʼ�����" ,
                "���ݣ�<h1>test,����</h1>",
                "chengliudegg@163.com",
                "chengliudegg@163.com,335252661@qq.com",
                null);
    }
}
