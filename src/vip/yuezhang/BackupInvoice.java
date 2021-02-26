package vip.yuezhang;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.LogHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/11
 */
public class BackupInvoice {

    static String  mm = DateTimeHelp.getDateTimeString("MM");
    static String BILL_INVOICE_DETAIL = "BILL_INVOICE_DETAIL_"+DateTimeHelp.getDateTimeString("MM");
    static String BILL_INVOICE = "BILL_INVOICE_"+DateTimeHelp.getDateTimeString("MM");

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
        String yyyyMM = DateTimeHelp.getDateTimeString("yyyyMM");
        //当前月第一天
        String yyyyMMdd = DateTimeHelp.dateToStr(DateTimeHelp.getMonthFirstDay(), "yyyyMMdd");
        //存在则删除
        SQLHelp.truncate(conncp,BILL_INVOICE_DETAIL);
        String cc="insert into "+BILL_INVOICE_DETAIL+"  select * from BILL_INVOICE_DETAIL PARTITION(P_BID_"+yyyyMM+")";
        try{
            ProcUtil.callProc(conncp,"sql_procedure",  new Object[]{cc});
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(BILL_INVOICE_DETAIL + "备份结束");


        SQLHelp.truncate(conncp,BILL_INVOICE);
        String qq="insert into "+BILL_INVOICE+"  select * from BILL_INVOICE where billing_cycle_id='"+yyyyMMdd+"'";
        try{
            ProcUtil.callProc(conncp,"sql_procedure",  new Object[]{qq});
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(BILL_INVOICE + "备份结束");



        System.out.println("--------------------------备份到07---------------------------");
        SQLHelp.dropTable(conn07,BILL_INVOICE_DETAIL);
        SQLHelp.exec(conn07 , "create table "+BILL_INVOICE_DETAIL+" as select * from "+BILL_INVOICE_DETAIL+"@to_iamzw_new");
        SQLHelp.exec(conn07 , "create index index_detail_acct_id_"+mm+" on "+BILL_INVOICE_DETAIL+"(account_no)");
        SQLHelp.exec(conn07 , "create index index_detail_ref_no_"+mm+" on "+BILL_INVOICE_DETAIL+"(bill_ref_no)");
        //create index index_acct_id_02 on bill_invoice_02(acct_id)


        SQLHelp.dropTable(conn07,BILL_INVOICE);
        SQLHelp.exec(conn07 , "create table "+BILL_INVOICE+" as select * from "+BILL_INVOICE+"@to_iamzw_new");
        SQLHelp.exec(conn07 , "create index index_acct_id_"+mm+" on "+BILL_INVOICE+"(acct_id)");
        SQLHelp.exec(conn07 , "create index index_ref_no_"+mm+" on "+BILL_INVOICE+"(bill_ref_no)");


        System.out.println("-------------------------备份结束，----------------------------");
        LogHelp.updateNoticeStatus(conn07 , 1,1);

    }

}
