package vip;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 程刘德
 * @version 1.0
 * @Description 十个线程获取数据
 * @date 2020/4/10
 */
public class CreateDateStep1_OCS implements Runnable{

//        private static String sign = "test";
    private static String sign = "pro";

    private static int mm=0;

    private static Connection conn = null;

    private static  String currMon = DateTimeHelp.dateToStr(new Date() , "yyyyMM");


    static{
        if(sign.equals("pro")){
            conn = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getDbusr07TestConn();
        }
    }

    public static void main(String[] args) {

        SQLHelp.dropTable(conn,"CLD_TEMP_DATA_OCS");

        String create_CLD_TEMP_DATA = "create table CLD_TEMP_DATA_OCS\n" +
                "(\n" +
                "  msisdn                VARCHAR2(600),\n" +
                "  serv_id               VARCHAR2(90),\n" +
                "prd_inst_id           VARCHAR2(90),\n" +
                "  product_offer_id      VARCHAR2(30),\n" +
                "  product_offer_inst_id NUMBER(15),\n" +
                "  acct_id               NUMBER(15),\n" +
                "cust_id               VARCHAR2(45),\n" +
                "  crm_acct_id           NUMBER,\n" +
                "  billing_cycle_id      VARCHAR2(40),\n" +
                "  billing_cycle_id_ori  VARCHAR2(40),\n" +
                "  charge                NUMBER,\n" +
                "  item_source           NUMBER,\n" +
                "  biz_type              NUMBER,\n" +
                "  acct_item_list        VARCHAR2(255),\n" +
                "  acct_item_type_id     NUMBER(10),\n" +
                "  acct_item_name        VARCHAR2(1200),\n" +
                "  billing_mode          VARCHAR2(40),\n" +
                "  acc_nbr               VARCHAR2(600),\n" +
                "  cdr_key               VARCHAR2(97),\n" +
                "  rome_type             VARCHAR2(10),\n" +
                "  meal_type             VARCHAR2(40),\n" +
                "  busi_type             VARCHAR2(10),\n" +
                "  reserver2             VARCHAR2(40),\n" +
                "  reserver3             VARCHAR2(40),\n" +
                "  reserver4             VARCHAR2(40),\n" +
                "  reserver5             VARCHAR2(40)\n" +
                ")" ;
        try{
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{create_CLD_TEMP_DATA});
            ProcUtil.callProc(conn,"sql_procedure",  new Object[]{"grant all on CLD_TEMP_DATA_OCS to PUBLIC"});
        }catch (Exception e){
            e.printStackTrace();
        }


