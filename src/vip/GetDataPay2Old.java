package vip;


import helps.SQLHelp;
import helps.TxtWriterHelp;
import utils.DBConn;
import utils.UtilTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description  销账文件补传数据
 * @date 2019/11/18
 */
public class GetDataPay2Old {


    //生产文件位置
    private static String dirLocation = "D:\\payment\\JtBill\\data\\WLW\\手动0708-old\\";

    //测试生产位置
//    private static String dirLocation = "/home/bgusr01/vip_backend/2.1new/";

    //每个文件存放数据条数
    private static Long limitTol = 100000L;

    //数据库连接
    private static Connection iamConn = null;

    //文件个数
    private static int fileTol = 801;

    //修改文件名
    private static ArrayList<String> fileNameList = new ArrayList<String>();

    //记录当前文件数据条数
    private static Long recordingTol = 0L;
    //记录总数据条数
    private static Long recordingAll = 0L;



    private static PrintWriter pw = null;
    private static OutputStream os = null;

    //单个文件金额合计
    private static Long chargeAll = 0L;

    //生产问文件名时间
    private static String sys_datetime="";

    //一批文件最大个数
    private static int size2 = 999;


    private static Long id = 0L;

    private static Long param = 110L;
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis(); //获取开始时间

        System.out.println("程序加载开始=========================="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) );
        System.out.println("文件存放位置==========================" + dirLocation);
        GetDataPay2Old getData = new GetDataPay2Old();

        //文件名时间为当前月份减一个月
        Date date = new Date();//获取当前时间 ? ?
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间 ? ?
        calendar.getTime();//获取一年前的时间，或者一个月前的时间 ? ?
        Date dt1 = calendar.getTime();
        sys_datetime = new SimpleDateFormat("yyyyMMdd").format(dt1);


        try {

//            iamConn = DBConn.getIamConn();
            //连接生产
//            iamConn = DBConn.getDbusr07ProConn();
//            iamConn = DBConn.getDbusr06ProConn();
            iamConn = DBConn.getDbusr01TestConn();
            System.out.println("连接生产完成");

            //判断文件夹路径是否存在不存在则创建
            File locationFile = new File(dirLocation);
            if (!locationFile.exists()) {
                locationFile.mkdirs();
                System.out.println("路径不存在，生成完成。");
            }



            //判断批次
            String fileName = "BILL2IOT.PAY." + sys_datetime ;


            long startTime2 = System.currentTimeMillis(); //获取开始时间
            //获取数据
            System.out.println("查询数据开始");
            ResultSet resultSet = getData.getData();

            long endTime2 = System.currentTimeMillis(); //获取结束时间
            String runTime2 = UtilTools.longToTime(endTime2 - startTime2);
            System.out.println("====================查询数据用时 ：" + runTime2);


            System.out.println("=====开始取数据写入文本");
            int i = 0;
            while (true) {
                if (i == 0) {
                    //获取行数据
                    String lineDate = getData.getLineData(resultSet);
                    getData.writeData(fileTol, lineDate, fileName);
                    i = 1;
                } else {
                    //判断当前行数，超过规定则新建文件重新写
                    if (recordingTol < limitTol) {
                        //获取行数据
                        String lineDate = getData.getLineData(resultSet);
                        //继续写
                        pw.println(lineDate);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
                    } else {
                        //添加尾部记录
                        pw.println("END|");

                        pw.close();
                        os.close();
                        //一个文件写完之后，金额合计重置为0
                        chargeAll = 0L;
                        fileTol++;
                        recordingTol = 0L;


                        //获取行数据
                        String lineDate = getData.getLineData(resultSet);
                        getData.writeData(fileTol, lineDate, fileName);
                    }

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("生产文件完成");
            System.out.println("数据总条数++" + (recordingAll - 1));

            pw.println("END");


            try {
                pw.close();
                os.close();
            } catch (Exception e) {

            }



            TxtWriterHelp.createFile(dirLocation,"BILL2IOT.SUM."+sys_datetime+".801.021",
                    "");

            TxtWriterHelp.writeMsg("STA|"+fileNameList.size());
            for(String filename : fileNameList){
                TxtWriterHelp.writeMsg(filename+"|100000");
            }
            TxtWriterHelp.writeMsg("END");

            TxtWriterHelp.close();
            System.out.println();


            //所有文件生成完成，修改文件名
//            if (fileNameList.size() > 1) {
//                //获取最后一个文件
//                String maxName = fileNameList.get(fileNameList.size() - 1);
//                String max = maxName.split("\\.")[4];
//                int maxInteger = Integer.parseInt(max);
//
//
//                System.out.println("要生成" + (maxInteger+1) + "个文件");
//
//                //要生成多少批次文件
//                int picinum = maxInteger / size2 + (maxInteger % size2 == 0 ? 0 : 1);
//                System.out.println("要生成" + picinum + "个批次，每个批次最大数量999");
//
//
//                for (String currName : fileNameList) {
//
//                    String curr0 = currName.split("\\.")[0];
//                    String curr1 = currName.split("\\.")[1];
//                    //pici
//                    String curr2 = currName.split("\\.")[2];
//                    //最大
//                    String curr3 = currName.split("\\.")[3];
//                    //当前是第几个文件
//                    int curr4 =Integer.parseInt( currName.split("\\.")[4]);
//                    //结尾
//                    String curr5 = currName.split("\\.")[5];
//                    String picin ="";
//                    int num=0;
//
//
//                    //判断当前是第几批次
//                    int currpici = curr4 / size2 + (curr4 % size2 == 0 ? 0 : 1);
//                    if(currpici < picinum){
//                        num = size2;
//                        picin = UtilTools.zeroPadding(currpici);
//                        curr4 = curr4-size2*(currpici-1);
//                    }else {
//                        //表示最后一个批次
//                        picin = UtilTools.zeroPadding(currpici);
//                        num = maxInteger-size2*(currpici-1);
//                        curr4 = curr4-size2*(currpici-1);
//                    }
//
//                    String curr4str = UtilTools.zeroPadding(curr4);
//                    String maxNum= UtilTools.zeroPadding(num);
//                    String newName = curr0 +"."+curr1+picin+maxNum +curr4str +"."+curr5;
//                    UtilTools.renameFile(dirLocation, currName, newName);
//
//                }
//            }


            long endTime = System.currentTimeMillis(); //获取结束时间
            String runTime = UtilTools.longToTime(endTime - startTime);
            System.out.println("====================程序运行时间 ：" + runTime);
            System.out.println(fileTol);
        }


    }

    private String getLineData(ResultSet rstInst) throws Exception {
        rstInst.next();

        recordingTol++;
        recordingAll++;
        if(id>=limitTol){
            id=0L;
        }
        id++;

//        String ID = rstInst.getString("ID");
        String ACCT_ID = rstInst.getString("ACCT_ID");
        String SERV_ID = rstInst.getString("SERV_ID");
        String SERV = rstInst.getString("SERV");
        String SH = rstInst.getString("SH");
        String BILLING_MONTH = rstInst.getString("BILLING_MONTH");
        String SUB_TYPE_CODE = rstInst.getString("SUB_TYPE_CODE");
        String AMOUNT = rstInst.getString("AMOUNT");
        String POSTDATE = rstInst.getString("POSTDATE");
        String STATU = rstInst.getString("STATU");




        String s =id + "|" + ACCT_ID + "|" + "-1" + "|" + SERV + "|" +
                SH + "|" + BILLING_MONTH + "|" + SUB_TYPE_CODE + "|" + AMOUNT + "|" +
                POSTDATE + "|" + STATU + "|";
        chargeAll += Long.parseLong(AMOUNT);
        return s;
    }


    public ResultSet getData() throws Exception {
        String sql = "select * from cld_pay_old ";
        PreparedStatement pstmtInst = iamConn.prepareStatement(sql);
        ResultSet rstInst = pstmtInst.executeQuery();

        ArrayList<String> strings = SQLHelp.querySQLReturnList2(iamConn, "select count(1) , sum(amount) from cld_pay_old");
        System.out.println(strings);

        return rstInst;
    }
    private String getXZNum2()throws Exception{
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String dateString = df.format(calendar.getTime());
        Long  partNum =(param++);
        String randStr=String.format("%08d", partNum);
        return "021"+dateString+randStr+"00";
    }

    private String writeData(int nnn,
                             String dataLine,
                             String fileName) throws Exception {

        String strnnn = UtilTools.zeroPadding(nnn);

        String ff = UtilTools.makeFF();
        //写数据 BBB.MAX.NNN
        String currentFileName = fileName  + strnnn + ".021";
        fileNameList.add(currentFileName);
        String location = dirLocation + currentFileName;
        //判断文件时候存在，不存在则创建
        UtilTools.judeFileExists(location);

        os = new FileOutputStream(location);
        pw = new PrintWriter(os);
        //            //添加头部记录
        pw.println("STA|" + limitTol + "|" );
        pw.println(dataLine);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
        return "";
    }



}
