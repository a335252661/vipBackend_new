package vip.wlwbad;

import helps.DateTimeHelp;
import helps.FileHelp;
import helps.SQLHelp;
import utils.DBConn;
import utils.UtilTools;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/10
 */
public class wlwbad2 extends Thread{
    private static String sign = "test";
    //    private static String sign = "pro";
    private static String dirLocation = "";
    private static String finaDir = "";
    //每个文件存放数据条数
    private static Long limitTol ;
    //数据库连接
    private static Connection iamConn = null;
    //记录当前文件数据条数
    private static volatile Long recordingTol = 0L;
    //记录当前是第几个文件
    private static int currentFileOrder = 1;
    private static PrintWriter pw = null;
    private static OutputStream os = null;

    private static String split = "|";

    private static String yyyyMMdd = DateTimeHelp.getDateTimeString("yyyyMMdd");



    private static    ResultSet resultSet = null;
    private static   List<String> columnNameList = null;
    private static wlwbad2 shishouCreateFile= null;


    //当前文件名
    private static String currentFileName ;
    private static Long allcount ;

    private static   List<String> fileNameList = new ArrayList<String>();

    static {
        if (sign.equals("pro")) {
            iamConn = DBConn.getDbusr07ProConn();
            dirLocation = "/home/bgusr01/vip_backend/2.1new/";
            finaDir = "/home/bgusr01/vip_backend/files/";

            limitTol = 100000L;
        } else {
            iamConn = DBConn.getCollTestConn();
            dirLocation = "D:\\file_temp\\CreateFileHelp2\\";
            finaDir = "D:\\bgusr01\\vip_backend\\files\\";
            limitTol = 1000L;
        }
    }

    public ResultSet getResultSet() {

         String MM = DateTimeHelp.getDateTimeString("MM");



        SQLHelp.dropTable(iamConn,"coll_bill_ref_no2");
            String st1 = "create table coll_bill_ref_no2 as\n" +
                    "select distinct bill_ref_no from coll_revoke_invoices@cq where revoke_flag=2\n";
            SQLHelp.exec(iamConn , st1);

        ////  select * from coll_invoices@cq where revoke_flag=2 and closed_date is null
////  --296664
//  select * from coll_revoke_invoices@cq  where revoke_flag=2  and to_char(closed_date,'yyyymm') = to_char(add_months(sysdate,-1),'yyyymm' )


        SQLHelp.dropTable(iamConn,"coll_pay_interim_seq2");
        String st2 = "create table coll_pay_interim_seq2 as\n" +
                "select a.seq from pay_interim_bill@to_iamzw_new a,coll_bill_ref_no2 b where a.bill_ref_no = b.bill_ref_no\n";
        SQLHelp.exec(iamConn , st2);

        SQLHelp.dropTable(iamConn,"coll_result2");
        String st3 = "create table coll_result2 as \n" +
                "        select serv_id, acct_id, billing_month, amount\n" +
                "       from (select a.serv_id,\n" +
                "                    a.acct_id,\n" +
                "                    a.billing_month,\n" +
                "                    sum(a.amount) as amount\n" +
                "               from jt_bill_data@to_iamzw_new a, coll_pay_interim_seq2 b\n" +
                "              where a.pay_interim_seq = b.seq\n" +
                "              group by a.serv_id, a.acct_id, a.billing_month)";
        SQLHelp.exec(iamConn, st3);

         allcount = Long.parseLong(SQLHelp.querySQLReturnField(iamConn, "select count(1) as FIELD from coll_result2"));

         resultSet = SQLHelp.querySQLReturnResultSet(iamConn, "select * from coll_result2");
        return resultSet;
    }

    public static void main(String[] args) {
        DateTimeHelp.start();
        try {
            shishouCreateFile = new wlwbad2();
            //获取数据
            shishouCreateFile.getResultSet();
            columnNameList = SQLHelp.getColumnName(resultSet);

            Long lastcount = allcount%limitTol;
            Long fileCount = (allcount/limitTol)+1;


            while (resultSet.next()) {
                recordingTol++;
                if (recordingTol == 1) {
                    //第一步，创建文件
                    shishouCreateFile.createFile();
                    //此处可以写头部数据
                    if(currentFileOrder== fileCount){
                        pw.println("STA|"+lastcount);
                    }else {
                        pw.println("STA|"+limitTol);
                        System.out.println("currentFileOrder+\"==\" +fileCount = " + currentFileOrder+"==" +fileCount);
                    }

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
                        pw.println("END");

                        pw.close();
                        os.close();

                        recordingTol = 0L;
                        //一个文件写完，表示开始写第二个文件
                        currentFileOrder++;
                    }
                }
            }
            //此处可以添加尾部记录
            pw.println("END");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pw.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //生成检验文件
            try {
                File existAndCreate = FileHelp.isExistAndCreate(dirLocation + "IOT_BADDEBT." + yyyyMMdd + "." + "000" + ".021");
                pw = new PrintWriter(new FileOutputStream(existAndCreate));
                for(String name : fileNameList){
                    pw.println(name);
                }
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
        return recordingTol+split+data+split;
    }

    public void createFile() {
        try {
            String str = String.format("%03d", currentFileOrder);
             currentFileName = "IOT_BADDEBT_" + yyyyMMdd + "." + str + ".021";
            fileNameList.add(currentFileName);
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
