package vip.yuezhang;

import Pro.ProcUtil;
import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/11
 */
public class GetInvoiceData {
    static String misdata = DateTimeHelp.getDateTimeString("yyyyMM");
    static String cld_msisdn = "CLD_MSISDN_"+misdata;

    static String BILL_INVOICE_DETAIL = "BILL_INVOICE_DETAIL_"+ DateTimeHelp.getDateTimeString("MM");
    static String BILL_INVOICE = "BILL_INVOICE_"+DateTimeHelp.getDateTimeString("MM");
    //            private static String sign = "test";
    private static String sign = "pro";
    //数据库连接
    private static Connection conncp = null;
    static{
        if(sign.equals("pro")){
            conncp = DBConn.getCopyProConn();
        }else {
            conncp = DBConn.getCopyTestConn();
        }
    }
    public static void main(String[] args) {

        //当前月第一天
        String yyyyMMdd = DateTimeHelp.dateToStr(DateTimeHelp.getMonthFirstDay(), "yyyyMMdd");
        String updatetime = DateTimeHelp.getDateTimeString("yyyy/MM")+"/04";

        //表是否存在  CLD_MSISDN_202101
        // cld_temp_data_all_new
        Boolean isExist = SQLHelp.isExistTable(conncp, cld_msisdn);
        if(!isExist){ //不存在则新建
            String msisdn = " CREATE TABLE "+cld_msisdn+" AS\n" +
                    " SELECT DISTINCT MSISDN,acct_id FROM cld_temp_data_all_new";
            SQLHelp.exec(conncp,msisdn);
        }


        //-3
        SQLHelp.truncate(conncp,"cld_msisdn_center");
        String daya3="insert into cld_msisdn_center \n" +
                "select distinct a.msisdn from "+cld_msisdn+" a,"+BILL_INVOICE+" b where\n" +
                "a.acct_id= b.acct_id and\n" +
                " b.billing_cycle_id='"+yyyyMMdd+"' and b.status_cd=-3 and to_char(b.UPDATE_DATE,'yyyy/mm/dd')>='"+updatetime+"'\n" +
                " ";
        SQLHelp.exec(conncp,daya3);

        //修改为负的金额
        SQLHelp.truncate(conncp,"cld_temp_data_center");
        String query = "insert into cld_temp_data_center  select a.MSISDN, \n" +
                "a.SERV_ID, \n" +
                "a.PRD_INST_ID, \n" +
                "a.PRODUCT_OFFER_ID, \n" +
                "a.PRODUCT_OFFER_ID_NEW, \n" +
                "a.PRODUCT_OFFER_INST_ID, \n" +
                "a.ACCT_ID, \n" +
                "a.ACCT_ID_NEW, \n" +
                "a.CUST_ID_NEW, \n" +
                "a.CRM_ACCT_ID, \n" +
                "a.BILLING_CYCLE_ID, \n" +
                "a.BILLING_CYCLE_ID_ORI, \n" +
                "(a.CHARGE*(-1)) as  CHARGE, \n" +
                "a.ITEM_SOURCE, \n" +
                "a.BIZ_TYPE, \n" +
                "a.ACCT_ITEM_LIST, \n" +
                "a.ACCT_ITEM_TYPE_ID, \n" +
                "a.ACCT_ITEM_NAME, \n" +
                "a.BILLING_MODE, \n" +
                "a.ACC_NBR, \n" +
                "a.CDR_KEY, \n" +
                "a.ROME_TYPE, \n" +
                "a.MEAL_TYPE, \n" +
                "a.BUSI_TYPE, \n" +
                "a.RESERVER2, \n" +
                "a.RESERVER3, \n" +
                "a.RESERVER4, \n" +
                "a.RESERVER5   from cld_temp_data_all_new a where a.msisdn in (select * from cld_msisdn_center)";
        SQLHelp.exec(conncp,query);


        /**
         * 第二部分
         */
        //删除
        SQLHelp.truncate(conncp,"CLD_TEMP_DATA_NEW");
        String center="  insert into CLD_TEMP_DATA_NEW\n" +
                "select /*+ USE_HASH(m u) */\n" +
                " c.user_id MSISDN,\n" +
                " c.subscr_no SERV_ID,\n" +
                " 11,\n" +
                " --n.prd_inst_id PROD_INST_ID,\n" +
                " c.OFR_ID PRODUCT_OFFER_ID,\n" +
                " nvl( H.EXT_OFFER_ID , H.offer_id ) PRODUCT_OFFER_ID_new,\n" +
                " c.source_inst_id PRODUCT_OFFER_INST_ID,\n" +
                " c.ACCOUNT_NO ACCT_ID,\n" +
                " 11,\n" +
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
                "  from "+BILL_INVOICE_DETAIL+"           c,--十一月抛数据就是用十一月分区  P_BID_202011\n" +
                "       -- bill_invoice之前只有预付费数据 现在 包含（预付费和付费数据）\n" +
                "       "+BILL_INVOICE+"                M,\n" +
                "       TB_BIL_ACCT_ITEM_TYPE@hss T ,\n" +
                "       ABP_QUERY.TEMPFY_OFFER_REL_1216@hss H\n" +
                " where M.BILL_REF_NO = c.BILL_REF_NO\n" +
                "   AND c.SUB_TYPE_CODE = T.ACCT_ITEM_TYPE_ID\n" +
                "   and c.account_no = m.acct_id\n" +
                "   and c.OFR_ID = H.ofr_id(+)\n" +
                "   AND c.user_id not LIKE '%%|%%'\n" +
                "   AND m.status_cd = 1\n" +
                "   and to_char(M.create_date,'yyyy/mm/dd')>='"+updatetime+"'";
        SQLHelp.exec(conncp,center);
    }
}
