package vip.wlwcoll;

import utils.DBConn;
import vip.wlw.XxlJobLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/8
 */
public class wlwcoll {
    private static DecimalFormat df = new DecimalFormat("000");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String filename = "BILL_IOT_ACCT_ITEM_";

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

    private static DecimalFormat df2 = new DecimalFormat("000");

    private String jtBillIotAcctItemAccount_filepath = "/acct/acct_payment/JtBill/data/wlw/coll/";

    private  static    Connection conn = DBConn.getCopyProConn();;



    public static void main(String[] args) {
        wlwcoll wlwcoll = new wlwcoll();

        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        XxlJobLogger.log("-----jtBillIotAcctItemAccount --start --"+df.format(day));
        wlwcoll.getBilData();
        XxlJobLogger.log("数据入库完成  9   时间 =="+df.format(day));
        wlwcoll.getBilDataEr();
        XxlJobLogger.log("数据入库完成 0   时间=="+df.format(day));
        wlwcoll.getBillData2();
        XxlJobLogger.log("数据入库完成 -1   时间=="+df.format(day));
        wlwcoll.getBillData3();
        XxlJobLogger.log("数据入库完成 !=9 !=3   时间=="+df.format(day));

        wlwcoll.getjtBillIotAuditInfo();
        XxlJobLogger.log("文件生成完成   时间=="+df.format(day));
        XxlJobLogger.log("-----jtBillIotAcctItemAccount --end --"+df.format(day));


    }
    private String getFileName(){
        return filename+sdf2.format(new Date())+".021";
    }
    /*
     * 导出文件
     */
    private  void getjtBillIotAuditInfo() {


        XxlJobLogger.log("开始查询表JT_BILL_IOT_ACCT_ITEM并生成文件...");
        int c = 0;
        int filecount = 0;
        int rowcount = 0;
        long totalsize = 0;
        StringBuffer files = new StringBuffer("");
        while(true){
            StringBuffer sb = new StringBuffer("STA|");
            String sql = "SELECT ACC_NBR,ACCT_ID,BLNG_CCL_ID,ACCT_ITEM_TP_ID,CHARGE_NBR,CHARGE_NBR_TYPE,OWE_AMNT,DEDUCT_TYPE from JT_BILL_IOT_ACCT_ITEM where rownum <= 100000 and FILE_NAME is null";
            c++;
            XxlJobLogger.log("执行sql:"+sql);
            List<List<Object>> l = JTRepJdbcUtil.excuteQuery2(sql);
            if(l.size()==0){
                break;
            }
            sb.append(l.size()+"|\r\n");
            for(List<Object> temp : l){
                for(Object o : temp){
                    if(o == null||o.toString().equals("null")){
                        sb.append("|");
                    }else{
                        sb.append(o+"|");
                    }
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("\r\n");
            }
            sb.append("END|");

            String filename = getFileName()+"."+df2.format(c);
            while(new File(jtBillIotAcctItemAccount_filepath+filename).exists()){
                c++;
                filename = getFileName()+"."+df2.format(c);
            }
            this.writeFile(sb.toString(), jtBillIotAcctItemAccount_filepath+filename);
            totalsize+=sb.length();
            XxlJobLogger.log("文件"+filename+"生成完成,行数:"+l.size());
            //TODO FILENAME1
            String tempsql = "update JT_BILL_IOT_ACCT_ITEM set FILE_NAME = '"+filename+"' , FILE_DATE = '"+sdf.format(new Date())+"' where rownum <= 100000 and FILE_NAME is null";
            JTRepJdbcUtil.executeUpdate(tempsql);
            XxlJobLogger.log("表状态修改完成");
            filecount++;
            rowcount += l.size();
            files.append(filename+"\r\n");
            sb = new StringBuffer();
        }
        XxlJobLogger.log("所有文件生成完毕,文件总数:"+filecount+",总行数:"+rowcount+",总大小:"+totalsize+".");
        //		if(filecount!=0){
        //即使没有文件输出也要有000文件
        this.writeFile(files.toString(), jtBillIotAcctItemAccount_filepath+getFileName()+".000");
        XxlJobLogger.log("000文件生成完毕.");

    }
    public static void writeFile(String sb, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(sb.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /*
     * 处理数据入库
     */
    public  void getBilData() {
        JTRepJdbcUtil.executeUpdate("truncate table JT_BILL_IOT_ACCT_ITEM");

        JTRepJdbcUtil.executeUpdate("INSERT INTO JT_BILL_IOT_ACCT_ITEM (ACC_NBR,ACCT_ID,BLNG_CCL_ID,ACCT_ITEM_TP_ID,CHARGE_NBR,CHARGE_NBR_TYPE,OWE_AMNT,DEDUCT_TYPE) SELECT A.SERV_CODE,-1,A.BILLING_MONTH,A.SUB_TYPE_CODE,A.SERV_CODE,A.SERV_TYPE,SUM(A.AMOUNT) AMOUNT,0 FROM JT_BILL_DATA A WHERE A.STATUS = 9 AND A.SERVICE_TYPE = 1 GROUP BY A.SERV_CODE, A.BILLING_MONTH, A.SUB_TYPE_CODE, A.SERV_TYPE");
    }

    // 查询数据入库（二）
    public  void getBilDataEr() {
        String sql = "INSERT INTO JT_BILL_IOT_ACCT_ITEM (ACC_NBR,ACCT_ID,BLNG_CCL_ID,ACCT_ITEM_TP_ID,CHARGE_NBR,CHARGE_NBR_TYPE,OWE_AMNT,DEDUCT_TYPE) SELECT A.SERV_CODE,-1,A.BILLING_MONTH,A.SUB_TYPE_CODE,A.SERV_CODE,A.SERV_TYPE,SUM(A.AMOUNT) AMOUNT,0 FROM JT_BILL_DATA A, JT_BILL_DATA_INVOICE B WHERE A.STATUS != 9 AND A.SERVICE_TYPE = 1 AND A.PAY_INTERIM_SEQ = B.PAY_INTERIM_SEQ AND B.STATUS != 3 GROUP BY A.SERV_CODE, A.BILLING_MONTH, A.SUB_TYPE_CODE, A.SERV_TYPE";

        JTRepJdbcUtil.executeUpdate(sql);
    }
    public  void getBillData2(){
        String sql = "INSERT INTO JT_BILL_IOT_ACCT_ITEM (ACC_NBR,ACCT_ID,BLNG_CCL_ID,ACCT_ITEM_TP_ID,CHARGE_NBR,CHARGE_NBR_TYPE,OWE_AMNT,DEDUCT_TYPE) SELECT A.SERV_CODE,-1,A.BILLING_MONTH,A.SUB_TYPE_CODE,A.SERV_CODE,A.SERV_TYPE,SUM(A.AMOUNT) AMOUNT,0 FROM JT_BILL_DATA A WHERE A.STATUS = 0 AND A.SERVICE_TYPE = 1 GROUP BY A.SERV_CODE, A.BILLING_MONTH, A.SUB_TYPE_CODE, A.SERV_TYPE";
        JTRepJdbcUtil.executeUpdate(sql);
    }
    public  void getBillData3(){
        String sql = "INSERT INTO JT_BILL_IOT_ACCT_ITEM (ACC_NBR,ACCT_ID,BLNG_CCL_ID,ACCT_ITEM_TP_ID,CHARGE_NBR,CHARGE_NBR_TYPE,OWE_AMNT,DEDUCT_TYPE) SELECT A.SERV_CODE,-1,A.BILLING_MONTH,A.SUB_TYPE_CODE,A.SERV_CODE,A.SERV_TYPE,SUM(A.AMOUNT) AMOUNT,0 FROM JT_BILL_DATA A WHERE A.STATUS = -1 AND A.SERVICE_TYPE = 1 GROUP BY A.SERV_CODE, A.BILLING_MONTH, A.SUB_TYPE_CODE, A.SERV_TYPE";
        JTRepJdbcUtil.executeUpdate(sql);
    }

}
