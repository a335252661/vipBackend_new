package vip.yizhifu;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/3/18
 */
public class DeleteJuede {
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
        Date monthFirstDay = DateTimeHelp.getMonthFirstDay();
        String dateStr = DateTimeHelp.dateToStr(monthFirstDay, "yyyy/MM/dd");
        System.out.println(dateStr);
        String deleteSQL=" delete from bestpay_promotion_return where to_char(STATEMENT_DATE , 'yyyy/mm/dd')='"+dateStr+"' and to_char(crt_date , 'yyyy/mm/dd') <'"+dateStr+"'";
        SQLHelp.deleteSQL(conncp , deleteSQL);
    }

}
