package test;

import helps.TxtWriterHelp;
import utils.FtpUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/18
 */
public class checkFile {
    public static void main(String[] args) {
        try {
            FtpUtil ftp = FtpUtil.connect("10.7.95.70","bgusr01","lc#v58iHH",
                    "/home/bgusr01/vip_backend/2.1new");
//			ftp.downloadFile("vip.sh","C:\\Users\\Admin\\Desktop\\常用\\logs\\");
            String[] fileNames = ftp.getFileNames();

            TxtWriterHelp.createFile("D:\\payment\\JtBill\\data\\WLW\\",
                    "springBoot_log22",
                    "txt");

            for(String str : fileNames){
                TxtWriterHelp.writeMsg(str+"|M");
            }
            TxtWriterHelp.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }



    }
}
