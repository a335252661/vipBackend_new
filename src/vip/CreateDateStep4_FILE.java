package vip;


import helps.*;
import utils.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2019/11/18
 */
public class CreateDateStep4_FILE {

//            private static String sign = "test";
    private static String sign = "pro";

    private static String dirLocation = "";


    //每个文件存放数据条数
    private static Long limitTol = 100000L;

    //数据库连接
    private static Connection iamConn = null;
    private static Connection bssConn = null;


    static{
        if(sign.equals("pro")){
            iamConn = DBConn.getDbusr07ProConn();
            dirLocation = "/home/bgusr01/vip_backend/2.1new/";
//            dirLocation =  "/acct/acct_other/vip_backend/2.1new/";
            limitTol = 100000L;
        }else {
            iamConn = DBConn.getCopyTestConn();
            dirLocation = "D:\\bgusr01\\vip_backend\\2.1new\\";
            limitTol = 10L;
        }
    }


    //文件个数
    private static int fileTol = 1;

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

    private static ArrayList<String> mealList= new ArrayList<String>();


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis(); //获取开始时间

        System.out.println("程序加载开始=========================="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) );
        System.out.println("文件存放位置==========================" + dirLocation);
        CreateDateStep4_FILE getData = new CreateDateStep4_FILE();

        //文件名时间为当前月份减一个月
        Date date = new Date();//获取当前时间 ? ?
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间 ? ?
        calendar.getTime();//获取一年前的时间，或者一个月前的时间 ? ?
        Date dt1 = calendar.getTime();
        sys_datetime = new SimpleDateFormat("yyyyMM").format(dt1);


        try {
            //判断文件夹路径是否存在不存在则创建
            File locationFile = new File(dirLocation);
            if (!locationFile.exists()) {
                locationFile.mkdirs();
                System.out.println("路径不存在，生成完成。");
            }


            System.out.println("meal存入集合");
            String mealSQL ="";

            if(sign.equals("pro")){
                mealSQL = " select acct_item_type_id  typeid, product_offer_id offerid from cld_temp_data_id_code";
            }else {
                mealSQL = " select acct_item_type_id  typeid, product_offer_id offerid from dbusr07.cld_temp_data_id_code@zwdb_prod";
            }

            List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(iamConn, mealSQL);
            for(LinkedHashMap<String, Object> map : linkedHashMaps){
               String acct_item_type_id =  map.get("TYPEID").toString();
                String product_offer_id = map.get("OFFERID").toString();
                mealList.add(acct_item_type_id+"&"+product_offer_id);
            }
            System.out.println("meal存入集合结束，集合大小="+mealList.size());


            //判断批次
            String fileName = "MBI_" + sys_datetime + ".021";


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
                        pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + recordingTol + "|" +chargeAll + "|");

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

            pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + (recordingTol-1)  + "|"+chargeAll +"|");


            try {
                pw.close();
                os.close();
            } catch (Exception e) {

            }


            ArrayList<String> newNameList = new ArrayList<String>();

            //所有文件生成完成，修改文件名
            if (fileNameList.size() > 1) {
                //获取最后一个文件
                String maxName = fileNameList.get(fileNameList.size() - 1);
                String max = maxName.split("\\.")[4];
                int maxInteger = Integer.parseInt(max);


                System.out.println("要生成" + (maxInteger+1) + "个文件");

                //要生成多少批次文件
                int picinum = maxInteger / size2 + (maxInteger % size2 == 0 ? 0 : 1);
                System.out.println("要生成" + picinum + "个批次，每个批次最大数量999");


                for (String currName : fileNameList) {

                    String curr0 = currName.split("\\.")[0];
                    String curr1 = currName.split("\\.")[1];
                    //pici
                    String curr2 = currName.split("\\.")[2];
                    //最大
                    String curr3 = currName.split("\\.")[3];
                    //当前是第几个文件
                    int curr4 =Integer.parseInt( currName.split("\\.")[4]);
                    //结尾
                    String curr5 = currName.split("\\.")[5];
                    String picin ="";
                    int num=0;


                    //判断当前是第几批次
                    int currpici = curr4 / size2 + (curr4 % size2 == 0 ? 0 : 1);
                    if(currpici < picinum){
                        num = size2;
                        picin = UtilTools.zeroPadding(currpici);
                        curr4 = curr4-size2*(currpici-1);
                    }else {
                        //表示最后一个批次
                        picin = UtilTools.zeroPadding(currpici);
                        num = maxInteger-size2*(currpici-1);
                        curr4 = curr4-size2*(currpici-1);
                    }

                    String curr4str = UtilTools.zeroPadding(curr4);
                    String maxNum= UtilTools.zeroPadding(num);
                    String newName = curr0 +"."+curr1+picin+maxNum +curr4str +"."+curr5;
                    UtilTools.renameFile(dirLocation, currName, newName);

                    newNameList.add(newName);

                }
            }


            String checkName = "MBI_"+sys_datetime+".021.001.000.000.000."+UtilTools.makeFF()+".CHECK";
            TxtWriterHelp.createFile(dirLocation, checkName,null);
            if(newNameList.size()>0){
                for(String name : newNameList){
                    TxtWriterHelp.writeMsg(name+"|M");
                }
            }
            TxtWriterHelp.close();
            System.out.println("check 文件生成完成");



            long endTime = System.currentTimeMillis(); //获取结束时间
            String runTime = UtilTools.longToTime(endTime - startTime);
            System.out.println("====================程序运行时间 ：" + runTime);
            System.out.println(fileTol);

            String msg ="月账文件生成完成，" +
                    "程序运行时间 ：" + runTime+
                    "生产的文件总数"+fileTol+",文件生成所在的位置 ： "+dirLocation;

            LogHelp.insertCldLogsPro(iamConn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    msg,true);

