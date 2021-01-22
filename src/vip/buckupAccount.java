package vip;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/22
 */
public class buckupAccount {
    public static void main(String[] args) {
        Connection conn = DBConn.getCopyProConn();
        SQLHelp.truncate(conn, "account");
        String cc = "insert into account \n" +
                "select  distinct acct_cd,acct_id,cust_id from cus.account@CRM_COPY";
        SQLHelp.insertSQL(conn, cc);
        System.out.println("备份结束");
    }
}
