package test;

import utils.DBConn;
import helps.SQLHelp;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author by cld
 * @date 2020/5/3  17:12
 * @description:
 */
public class SQLtest {
    public static void main(String[] args) {
        String mm="select * from file_export_detail";
        Connection conn = DBConn.getLocalMySQLTestConn();
        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(conn, mm);
        System.out.println(linkedHashMaps.size());
    }
}
