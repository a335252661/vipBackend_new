package vip.shishou;

import helps.DateTimeHelp;
import helps.ListHelp;
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
public class ShishouCreateFile {
    private static String sign = "test";
    //    private static String sign = "pro";
    private static String dirLocation = "";
    private static String finaDir = "";
    //每个文件存放数据条数
    private static Long limitTol = 100000L;
    //数据库连接
    private static Connection iamConn = null;
    //记录当前文件数据条数
    private static Long recordingTol = 0L;

    private static int currentFileOrder = 1;
    private static PrintWriter pw = null;
    private static OutputStream os = null;

    private static String split = "^";

    private static String yyyyMMdd = DateTimeHelp.getDateTimeString("yyyyMMdd");

    static {
        if (sign.equals("pro")) {
            iamConn = DBConn.getDbusr07ProConn();
            dirLocation = "/home/bgusr01/vip_backend/2.1new/";
            finaDir = "/home/bgusr01/vip_backend/files/";

            limitTol = 100000L;
        } else {
            iamConn = DBConn.getDbusr01TestConn();
            dirLocation = "D:\\file_temp\\shishou\\";
            finaDir = "D:\\bgusr01\\vip_backend\\files\\";
            limitTol = 100000L;
        }
    }

    public static void main(String[] args) {
        DateTimeHelp.start();
        try {
           final ShishouCreateFile shishouCreateFile = new ShishouCreateFile();
            ResultSet resultSet = SQLHelp.querySQLReturnResultSet(iamConn, "select * from dbusr01.jt_shishou_result@zwdb_prod");
            List<String> columnNameList = shishouCreateFile.getColumnName(resultSet);

            int i = 0;
            while (true) {
                if (i == 0) {

                    //第一步，创建文件
                    shishouCreateFile.createFile();

                    //获取行数据
                    String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
                    pw.println(lineDate);

                    i = 1;
                } else {
                    //判断当前行数，超过规定则新建文件重新写
                    if (recordingTol < limitTol) {

                        //写文件

//                        //获取行数据
                        String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
//                        //继续写
                        pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取


//                        ArrayList<Integer> fast = ListHelp.fast(2);
//                        for(Integer ll :fast){
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    shishouCreateFile.fun();
//
//                                }
//                            },"T"+ll).start();
//                        }

                    } else {
                        //一个文件写完


                        //添加尾部记录
//                        pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + recordingTol + "|" +chargeAll + "|");

                        pw.close();
                        os.close();
                        //一个文件写完之后，金额合计重置为0


                        recordingTol = 0L;

                        //一个文件写完，表示开始写第二个文件
                        currentFileOrder++;

                        //第一步，创建文件
                        shishouCreateFile.createFile();

                        //获取行数据
                        String lineDate = shishouCreateFile.getLineData(resultSet, split, columnNameList);
                        pw.println(lineDate);
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

    public List<String> getColumnName(ResultSet resultSet) {
        List<String> cols = new ArrayList<String>();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String columnName = rsmd.getColumnName(i).toLowerCase();
                cols.add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cols;

    }

    public String getLineData(ResultSet resultSet, String split, List<String> columnNameList) throws Exception{
        String data = "";
        resultSet.next();
        recordingTol++;
        for (String name : columnNameList) {
            String value = resultSet.getString(name);

            if (data.equals("")) {
                data = "0"+value;
            } else {
                data = data + split + value;
            }

        }
        return data;

    }

    public void createFile() {
        try {
            String str = String.format("%03d", currentFileOrder);
            String currentFileName = "real_fee_021_" + yyyyMMdd + "_" + str + ".txt";
            String location = dirLocation + currentFileName;
            //判断文件时候存在，不存在则创建
            UtilTools.judeFileExists(location);
            os = new FileOutputStream(location);
            pw = new PrintWriter(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void fun(ResultSet resultSet ,List<String> columnNameList ) throws  Exception{
        //获取行数据
        String lineDate = this.getLineData(resultSet, split, columnNameList);
        //继续写
        pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
    }

}
