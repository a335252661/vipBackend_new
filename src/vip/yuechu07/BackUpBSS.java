package vip.yuechu07;

import helps.DateTimeHelp;
import helps.LogHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/2/25
 */
public class BackUpBSS {

    private static Connection connbss = DBConn.getBssConn();
    private static Connection conn07 = DBConn.getDbusr07ProConn();
    public static void main(String[] args) {
        String yyyyMM = DateTimeHelp.getDateTimeString("yyyyMM");
        String tableName = "MBI_"+yyyyMM;
        System.out.println("-----------------------------------------------------");

        new BackUpBSS().fun();

        System.out.println("dbusr07.cld_temp_data_all_new ------  生成完成");
        String sql = "create table "+tableName+" AS select * from dbusr07.cld_temp_data_all_new@zwdb";
        SQLHelp.dropTable(connbss,tableName);
        SQLHelp.exec(connbss , sql);
        SQLHelp.exec(connbss , "grant all on "+tableName+" to public");
        SQLHelp.exec(connbss , "create index msisdn_index_"+yyyyMM+" on  "+tableName+"(msisdn)");
        SQLHelp.exec(connbss , "create index msisdn_acctid_"+yyyyMM+" on  "+tableName+"(ACCT_ID)");

        LogHelp.insertCldLogsPro(conn07,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "数据备份到bss完成，表名字："+tableName
                ,true);

    }

    public void fun() {
        int noticeStatus = LogHelp.getNoticeStatus(conn07, 99);
        if (noticeStatus == 0){
            try {
                Thread.sleep(20 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fun();
        }
    }

}
