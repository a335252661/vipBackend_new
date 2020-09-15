package vip;


import helps.MailSend16Help;
import helps.WeChatHelp;
import utils.*;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 程刘德
 * @version 1.0
 * @Description ?	接口方式：ASN.1编码文件；
 * ?	交互频度：每月一次，次月5日24点前上传；
 * ?	文件命名： MBI_AMOUNT_YYYYMM.ZZZ.BBB.MAX.NNN.FF
 * ?	文件大小：单个文件记录数不允许超过十万条，如果超过十万条，则分成多个文件上传
 * ?	传输方向：省计费->全国中心
 *       12 月份 跑11月份的表
 * @date 2019/11/27
 */
public class UsageAmountApplication2 implements Runnable {

//    private static String sign = "test";
    private static String sign = "pro";

    private static String dirLocation = "";

    private static String lastMonMaxDay = "";
    static{

//         lastMonMaxDay = DateTimeHelp.getLastMon();

        if(sign.equals("pro")){
            dirLocation = "/dwm/cqd_split/2.4/files/";
        }else {
            dirLocation = "D:\\file_temp\\2.4\\";
        }
    }


    //每次查数据条数
    private static Long limitTol = 100000L;

    //记录当前文件数据条数
    private static Long recordingTol = 0L;
    //记录总数据条数
    private static Long recordingAll = 0L;

    private static PrintWriter pw = null;
    private static OutputStream os = null;
    //文件个数
    private static int fileTol = 1;

    //修改文件名
    private static ArrayList<String> fileNameList = new ArrayList<String>();

    private static String mounthMaxDay = "";

    private static List<List> lists = null;

    private static ArrayList<String> dataList = new ArrayList<String>();

    //生产问文件名时间
    private static String last_sys_datetime = "";

    private static String last_sys_month = "";

    private static HashMap<String,String> dateMap = new HashMap<String, String>();
    private static HashMap<String,String> idMap = new HashMap<String, String>();
    private static HashMap<String,String> instidMap = new HashMap<String, String>();
    private static HashMap<String,String> codeMap = new HashMap<String, String>();


    private static Long seq = 0L;

    public static void main(String[] args) {

        //文件名时间为当前月份减一个月
        Date date = new Date();//获取当前时间 ? ?
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间 ? ?
        calendar.getTime();//获取一年前的时间，或者一个月前的时间 ? ?
        Date dt1 = calendar.getTime();
        last_sys_datetime = new SimpleDateFormat("yyyyMM").format(dt1);
        last_sys_month = new SimpleDateFormat("MM").format(dt1);


        // 查询期初金额 应该根据表汇总获取
        long startTime = System.currentTimeMillis(); //获取开始时间

        System.out.println("程序加载开始==========================");
        System.out.println("文件存放位置==========================" + dirLocation);
        UsageAmountApplication2 getData = new UsageAmountApplication2();
        mounthMaxDay = getData.getMounthMaxDay();

        try {

            //读取设备文件
            //当前项目下路径
            File file = new File("");
            String filePath = file.getCanonicalPath();

            String fileLocation = "";
            String code_crm = "";
            if(sign.equals("pro")){
                fileLocation = "/dwm/cqd_split/2.4/configuration/service_nbr_pro.txt";
                code_crm = "/dwm/cqd_split/2.4/configuration/code_crm.txt";
            }else {
                fileLocation = filePath + "\\service_nbr_pro.txt";
                code_crm = filePath + "\\code_crm.txt";
            }


            //判断文件夹路径是否存在不存在则创建
            File locationFile = new File(dirLocation);
            if (!locationFile.exists()) {
                locationFile.mkdirs();
                System.out.println("路径不存在，生成完成。");
            }

            //文件名
            String fileName = "MBI_AMOUNT_" + last_sys_datetime + ".021";


            //读文件获取数据
            List<String> arrayList = new ArrayList<String>();
            try {
                FileReader fr = new FileReader(fileLocation);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    arrayList.add(str);
                }
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //读文件获取数据
             codeMap = new HashMap<String, String>();
            try {
                FileReader fr = new FileReader(code_crm);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    codeMap.put(str.split("%")[0],str.split("%")[1]);
                }
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //将大集合分成100个小集合
            lists = getData.subList2(arrayList, 100);

            System.out.println("小集合的数量============" + lists.size());


            ExecutorService exe = Executors.newCachedThreadPool();
            for (int i = 0; i < lists.size(); i++) {
                exe.execute(new UsageAmountApplication2());
            }
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    System.out.println("所有线程运行结束");
                    break;
                }
            }


