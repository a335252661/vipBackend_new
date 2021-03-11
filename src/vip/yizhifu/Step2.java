package vip.yizhifu;

import helps.SQLHelp;
import org.apache.commons.lang.StringUtils;
import utils.DBConn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/11
 */
public class Step2 {

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

        String up = "        update\n" +
                "        bestpay_promotion_return a\n" +
                "        set   status=2\n" +
                "        where  bill_ref_no is null\n" +
                "        or    status =-1";

        SQLHelp.updateSQL(conncp , up);

        SQLHelp.truncate(conncp,"bestpay_promotion_return_temp");
        String temp_turn="insert into  bestpay_promotion_return_temp \n" +
                "        select a.seq, a.account_no , a.status, a.statement_date, a.service_nbr,is_prepayment\n" +
                "        from bestpay_promotion_return a\n" +
                "        where (a.is_balance != 1 or a.is_balance is null)\n" +
                "              and a.jt_flag != 1\n" +
                "              and a.upload_date is null\n" +
                "              and a.service_nbr is not null\n" +
                "              and a.is_prepayment in(0,2)\n" +
                "              and a.promotion_id not in\n" +
                "                  (select promotion_id\n" +
                "                   from bestpay_promotion_def def\n" +
                "                   where def.is_balance != 1\n" +
                "                         and def.is_prepayment in(0,2))";
            SQLHelp.exec(conncp,temp_turn);

            String up2 = "        select   b.seq,\n" +
                    "        case\n" +
                    "        when  a.bill_ref_no is null  then -1\n" +
                    "        else  to_number(a.bill_ref_no)\n" +
                    "        end  bill_ref_no,\n" +
                    "        case\n" +
                    "        when   a.status_cd is null  then -1\n" +
                    "        else a.status_cd\n" +
                    "        end  status,\n" +
                    "        a.BILLING_CYCLE_ID statement_date,\n" +
                    "       b.statement_date statement_date_re,\n" +
                    "        nvl( a.close_date, null) close_date ,\n" +
                    "        case\n" +
                    "        when a.balance_due is null then -1\n" +
                    "        when a.balance_due=0 then 1\n" +
                    "        else  0\n" +
                    "        end balance_due\n" +
                    "        from bill_invoice a ,  bestpay_promotion_return_temp b \n" +
                    "      where a.acct_id =b.account_no\n" +
                    "        and a.BILLING_CYCLE_ID=  to_char(b.statement_date,'yyyymmdd')\n" +
                    "        and  a.interim_bill_flag=0\n" +
                    "        and  a.status_cd=1 and a.close_date is not null";
        try {
            ResultSet resultSet = SQLHelp.querySQLReturnResultSet(conncp, up2);
            while (resultSet.next()){
                String seq = resultSet.getString("SEQ");
                String BILL_REF_NO = resultSet.getString("BILL_REF_NO");
                String status = resultSet.getString("status");
                String CLOSE_DATE = resultSet.getString("CLOSE_DATE");
                String BALANCE_DUE = resultSet.getString("BALANCE_DUE");

                String postdata = "";

                if(!StringUtils.isEmpty(CLOSE_DATE)){
                    CLOSE_DATE=CLOSE_DATE.substring(0,CLOSE_DATE.length()-2);
                    postdata = " ,post_date =to_date('"+CLOSE_DATE+"' , 'yyyy-mm-dd hh24:mi:ss')  ";
                }

                String update = " update bestpay_promotion_return set " +"bill_ref_no ='"+BILL_REF_NO+"'," +"status='"+status+"'," + " chg_date= sysdate," + "is_balance= '"+BALANCE_DUE+"'" +postdata + " where seq ='"+seq+"'";
                SQLHelp.updateSQL(conncp,update);

            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }


    }
}
