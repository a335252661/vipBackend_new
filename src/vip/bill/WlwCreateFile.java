package vip.bill;

import helps.DateTimeHelp;
import helps.FileHelp;
import helps.TxtWriterHelp;
import utils.DBConn;

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
            dirLocation = "/home/bgusr01/payment/JtBill/data/wlw/chk/";
            errLocation = "/home/bgusr01/payment/JtBill/data/wlw/chk/orig/errFiles/";
        }else {
            dirLocation = "C:\\Users\\Admin\\Desktop\\常用\\IDC-云提-物联网\\物联网\\WLWFILE\\";
            errLocation = "C:\\Users\\Admin\\Desktop\\常用\\IDC-云提-物联网\\物联网\\WLWFILE\\err\\";
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

    }
}