        ExecutorService exe = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            exe.execute(new CreateDateStep1_OCS());
        }
        exe.shutdown();
        while (true) {
            if (exe.isTerminated()) {
                System.out.println("所有线程运行结束");
                break;
            }
        }



    }

    @Override
    public void run() {
        int ll = mm++;


        System.out.println("OCS 当前线程====  "+ll);
        //生产
        String insertDate = "  insert into CLD_TEMP_DATA_OCS\n" +
                "select /*+ USE_HASH(m u) */\n" +
                " c.user_id MSISDN,\n" +
                " c.subscr_no SERV_ID,\n" +
                " 11,\n" +
                " --n.prd_inst_id PROD_INST_ID,\n" +
                " c.OFR_ID PRODUCT_OFFER_ID,\n" +
                " c.source_inst_id PRODUCT_OFFER_INST_ID,\n" +
                " c.ACCOUNT_NO ACCT_ID,\n" +
                " 11,\n" +
                " --a.cust_cd CUST_ID,\n" +
                " m.account_no CRM_ACCT_ID,\n" +
                " TO_CHAR(ADD_MONTHS(c.STATEMENT_DATE, -1), 'yyyymm') BILLING_CYCLE_ID,\n" +
                " CASE\n" +
                "   WHEN TO_NUMBER(SUBSTR(c.BILLING_CYCLE_ID, 0, 6)) =\n" +
                "        TO_NUMBER(TO_CHAR(ADD_MONTHS(c.STATEMENT_DATE, -1), 'yyyymm')) THEN\n" +
                "    '0'\n" +
                "   ELSE\n" +
                "    TO_CHAR(TO_NUMBER(SUBSTR(c.BILLING_CYCLE_ID, 0, 6)))\n" +
                " END BILLING_CYCLE_ID_ORI,\n" +
                " nvl(c.amount, 0) CHARGE,\n" +
                " case\n" +
                "   when c.amount > 0 then\n" +
                "    0\n" +
                "   else\n" +
                "    1\n" +
                " end ITEM_SOURCE,\n" +
                " 1 BIZ_TYPE,\n" +
                " nvl((select \"MuLuBianMa\"\n" +
                "       from tmp_acct_item_type_id_to_m\n" +
                "      where acct_item_type_id = t.acct_item_type_id),\n" +
                "     '0') ACCT_ITEM_LIST,\n" +
                " t.acct_item_type_id ACCT_ITEM_TYPE_ID,\n" +
                " t.acct_item_type_name ACCT_ITEM_NAME,\n" +
                "\n" +
                " CASE\n" +
                "   WHEN c.user_bill_id = 0 THEN\n" +
                "    '1'\n" +
                "   WHEN c.user_bill_id = 1 THEN\n" +
                "    '3'\n" +
                "   WHEN c.user_bill_id = 2 THEN\n" +
                "    '3'\n" +
                "   ELSE\n" +
                "    '4'\n" +
                " END BILLING_MODE,\n" +
                " c.user_id ACC_NBR,\n" +
                " c.BILL_REF_NO || '#' || c.BILL_INVOICE_ROW || '#' || c.PRODUCT_LINE_ID AS CDR_KEY,\n" +
                " (select ROME_TYPE\n" +
                "    from tmp_zhengww_1111_peizhi m\n" +
                "   where t.acct_item_type_id = m.acct_item_type_id) ROME_TYPE,\n" +
                " '01' MEAL_TYPE,\n" +
                " (select BUSI_TYPE\n" +
                "    from tmp_zhengww_1111_peizhi m\n" +
                "   where t.acct_item_type_id = m.acct_item_type_id) BUSI_TYPE,\n" +
                " ' ' RESERVER2,\n" +
                " ' ' RESERVER3,\n" +
                " ' ' RESERVER4,\n" +
                " ' ' RESERVER5\n" +
                "  from IAM.ocs_bill_invoice_detail            PARTITION(P_O_BD_"+currMon+") c, /*?      */\n" +
                "       IAM.ocs_bill_invoice                  PARTITION(P_O_BI_"+currMon+") M, /*?      */\n" +
                "       TMP_TB_BIL_ACCT_ITEM_TYPE_GPB T \n" +
                " where M.BILL_REF_NO = c.BILL_REF_NO\n" +
                "   AND c.SUB_TYPE_CODE = T.ACCT_ITEM_TYPE_ID\n" +
                "   and c.account_no = m.account_no\n" +
                "   AND c.user_id not LIKE '%%|%%'\n" +
                "   AND m.status = 1\n" +
                "   and mod(m.account_no,10)="+ll+"\n" +
                "  -- and rownum <100\n";


            //  测试
            String testSQL="   --向临时表中存数据\n" +
                    "  insert into CLD_TEMP_DATA_OCS\n" +
                    "select /*+ USE_HASH(m u) */\n" +
                    " c.user_id MSISDN,\n" +
                    " c.subscr_no SERV_ID,\n" +
                    " 11,\n" +
                    " --n.prd_inst_id PROD_INST_ID,\n" +
                    " c.OFR_ID PRODUCT_OFFER_ID,\n" +
                    " c.source_inst_id PRODUCT_OFFER_INST_ID,\n" +
                    " c.ACCOUNT_NO ACCT_ID,\n" +
                    " 11,\n" +
                    " --a.cust_cd CUST_ID,\n" +
                    " m.account_no CRM_ACCT_ID,\n" +
                    " TO_CHAR(ADD_MONTHS(c.STATEMENT_DATE, -1), 'yyyymm') BILLING_CYCLE_ID,\n" +
                    " CASE\n" +
                    "   WHEN TO_NUMBER(SUBSTR(c.BILLING_CYCLE_ID, 0, 6)) =\n" +
                    "        TO_NUMBER(TO_CHAR(ADD_MONTHS(c.STATEMENT_DATE, -1), 'yyyymm')) THEN\n" +
                    "    '0'\n" +
                    "   ELSE\n" +
                    "    TO_CHAR(TO_NUMBER(SUBSTR(c.BILLING_CYCLE_ID, 0, 6)))\n" +
                    " END BILLING_CYCLE_ID_ORI,\n" +
                    " nvl(c.amount, 0) CHARGE,\n" +
                    " case\n" +
                    "   when c.amount > 0 then\n" +
                    "    0\n" +
                    "   else\n" +
                    "    1\n" +
                    " end ITEM_SOURCE,\n" +
                    " 1 BIZ_TYPE,\n" +
                    " nvl((select \"MuLuBianMa\"\n" +
                    "       from tmp_acct_item_type_id_to_m\n" +
                    "      where acct_item_type_id = t.acct_item_type_id),\n" +
                    "     '0') ACCT_ITEM_LIST,\n" +
                    " t.acct_item_type_id ACCT_ITEM_TYPE_ID,\n" +
                    " t.acct_item_type_name ACCT_ITEM_NAME,\n" +
                    "\n" +
                    " CASE\n" +
                    "   WHEN c.user_bill_id = 0 THEN\n" +
                    "    '1'\n" +
                    "   WHEN c.user_bill_id = 1 THEN\n" +
                    "    '3'\n" +
                    "   WHEN c.user_bill_id = 2 THEN\n" +
                    "    '3'\n" +
                    "   ELSE\n" +
                    "    '4'\n" +
                    " END BILLING_MODE,\n" +
                    " c.user_id ACC_NBR,\n" +
                    " c.BILL_REF_NO || '#' || c.BILL_INVOICE_ROW || '#' || c.PRODUCT_LINE_ID AS CDR_KEY,\n" +
                    " (select ROME_TYPE\n" +
                    "    from tmp_zhengww_1111_peizhi m\n" +
                    "   where t.acct_item_type_id = m.acct_item_type_id) ROME_TYPE,\n" +
                    " '01' MEAL_TYPE,\n" +
                    " (select BUSI_TYPE\n" +
                    "    from tmp_zhengww_1111_peizhi m\n" +
                    "   where t.acct_item_type_id = m.acct_item_type_id) BUSI_TYPE,\n" +
                    " ' ' RESERVER2,\n" +
                    " ' ' RESERVER3,\n" +
                    " ' ' RESERVER4,\n" +
                    " ' ' RESERVER5\n" +
                    "  from iam.ocs_bill_invoice_detail@zwdb_prod         c, \n" +
                    "       iam.ocs_bill_invoice@zwdb_prod                 M, \n" +
                    "       dbusr07.TMP_TB_BIL_ACCT_ITEM_TYPE_GPB@zwdb_prod T \n" +
                    " where M.BILL_REF_NO = c.BILL_REF_NO\n" +
                    "   AND c.SUB_TYPE_CODE = T.ACCT_ITEM_TYPE_ID\n" +
                    "   and c.account_no = m.account_no\n" +
                    "   AND c.user_id not LIKE '%%|%%'\n" +
                    "   AND m.status = 1\n" +
                    "   and mod(m.account_no,10)="+ll+"\n" +
                    "   and rownum <10000";

        try{
            if(sign.equals("pro")){
                SQLHelp.insertSQL(conn , insertDate , false);
            }else {
                SQLHelp.insertSQL(conn , testSQL , false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
