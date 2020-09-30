package vip;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.LogHelp;
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

        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "在 cld_temp_data_new 删除"+yyyyMM+"账期数据完成"
                ,true);


        ArrayList<String> cld_temp_data_new_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_new");
        String count1 = cld_temp_data_new_list.get(0);
        String sum1 = cld_temp_data_new_list.get(1);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "基础cld_temp_data_new ： "+cld_temp_data_new_list
                ,true);


        ArrayList<String> cld_temp_data_last_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_last");
        String count2 = cld_temp_data_last_list.get(0);
        String sum2 = cld_temp_data_last_list.get(1);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "基础cld_temp_data_last ： "+cld_temp_data_last_list
                ,true);

        ArrayList<String> CLD_TEMP_DATA_OCS_list = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from CLD_TEMP_DATA_OCS");
        String count3 = CLD_TEMP_DATA_OCS_list.get(0);
        String sum3 = CLD_TEMP_DATA_OCS_list.get(1);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "基础CLD_TEMP_DATA_OCS ： "+CLD_TEMP_DATA_OCS_list
                ,true);

        //原数据统计
        Long allcount = Long.parseLong(count1)+Long.parseLong(count2)+Long.parseLong(count3);
        Long allcharge = Long.parseLong(sum1)+Long.parseLong(sum2)+Long.parseLong(sum3);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "数据统计条数 ： "+count1 + "+" +count2 + "+" +count3+"="+allcount
                ,true);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "金额统计 ： "+sum1 + "+" +sum2 + "+" +sum3+"="+allcharge
                ,true);


        //将立即出账数据存入cld_temp_data 数据
        String insert = "insert into cld_temp_data_new select * from cld_temp_data_last";
        SQLHelp.insertSQL(conn,insert);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "立即出账数据插入cld_temp_data_new完成"
                ,true);

        //将ocs的数据插入cld_temp_data表
        String some = "insert into CLD_TEMP_DATA_NEW select * from CLD_TEMP_DATA_OCS";
        SQLHelp.insertSQL(conn,some);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "将ocs的数据插入cld_temp_data表完成"
                ,true);



        ArrayList<String> all = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_new");
        String co = all.get(0);
        String cha = all.get(1);
        LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                "数据汇总之后cld_temp_data_new " +all
                ,true);
        if((allcount+"").equals(co)   && (allcharge+"").equals(cha)  ){
            LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    "******数据校验一致*****"
                    ,true);
        }else {
            LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    "******数据校验不一致*****"
                    ,true);
        }



    }

}
