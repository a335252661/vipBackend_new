package vip.yuechu07;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description  当前月份6  获取上一个月份分区5的数据  其中账期应该为当前月份6
 * @date 2020/4/10
 */
public class CreateDateStep0_LAST {


    public static void main(String[] args) {
        String MM = DateTimeHelp.dateToStr(new Date(), "MM");
        String lasterMM = DateTimeHelp.dateToStr(DateTimeHelp.getLastMonth(), "MM");

        Connection conn = DBConn.getDbusr07ProConn();

        String insert = "INSERT INTO  CLD_TEMP_DATA_LAST\n" +
                "select /*+ USE_HASH(m u) */\n" +
                " c.user_id MSISDN,\n" +
                " c.subscr_no SERV_ID,\n" +
                " 11 PRD_INST_ID,\n" +
                " --n.prd_inst_id PROD_INST_ID,\n" +
                " c.OFR_ID PRODUCT_OFFER_ID,\n" +
                " nvl( H.EXT_OFFER_ID , H.offer_id ) PRODUCT_OFFER_ID_new,\n" +
                " c.source_inst_id PRODUCT_OFFER_INST_ID,\n" +
                " c.ACCOUNT_NO ACCT_ID,\n" +
                " 11 CUST_ID,\n" +
                " --a.cust_cd CUST_ID,\n" +
                " m.acct_id CRM_ACCT_ID,\n" +
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
                " decode(round(t.JT_ACCT_ITEM_TYPE_ID_BSS3),null,'770202',round (t.JT_ACCT_ITEM_TYPE_ID_BSS3) )  AS ACCT_ITEM_LIST,\n" +
                " decode(t.JT_ACCT_ITEM_TYPE_SEQ_BSS3,null,'1903',t.JT_ACCT_ITEM_TYPE_SEQ_BSS3)  AS ACCT_ITEM_TYPE_ID,\n" +
                " nvl(t.JT_ACCT_ITEM_TYPE_NAME_BSS3 , '政企非品牌-其他-其他') AS ACCT_ITEM_NAME,\n" +
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
                "T.ROME_TYPE AS  ROME_TYPE,\n" +
                " '01' MEAL_TYPE,\n" +
                "T.BUSI_TYPE AS BUSI_TYPE,\n" +
                " ' ' RESERVER2,\n" +
                " ' ' RESERVER3,\n" +
                " ' ' RESERVER4,\n" +
                " ' ' RESERVER5\n" +
                "  from BILL_INVOICE_DETAIL_"+lasterMM+"            c,-- 12月一号  ，使用 BILL_INVOICE_DETAIL_11   bill_invoice_12\n" +
                "       bill_invoice_"+MM+"           M,\n" +
                "       TB_BIL_ACCT_ITEM_TYPE@hss T ,\n" +
                "       ABP_QUERY.TEMPFY_OFFER_REL_1216@hss H\n" +
                " where M.BILL_REF_NO = c.BILL_REF_NO\n" +
                "   AND c.SUB_TYPE_CODE = T.ACCT_ITEM_TYPE_ID\n" +
                "   and c.account_no = m.acct_id\n" +
                "   and c.OFR_ID = H.ofr_id(+)\n" +
                "   AND c.user_id not LIKE '%%|%%'\n" +
                "   AND c.statement_date = trunc(sysdate,'MM')\n" +
                "   AND m.status_cd = 1";

        SQLHelp.insertSQL(conn, insert);
        System.out.println("CLD_TEMP_DATA_LAST 操作结束");

    }

}
