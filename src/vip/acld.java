package vip;

import helps.SQLHelp;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/6/28
 */
public class acld {
    public static void main(String[] args) {
        System.out.println(StringUtils.isEmpty("null"));

        HashSet<String> ll = new HashSet<String>();
        ll.add("1");
        ll.add("12");
        ll.add("1");

        System.out.println(ll);


        String update = "update  JT_BILL_DATA_INVOICE set load_flag ='2' , CHG_DATE = sysdate where PAY_INTERIM_SEQ in (?)";
        String newSql = String.format(update.replace("?", "%s"), SQLHelp.toSQLin(ll.toArray()));
        System.out.println(newSql);

        //"billMonth" : "2020-05-01 00:00:00.0",
        String str[] = "2020-05-01 00:00:00.0".toString().split("-");
        String mon = str[0].concat(str[1]);
        System.out.println(mon);

    }
}
