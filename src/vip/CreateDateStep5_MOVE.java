package vip;

import helps.FileHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/21
 */
public class CreateDateStep5_MOVE {
//                private static String sign = "test";
    private static String sign = "pro";
    private static String dirLocation = "";
    private static String finaDir = "";
    static{
        if(sign.equals("pro")){
            dirLocation = "/home/bgusr01/vip_backend/2.1new/";
            finaDir = "/home/bgusr01/vip_backend/files/";
        }else {
            dirLocation = "D:\\bgusr01\\vip_backend\\2.1new\\";
            finaDir = "D:\\bgusr01\\vip_backend\\files\\";
        }
    }
    public static void main(String[] args) {
        // 清空某文件夹数据  ,将这一批数据移动到改文件夹，
        FileHelp.deleteAllFile(finaDir);
        FileHelp.dirToDir(dirLocation , finaDir);
    }
}
