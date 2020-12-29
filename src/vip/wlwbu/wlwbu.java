package vip.wlwbu;

import helps.ReadCsvHelp;
import helps.ReadExcelHelp;
import helps.SQLHelp;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.DBConn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/25
 */
public class wlwbu {
    
//    private static Connection conn = DBConn.getDbusr01TestConn();
    private static Connection conn = DBConn.getCopyProConn();
    private static String  mark2= "";
    private static Boolean sign = true;

    public static void main(String[] args) {
        wlwbu wlw = new wlwbu();
        //跟新状态
        String update = "update jt_bill_data set status = 9 where  status = 0";
        SQLHelp.updateSQL(conn , update);
        List<String> list = null;
        try {
            list = wlw.readXlsx();
        } catch (Exception e) {
            e.printStackTrace();
        }
        wlw.update(list);
    }



    public  List<String> readXlsx()  throws  Exception{
        String fileDir = "/acct/acct_payment/JtBill/data/wlw/bu";
        String filename="wlw_supple.csv";
        String fileFullName = fileDir+ File.separator+filename;
        //清空白名单
        SQLHelp.deleteSQL(conn,"delete from  wlw_com_list");
        List<List<String>> dataLists = ReadCsvHelp.readCsv(fileFullName);
        Set<String> bu_serv_code_set = new HashSet();

        for(List<String> list:dataLists){
            //账期
            String billing_month = list.get(0);
            String serv_code = list.get(1);
            String acct_id = list.get(2);
            String amount = list.get(3);
            String bu_mon = list.get(4);
            String bu_serv_code = list.get(5);
            String bu_account = list.get(6);
            String bu_mark = list.get(7);
            if("".equals(mark2)){
                mark2=bu_mark;
            }

            bu_serv_code_set.add(bu_serv_code);

            //对比金额
            String query = "select sum(amount) as FIELD from jt_bill_data where  service_type =1 and status = 9 and \n" +
                    "billing_month='"+billing_month+"' and serv_code in ('"+serv_code+"') and acct_id='"+acct_id+"'";

            String FIELD = SQLHelp.querySQLReturnField(conn, query);
            System.out.println("===="+FIELD);
            if(!FIELD.equals(amount)){
                System.out.println("----------金额不一致请检查");
                throw  new Exception("金额不一致");
            }else {
                System.out.println(billing_month+"------"+"金额一致 ："+amount);
            }
        }


        //金额一致，修改状态
        List<String> spare_field1_List = new ArrayList<String>();
        for(List<String> list:dataLists){
            //账期
            String billing_month = list.get(0);
            String serv_code = list.get(1);
            String acct_id = list.get(2);
            String amount = list.get(3);
            String bu_mon = list.get(4);
            String bu_serv_code = list.get(5);

            String spare_field1 = billing_month+"_"+serv_code;
            spare_field1_List.add(spare_field1);
            String updatedata = "update jt_bill_data set status = 0 , serv_code ='"+bu_serv_code+"',billing_month='"+bu_mon+"'  , spare_field1 = '"+spare_field1+"'\n" +
                    " where  acct_id='"+acct_id+"' and service_type =1 and status = 9 and billing_month='"+billing_month+"' and serv_code in ('"+serv_code+"')";
            SQLHelp.updateSQL(conn, updatedata);
        }



        for(String ser_code : bu_serv_code_set){
            String inser = "insert into wlw_com_list select distinct null ,null,null,null,'"+ser_code+"',null from dual";
            SQLHelp.updateSQL(conn , inser);
        }
        //删除空格
        SQLHelp.updateSQL(conn,"update wlw_vip_list set serv_code =trim(serv_code) ,jt_serv_code =trim(jt_serv_code)");

        return spare_field1_List;
    }

    public void update(List<String> list ) {
        while (sign){
            //查询是否开账成功
            System.out.println("查询是否开账成功");
            for(String sp :list){
                String sele = "select count(*) as FIELD from  jt_bill_data where pay_interim_seq is not null and spare_field1 = '"+sp+"'";
                String count = SQLHelp.querySQLReturnField(conn, sele);
                if(Integer.parseInt(count) > 0){
                    //开账成功，跟新回去
                    String[] split = sp.split("_");
                    String mon = split[0];
                    String serv_code = split[1];

                    String updata = "update jt_bill_data set billing_month='"+mon+"', annotation='"+mark2+"' ,serv_code = '"+serv_code+"' where spare_field1='"+sp+"'";
                    SQLHelp.updateSQL(conn,updata);
                    sign = false;
                    System.out.println(split+" :跟新结束");
                }else {
                    System.out.println("未结束，继续等待");
                    sign = true;
                    break;
                }
            }

            try {
                System.out.println("睡眠5分钟");
                Thread.sleep(36000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
