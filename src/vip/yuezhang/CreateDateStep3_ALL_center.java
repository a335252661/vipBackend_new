package vip.yuezhang;

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
public class CreateDateStep3_ALL_center {

//        private static String sign = "test";
    private static String sign = "pro";

    private static int mm=0;

    private static Connection conn = null;
    private static Connection conn07 = null;

    private static  String currMon = DateTimeHelp.dateToStr(new Date() , "yyyymm");
    private static  String lastMon = DateTimeHelp.dateToStr(DateTimeHelp.adjMonReDate(new Date(), -1), "yyyyMM");

    private static ArrayList<String> zq_list = null;



    private static String project = "月账-"+DateTimeHelp.getDateTimeString("yyyy-MM")+"-月中补开";


    static{
        if(sign.equals("pro")){
            conn = DBConn.getCopyProConn();
            conn07 = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getCopyTestConn();
        }
    }

    public static void main(String[] args) {
        LogHelp.updateNoticeStatus(conn07,99 , 0);

        //表存在则清空数据
        SQLHelp.dropTable(conn07,"cld_serv_acc_new");
        String create_cld_serv_acc = "create table   cld_serv_acc_new  as\n" +
                " select \n" +
                "/*+ USE_HASH(m u) */ \n" +
                "msisdn,  serv_id , acct_id,sum(charge)charge \n" +
                "from cld_temp_data_new \n" +
                "group by msisdn,serv_id , acct_id";
        SQLHelp.insertSQL(conn07,create_cld_serv_acc);
        LogHelp.insertCldLogsPro(conn07,project,"cld_serv_acc_new 创建成功" ,true);
        String index_acc_id = "create index index_acc_id on cld_serv_acc_new(acct_id)";
        SQLHelp.exec(conn07,index_acc_id);



        SQLHelp.dropTable(conn07,"CLD_acct_id");
        String CLD_acct_id = " create table CLD_acct_id as\n" +
                " select  /*+ USE_HASH(a c) */  \n" +
                "   a.msisdn , a.serv_id, a.acct_id ,a.charge\n" +
                "  --,b.prod_inst_id as msisdn_new\n" +
                "  ,c.acct_id as acct_id_new\n" +
                "  ,c.cust_id as cust_id_new\n" +
                "   from CLD_SERV_ACC_NEW a \n" +
                "   --left join  cus.prod_inst@CRM_COPY b  on a.msisdn = b.acc_num\n" +
                " left join  account c  on c.acct_cd = to_char(a.acct_id)  ";
        SQLHelp.exec(conn07 , CLD_acct_id);
        String index_cld_msid = "create index index_cld_msid on CLD_acct_id(msisdn)";
        SQLHelp.exec(conn07,index_cld_msid);


        SQLHelp.dropTable(conn07,"cld_all_data_new");
        String cld_all_data_new = "create table  cld_all_data_new  as \n" +
                "    select  /*+ USE_HASH(a b) */  \n" +
                "    a.msisdn , a.serv_id, a.acct_id ,a.charge ,  b.prod_inst_id as msisdn_new , a.acct_id_new,a.cust_id_new\n" +
                "   from CLD_acct_id a left join  prod_inst_temp b  on a.msisdn = b.acc_num";
        SQLHelp.exec(conn07 , cld_all_data_new);


        //删除 cld_all_data_new 里面重复数据
        String delete = "DELETE\n" +
                "FROM\n" +
                "  cld_all_data_new A\n" +
                "WHERE\n" +
                "  (A.msisdn, A.serv_id , A.acct_id) IN (\n" +
                "    SELECT\n" +
                "      msisdn,\n" +
                "      serv_id,acct_id\n" +
                "    FROM\n" +
                "      cld_all_data_new\n" +
                "    GROUP BY\n" +
                "      msisdn,\n" +
                "      serv_id,acct_id\n" +
                "    HAVING\n" +
                "      COUNT (*) > 1\n" +
                "  )\n" +
                "AND ROWID NOT IN (\n" +
                "  SELECT\n" +
                "    MIN (ROWID)\n" +
                "  FROM\n" +
                "      cld_all_data_new\n" +
                "    GROUP BY\n" +
                "      msisdn,\n" +
                "      serv_id,acct_id\n" +
                "    HAVING\n" +
                "      COUNT (*) > 1\n" +
                ")";
        SQLHelp.deleteSQL(conn07,delete);
        String mm = "cld_all_data_new=="+SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_all_data_new");
        LogHelp.insertCldLogsPro(conn07,project, mm,true);

        SQLHelp.dropTable(conn07,"cld_temp_meal");
        String create_cld_temp_meal = "create table  cld_temp_meal as\n" +
                "select \n" +
                "a.ACCT_ITEM_TYPE_ID ,\n" +
                "a.msisdn,\n" +
                "a.acct_id, \n" +
                "sum(charge) charge , \n" +
                "CASE \n" +
                "WHEN sum(charge) > 0 THEN  '02' --套外\n" +
                "WHEN sum(charge) = 0 THEN  '01' --套内\n" +
                "ELSE  '03'\n" +
                "END MEAL_TYPE\n" +
                "from cld_temp_data_new a  \n" +
                "group by a.ACCT_ITEM_TYPE_ID ,\n" +
                "a.msisdn,\n" +
                "a.acct_id";
        SQLHelp.exec(conn07,create_cld_temp_meal);
        System.out.println("cld_temp_meal=="+SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_temp_meal"));


        SQLHelp.dropTable(conn07,"cld_temp_data_meal");
        String create_cld_temp_data4 =
                " create table  cld_temp_data_meal as \n" +
                        "                          select\n" +
                        "                          /*+ USE_HASH(t m) */\n" +
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
                        "               from cld_temp_data_new t left join cld_temp_meal m \n" +
                        "                            on t.msisdn = m.msisdn\n" +
                        "                          and t.ACCT_ITEM_TYPE_ID= m.ACCT_ITEM_TYPE_ID\n" +
                        "                          and t.acct_id = m.acct_id ";
        SQLHelp.exec(conn07,create_cld_temp_data4);
        System.out.println("cld_temp_data_meal=="+SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_temp_data_meal"));




        //创建最终表
        SQLHelp.dropTable(conn07,"CLD_TEMP_DATA_ALL_new");

        //联合查询，将cust_id 和prdinstid 存入
        String sql = "create table  cld_temp_data_all_new as \n" +
                "                select\n" +
                "                /*+ USE_HASH(t c) */\n" +
                "                 t.msisdn,\n" +
                "                 t.serv_id,\n" +
                "                  c.msisdn_new prd_inst_id,\n" +
                "                 t.product_offer_id,\n" +
                "                 t.product_offer_id_new,\n" +
                "                 t.product_offer_inst_id,\n" +
                "                 t.acct_id,\n" +
                "                 c.acct_id_new,\n" +
                "                 --t.cust_id             ,\n" +
                "                 c.cust_id_new,\n" +
                "                 t.crm_acct_id,\n" +
                "                 t.billing_cycle_id,\n" +
                "                 t.billing_cycle_id_ori,\n" +
                "                 t.charge,\n" +
                "                 t.item_source,\n" +
                "                 t.biz_type,\n" +
                "                 t.acct_item_list,\n" +
                "                 t.acct_item_type_id,\n" +
                "                 t.acct_item_name,\n" +
                "                 t.billing_mode,\n" +
                "                 t.acc_nbr,\n" +
                "                 t.cdr_key,\n" +
                "                 t.rome_type,\n" +
                "                  t.meal_type           ,\n" +
                "                 --m.meal_type,\n" +
                "                 t.busi_type,\n" +
                "                 t.reserver2,\n" +
                "                 t.reserver3,\n" +
                "                 t.reserver4,\n" +
                "                 t.reserver5\n" +
                "from  cld_temp_data_meal  t \n" +
                "left join cld_all_data_new c \n" +
                "on \n" +
                "t.serv_id = c.serv_id\n" +
                " and   t.acct_id = c.acct_id\n" +
                "and   t.msisdn = c.msisdn ";
        SQLHelp.exec(conn07,sql);
        SQLHelp.exec(conn07,"grant all on CLD_TEMP_DATA_ALL_new to public");

        ArrayList<String> quer = SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_temp_data_all_new");
        LogHelp.insertCldLogsPro(conn07,project,"cld_temp_data_all_new=="+quer,true);



        //将负的金额cld_temp_data_center 放入 cld_temp_data_all_new
        String all = "insert into cld_temp_data_all_new select * from cld_temp_data_center";
        SQLHelp.exec(conn07 , all);
        ArrayList<String> quer2 = SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_temp_data_center");
        LogHelp.insertCldLogsPro(conn07,project,"cld_temp_data_center=="+quer2,true);


        ArrayList<String> quer3 = SQLHelp.querySQLReturnList2(conn07,"select count(1),sum(charge) from cld_temp_data_all_new");
        LogHelp.insertCldLogsPro(conn07,project,"将负的金额cld_temp_data_center 放入 cld_temp_data_all_new",true);
        LogHelp.insertCldLogsPro(conn07,project,"cld_temp_data_all_new=="+quer3,true);



        System.out.println("-----------------------------------------------------");
        LogHelp.updateNoticeStatus(conn07,99,1);



    }

}
