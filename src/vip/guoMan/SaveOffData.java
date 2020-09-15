package vip.guoMan;

import helps.FileHelp;
import utils.DBConn;

import javax.tools.Tool;
import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/28
 */
public class SaveOffData {
    //            private static String sign = "test";
    private static String sign = "pro";
    //数据库连接
    private static Connection iamConn = null;
    private static String billbakFilePath = null;
    private static String billFileReadPath = null;
    private static String delimiter = ",";


    static{
        if(sign.equals("pro")){
            iamConn = DBConn.getDbusr01ProConn();
            billbakFilePath = "/home/bgusr01/payment/interNationRoamingAccFile/files/JTIOBILL/bak/";
            billFileReadPath = "/home/bgusr01/payment/interNationRoamingAccFile/files/JTIOBILL/";
        }else {
            iamConn = DBConn.getDbusr01TestConn();
            billbakFilePath = "/home/bgusr01/payment/interNationRoamingAccFile/files/JTIOBILL/bak/";
            billFileReadPath = "/home/bgusr01/payment/interNationRoamingAccFile/files/JTIOBILL/";
        }
    }

    public static void main(String[] args) {

    }


}
