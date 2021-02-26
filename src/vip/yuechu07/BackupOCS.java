package vip.yuechu07;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.LogHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/2/23
 */
public class BackupOCS {
    private String count = "";
    static String  mm = DateTimeHelp.getDateTimeString("MM");
    static  String yyyyMM = DateTimeHelp.dateToStr(new Date(), "yyyyMM");
    //            private static String sign = "test";
    private static String sign = "pro";
    //数据库连接
    private static Connection conncp = null;
    private static Connection conn07 = null;
    static{
        if(sign.equals("pro")){
            conncp = DBConn.getCopyProConn();
            conn07 = DBConn.getDbusr07ProConn();
        }else {
            conncp = DBConn.getCopyTestConn();
            conn07 = DBConn.getDbusr07TestConn();
        }
    }

    public static void main(String[] args) {

        //判断表里是否有数据
        BackupOCS backupOCS = new BackupOCS();
        backupOCS.queryOCS();

        //有数据，则首先在复制库备份，再备份到07
        //存在则删除
        SQLHelp.truncate(conncp,"ocs_bill_current");
        String cc="insert into ocs_bill_current  select * from ocs_bill_invoice_detail           PARTITION(P_O_BD_"+yyyyMM+")";
        SQLHelp.exec(conncp,cc);
        System.out.println("ocs_bill_current" + "备份结束");

        System.out.println("--------------------------备份到07---------------------------");
        SQLHelp.dropTable(conn07,"ocs_bill_current");
        SQLHelp.exec(conn07 , "create table ocs_bill_current as select * from ocs_bill_current@to_iamzw_new");
        SQLHelp.exec(conn07 , "create index ocs_acct_id_"+mm+" on ocs_bill_current(account_no)");
        SQLHelp.exec(conn07 , "create index ocs_ref_no_"+mm+" on ocs_bill_current(bill_ref_no)");

        System.out.println("-------------------------备份结束，----------------------------");
        LogHelp.updateNoticeStatus(conn07 , 3 , 1);
    }

    public String fun() {

        String table = "select count(1) as FIELD from ocs_bill_invoice_detail           PARTITION(P_O_BD_"+yyyyMM+") ";
        Connection conn = DBConn.getCopyProConn();
        String s = SQLHelp.querySQLReturnField(conn, table);
        return s;
    }

    public void queryOCS() {

        String s = fun();

        if (s.equals("0")){
            try {
                System.out.println("睡眠一小时");
                Thread.sleep(60*60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            queryOCS();

        }else {
            count = s;

            try {
                System.out.println("睡眠两分钟");
                Thread.sleep(2*60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(count.equals(fun())){
                //继续向下执行

            }else {
                //还在录入数据
                queryOCS();
            }

        }

    }
}
