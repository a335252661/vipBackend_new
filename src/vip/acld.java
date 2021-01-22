package vip;

import helps.CommonHelp;
import helps.SQLHelp;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/6/28
 */
public class acld {
    public static void main(String[] args) {
        //获取上个月
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH , -1);
        Date time = instance.getTime();
        System.out.println(time);


    }
}
