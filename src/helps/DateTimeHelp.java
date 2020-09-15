package helps;

import utils.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/4/10
 */
public class DateTimeHelp {

    public static String dateTimeString = "yyyy-MM-dd HH:mm:ss";
    public static long startTime = 0;
    public static long endTime = 0;

    public static void main(String[] args) {
//        System.out.println(DateTimeHelp.adjMon(new Date() , 4) );

//        System.out.println(DateTimeHelp.dateToStr(new Date() , "yyyyMMdd") );
//        System.out.println(DateTimeHelp.strToStr("20200303" , "yyyyMMdd" , "yyyy-MM+dd") );

        //2020/4/1  --20200401
//        System.out.println(DateTimeHelp.strToStr("2018-05-01 00:00:00" , dateTimeString,"yyyyMMdd"));

        System.out.println("你好");
    }


    public static String getDateTimeString(String formate) {
        return DateTimeHelp.dateToStr(new Date() , formate);
    }


    public static  String dateToStr(Date date , String formate){
        SimpleDateFormat sim = new SimpleDateFormat(formate);
        String str = sim.format(date);
        return str;
    }
    public static  Date dateTodate(Date date , String formate){
        SimpleDateFormat sim = new SimpleDateFormat(formate);
        String str = sim.format(date);
        Date date1 = DateTimeHelp.strToDate(str, formate);
        return date1;
    }

    public static Date  strToDate(String dateStr,String formate){
        Date parse = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(formate);
             parse = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    /**
     *
     * @param dateStr  日期字符串
     * @param currFormate 日期字符串对应的格式
     * @param toFormate 需要转化的格式
     * @return
     */
    public static String strToStr(String dateStr,String currFormate,String toFormate){
        Date parse = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(currFormate);
            parse = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String s = DateTimeHelp.dateToStr(parse, toFormate);
        return s;
    }
    public static String  adjMon(Date date,int adjust){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, adjust);
        Date dt1 = calendar.getTime();

        String str = DateTimeHelp.dateToStr(dt1, dateTimeString);
        return str;
    }

    public static Date  adjMonReDate(Date date,int adjust){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, adjust);
        Date dt1 = calendar.getTime();
        return dt1;
    }

    /**
     * 毫秒转时分秒
     * @param ms
     * @return
     */
    public static String longToTime(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

    public static void start() {
        startTime =  System.currentTimeMillis(); //获取开始时间
    }
    public static String end() {
        endTime =  System.currentTimeMillis(); //获取开始时间
        if(0L==startTime){
            System.out.println("请初始化startTime");
        }
        String runTime = DateTimeHelp.longToTime(endTime - startTime);
        System.out.println("====================运行时间 ："+runTime);
        return runTime;
    }



}
