package helps;

import org.apache.commons.lang.StringUtils;
import utils.DBConn;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

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


    public static void insertCldLogsPro(Connection conn ,String project ,String msg,Boolean flag) {
        System.out.println(msg);
        String send="99";
        if(flag){
            send="0";
        }
        UUID uuid = UUID.randomUUID();
        String in = "insert into  CLD_LOGS   values('"+project+"','"+uuid+"',SYSDATE,'"+send+"',null,'"+msg+"')";
        SQLHelp.insertSQL(conn,in);
    }

    public static void insertCldLogsTest(Connection conn ,String project ,String msg,Boolean flag) {
        System.out.println(msg);
        String send="99";
        if(flag){
            send="0";
        }
        UUID uuid = UUID.randomUUID();
        String in = "insert into  dbusr07.CLD_LOGS@zwdb_prod   values('"+project+"','"+uuid+"',SYSDATE,'"+send+"',null,'"+msg+"')";
        SQLHelp.insertSQL(conn,in);
    }

    public static void sendMsg(Connection conn) {
        String sele="select name,msg_id , msg from dbusr07.CLD_LOGS@zwdb_prod where send='0'  order by create_time";
        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(conn, sele);
        for(LinkedHashMap<String, Object> map : linkedHashMaps){
            String NAME = map.get("NAME").toString();
            String MSG_ID = map.get("MSG_ID").toString();
            String MSG = map.get("MSG").toString();
            WeChatHelp.sendCompanyWeChatMsg(WeChatHelp.log_secret,"==========="+NAME + "=========="+MSG);
            String up = "update dbusr07.CLD_LOGS@zwdb_prod set send  =1 ,send_time =sysdate where MSG_ID='"+MSG_ID+"'";
            SQLHelp.updateSQL(conn,up);

        }

    }


    public static void main(String[] args) {
        Connection conn = DBConn.getDbusr07TestConn();
//        LogHelp.insertCldLogs(conn,"月账test","测试1",true);
//        LogHelp.insertCldLogs(conn,"月账test","测试2",true);
//        LogHelp.insertCldLogs(conn,"月账test","测试3",true);
//        LogHelp.insertCldLogs(conn,"月账test","测试4",true);
//        LogHelp.insertCldLogs(conn,"月账test","测试dsadsa223333",true);

        LogHelp.sendMsg(conn);

    }
}
