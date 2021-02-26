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
public class CreateDateStep2_5_MEAL {

//        private static String sign = "test";
    private static String sign = "pro";

    private static int mm=0;

    private static Connection conn = null;
    private static Connection conn07 = null;

    private static  String currMon = DateTimeHelp.dateToStr(new Date() , "yyyymm");
    private static  String lastMon = DateTimeHelp.dateToStr(DateTimeHelp.adjMonReDate(new Date(), -1), "yyyyMM");

    private static ArrayList<String> zq_list = null;

    private static String project = "月账-"+DateTimeHelp.getDateTimeString("yyyy-MM");


    static{
        if(sign.equals("pro")){
            conn = DBConn.getCopyProConn();
            conn07 = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getCopyTestConn();
        }
    }

    public static void main(String[] args) {

//        SQLHelp.truncate(conn,"cld_temp_meal");
//        String create_cld_temp_meal = "insert into cld_temp_meal\n" +
//                "select \n" +
//                "/*+ USE_HASH(m u) */  \n" +
//                "a.ACCT_ITEM_TYPE_ID ,\n" +
//                "a.msisdn,\n" +
//                "a.acct_id, \n" +
//                "sum(charge) charge , \n" +
//                "CASE \n" +
//                "WHEN sum(charge) > 0 THEN  '02' --套外\n" +
//                "WHEN sum(charge) = 0 THEN  '01' --套内\n" +
//                "ELSE  '03'\n" +
//                "END MEAL_TYPE\n" +
//                "from cld_temp_data_new_temp02 a  \n" +
//                "group by a.ACCT_ITEM_TYPE_ID ,\n" +
//                "a.msisdn,\n" +
//                "a.acct_id";
//        SQLHelp.exec(conn,create_cld_temp_meal);
//        System.out.println("cld_temp_meal=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_meal"));


        SQLHelp.truncate(conn,"cld_temp_data_meal");
        String create_cld_temp_data4 =
                "  insert into cld_temp_data_meal\n" +
                        "                          select\n" +
                        "                          /*+ USE_HASH(m u) */\n" +
                        "                           t.msisdn,\n" +
                        "                           t.serv_id,\n" +
                        "                           t.prd_inst_id        ,\n" +
                        "                           t.product_offer_id,\n" +
                        "                           t.product_offer_id_new,\n" +
                        "                           t.product_offer_inst_id,\n" +
                        "                           t.acct_id,\n" +
                        "                           t.cust_id             ,\n" +
                        "                           t.crm_acct_id,\n" +
                        "                           t.billing_cycle_id,\n" +
                        "                           t.billing_cycle_id_ori,\n" +
                        "                           t.charge,\n" +
                        "                           t.item_source,\n" +
                        "                           t.biz_type,\n" +
                        "                           t.acct_item_list,\n" +
                        "                           t.acct_item_type_id,\n" +
                        "                           t.acct_item_name,\n" +
                        "                           t.billing_mode,\n" +
                        "                           t.acc_nbr,\n" +
                        "                           t.cdr_key,\n" +
                        "                           t.rome_type,\n" +
                        "                           m.meal_type,\n" +
                        "                           t.busi_type,\n" +
                        "                           t.reserver2,\n" +
                        "                           t.reserver3,\n" +
                        "                           t.reserver4,\n" +
                        "                           t.reserver5\n" +
                        "               from cld_temp_data_new_temp02 t left join cld_temp_meal m \n" +
                        "                            on t.msisdn = m.msisdn\n" +
                        "                          and t.ACCT_ITEM_TYPE_ID= m.ACCT_ITEM_TYPE_ID\n" +
                        "                          and t.acct_id = m.acct_id ";
        SQLHelp.exec(conn,create_cld_temp_data4);
        System.out.println("cld_temp_data_meal=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_data_meal"));



    }

}