//            WeChatHelp.sendCompanyWeChatMsg(WeChatHelp.log_secret,msg);


//            MailSend16Help.doSendHtmlEmail("月账文件生成" ,
//                    msg,
//                    "chengliudegg@163.com",
//                    "chengliudegg@163.com",
//                    null);


            //开启数据备份
//            String yyyyMM = DateTimeHelp.getDateTimeString("yyyyMM");
//            String tableName = "MBI_"+yyyyMM;
//            String bsstable = "create table "+tableName+" AS  select * from dbusr07.cld_temp_data_all_new@zwdb";
//            try{
//                ProcUtil.callProc(bssConn,"sql_procedure",  new Object[]{bsstable});
//                ProcUtil.callProc(bssConn,"sql_procedure",  new Object[]{"grant all on tableName to public"});
//                ProcUtil.callProc(bssConn,"sql_procedure",  new Object[]{"create index msisdn_index_"+yyyyMM+" on  "+tableName+"(msisdn)"});
//            }catch (Exception e){
//                e.printStackTrace();
//            }

        }


    }

    private String getLineData(ResultSet rstInst) throws Exception {
        rstInst.next();

        recordingTol++;
        recordingAll++;

        String msisd = rstInst.getString("msisdn");
        String serv_id = rstInst.getString("serv_id");
        String prod_inst_id = rstInst.getString("prd_inst_id");
        String prod_inst_id_new = this.checkNull(prod_inst_id,"10");

        String product_offer_id = rstInst.getString("product_offer_id");
        String product_offer_id_new = rstInst.getString("product_offer_id_new");
        product_offer_id_new = this.checkNull(product_offer_id_new,product_offer_id);


        String product_offer_inst_id = rstInst.getString("product_offer_inst_id");

        String acct_id = rstInst.getString("acct_id");
        String acct_id_new = rstInst.getString("acct_id_new");
        String acct_id_new2 = this.checkNull(acct_id_new ,acct_id);

        String cust_id = rstInst.getString("cust_id_new");
//        String crm_acct_id = rstInst.getString("crm_acct_id");
        String billing_cycle_id = rstInst.getString("billing_cycle_id");
        String billing_cycle_id_ori = rstInst.getString("billing_cycle_id_ori");
        String charge = rstInst.getString("charge");
        String item_source = rstInst.getString("item_source");
        String biz_type = rstInst.getString("biz_type");
        String acct_item_list = rstInst.getString("acct_item_list");
//            String acct_item_list = "15240";
        String acct_item_type_id = rstInst.getString("acct_item_type_id");
        String acct_item_name = rstInst.getString("acct_item_name");

        if(acct_item_name.contains(",")){
            acct_item_name= acct_item_name.replaceAll(",","，");
        }

        String billing_mode = rstInst.getString("billing_mode");
//            String billing_mode = "0LEK";
        String acc_nbr = rstInst.getString("acc_nbr");
        String cdr_key = rstInst.getString("cdr_key");
        String rome_type = rstInst.getString("rome_type");

        if ("".equals(rome_type) || null == rome_type || rome_type.length() == 1) {
            rome_type = "06";
        }
        String busi_type = rstInst.getString("busi_type");
        if ("".equals(busi_type) || null == busi_type || busi_type.length() == 1) {
            busi_type = "08";
        }

        //重新定义套餐类型
        String meal_type="";
        String some = acct_item_type_id+"&"+product_offer_id;
        if(mealList.contains(some)){
            meal_type="01";
        }else {
            meal_type = rstInst.getString("meal_type");
        }

        String reserver2 = rstInst.getString("reserver2");
        String reserver3 = rstInst.getString("reserver3");
        String reserver4 = rstInst.getString("reserver4");
        String reserver5 = rstInst.getString("reserver5");

//        String s =msisd + "|" + serv_id + "|" + prod_inst_id_new + "|" + product_offer_id_new + "|" +
//                product_offer_inst_id + "|" + acct_id_new2 + "|" + cust_id + "|" + acct_id_new2 + "|" +
//                billing_cycle_id + "|" + billing_cycle_id_ori + "|" + charge + "|" + item_source + "|" +
//                biz_type + "|" + acct_item_list + "|" + acct_item_type_id + "|" + acct_item_name + "|" +
//                billing_mode + "|" + acc_nbr + "|" + cdr_key + "|" + rome_type + "|" +
//                meal_type + "|" + busi_type + "|" + reserver2 + "|" + reserver3 + "|" +
//                reserver4 + "|" + reserver5 + "|";
        String sp = "|";
        StringBuilder lineData = new StringBuilder();
        lineData.append(msisd).append(sp)
                .append(serv_id).append(sp)
                .append(prod_inst_id_new).append(sp)
                .append(product_offer_id_new).append(sp)
                .append(product_offer_inst_id).append(sp)
                .append(acct_id_new2).append(sp)
                .append(cust_id).append(sp)
                .append(acct_id_new2).append(sp)
                .append(billing_cycle_id).append(sp)
                .append(billing_cycle_id_ori).append(sp)
                .append(charge).append(sp)
                .append(item_source).append(sp)
                .append(biz_type).append(sp)
                .append(acct_item_list).append(sp)
                .append(acct_item_type_id).append(sp)
                .append(acct_item_name).append(sp)
                .append(billing_mode).append(sp)
                .append(acc_nbr).append(sp)
                .append(cdr_key).append(sp)
                .append(rome_type).append(sp)
                .append(meal_type).append(sp)
                .append(busi_type).append(sp)
                .append(reserver2).append(sp)
                .append(reserver3).append(sp)
                .append(reserver4).append(sp)
                .append(reserver5).append(sp);


        chargeAll += Long.parseLong(charge);
        return lineData.toString();
    }


    public ResultSet getData() throws Exception {

       String sql = "select * from cld_temp_data_all_new";
//       String sql = "select * from MBI_202101";

        String sqltest = "select * from dbusr07.cld_temp_data_zq@zwdb_prod";


        PreparedStatement pstmtInst = null;
        if(sign.equals("pro")){
            pstmtInst = iamConn.prepareStatement(sql);
        }else {
            pstmtInst = iamConn.prepareStatement(sqltest);
        }

        ResultSet rstInst = pstmtInst.executeQuery();
        return rstInst;
    }


    private String writeData(int nnn,
                             String dataLine,
                             String fileName) throws Exception {

        String strnnn = UtilTools.zeroPadding(nnn);

        String ff = UtilTools.makeFF();
        //写数据 BBB.MAX.NNN
        String currentFileName = fileName + ".001" + strnnn + "" + strnnn + "." + ff;
        fileNameList.add(currentFileName);
        String location = dirLocation + currentFileName;
        //判断文件时候存在，不存在则创建
        UtilTools.judeFileExists(location);

        os = new FileOutputStream(location);
        pw = new PrintWriter(os);
        //            //添加头部记录
        pw.println("10" + "|" + "46003021" + "|" + fileTol + "|" + sys_datetime + "|10|");
        pw.println(dataLine);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
        return "";
    }

    public String checkNull(String str , String st) {
        if(null==str || "null".equals(str) || "".equals(str)){
            return st;
        }else {
            return str;
        }
    }


}
