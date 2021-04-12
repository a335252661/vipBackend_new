package test;

import helps.DateTimeHelp;
import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/5/18
 */
public class testVo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        int batchRun;
        Connection conn = DBConn.getCollTestConn();
        String currentMon = DateTimeHelp.getDateTimeString("yyyy-MM");
        //查询表中时间是否是当月，没有数据，或者不是当月，表示第一次运行，时间是当月表示是第二批运行
        String field = SQLHelp.querySQLReturnField(conn, "select begin_date as field from coll_config_sms where id=1");
        if(null==field){
            // 第一批
            batchRun=1;
        }else {
            String substring = field.substring(0,6);
            if(currentMon.equals(substring)){
                // 第一批
                batchRun=1;
            }else {
                // 第二批
                batchRun=2;
            }
        }

        System.out.println(batchRun);

//        String field = SQLHelp.querySQLReturnField(conn, "select begin_date as field from coll_config_sms where id=2");
//        System.out.println(field.equals("null"));

          // **月4日15时
        String timeString = DateTimeHelp.getDateTimeString("MM月dd日dd时");
        System.out.println(timeString);

        System.out.println("你好");

    }

}
