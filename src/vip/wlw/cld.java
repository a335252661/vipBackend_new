package vip.wlw;

import helps.FileHelp;
import utils.FtpUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/10/29
 */
public class cld {
    public static void main(String[] args) {
        XxlJobLogger.log("11111111111111");

        //文件生成完成，移动到75服务器
        try {
//            FtpUtil ftp = FtpUtil.connect("10.7.95.70","bgusr01","lc#v58iHH",
//                    "/home/bgusr01/vip_backend/wlw/1029");

            FtpUtil ftp = FtpUtil.connect("10.145.195.75","acct_pay","Pay!3#we",
                    "/acct/acct_payment/JtBill/data/wlw/pay/11");
            ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation("D:\\file_temp\\wlw\\1029", "BILL.*");
            for(String name :currentFileAllLocation){
                File currfile = new File(name);
                System.out.println(currfile.getName());
                ftp.uploadFile(currfile, currfile.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
