package vip.bill;

import helps.DateTimeHelp;
import helps.FileHelp;
import helps.TxtWriterHelp;
import utils.DBConn;
import utils.FtpUtil;
import vip.IDCLoad.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/25
 */
public class WlwCreateFile {
//            private static String sign = "test";
    private static String sign = "pro";
    private static String dirLocation = "";
    private static String errLocation = "";
    static{
        if(sign.equals("pro")){
            dirLocation = "/acct/acct_payment/JtBill/data/wlw/chk/";
            errLocation = "/acct/acct_payment/JtBill/data/wlw/chk/errFiles/";
        }else {
            dirLocation = "D:\\file_temp\\wlw\\err\\";
            errLocation = "D:\\file_temp\\wlw\\err\\11\\";
        }
    }
    public static void main(String[] args) {
        //获取原始话单文件
        //IOTBILL.ZD.202004.001.021
        Date date = DateTimeHelp.adjMonReDate(new Date(), -1);
        String dateToStr = DateTimeHelp.dateToStr(date, "yyyyMM");
        String regex = "IOTBILL.ZD."+dateToStr+".[0-9]{3}.021";

        System.out.println(regex);

        ArrayList<String> currentFileName = FileHelp.getCurrentFileName(dirLocation, regex);
        for(String filestr : currentFileName){
            String errFile = "E"+filestr;
            TxtWriterHelp.createFile(errLocation,errFile,null);
            TxtWriterHelp.writeMsg("STA,0,");
            TxtWriterHelp.writeMsg("END,");
            TxtWriterHelp.close();
        }
        //获取文件推送到70
        try {
            FtpUtil ftp = FtpUtil.connect("10.7.95.70","bgusr01","lc#v58iHH",
                    "/home/bgusr01/payment/JtBill/data/wlw/chk/orig/errFiles");
            ArrayList<String> currentFileAllLocation = FileHelp.getCurrentFileAllLocation(errLocation);
            for(String path : currentFileAllLocation){
                File file = new File(path);
                ftp.uploadFile2(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //删除文件
        FileHelp.deleteAllFile(errLocation);

    }
}
