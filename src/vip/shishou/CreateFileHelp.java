package vip.shishou;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;
import utils.UtilTools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/10
 */
public class CreateFileHelp extends Thread{
    private static String sign = "test";
    //    private static String sign = "pro";
    private static String dirLocation = "";
    private static String finaDir = "";
    //每个文件存放数据条数
    private static Long limitTol = 100000L;
    //数据库连接
    private static Connection iamConn = null;
    //记录当前文件数据条数
    private static volatile Long recordingTol = 0L;
    //记录当前是第几个文件
    private static int currentFileOrder = 1;
    private static PrintWriter pw = null;
    private static OutputStream os = null;

    private static String split = "$$";

    private static String yyyyMMdd = DateTimeHelp.getDateTimeString("yyyyMMdd");



    private static    ResultSet resultSet = null;
    private static   List<String> columnNameList = null;
    private static CreateFileHelp shishouCreateFile= null;

    static {
        if (sign.equals("pro")) {
            iamConn = DBConn.getDbusr07ProConn();
            dirLocation = "/home/bgusr01/vip_backend/2.1new/";
            finaDir = "/home/bgusr01/vip_backend/files/";

            limitTol = 100000L;
        } else {
            iamConn = DBConn.getLocalConn();
            dirLocation = "D:\\file_temp\\CreateFileHelp\\";
            finaDir = "D:\\bgusr01\\vip_backend\\files\\";
            limitTol = 300000L;
        }
    }

    public static void main(String[] args) {
        DateTimeHelp.start();


        String querySQL = "select * from jt_bill_data_invoice";

        try {
            shishouCreateFile = new CreateFileHelp();
             resultSet = SQLHelp.querySQLReturnResultSet(iamConn, querySQL);
            columnNameList = SQLHelp.getColumnName(resultSet);

            while (resultSet.next()) {

                recordingTol++;
                if (recordingTol == 1) {
                    //第一步，创建文件
                    shishouCreateFile.createFile();


                    //此处可以写头部数据

                    //获取行数据
                    String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
                    pw.println(lineDate);
                } else {
                    //判断当前行数，超过规定则新建文件重新写
                    if (recordingTol < limitTol) {
//                        //获取行数据
                        String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
//                        //继续写
                        pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
                    } else {
                        String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
                        pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
                        //一个文件写完
                        //此处可以添加尾部记录
//                        pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + recordingTol + "|" +chargeAll + "|");
                        pw.close();
                        os.close();
                        recordingTol = 0L;
                        //一个文件写完，表示开始写第二个文件
                        currentFileOrder++;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pw.close();
                os.close();
            } catch (Exception e) {

            }

            DateTimeHelp.end();



        }


    }


    public String getLineData(ResultSet resultSet, String split, List<String> columnNameList) throws Exception{
        String data = "";
        for (String name : columnNameList) {
            String value = resultSet.getString(name);
            if (data.equals("")) {
                data = value;
            } else {
                data = data + split + value;
            }
        }
        return data;
    }

    public void createFile() {
        try {
            String str = String.format("%03d", currentFileOrder);
            String currentFileName = "jt_bill_data_invoice_021_" + yyyyMMdd + "_" + str + ".txt";
            String location = dirLocation + currentFileName;
            //判断文件时候存在，不存在则创建
            UtilTools.judeFileExists(location);
            os = new FileOutputStream(location);
            pw = new PrintWriter(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void fun(){

        try {
            //获取行数据
            String lineDate = this.getLineData(resultSet, split, columnNameList);
            //继续写
            pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true){
            if (recordingTol < limitTol) {
                fun();
            }
        }
    }
}
