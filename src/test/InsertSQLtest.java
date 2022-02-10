package test;

import helps.DateTimeHelp;
import helps.NameHelps;
import utils.DBConn;
import helps.SQLHelp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author by cld
 * @date 2020/5/3  17:12
 * @description:
 *
 *
 * mysql 乱码问题
 *
 */
public class InsertSQLtest {
    public static void main(String[] args) {
//        String mm="select * from mark";
//        Connection conn = DBConn.getLocalMySQLTestConn("lingling");
//        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(conn, mm);
//        System.out.println(linkedHashMaps.size());
//
//        ArrayList<String> strings = SQLHelp.querySQLReturnList2(conn, mm);
//        System.out.println(strings);



        Connection conn = DBConn.getLocalMySQLTestConn("lingling");
        try {
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




        for (int i = 0; i < 100; i++) {
            String name = NameHelps.getRandomName();
            String timeString = DateTimeHelp.getDateTimeString("yyyy-MM-dd HH:mm:ss");

            String insert = "INSERT INTO mark( name, english, chinese, history, createtime) VALUES ( '"+name+"', "+fun()+", "+fun()+", "+fun2()+", '"+timeString+"')";
            SQLHelp.insertSQL(conn , insert);
        }


    }

    public static double fun() {
        Random random = new Random();
        double d2 = random.nextDouble( ) * 100;
        double d3 =  (double) Math.round(d2 * 100) / 100;
        return d3;
    }


    public static int fun2() {
        Random random1 = new Random();
        int i = random1.nextInt(100);
        return i;
    }

}
