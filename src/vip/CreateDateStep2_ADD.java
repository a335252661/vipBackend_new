package vip;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description 对生成的数据进行处理，生成最终数据
 * @date 2020/4/10
 */
public class CreateDateStep2_ADD {

//        private static String sign = "test";
    private static String sign = "pro";


    private static Connection conn = null;
    static{
        if(sign.equals("pro")){
            conn = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getDbusr07TestConn();
        }
    }

    public static void main(String[] args) {

        //删除
        String yyyyMM = DateTimeHelp.getDateTimeString("yyyyMM");
        String delete2 = "delete cld_temp_data_new where billing_cycle_id ='"+yyyyMM+"' ";
        SQLHelp.deleteSQL(conn,delete2);
        System.out.println("在 cld_temp_data_new 删除"+yyyyMM+"账期数据完成");


        ArrayList<String> cld_temp_data_new_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_new");
        ArrayList<String> cld_temp_data_last_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_last");
        ArrayList<String> CLD_TEMP_DATA_OCS_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from CLD_TEMP_DATA_OCS");

        //将立即出账数据存入cld_temp_data 数据
        String insert = "insert into cld_temp_data_new select * from cld_temp_data_last";
        SQLHelp.insertSQL(conn,insert);
        System.out.println("立即出账数据插入cld_temp_data_new完成");

        //将ocs的数据插入cld_temp_data表
        String some = "insert into CLD_TEMP_DATA_NEW select * from CLD_TEMP_DATA_OCS";
        SQLHelp.insertSQL(conn,some);
        System.out.println("将ocs的数据插入cld_temp_data表完成");



        System.out.println("===================数据统计======================");
        //CLD_TEMP_DATA_new
        System.out.println("基础cld_temp_data_new ： "+cld_temp_data_new_list);
        System.out.println("基础cld_temp_data_last ： "+cld_temp_data_last_list);
        System.out.println("基础CLD_TEMP_DATA_OCS ： "+CLD_TEMP_DATA_OCS_list);

        System.out.println("=================================================");

    }

}
