package vip.yuezhang;

import Pro.ProcUtil;
import helps.DateTimeHelp;
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

    static String BILL_INVOICE_DETAIL = "BILL_INVOICE_DETAIL_"+DateTimeHelp.getDateTimeString("MM");
    static String BILL_INVOICE = "BILL_INVOICE_"+DateTimeHelp.getDateTimeString("MM");

    //            private static String sign = "test";
    private static String sign = "pro";
    //数据库连接
    private static Connection conncp = null;
    static{
        if(sign.equals("pro")){
            conncp = DBConn.getCopyProConn();
        }else {
            conncp = DBConn.getCopyTestConn();
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
        String mm="insert into "+BILL_INVOICE+"  select * from BILL_INVOICE where billing_cycle_id='"+yyyyMMdd+"'";
        try{
            ProcUtil.callProc(conncp,"sql_procedure",  new Object[]{mm});
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(BILL_INVOICE + "备份结束");
    }

}
