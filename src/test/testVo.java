package test;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author ������
 * @version 1.0
 * @Description TODO
 * @date 2020/5/18
 */
public class testVo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        int batchRun;
        Connection conn = DBConn.getCollTestConn();
        String currentMon = DateTimeHelp.getDateTimeString("yyyy-MM");
        //��ѯ����ʱ���Ƿ��ǵ��£�û�����ݣ����߲��ǵ��£���ʾ��һ�����У�ʱ���ǵ��±�ʾ�ǵڶ�������
        String field = SQLHelp.querySQLReturnField(conn, "select begin_date as field from coll_config_sms where id=1");
        if(null==field){
            // ��һ��
            batchRun=1;
        }else {
            String substring = field.substring(0,6);
            if(currentMon.equals(substring)){
                // ��һ��
                batchRun=1;
            }else {
                // �ڶ���
                batchRun=2;
            }
        }

        System.out.println(batchRun);

//        String field = SQLHelp.querySQLReturnField(conn, "select begin_date as field from coll_config_sms where id=2");
//        System.out.println(field.equals("null"));

          // **��4��15ʱ
        String timeString = DateTimeHelp.getDateTimeString("MM��dd��ddʱ");
        System.out.println(timeString);

        System.out.println("���");

    }

}
