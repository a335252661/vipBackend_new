package vip.coll;

import com.alibaba.fastjson.JSONObject;
import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;
import utils.HttpClientHelps;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vip.UsageAmountApplication2.subList2;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/17 coll.AcctBalanceDebtoff
 *  java -cp D:\project\company\old\auditProject\out\artifacts\auditProject_jar  coll.AcctBalanceDebtoff
 */
public class AcctBalanceDebtoff extends Thread{

    private static Connection collTestConn = DBConn.getColl04Conn();
//    private static Connection collTestConn = DBConn.getCollTestConn();


    private  List<LinkedHashMap<String, Object>> list=null;
    public AcctBalanceDebtoff(List<LinkedHashMap<String, Object>> linkedHashMaps) {
        list = linkedHashMaps;
    }

    public static void main(String[] args) {
//        AcctBalanceDebtoff ff = new AcctBalanceDebtoff(null);
//        for(LinkedHashMap<String, Object> map :list){
//            String bill_ref_no = map.get("BILL_REF_NO").toString();
//            String ACCOUNT_NO = map.get("ACCOUNT_NO").toString();
//            ff.fun(bill_ref_no,ACCOUNT_NO);
//        }
    }

    @Override
    public void run() {
        for(LinkedHashMap<String, Object> map :list){
            String bill_ref_no = map.get("BILL_REF_NO").toString();
            String ACCOUNT_NO = map.get("ACCOUNT_NO").toString();

//            System.out.println(bill_ref_no);

            this.fun(bill_ref_no,ACCOUNT_NO);
        }
    }

    public void fun(String bill_ref_no, String ACCOUNT_NO) {
        String url = "http://10.145.167.212:8063/acct/acctbizbalance/acctBalanceDebtoff";
//        String url = "http://10.145.221.42:8263//acct/acctbizbalance/acctBalanceDebtoff";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("billRefNo" , bill_ref_no);
        jsonObject.put("accountNo" , ACCOUNT_NO);
        jsonObject.put("requestSeq" , "1");
        jsonObject.put("requestTime" , "1");
        jsonObject.put("csrId" , "ACCT_SYSTEM");
        jsonObject.put("officeId" , "BILLING_00");
        String post = HttpClientHelps.sendPost(url, jsonObject.toJSONString());
        JSONObject jsonObject1 = JSONObject.parseObject(post);
        String code = jsonObject1.getString("code");
        String message = jsonObject1.getString("message");
        System.out.println(post);
        String uodate = "update cld_yubaosun set result = '"+post+"' ,result_code='"+code+"' ,result_mess='"+message+"' where bill_ref_no='"+bill_ref_no+"' and account_no='"+ACCOUNT_NO+"'";
        SQLHelp.updateSQL(collTestConn , uodate);
    }

}