            System.out.println("=====开始dataList集合中拿取所有数据并写入文本");
            System.out.println("=====dataList.size()+"+dataList.size());
            for (int i = 0; i < dataList.size(); i++) {

                if (i == 0) {
                    getData.writeData(getData, fileTol, dataList.get(i), fileName);
                } else {
                    //判断当前行数，超过规定则新建文件重新写
                    if (recordingTol < limitTol) {
                        //继续写
                        pw.println(dataList.get(i));//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
                        recordingTol++;
                    } else {
                        //添加尾部记录
                        pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + recordingTol  + "|");
                        pw.close();
                        os.close();
                        fileTol++;
                        recordingTol = 0L;

                        getData.writeData(getData, fileTol, dataList.get(i), fileName);
                    }

                }

            }



            System.out.println("生产文件完成");
            System.out.println("数据总条数++" + recordingAll);

            if(dataList.size()!=0){
                pw.println("90" + "|" + "46003021" + "|" + fileTol + "|" + recordingTol + "|");
                pw.close();
                os.close();
            }


            //所有文件生成完成，修改文件名
            if (fileNameList.size() > 1) {
                //获取最后一个文件
                String maxName = fileNameList.get(fileNameList.size() - 1);
                String max = maxName.substring(30, 33);
                for (String currName : fileNameList) {
                    String newName = currName.substring(0, 26) + max + currName.substring(29, currName.length());
//                    System.out.println("old======" + currName);
//                    System.out.println("new======" + newName);
                    UtilTools.renameFile(dirLocation, currName, newName);
                }
            }


            long endTime = System.currentTimeMillis(); //获取结束时间
            String runTime = UtilTools.longToTime(endTime - startTime);
            System.out.println("====================程序运行时间 ：" + runTime);

            String msg1 = "使用量文件生成完成，" +
                    "程序运行时间 ：" + runTime+
                    "生产的文件总数"+fileTol+",文件生成所在的位置 ： "+dirLocation;

