package vip.stopRevive;

import Pro.ProcUtil;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/14
 *测试
 * #syschronizedAbb=http://10.145.205.69:9011/Ducc/stopRevive
 * #生产
 * syschronizedAbb=http://10.145.196.13:9011/Ducc/stopRevive
 * telnet 10.145.196.13  9011
 *
 */
public class SynchronizedABP implements Runnable {
//    private static String sign = "test";
        private static String sign = "pro";
    //数据库连接
    private static Connection iamConn = null;

    //首次执行延迟时间(秒)
    private long initialDelay=0;
    //执行周期(秒)
    private long period=7200;

    //最后一次程序是否执行完成 true为执行完毕 false为还在执行
    private volatile boolean isFinish = true;

    private static  SynchronizedABP synchronizedABP = new SynchronizedABP();

    static {
        if (sign.equals("pro")) {
            iamConn = DBConn.getColl04Conn();
        } else {
            iamConn = DBConn.getCollTestConn();
        }
    }

    public void startService() {
        ScheduledExecutorService scheduler=Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this, this.initialDelay, this.period, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        synchronizedABP.startService();
    }

    @Override
    public void run() {
        synchronizedABP.execute();
    }


    public void execute() {
        if (isFinish) {
            isFinish = false;
            SQLHelp.dropTable(iamConn, "cld_coll2");
            String create_cld_all_data = "create table cld_coll2 as\n" +
                    "select suspended_seq,account_no ,service_external_id,send_flag , process_result,case when\n" +
                    "handle_type=1 and suspended_type=11  then '单停'\n" +
                    "when handle_type=2 and suspended_type=11  then '双停' \n" +
                    "  when handle_type=3 and suspended_type=10  then '虚拟停机' \n" +
                    "  when handle_type=2 and suspended_type=16  then '复机' \n" +
                    "    when handle_type=3 and suspended_type=16  then '紧急复机' \n" +
                    "end as mis ,handle_type,suspended_type,b.prd_inst_stas_id\n" +
                    " from coll_suspended_list  a   left join   tb_prd_prd_inst_21@hss b on a.service_external_id = b.service_nbr and  b.exp_date >sysdate\n" +
                    "where  closed_date is  null\n" +
                    "and request_date >sysdate-1\n" +
                    "and request_date <sysdate-0.0833\n" +
                    "and a.service_external_id not in (select * from cld_abp_err)\n" +
                    "and send_flag =2 and process_result=2 ";
            try {
                ProcUtil.callProc(iamConn, "sql_procedure", new Object[]{create_cld_all_data});
            } catch (Exception e) {
                e.printStackTrace();
            }


            String query = "select * from cld_coll2 where mis='复机' and prd_inst_stas_id not in (1001,1101)\n" +
                    "union\n" +
                    "select * from cld_coll2 where mis='双停' and prd_inst_stas_id<>1202\n" +
                    "union\n" +
                    "select * from cld_coll2 where mis='单停' and prd_inst_stas_id<>1205\n" +
                    "union\n" +
                    "select * from cld_coll2 where mis='紧急复机' and prd_inst_stas_id not in (5001,1001)";

            List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(iamConn, query);

            String operType = "";
            int stopAndRevive = 0;


            for (LinkedHashMap map : linkedHashMaps) {
                String suspended_seq = map.get("SUSPENDED_SEQ").toString();
                String service_external_id = map.get("SERVICE_EXTERNAL_ID").toString();
                String handle_type = map.get("HANDLE_TYPE").toString();
                String suspended_type = map.get("SUSPENDED_TYPE").toString();

                if ("11".equals(suspended_type)) {
                    operType = "stop";
                    stopAndRevive = 11;
                }
                if ("16".equals(suspended_type)) {
                    operType = "revive";
                    stopAndRevive = 16;
                }


                HashMap reMap = StopOrReviveHelp.sendRequest(iamConn,
                        service_external_id,//设备号
                        operType,//停机
                        suspended_seq + "",//请求流水
                        handle_type,
                        stopAndRevive,//11停机，16复机
                        "同步ABP_500",
                        "",
                        "",
                        "",//请求员工
                        "syschronizedAbb" // 请求crm   request_crm_url  ，同步abp  syschronizedAbb
                );
                String code = reMap.get("code").toString();
                String msg = reMap.get("msg").toString();

                String inn="insert into cld_abp_err values ('"+service_external_id+"')";
                if("-1".equals(code)){
                    SQLHelp.insertSQL(iamConn,inn);
                }

            }
            isFinish=true;
        }
    }

}
