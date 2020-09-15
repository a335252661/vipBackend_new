package vip;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description 对生成的数据进行处理，生成最终数据
 * @date 2020/4/10
 */
public class CreateDateStep3_ALL {

//        private static String sign = "test";
    private static String sign = "pro";

    private static int mm=0;

    private static Connection conn = null;

    private static  String currMon = DateTimeHelp.dateToStr(new Date() , "yyyymm");
    private static  String lastMon = DateTimeHelp.dateToStr(DateTimeHelp.adjMonReDate(new Date(), -1), "yyyyMM");

    private static ArrayList<String> zq_list = null;




    static{
        if(sign.equals("pro")){
            conn = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getDbusr07TestConn();
        }
    }

    public static void main(String[] args) {
        //表存在则删除    cld_serv_acc
        SQLHelp.dropTable(conn,"cld_serv_acc_new");
        String create_cld_serv_acc = "create table  cld_serv_acc_new tablespace CUST_USER \n" +
                " as\n" +
                " select \n" +
                "/*+ USE_HASH(m u) */ \n" +
                "msisdn,  serv_id , acct_id,sum(charge)charge \n" +
                "from cld_temp_data_new \n" +
                "group by msisdn,serv_id , acct_id";


        try{
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{create_cld_serv_acc});

            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index serv_acc_new_01 on cld_serv_acc_new(msisdn)"});
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index serv_acc_new_02 on cld_serv_acc_new(acct_id)"});


        }catch (Exception e){
            e.printStackTrace();
        }

//        SQLHelp.dropTable(conn,"account");
//        String cc="create table account as select * from cus.account@CRM_COPY";
//        try{
//            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{cc});
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("备份结束");


        SQLHelp.dropTable(conn,"cld_all_data_new");
        String create_cld_all_data = "  create table cld_all_data_new as\n" +
                " select  /*+ USE_HASH(m u) */  \n" +
                "   a.msisdn , a.serv_id, a.acct_id ,a.charge\n" +
                "  ,b.prod_inst_id as msisdn_new\n" +
                "  ,c.acct_id as acct_id_new\n" +
                "  ,c.cust_id as cust_id_new\n" +
                "   from cld_serv_acc_new a left join  cus.prod_inst@CRM_COPY b  \n" +
                " on a.msisdn = b.acc_num\n" +
                " left join  account c  on c.acct_cd = to_char(a.acct_id)";
        try{
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{create_cld_all_data});
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index new_msis_01 on cld_all_data_new(msisdn)"});
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index new_serv_id_01 on cld_all_data_new(serv_id)"});
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index new_acct_01 on cld_all_data_new(acct_id)"});
        }catch (Exception e){
            e.printStackTrace();
        }



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
        System.out.println("cld_all_data_new=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_all_data_new"));




        SQLHelp.dropTable(conn,"cld_temp_meal");
        String create_cld_temp_meal = "create table  cld_temp_meal tablespace CUST_USER as\n" +
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
            try{
                ProcUtil.callProc(conn,"sql_procedure",  new Object[]{create_cld_temp_meal});

                ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index cld_temp_meal_01 on cld_temp_meal(msisdn)"});
                ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index cld_temp_meal_02 on cld_temp_meal(ACCT_ITEM_TYPE_ID)"});
                ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"create index cld_temp_meal_03 on cld_temp_meal(acct_id)"});

            }catch (Exception e){
                e.printStackTrace();
        }
        System.out.println("cld_temp_meal=="+SQLHelp.querySQLReturnList2(conn,"select count(1),sum(charge) from cld_temp_meal"));


        SQLHelp.dropTable(conn,"cld_temp_data_meal");
        String create_cld_temp_data4 =
                "  create table cld_temp_data_meal as\n" +
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
        SQLHelp.dropTable(conn,"CLD_TEMP_DATA_ALL_new");

        //联合查询，将cust_id 和prdinstid 存入
        String sql = "   create table cld_temp_data_all_new\n" +
                "  as\n" +
                "                select\n" +
                "                /*+ USE_HASH(m u) */\n" +
                "                 t.msisdn,\n" +
//                "                 c.msisdn_new,\n" +
                "                 t.serv_id,\n" +
//                "                 c.msisdn_new serv_id_new,\n" +
//                "                 --t.prod_inst_id        ,\n" +
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
        SQLHelp.insertSQL(conn,sql);

        //
        try{
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"grant all on CLD_TEMP_DATA_ALL_new to public"});
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("===================数据统计======================");

//        CreateDateStep3_ALL step2 = new CreateDateStep3_ALL();
//        step2.queryZQ();
//
//        ArrayList<String> cld_temp_data_new_list2 = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from cld_temp_data_new");
//        System.out.println("所有数据汇入完成");
//        System.out.println("最终cld_temp_data_new ： "+cld_temp_data_new_list2);
//
//        ArrayList<String> CLD_TEMP_DATA_ALL_new = SQLHelp.querySQLReturnList2(conn, "select count(1) coun , sum(charge) charge from CLD_TEMP_DATA_ALL_new");
//        System.out.println("最终CCLD_TEMP_DATA_ALL_NEW ： "+CLD_TEMP_DATA_ALL_new);


        System.out.println("=================================================");

    }


    public void queryZQ() {

        zq_list = SQLHelp.querySQLReturnList2(conn,
                "select count(1) coun , sum(charge) charge from ASSE_HTSR.ASSE_HTSR_COLLECT_2@icstax where billing_cycle_id='"+lastMon+"'");
        if(zq_list.get(0).equals("0")){
            //政企数据未查询到，等待一小时后再运行
            try {
                Thread.sleep(3600000);
                this.queryZQ();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {

            System.out.println("基础政企ASSE_HTSR_COLLECT_2 ： "+zq_list);


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
                    " from ASSE_HTSR.ASSE_HTSR_COLLECT_2@icstax a where billing_cycle_id='"+lastMon+"'";
            SQLHelp.insertSQL(conn,insert);
            System.out.println("政企数据插入CLD_TEMP_DATA_ALL_new完成");
        }

    }

}
