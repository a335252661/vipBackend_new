package vip.ai;

import helps.DateTimeHelp;
import helps.SQLHelp;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.DBConn;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/8
 */
public class BatchCollBackstage implements Job {

    public void fun() {

        DateTimeHelp.start();

        int type = 0;

        Connection collConn = DBConn.getColl04Conn();
//        Connection collConn =DBConn.getCollTestConn();
//        Connection collConn = ConnDb.connCollDb();
        //获取正在生成清单的数据
        String query = " select batch_seq , ai_type , min_balance , max_balance , bureau_no" +
                ",STATEMENT_DATE  , csr_file from coll_ai_crontrol_config a" +
                " where process_flag=2";
        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(collConn, query);


        //防止每小时程序运行 读到上次未运行完成的程序
        String update_process_flag = "update coll_ai_crontrol_config set process_flag=9 where process_flag=2";
        SQLHelp.updateSQL(collConn,update_process_flag);

        String BATCH_SEQ = "";
        String AI_TYPE = "";
        String MIN_BALANCE = "";
        String MAX_BALANCE = "";
        String BUREAU_NO = "";
        String STATEMENT_DATE = "";
        String CSR_FILE = "";
        for (LinkedHashMap<String, Object> maps : linkedHashMaps) {
            BATCH_SEQ = maps.get("BATCH_SEQ").toString();
            AI_TYPE = maps.get("AI_TYPE").toString();
            MIN_BALANCE = maps.get("MIN_BALANCE").toString();
            MAX_BALANCE = maps.get("MAX_BALANCE").toString();
            BUREAU_NO = maps.get("BUREAU_NO").toString();
            CSR_FILE = maps.get("CSR_FILE").toString();
            STATEMENT_DATE = maps.get("STATEMENT_DATE").toString().substring(0, 10);


            /*客户类型*/
            String query3 = "select * from coll_ai_multiple_config where batch_seq ='" + BATCH_SEQ + "' and type='CLASS'\n ";
            String calss_list = "";
            List<LinkedHashMap<String, Object>> linkedHashMaps1 = SQLHelp.querySQLReturnList(collConn, query3);
            for (LinkedHashMap<String, Object> map : linkedHashMaps1) {
                String CLASS_ID = map.get("TYPE_CODE").toString();
                calss_list = calss_list + "'" + CLASS_ID + "'" + ",";
            }
            calss_list = calss_list.substring(0, calss_list.length() - 1);


            /*信用度*/
            String query4 = "select * from coll_ai_multiple_config where batch_seq ='" + BATCH_SEQ + "' and type='CREDIT'\n ";
            String great_list = "";
            List<LinkedHashMap<String, Object>> linkedHashMaps2 = SQLHelp.querySQLReturnList(collConn, query4);
            for (LinkedHashMap<String, Object> map : linkedHashMaps2) {
                String CLASS_ID = map.get("TYPE_CODE").toString();
                great_list = great_list + "'" + CLASS_ID + "'" + ",";
            }
            great_list = great_list.substring(0, great_list.length() - 1);


            if (AI_TYPE.equals("1")) {//月初
                type = 12;
            } else if (AI_TYPE.equals("30")) {
                type = 11;
            }

            String mm = " select b.account_no,\n" +
                    "             b.bill_lname, --账户名称\n" +
                    "             b.contact_phone2, --催欠联系人\n" +
                    "             a.main_devno， --主设备号\n" +
                    "             b.contact1_phone, ---常用联系人\n" +
                    "             b.ACCOUNT_CLASS, --客户类型       \n" +
                    "             b.BUREAU_NO, --区局\n" +
                    "             b.bill_address, ---账户地址\n" +
                    "             a.CREDIT_WORTH --信用度\n" +
                    "        from coll_accounts      a,\n" +
                    "             server_lookup_need b,\n" +
                    "             coll_invoices      c\n" +
                    "       where a.account_no = b.account_no\n" +
                    "         AND a.closed_date is null\n" +
                    "         and c.statement_date = to_date('" + STATEMENT_DATE + "', 'yyyy/mm/dd')\n" +
                    "         and c.balance_due < '" + MAX_BALANCE + "'\n" +
                    "         and c.balance_due > '" + MIN_BALANCE + "'\n" +
                    "         and c.invoice_status = 0\n" +
                    "         and b.account_class in ( " + calss_list + " )\n" +
                    "            \n" +
                    "         and a.credit_worth in (  " + great_list + "   )\n" +
                    "         and b.BUREAU_NO = '" + BUREAU_NO + "'\n" +
                    "            \n" +
                    "         and b.vip_code not in (2, 3, 4, 7)\n" +
                    "         and a.account_no = c.account_no";


            List<LinkedHashMap<String, Object>> linkedHashMaps3 = SQLHelp.querySQLReturnList(collConn, mm);
            for (LinkedHashMap<String, Object> map : linkedHashMaps3) {
                String account_no = map.get("ACCOUNT_NO").toString();
                String call = "call COLL_IVR_INNER_PKG.COLL_TMP_IVR_PC_INFO('" + account_no + "','" + type + "','" + CSR_FILE + "','" + BATCH_SEQ + "','" + 10 + "')";
                SQLHelp.call(collConn, call);
            }


            ///////////////////////////////////////////////////////////////////////////////

            DateTimeHelp.end();
            //獲取插入的pc表总记录数
            String pccount = "select count(*) FIELD from coll_tmp_ivr_pc  where status=10 and ai_seq ='" + BATCH_SEQ + "'";
            String count = SQLHelp.querySQLReturnField(collConn, pccount);
//
//            // pc 表插入成功 ，修改ai表状态为1 ， 表示清单生成完成 , 已经插入pc表总记录
//
            if(Integer.parseInt(count)>0){
                String upda = "update coll_ai_crontrol_config set process_flag = '1',create_cnt='" + count + "' where batch_seq=" + BATCH_SEQ;
                SQLHelp.updateSQL(collConn, upda);
            }else {
                System.out.println("插入的pc表总记录数為0");
            }


        }


        /**
         * 获取已经生成文件已经推送的数据，跟新记录条数
         */
        String selectpc = "select * from coll_tmp_ivr_pc  where jtflag=1 and ai_seq is not null";
        List<LinkedHashMap<String, Object>> linkedHashMaps1 = SQLHelp.querySQLReturnList(collConn, selectpc);
        HashMap<String, String> map = new HashMap<String, String>();
        if (linkedHashMaps1.size() > 0) {
            for (LinkedHashMap<String, Object> pcmap : linkedHashMaps1) {
                String ai_seq = pcmap.get("AI_SEQ").toString();

                String ll = "   select  count(*) FIELD\n" +
                        "  from coll_tmp_ivr_pc\n" +
                        " where jtflag=1\n" +
                        "   and ai_seq ='" + ai_seq + "'";

                String count = SQLHelp.querySQLReturnField(collConn, ll);

                if (map.keySet().contains(ai_seq)) {
                    continue;
                } else {
                    map.put(ai_seq, count);
                }


            }
            if (map.keySet().size() > 0) {
                for (String key : map.keySet()) {
                    String counts = map.get(key);
                    //已经提交
                    String upd = "update coll_ai_crontrol_config set commit_cnt='" + counts + "' " +
                            ", process_flag = '5'  where batch_seq ='" + key + "'";
                    SQLHelp.updateSQL(collConn, upd);
                }
            }

        }



        /**
         *  跟新状态为外呼完毕状态
         *  查询是否下发文件入库完毕
         */
        String coll_se="select * from coll_ai_crontrol_config where process_flag = '5' ";
        List<LinkedHashMap<String, Object>> coll_list = SQLHelp.querySQLReturnList(collConn, coll_se);
        for(LinkedHashMap<String, Object> collmap : coll_list){
            String BATCH_SEQ5 = collmap.get("BATCH_SEQ").toString();

            String coll_pc="select SEQ_DEMO  AS FIELD from coll_tmp_ivr_pc where ai_seq = "+BATCH_SEQ5 +" and rownum=1";
            String SEQ_DEMO = SQLHelp.querySQLReturnField(collConn, coll_pc);


            String coll_10000=" select AISTATUS  AS FIELD from coll_10000_ivr_invoices where seq='"+SEQ_DEMO+"' AND ROWNUM=1";
            String AISTATUS = SQLHelp.querySQLReturnField(collConn, coll_10000);

            if(AISTATUS.equals("2")){
                //已经提交
                String upd = "update coll_ai_crontrol_config set process_flag = '6'  where batch_seq ='" + BATCH_SEQ5 + "' and process_flag = '5' ";
                SQLHelp.updateSQL(collConn, upd);
            }

        }


    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        fun();
    }

    public static void main(String[] args) {
      new BatchCollBackstage().fun();
    }

}
