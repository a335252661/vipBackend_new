package helps;

import org.apache.commons.lang.StringUtils;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/7/28
 */
public class LogHelp {
    public static void insertLog(Connection conn ,String project , String mess) {
        String in = "insert into  vip_log values('"+project+"','"+mess+"',SYSDATE)";
        SQLHelp.insertSQL(conn,in);
    }
    public static void insertLog(Connection conn ,String project , String mess ,String remake) {
        if(StringUtils.isEmpty(remake)){
            LogHelp.insertLog2(conn,project,mess);
        }else {
            String in = "insert into  vip_log values('"+project+"','"+mess+"','"+remake+"',SYSDATE)";
            SQLHelp.insertSQL(conn,in);
        }
    }


    public static void insertLog2(Connection conn ,String project , String mess) {
        String in = "insert into  dbusr07.vip_log@zwdb_prod values('"+project+"','"+mess+"',SYSDATE)";
        SQLHelp.insertSQL(conn,in);
    }
    public static void insertLog2(Connection conn ,String project , String mess ,String remake) {
        if(StringUtils.isEmpty(remake)){
            LogHelp.insertLog2(conn,project,mess);
        }else {
            String in = "insert into  dbusr07.vip_log@zwdb_prod values('"+project+"','"+mess+"','"+remake+"',SYSDATE)";
            SQLHelp.insertSQL(conn,in);
        }
    }

    public static void main(String[] args) {
        Connection conn = DBConn.getDbusr07TestConn();
        LogHelp.insertLog2(conn,"test","测试dsadsa");
    }
}
