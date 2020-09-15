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
 * @Description  当前月份6  获取上一个月份分区5的数据  其中账期应该为当前月份6
 * @date 2020/4/10
 */
public class CreateDateStep0_LAST {

//        private static String sign = "test";
    private static String sign = "pro";

    private static int mm=0;

    private static Connection conn = null;

    private static  String lastMon = DateTimeHelp.dateToStr(DateTimeHelp.adjMonReDate(new Date(), -1), "yyyyMM");



    static{
        if(sign.equals("pro")){
            conn = DBConn.getDbusr07ProConn();
        }else {
            conn = DBConn.getDbusr07TestConn();
        }
    }

    public static void main(String[] args) {

        SQLHelp.dropTable(conn,"CLD_TEMP_DATA_LAST");


        //生产
        String insertDate = "  create table  CLD_TEMP_DATA_LAST  as  \n" +
                "select /*+ USE_HASH(m u) */\n" +
                " c.user_id MSISDN,\n" +
                " c.subscr_no SERV_ID,\n" +
                " 11  PRD_INST_ID,\n" +
                " c.OFR_ID PRODUCT_OFFER_ID,\n" +
                " c.source_inst_id PRODUCT_OFFER_INST_ID,\n" +
                " c.ACCOUNT_NO ACCT_ID,\n" +
                " 11 as CUST_ID,\n" +
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
                "  from BILL_INVOICE_DETAIL           PARTITION(P_BID_"+lastMon+") c, \n" +
                "       bill_invoice                  PARTITION(p_bi_"+lastMon+") M, \n" +
                "       TMP_TB_BIL_ACCT_ITEM_TYPE_GPB T \n" +
                " where M.BILL_REF_NO = c.BILL_REF_NO\n" +
                "   AND c.SUB_TYPE_CODE = T.ACCT_ITEM_TYPE_ID\n" +
                "   and c.account_no = m.account_no\n" +
                "   AND c.user_id not LIKE '%%|%%'\n" +
                "   AND m.status = 1\n" +
                "   AND c.statement_date = trunc(sysdate,'MM')\n" +
                "   --and rownum <100\n";

        try{
            if(sign.equals("pro")){
                SQLHelp.insertSQL(conn , insertDate);
            }else {
//                SQLHelp.insertSQL(conn , testSQL);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
