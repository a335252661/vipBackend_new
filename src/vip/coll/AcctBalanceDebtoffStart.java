package vip.coll;

import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vip.UsageAmountApplication2.subList2;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/12/21 vip.coll.AcctBalanceDebtoffStart
 */
public class AcctBalanceDebtoffStart {
        private static Connection collTestConn = DBConn.getColl04Conn();
//    private static Connection collTestConn = DBConn.getCollTestConn();
    public static void main(String[] args) {
        String no =  args[0];
        String sql = "select * from cld_yubaosun where result is null and mod(bill_ref_no,10)="+no;
        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(collTestConn, sql);
        //将大集合分成10个小集合
        List<List> lists = subList2(linkedHashMaps, 20);
        ExecutorService exe = Executors.newCachedThreadPool();
        for (int i = 0; i < lists.size(); i++) {
            exe.execute(new AcctBalanceDebtoff(lists.get(i)));
        }
        exe.shutdown();
        while (true) {
            if (exe.isTerminated()) {
                System.out.println("所有线程运行结束");
                break;
            }
        }

    }
}
