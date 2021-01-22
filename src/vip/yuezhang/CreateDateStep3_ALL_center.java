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
        //表存在则清空数据
        SQLHelp.truncate(conn,"cld_serv_acc_new");
        String create_cld_serv_acc = "insert into  cld_serv_acc_new  \n" +
                " select \n" +
                "/*+ USE_HASH(m u) */ \n" +
                "msisdn,  serv_id , acct_id,sum(charge)charge \n" +
                "from cld_temp_data_new \n" +
                "group by msisdn,serv_id , acct_id";
        SQLHelp.insertSQL(conn,create_cld_serv_acc);
        LogHelp.insertCldLogsPro(conn,project,"cld_serv_acc_new 创建成功" ,true);



        SQLHelp.truncate(conn,"cld_all_data_new");
        String create_cld_all_data = "  insert into  cld_all_data_new  \n" +
                " select  /*+ USE_HASH(m u) */  \n" +
                "   a.msisdn , a.serv_id, a.acct_id ,a.charge\n" +
                "  ,b.prod_inst_id as msisdn_new\n" +
                "  ,c.acct_id as acct_id_new\n" +
                "  ,c.cust_id as cust_id_new\n" +
                "   from cld_serv_acc_new a left join  cus.prod_inst@CRM_COPY b  \n" +
                " on a.msisdn = b.acc_num\n" +
                " left join  account c  on c.acct_cd = to_char(a.acct_id)";
        SQLHelp.insertSQL(conn,create_cld_all_data);

        LogHelp.insertCldLogsPro(conn,project, "cld_all_data_new 创建成功",true);

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
        SQLHelp.deleteSQL(conn,delete);
        String mm = "cld_all_data_new=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_all_data_new");
        LogHelp.insertCldLogsPro(conn,project, mm,true);



        SQLHelp.truncate(conn,"cld_temp_meal");
        String create_cld_temp_meal = "insert into cld_temp_meal\n" +
                "select \n" +
                "/*+ USE_HASH(m u) */  \n" +
                "a.ACCT_ITEM_TYPE_ID ,\n" +
                "a.msisdn,\n" +
                "a.acct_id, \n" +
                "sum(charge) charge , \n" +
                "CASE \n" +
                "WHEN sum(charge) > 0 THEN  '02' --套外\n" +
                "WHEN sum(charge) = 0 THEN  '01' --套内\n" +
                "ELSE  '03'\n" +
                "END MEAL_TYPE\n" +
                "from cld_temp_data_new a \n" +
                "group by a.ACCT_ITEM_TYPE_ID ,\n" +
                "a.msisdn,\n" +
                "a.acct_id";
        SQLHelp.exec(conn,create_cld_temp_meal);
        System.out.println("cld_temp_meal=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_meal"));


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
                        "               from cld_temp_data_new t left join cld_temp_meal m \n" +
                        "                            on t.msisdn = m.msisdn\n" +
                        "                          and t.ACCT_ITEM_TYPE_ID= m.ACCT_ITEM_TYPE_ID\n" +
                        "                          and t.acct_id = m.acct_id";
        try{
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{create_cld_temp_data4});
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("cld_temp_data_meal=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_data_meal"));


        //创建最终表
        SQLHelp.truncate(conn,"CLD_TEMP_DATA_ALL_new");
        //联合查询，将cust_id 和prdinstid 存入
        String sql = "   insert into CLD_TEMP_DATA_ALL_new\n" +
                "                select\n" +
                "                /*+ USE_HASH(m u) */\n" +
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
                "from cld_temp_data_meal  t \n" +
                "left join cld_all_data_new c \n" +
                "on \n" +
                "t.serv_id = c.serv_id\n" +
                " and   t.acct_id = c.acct_id\n" +
                "and   t.msisdn = c.msisdn";
        SQLHelp.exec(conn,sql);
        SQLHelp.exec(conn,"grant all on CLD_TEMP_DATA_ALL_new to public");

        ArrayList<String> quer = SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_data_all_new");
        LogHelp.insertCldLogsPro(conn,project,"cld_temp_data_all_new=="+quer,true);



        //将负的金额cld_temp_data_center 放入 cld_temp_data_all_new
        String all = "insert into cld_temp_data_all_new select * from cld_temp_data_center";
        SQLHelp.exec(conn , all);
        ArrayList<String> quer2 = SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_data_center");
        LogHelp.insertCldLogsPro(conn,project,"cld_temp_data_center=="+quer2,true);


        ArrayList<String> quer3 = SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_data_all_new");
        LogHelp.insertCldLogsPro(conn,project,"将负的金额cld_temp_data_center 放入 cld_temp_data_all_new",true);
        LogHelp.insertCldLogsPro(conn,project,"cld_temp_data_all_new=="+quer3,true);



        LogHelp.insertCldLogsPro(conn,project,"备份到07开始开始",true);
        System.out.println("=================================================");
        SQLHelp.dropTable(conn07,"cld_temp_data_all_new");
        String cp01 = "create table cld_temp_data_all_new as\n" +
                "select * from cld_temp_data_all_new@to_iamzw";
        try{
            LogHelp.insertCldLogsPro(conn,project,
                    "备份到07开始开始"
                    ,true);
            ProcUtil.callProc(conn07,"sql_procedure",  new Object[]{cp01});
            ProcUtil.callProc(conn07,"sql_procedure",  new Object[]{"grant all on cld_temp_data_all_new to public"});
            LogHelp.insertCldLogsPro(conn,project,
                    "备份到07开始结束"
                    ,true);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("=================================================");



    }


    public void queryZQ() {
        String cyc = "select count(1) coun , sum(charge) charge from ASSE_HTSR.ASSE_HTSR_COLLECT_2@icstax where billing_cycle_id='"+lastMon+"'";

        if(sign.equals("test")){
            cyc = "select count(1) coun , sum(charge) charge from dbusr07.ASSE_HTSR_COLLECT_2@zwdb_prod";
        }
        zq_list = SQLHelp.querySQLReturnList2(conn07, cyc);
        if(zq_list.get(0).equals("0")){
            //政企数据未查询到，等待一小时后再运行

            LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    "政企数据未查询到，等待一小时后再运行"
                    ,true);

            try {
                Thread.sleep(3600000);
                this.queryZQ();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            //@iamzwdb
            //表存在则删除    cld_serv_acc
            SQLHelp.dropTable(conn07,"ASSE_HTSR_COLLECT_2");
            String create_ASSE_HTSR_COLLECT_2 = "create table ASSE_HTSR_COLLECT_2 as\n" +
                    "select * from ASSE_HTSR.ASSE_HTSR_COLLECT_2@icstax";
            try{
                ProcUtil.callProc(conn07,"sql_procedure",  new Object[]{create_ASSE_HTSR_COLLECT_2});
                ProcUtil.callProc(conn07,"sql_procedure",  new Object[]{"grant all on ASSE_HTSR_COLLECT_2 to public"});
            }catch (Exception e){
                e.printStackTrace();
            }





            LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    "基础政企ASSE_HTSR_COLLECT_2 ： "+zq_list
                    ,true);

            //将立即出账数据存入CLD_TEMP_DATA_ALL_new 数据
            String insert = "insert into CLD_TEMP_DATA_ALL_new select a.MSISDN\n" +
                    ",a.SERV_ID\n" +
                    ",a.PROD_INST_ID\n" +
                    ",a.PRODUCT_OFFER_ID\n" +
                    ",0\n" +
                    ",a.PRODUCT_OFFER_INST_ID\n" +
                    ",a.ACCT_ID\n" +
                    ",0 ACCT_ID_new\n" +
                    ",a.CUST_ID \n" +
                    ",a.CRM_ACCT_ID\n" +
                    ",a.BILLING_CYCLE_ID\n" +
                    ",a.BILLING_CYCLE_ID_ORI\n" +
                    ",a.CHARGE\n" +
                    ",a.ITEM_SOURCE\n" +
                    ",a.BIZ_TYPE\n" +
                    ",a.ACCT_ITEM_LIST\n" +
                    ",a.ACCT_ITEM_TYPE_ID\n" +
                    ",a.ACCT_ITEM_NAME\n" +
                    ",a.BILLING_MODE\n" +
                    ",a.ACC_NBR\n" +
                    ",a.CDR_KEY\n" +
                    ",a.ROME_TYPE\n" +
                    ",a.MEAL_TYPE\n" +
                    ",a.BUSI_TYPE\n" +
                    ",a.RESERVER2\n" +
                    ",a.RESERVER3\n" +
                    ",a.RESERVER4\n" +
                    ",a.RESERVER5\n" +
                    " from DBUSR07.ASSE_HTSR_COLLECT_2@iamzwdb a where billing_cycle_id='"+lastMon+"'";
            if(sign.equals("test")){
                insert = insert.replaceAll("ASSE_HTSR.ASSE_HTSR_COLLECT_2@icstax" , "dbusr07.ASSE_HTSR_COLLECT_2@zwdb_prod");
            }
            SQLHelp.insertSQL(conn,insert);

            LogHelp.insertCldLogsPro(conn,"月账-"+DateTimeHelp.getDateTimeString("yyyy-MM"),
                    "政企数据插入CLD_TEMP_DATA_ALL_new完成"
                    ,true);

        }

    }

}