//            WeChatHelp.sendCompanyWeChatMsg(WeChatHelp.log_secret,msg1);


            String msg2 = "";
            //上传到转码服务器
            try {
                FtpUtil ftp = FtpUtil.connect("10.7.95.70","bgusr01","lc#v58iHH",
                        "/home/bgusr01/vip_backend/files");
                String s = fileNameList.get(0);
                System.out.println(dirLocation+s);
                File currfile = new File(dirLocation+s);
                boolean b = ftp.uploadFile(currfile, s);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            MailSend16Help.doSendHtmlEmail("使用量文件" ,
//                    msg1 +"<br>------------------<br>"+msg2,//换行
//                    "chengliudegg@163.com",
//                    "chengliudegg@163.com",
//                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getZDAAS() throws Exception {
//        String dbUrl = "jdbc:mysql://10.145.206.227:8066/dbtest";
//        String dbUser = "root";
//        String dbPwd = "123";
        //生产
        String dbUrl = "jdbc:mysql://10.7.69.205:8066/dbtest";
        String dbUser = "cqd_app";
        String dbPwd = "ri3@rgk0";




        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPwd);
    }

    public Connection getOutside() throws Exception {
        String dbUrl ;
        String dbUser ;
        String dbPwd ;

        if(sign.equals("pro")){
            dbUrl = "jdbc:mysql://10.145.172.243:8946/detectdb";
            dbUser = "detect_app";
            dbPwd = "x3I*VO9E";
        }else {
            dbUrl = "jdbc:mysql://10.145.242.14:3306/test";
            dbUser = "ycluser";
            dbPwd = "eastern234";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPwd);
    }


    // 判断文件是否存在
    public void judeFileExists(String location) {
        File file = new File(location);
        if (file.exists()) {
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String makeFF() {
        String format1 = new SimpleDateFormat("dd").format(new Date());
        String format2 = new SimpleDateFormat("hh").format(new Date());
        return format1.substring(1) + format2.substring(1);
    }

    public String zeroPadding(int m) {
        String cuf = "";
        String mm = m + "";
        if (mm.length() == 1) {
            cuf = ".00" + m;
        } else if (mm.length() == 2) {
            cuf = ".0" + m;
        } else if (mm.length() == 3) {
            cuf = "." + m;
        }
        return cuf;
    }

    /*套内查数据*/
    private String selectAndWriteData(Connection iamConn,
                                      String serviceNum,
                                      UsageAmountApplication2 getData,
                                      String fileName) throws Exception {

        String SERV_ID = serviceNum.split("\\|")[0];
        String ACC_NBR = serviceNum.split("\\|")[1];
        String time = serviceNum.split("\\|")[2].replaceAll("/","");


//        System.out.println("设备号码=================" + ACC_NBR);

        String service_info = "service_info_"+last_sys_month;

        String ofrSQL = "select a.ofr_id from "+service_info+" a where a.SERVICE_NBR = " + ACC_NBR + " and FLAG=1";
        PreparedStatement ofrSQLPre = iamConn.prepareStatement(ofrSQL);
        ResultSet rstInstPre = ofrSQLPre.executeQuery();

        String ofrIdList = "";
        while (rstInstPre.next()) {
            String ofr_id = "'" + rstInstPre.getString("ofr_id");
            ofrIdList += ofr_id + "',";
        }

        if (ofrIdList.equals("")) {
            return "";
        }

        String ofrIdListNew = ofrIdList.substring(0, ofrIdList.length() - 1);

        String OFR_RATABLE_REL = "OFR_RATABLE_REL_"+last_sys_month;

        String dataSQL = "select DISTINCT * from "+OFR_RATABLE_REL+" where ofr_id in (" + ofrIdListNew + ") and FLAG=1";
        PreparedStatement dataSQLPre = iamConn.prepareStatement(dataSQL);
        ResultSet dataInstPre = dataSQLPre.executeQuery();

        while (dataInstPre.next()) {
            //累计值
            String CUT_VALUE = dataInstPre.getString("RATABLE_USE");

            recordingAll++;

            String BUSI_TYPE = "";
            String ratable_resource_id = dataInstPre.getString("ratable_resource_id").substring(0, 1);
            if (ratable_resource_id.equals("1")) {
                //语音
                BUSI_TYPE = "01";
                //语音   分钟转秒
                CUT_VALUE = Integer.parseInt(CUT_VALUE)*60+"";
            } else if (ratable_resource_id.equals("2")) {
                //wifi
                BUSI_TYPE = "05";
            } else if (ratable_resource_id.equals("3")) {
                //数据
                BUSI_TYPE = "02";
            } else if (ratable_resource_id.equals("4")) {
                //短信
                BUSI_TYPE = "03";
            } else {
                //其他
                BUSI_TYPE = "08";
            }


            String MEAL_TYPE = "01";
            //账期
            String BILLING_CYCLE_ID = dataInstPre.getString("PROCESS_MONTH");
            //积累类型
            String ACCU_TYPE = BUSI_TYPE;
            //累计值
//            String CUT_VALUE = dataInstPre.getString("RATABLE_USE");
            //累积开始时间
            String CYCLE_BEGIN_TIME = dataInstPre.getString("EFF_DATE").substring(0, 8);
            //累计结束时间
            String CYCLE_END_TIME = dataInstPre.getString("EXP_DATE").substring(0, 8);
            //累计ID
            String EVENT_AGGR_ID = seq++ + "";   //修正
            //销售平ID
            String OFR_CODE = dataInstPre.getString("OFR_CODE");
            String PRODUCT_OFFER_ID = codeMap.get(OFR_CODE)==null?OFR_CODE:codeMap.get(OFR_CODE);
            //销售品实例ID
            String PRODUCT_OFFER_INST_ID = dataInstPre.getString("OFR_INST_CD");

            if(null==idMap.get(ACC_NBR)){
                idMap.put(ACC_NBR,PRODUCT_OFFER_ID);
            }
            if(null==instidMap.get(ACC_NBR)){
                instidMap.put(ACC_NBR,PRODUCT_OFFER_INST_ID);
            }




            //合约实际生效日期
            String effective_DATE = time;   //xiugai

            String dataLine = "01|".concat(BUSI_TYPE + "|01|").concat(BILLING_CYCLE_ID + "|")
                    .concat(ACCU_TYPE + "|").concat(CUT_VALUE + "|").concat(CYCLE_BEGIN_TIME + "|")
                    .concat(CYCLE_END_TIME + "|").concat(EVENT_AGGR_ID + "|")
                    .concat(PRODUCT_OFFER_ID + "|").concat(PRODUCT_OFFER_INST_ID + "|").concat(effective_DATE + "|")
                    .concat(SERV_ID + "|").concat(ACC_NBR + "|");


//            System.out.println(dataLine+"==============================");

            dataList.add(dataLine);
        }

        return "";
    }

    /*套外查数据*/
    private String selectAndWriteDataOutside(Connection iamConn,
                                             String serviceNum,
                                             UsageAmountApplication2 getData,
                                             String fileName) throws Exception {

        String SERV_ID = serviceNum.split("\\|")[0];
        String ACC_NBR = serviceNum.split("\\|")[1];
        String date2 = serviceNum.split("\\|")[2].replaceAll("/","");
//        System.out.println("设备号码=================" + ACC_NBR);

        String cqd_real_statistics = "cqd_real_statistics_"+last_sys_month;

        String ofrSQL = "select cdr_date,call_nbr,busi_class,sum(flue_out) flue from "+cqd_real_statistics+" where call_nbr =" + ACC_NBR + "  GROUP BY call_nbr,busi_class";
        PreparedStatement ofrSQLPre = iamConn.prepareStatement(ofrSQL);
        ResultSet dataInstPre = ofrSQLPre.executeQuery();

        while (dataInstPre.next()) {
            recordingAll++;
            //累计值
            String CUT_VALUE = Long.parseLong(dataInstPre.getString("flue")) + "";

            String BUSI_TYPE = "";
            String busi_class = dataInstPre.getString("busi_class");
            if (busi_class.equals("voi")) {
                //语音
                BUSI_TYPE = "01";
                CUT_VALUE = Long.parseLong(dataInstPre.getString("flue"))+"";   //要传秒
            } else if (busi_class.equals("dat")) {
                //数据
                BUSI_TYPE = "02";   //KB 变 M
                CUT_VALUE = Long.parseLong(dataInstPre.getString("flue")) / 1024L + "";
            } else if (busi_class.equals("sms")) {
                //短信
                BUSI_TYPE = "03";
            } else {
                //其他
                BUSI_TYPE = "08";
            }


            String MEAL_TYPE = "01";
            //账期
            String BILLING_CYCLE_ID = last_sys_datetime;
            //积累类型
            String ACCU_TYPE = BUSI_TYPE;
            //累计值
//            String CUT_VALUE = Long.parseLong(dataInstPre.getString("flue")) / 1024L + "";
            //累积开始时间
            String CYCLE_BEGIN_TIME = last_sys_datetime + "01"; //jianyge yue 20200301
            //累计结束时间
            String CYCLE_END_TIME = lastMonMaxDay;  //jianyge yue    20200331
            //累计ID
            String EVENT_AGGR_ID = seq++ +"";  // 累加

            //销售平ID
            String PRODUCT_OFFER_ID = idMap.get(ACC_NBR);    // 用套内的
            //销售品实例ID
            String PRODUCT_OFFER_INST_ID = instidMap.get(ACC_NBR);  // 用套内的
            //合约实际生效日期
            String effective_DATE = date2;  //修改    txt ok


            String dataLine = "01|".concat(BUSI_TYPE + "|02|").concat(BILLING_CYCLE_ID + "|")
                    .concat(ACCU_TYPE + "|").concat(CUT_VALUE + "|").concat(CYCLE_BEGIN_TIME + "|")
                    .concat(CYCLE_END_TIME + "|").concat(EVENT_AGGR_ID + "|")
                    .concat(PRODUCT_OFFER_ID + "|").concat(PRODUCT_OFFER_INST_ID + "|").concat(effective_DATE + "|")
                    .concat(SERV_ID + "|").concat(ACC_NBR + "|");

            dataList.add(dataLine);
        }
        return "";
    }


    private String writeData(UsageAmountApplication2 getData,
                             int nnn,
                             String dataLine,
                             String fileName) throws Exception {

        String strnnn = getData.zeroPadding(nnn);
        String sys_datetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String ff = getData.makeFF();
        //写数据 BBB.MAX.NNN
        String currentFileName = fileName + ".001" + strnnn + "" + strnnn + "." + ff;
        fileNameList.add(currentFileName);
        String location = dirLocation + currentFileName;
        //判断文件时候存在，不存在则创建
        getData.judeFileExists(location);

        os = new FileOutputStream(location);
        pw = new PrintWriter(os);
        //            //添加头部记录
        pw.println("10" + "|" + "46003021" + "|" + fileTol + "|" + sys_datetime + "|10|");
        if(null==dataLine || dataLine.equals("")){
            return "";
        }
        pw.println(dataLine);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
        recordingTol++;
        return "";
    }


    public String getMounthMaxDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(ca.getTime());
    }

    @Override
    public void run() {
//        System.out.println(Thread.currentThread().getName());
        String name = Thread.currentThread().getName();
        String i = name.split("-")[3];
        int currentIndex = Integer.parseInt(i);
        //获取当前对应数据
        List list = lists.get(currentIndex - 1);
        //文件名
        String fileName = "MBI_AMOUNT_" + last_sys_datetime + ".021";
        UsageAmountApplication2 getData = new UsageAmountApplication2();

        //查询套餐内数据存入集合
        for (Object serviceNum : list) {
            String strserviceNum = (String) serviceNum;
            if (strserviceNum.equals("")) {
                continue;
            }
            try {
                Connection inConn = getData.getZDAAS();
                //套餐内数据
                getData.selectAndWriteData(inConn, strserviceNum, getData, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        查询套餐外数据存入集合
        for (Object serviceNum : list) {
            String strserviceNum = (String) serviceNum;
            if (strserviceNum.equals("")) {
                continue;
            }
            try {
                Connection inConn = getData.getOutside();
                //套餐外数据
                getData.selectAndWriteDataOutside(inConn, strserviceNum, getData, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 从大集合中取等量数据
     *
     * @param list 大集合
     * @param num  每次取的个数
     * @return
     */
    public static List<List> subList(List list, int num) {
        List<List> returnList = new ArrayList();
        for (int i = 0; i < list.size(); i = i + num) {
            //最后一次截取集合
            if (i + num > list.size()) {
                num = list.size() - i;
            }
            List newList = list.subList(i, i + num);
            returnList.add(newList);
        }
        return returnList;
    }

    /**
     * @param list
     * @param num  分成 多少个集合
     *             <p>
     *             3490
     *             分成100个集合
     * @return
     */
    public static List<List> subList2(List list, int num) {

        Integer total = list.size();
        BigDecimal mm = new BigDecimal(total).divide(new BigDecimal(num), 0);
        List<List> lists = subList(list, mm.intValue());
        return lists;
    }

}
