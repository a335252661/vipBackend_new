package vip.wlw;


//import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mysql.jdbc.Clob;
//import com.xxl.job.executor.entity.ABMResponseInfo;
//import com.xxl.job.executor.entity.ABMServiceResult;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class BaseUtil {

    private static String serialVersionUID = "serialVersionUID";
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATETIME_PATTERN1 = "yyyyMMddHHmmss";
    public static String DATETIME_PATTERN2 = "yyyyMMddHHmmssSSS";
    public static String DATE_PATTERN = "yyyy-MM-dd";
    public static String DATE_PATTERN1 = "yyyyMMdd";
    public static String MONTH_PATTERN = "yyyy-MM";
    public static String TIME_PATTERN = "HH:mm:ss";
    public static String MONTH_PATTERN1 = "yyyyMM";


    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    private static Map printTableMap = new HashMap();

    static {
        printTableMap.put("TBL_MSG_CONTROL", "TBL_MSG_CONTROL");
        printTableMap.put("PRINT_SIM_DETAIL_CONVERGE", "SIM_DTL_CON");
        printTableMap.put("PRINT_SERVICE_DETAIL", "SERVICE_DETAIL");
        printTableMap.put("PRINT_SERVICE", "PRINT_SERVICE");
        printTableMap.put("PRINT_LDD_DETAIL", "PRINT_LDD_DETAIL");
        printTableMap.put("PRINT_INVOICE", "PRINT_INVOICE");
        printTableMap.put("PRINT_ELE_CMF", "PRINT_ELE_CMF");
        printTableMap.put("PRINT_EJ_DETAIL_CONVERGER", "PRINT_EJ_DTL_CON");
        printTableMap.put("PRINT_CMF", "PRINT_CMF");
        printTableMap.put("PRINT_FILE_PAPER_JOB_DETAIL", "PAPER_JOB_DTL");
        printTableMap.put("PRINT_LASTMONTH_PAID_RECORD", "LM_PAID_RECORD");
        printTableMap.put("print_LDD_REQUEST", "LDD_REQUEST");
        printTableMap.put("PRINT_INVOICE_DETAIL_CONVERGE", "INVOICE_DTL_CON");
        printTableMap.put("PRINT_INVOICE_DETAIL", "INVOICE_DETAIL");
        printTableMap.put("PRINT_FILE_INTERCEPT_DETAIL", "INTERCEPT_DTL");
        printTableMap.put("PRINT_HIDE_SUBSCR_NO", "HIDE_SUBSCR_NO");
        printTableMap.put("PRINT_EN_DETAIL_CONVERGE", "EN_DTL_CON");
        printTableMap.put("PRINT_DATA_DEVICE", "DATA_DEVICE");
        printTableMap.put("POINTS_USER_ACCOUNT", "POINTS_USER_ACCOUNT");
    }


    /**
     * 将日期格式化为指定格式的字串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (null == date)
            return null;
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(date);
    }

    public static String format(Object date, String pattern) {
        if (null == date || !(date instanceof java.util.Date))
            return null;
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format((Date) date);
    }

    /**
     * 当前日期加上12个小时的时间
     */
    public static String addHours(String pattren, int hour) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattren);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return fmt.format(date);
    }

    /**
     * 将指定格式的字串格式化为日期
     *
     * @param sdate
     * @param pattern
     * @return
     */
    public static Date parseDate(String sdate, String pattern) {
        if (sdate == null || pattern == null)
            return null;
        Date retn = null;
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(pattern);
            retn = fmt.parse(sdate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return retn;
    }

    public static String formatDate(String date, String pattern) {
        Date dd = parseDate(date, pattern);
        return format(dd, pattern);
    }

    /**
     * 两日期格式转化
     *
     * @param sdate
     * @param fromPattern
     * @param toPattern
     * @return
     */
    public static String transFormat(
            String sdate,
            String fromPattern,
            String toPattern) {
        if (sdate == null || fromPattern == null || toPattern == null)
            return null;
        Date temp = parseDate(sdate, fromPattern);
        return format(temp, toPattern);
    }

    public static String transFormat(
            Object sdate,
            String fromPattern,
            String toPattern) {
        if (sdate == null || fromPattern == null || toPattern == null)
            return null;
        Date temp = parseDate(sdate.toString(), fromPattern);
        return format(temp, toPattern);
    }

    public static String qLike(String val) {
        if (null == val)
            return null;
        return "%" + val + "%";
    }

    public static Date getFirstTime(String dt) {
        Date dd = BaseUtil.parseDate(dt, BaseUtil.DATE_PATTERN);
        return getFirstTime(dd);
    }

    public static Date getLastTime(String dt) {
        Date dd = BaseUtil.parseDate(dt, BaseUtil.DATE_PATTERN);
        return getLastTime(dd);
    }

    /**
     * 得某天的第一秒日期
     *
     * @param dd
     * @return
     */
    public static Date getFirstTime(Date dd) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得某天的最后一秒日期
     *
     * @param dd
     * @return
     */
    public static Date getLastTime(Date dd) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 得到当前日期
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return BaseUtil.format(new Date(), pattern);
    }

    public static String addMonth(String date, String pattern, int amount) {
        Date dd = parseDate(date, pattern);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.add(Calendar.MONTH, amount);
        return format(cal.getTime(), pattern);

    }

    /**
     * 月份账期：
     *
     * @param billingCycleId 日期
     * @param i              加减月份
     * @return 加减 i 月后yyyyMM格式日期
     */
    public static String getBillingCycle(String billingCycleId, int i) throws ParseException {
        Date date = parseDate(billingCycleId, BaseUtil.MONTH_PATTERN1);
        SimpleDateFormat formatter = new SimpleDateFormat(BaseUtil.MONTH_PATTERN1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);
        return formatter.format(calendar.getTime());
    }

    public static String addDay(String date, String pattern, int amount) {
        Date dd = parseDate(date, pattern);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.add(Calendar.DATE, amount);
        return format(cal.getTime(), pattern);

    }

    /**
     * 根据年月天字串得当天的第一秒字符串
     *
     * @param yearmonthday 年月天字串 yyyy-MM-dd
     * @return
     */
    public static String getFirstTimeString(String yearmonthday) {
        Date dd = parseDate(yearmonthday, DATE_PATTERN);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return format(cal.getTime(), DATETIME_PATTERN);
    }

    /**
     * 根据年月天字串得得当天的最后一秒字符串
     *
     * @param yearmonthday 年月天字串 yyyy-MM-dd
     * @return
     */
    public static String getLastTimeString(String yearmonthday) {
        Date dd = parseDate(yearmonthday, DATE_PATTERN);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format(cal.getTime(), DATETIME_PATTERN);
    }

    /**
     * 根据年月字串得月的第一天字串
     *
     * @param yearmonth 年月字串 yyyy-MM
     * @return
     */
    public static String getFirstDateString(String yearmonth) {
        Date dd = parseDate(yearmonth, MONTH_PATTERN);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return format(cal.getTime(), DATE_PATTERN);
    }

    /**
     * 根据年月字串得月的第一天字串
     *
     * @param date
     * @return
     */
    public static String getFirstDateString(String date, String pattern) {
        Date dd = parseDate(date, pattern);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return format(cal.getTime(), pattern);
    }

    /**
     * 根据年月字串得月的最后一天字串
     *
     * @param yearmonth 年月字串 yyyy-MM
     * @return
     */
    public static String getLastDateString(String yearmonth) {
        Date dd = parseDate(yearmonth, MONTH_PATTERN);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format(cal.getTime(), DATE_PATTERN);
    }


    /**
     * 根据年月字串得月的最后一天字串
     *
     * @param date    年月字串 yyyy-MM
     * @param pattern
     * @return
     */
    public static String getLastDateString(String date, String pattern) {
        Date dd = parseDate(date, pattern);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format(cal.getTime(), pattern);
    }


    /**
     * 根据年月字串得月的最后一天字串
     *
     * @param yearmonth 年月字串 yyyyMM
     * @return
     */
    public static String getLastDateString1(String yearmonth) {
        Date dd = parseDate(yearmonth, MONTH_PATTERN1);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dd);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return format(cal.getTime(), DATE_PATTERN1);
    }

    /**
     * 得某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 得年月字串
     *
     * @param year
     * @param month
     * @return
     */
    public static String getMonthString(
            String year,
            String month,
            String monthpattern) {
        return format(
                getFirstDateOfMonth(
                        Integer.parseInt(year),
                        Integer.parseInt(month)),
                monthpattern);
    }

    /**
     * 得年月字串
     *
     * @param year
     * @param month
     * @return
     */
    public static String getMonthString(int year, int month) {
        return format(getFirstDateOfMonth(year, month), MONTH_PATTERN);
    }

    /**
     * 得某年某月的最后的一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 在字串前后打上',用于拼SQL
     *
     * @param str
     * @return
     */
    public static String q(String str) {
        if (null == str)
            return str;
        return "'" + str + "'";
    }

    /**
     * 在字串前后打上",用于导文件
     *
     * @param str
     * @return
     */
    public static String dq(String str) {
        if (BaseUtil.isEmpty(str))
            return "";
        return "\"" + str + "\"";
    }

    /**
     * @param obj
     * @return
     */
    public static String dq(Object obj) {
        if (null == obj)
            return "";
        return "\"" + obj + "\"";
    }

    /**
     * <p>Splits the provided text into an array, separator specified.
     * This is an alternative to using StringTokenizer.</p>
     * <p>
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     * <p>
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     * <p>
     * <pre>
     * BaseUtil.split(null, *)         = null
     * BaseUtil.split("", *)           = []
     * BaseUtil.split("a.b.c", '.')    = ["a", "b", "c"]
     * BaseUtil.split("a..b.c", '.')   = ["a", "b", "c"]
     * BaseUtil.split("a:b:c", '.')    = ["a:b:c"]
     * BaseUtil.split("a\tb\nc", null) = ["a", "b", "c"]
     * BaseUtil.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     *
     * @param str           the String to parse, may be null
     * @param separatorChar the character used as the delimiter,
     *                      <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     * @since 2.0
     */
    public static String[] split(String str, char separatorChar) {
        // Performance tuned for 2.0 (JDK1.4)
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 获取字符串中汉字的个数
     *
     * @param sStr 源字符串
     * @return 汉字的个数
     */
    public static int unicodeCount(String sStr) {
        if (isEmpty(sStr))
            return 0;
        int count = 0;
        for (int i = 0; i < sStr.length(); i++) {
            if ((int) sStr.charAt(i) > 255)
                count++;
        }
        return count;
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     * <p>
     * <pre>
     * BaseUtil.padding(0, 'e')  = ""
     * BaseUtil.padding(3, 'e')  = "eee"
     * BaseUtil.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     *
     * @param repeat  number of times to repeat delim
     * @param padChar character to repeat
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
     */
    private static String padding(int repeat, char padChar) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < repeat; i++) {
            s.append(padChar);
        }
        return s.toString();
    }

    /**
     * <p>Right pad a String with a specified character.</p>
     * <p>
     * <p>The String is padded to the size of <code>size</code>.</p>
     * <p>
     * <pre>
     * BaseUtil.rightPad(null, *, *)     = null
     * BaseUtil.rightPad("", 3, 'z')     = "zzz"
     * BaseUtil.rightPad("bat", 3, 'z')  = "bat"
     * BaseUtil.rightPad("bat", 5, 'z')  = "batzz"
     * BaseUtil.rightPad("bat", 1, 'z')  = "bat"
     * BaseUtil.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return right padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String rightPad(String str, int size, char padChar) throws IOException {
        if (str == null) {
            return null;
        }
        //判断字符串中有没有汉字,如果有则指定长度要减去汉字的个数
        size -= unicodeCount(str);
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    /**
     * <p>Right pad a String with a specified String.</p>
     * <p>
     * <p>The String is padded to the size of <code>size</code>.</p>
     * <p>
     * <pre>
     * BaseUtil.rightPad(null, *, *)      = null
     * BaseUtil.rightPad("", 3, "z")      = "zzz"
     * BaseUtil.rightPad("bat", 3, "yz")  = "bat"
     * BaseUtil.rightPad("bat", 5, "yz")  = "batyz"
     * BaseUtil.rightPad("bat", 8, "yz")  = "batyzyzy"
     * BaseUtil.rightPad("bat", 1, "yz")  = "bat"
     * BaseUtil.rightPad("bat", -1, "yz") = "bat"
     * BaseUtil.rightPad("bat", 5, null)  = "bat  "
     * BaseUtil.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return right padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String rightPad(String str, int size, String padStr) throws IOException {
        if (str == null) {
            return null;
        }
        //判断字符串中有没有汉字,如果有则指定长度要减去汉字的个数
        //size -= unicodeCount(str);
        if (padStr == null || padStr.length() == 0) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>Left pad a String with a specified character.</p>
     * <p>
     * <p>Pad to a size of <code>size</code>.</p>
     * <p>
     * <pre>
     * BaseUtil.leftPad(null, *, *)     = null
     * BaseUtil.leftPad("", 3, 'z')     = "zzz"
     * BaseUtil.leftPad("bat", 3, 'z')  = "bat"
     * BaseUtil.leftPad("bat", 5, 'z')  = "zzbat"
     * BaseUtil.leftPad("bat", 1, 'z')  = "bat"
     * BaseUtil.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     the String to pad out, may be null
     * @param size    the size to pad to
     * @param padChar the character to pad with
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     * @since 2.0
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            str = "";
        }
        //判断字符串中有没有汉字,如果有则指定长度要减去汉字的个数
        size -= unicodeCount(str);
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    /**
     * <p>左边填指定字符串（右对齐）Left pad a String with a specified String.</p>
     * <p>
     * <p>填为指定长度 Pad to a size of <code>size</code>.</p>
     * <p>
     * <pre>
     * BaseUtil.leftPad(null, *, *)      = null
     * BaseUtil.leftPad("", 3, "z")      = "zzz"
     * BaseUtil.leftPad("bat", 3, "yz")  = "bat"
     * BaseUtil.leftPad("bat", 5, "yz")  = "yzbat"
     * BaseUtil.leftPad("bat", 8, "yz")  = "yzyzybat"
     * BaseUtil.leftPad("bat", 1, "yz")  = "bat"
     * BaseUtil.leftPad("bat", -1, "yz") = "bat"
     * BaseUtil.leftPad("bat", 5, null)  = "  bat"
     * BaseUtil.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str    the String to pad out, may be null
     * @param size   the size to pad to
     * @param padStr the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     * <code>null</code> if null String input
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (padStr == null || padStr.length() == 0) {
            padStr = " ";
        }
        //判断字符串中有没有汉字,如果有则指定长度要减去汉字的个数
        size -= unicodeCount(str);
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * 把分转为 #.## 格式的元字串
     *
     * @param fen
     * @return
     */
    public static String fen2yuan(long fen) {
        double yuan = fen / 100d;
        //DecimalFormat fmt = new DecimalFormat("#,##0.00");
        DecimalFormat fmt = new DecimalFormat("0.00");
        return fmt.format(yuan);
    }

    public static String fen2yuan3(long fen) {
        double yuan = fen / 100d;
        DecimalFormat fmt = new DecimalFormat("#,##0.00");
        return fmt.format(yuan);
    }

    /**
     * 把分转为 #.## 格式的元字串
     *
     * @param parm
     * @return
     */
    public static String fen2yuan(String parm) {
        if (isEmpty(parm))
            return "";
        long fen = Long.parseLong(parm);
        double yuan = fen / 100d;
        //DecimalFormat fmt = new DecimalFormat("#,##0.00");
        DecimalFormat fmt = new DecimalFormat("0.00");
        return fmt.format(yuan);
    }

    public static String fen2yuan(Object parm, String defaultval) {
        if (null == parm || parm.toString().length() == 0)
            return defaultval;
        long fen = Long.parseLong(parm.toString());
        double yuan = fen / 100d;
        //DecimalFormat fmt = new DecimalFormat("#,##0.00");
        DecimalFormat fmt = new DecimalFormat("0.00");
        return fmt.format(yuan);
    }

    public static String fen2yuan3(String parm) {
        if (isEmpty(parm))
            return "";
        long fen = Long.parseLong(parm);
        double yuan = fen / 100d;
        DecimalFormat fmt = new DecimalFormat("#,##0.00");
        return fmt.format(yuan);
    }

    /*
     * 把分转换为元，小数点后保留原样 不做四舍五入
     */
    public static String fentoyuan(String parm) {
        String str = "";
        if (BaseUtil.isEmpty(parm)) {
            return "0";
        }
        if (parm.contains(".")) {
            parm = parm.replace(".", "-");
            String[] arr = parm.split("-");
            if (Long.parseLong(arr[0]) > 0) {
                if (Long.parseLong(arr[0]) > 99) {
                    str = arr[0].substring(0, arr[0].length() - 2) + "." + arr[0].substring(arr[0].length() - 2) + arr[1];
                } else if (Long.parseLong(arr[0]) > 9 && Long.parseLong(arr[0]) <= 99) {
                    str = "0." + arr[0] + arr[1];
                } else {
                    str = "0.0" + arr[0] + arr[1];
                }
            } else {
                str = "0.00" + arr[1];
            }
        } else {
            if (Long.parseLong(parm) > 99) {
                str = parm.substring(0, parm.length() - 2) + "." + parm.substring(parm.length() - 2);
            } else if (Long.parseLong(parm) <= 99 && Long.parseLong(parm) > 9) {
                str = "0." + parm;
            } else {
                str = "0.0" + parm;
            }
        }
        return str;
    }

    /*
     * 把分转换为元，小数点后保留原样 不做四舍五入
     */
    public static String fentoyuan1(String parm) {
        String str = "";
        String tmpParm = parm;
        if (BaseUtil.isEmpty(parm)) {
            return "0";
        }

        if (Long.parseLong(tmpParm) < 0) {
            parm = parm.replace("-", "");
        }
        if (parm.contains(".")) {
            parm = parm.replace(".", "-");
            String[] arr = parm.split("-");
            if (Long.parseLong(arr[0]) > 0) {
                if (Long.parseLong(arr[0]) > 99) {
                    str = arr[0].substring(0, arr[0].length() - 2) + "." + arr[0].substring(arr[0].length() - 2) + arr[1];
                } else if (Long.parseLong(arr[0]) > 9 && Long.parseLong(arr[0]) <= 99) {
                    str = "0." + arr[0] + arr[1];
                } else {
                    str = "0.0" + arr[0] + arr[1];
                }
            } else {
                str = "0.00" + arr[1];
            }
        } else {
            if (Long.parseLong(parm) > 99) {
                str = parm.substring(0, parm.length() - 2) + "." + parm.substring(parm.length() - 2);
            } else if (Long.parseLong(parm) <= 99 && Long.parseLong(parm) > 9) {
                str = "0." + parm;
            } else {
                str = "0.0" + parm;
            }
        }

        if (Long.parseLong(tmpParm) < 0) {
            str = "-" + str;
        }
        return str;
    }

    /**
     * 把#.## 格式的元字串转为分
     *
     * @param syuan
     * @return long
     */
    public static long yuan2fen(String syuan) {

        DecimalFormat fmt = new DecimalFormat("#,##0.00");
        Number yuan = null;
        try {
            yuan = fmt.parse(syuan);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Math.round(yuan.doubleValue() * 100);
    }

    public static String getJsStr(String s) {
        String ss = BaseUtil.replace(s, "<", "〈");
        ss = BaseUtil.replace(ss, ">", "〉");
        ss = BaseUtil.replace(ss, "'", "‘");
        ss = BaseUtil.replace(ss, "\"", "“");
        ss = BaseUtil.replace(ss, "(", " ");
        ss = BaseUtil.replace(ss, ")", " ");
        return ss;
    }

    /**
     * 得异常的堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getStackTrace(Throwable ex) {
        java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
        ex.printStackTrace(new java.io.PrintWriter(cw));
        return cw.toString();
    }

    /**
     * 替掉' " < >
     *
     * @param ss
     * @return
     */
    public static String getXmlStr(String ss) {
        ss = BaseUtil.replace(ss, "<", "〈");
        ss = BaseUtil.replace(ss, ">", "〉");
        ss = BaseUtil.replace(ss, "'", "‘");
        ss = BaseUtil.replace(ss, "\"", "“");
        return ss;
    }

    /**
     * 字符串转为int
     *
     * @param s
     * @return
     */
    public static int parseInt(String s) {
        if (isNumeric(s)) {
            return Integer.parseInt(s);
        } else
            return Integer.MIN_VALUE;
    }

    /**
     * 字符串转为long
     *
     * @param s
     * @return
     */
    public static long parseLong(String s) {
        if (isNumeric(s)) {
            return Long.parseLong(s);
        } else
            return Long.MIN_VALUE;
    }

    /**
     * String isEmpty
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return
                null == s
                        || s.trim().length() == 0
                        || "null".equalsIgnoreCase(s.trim());
    }

    /**
     * 判断对象是否Empty(null或元素为0)
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj == "")
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null)
            return false;
        if (pObj == "")
            return false;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return false;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否仅数字0-9
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        int sz = str.length();
        int j = 0;
        if ('-' == str.charAt(0))
            j = 1;
        for (int i = j; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 求两java日期相隔的天数
     *
     * @param fromDate 开始日期
     * @param toDate   结束日期
     */
    public static int diffDays(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return -1;
        int nDiff = 0;
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(fromDate);
        cal2.setTime(toDate);
        while (cal1.before(cal2)) {
            cal1.add(Calendar.DATE, 1);
            nDiff++;
        }
        return nDiff;
    }

    public static int diffMonths(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return -1;
        int nDiff = 0;
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(fromDate);
        cal2.setTime(toDate);
        while (cal1.before(cal2)) {
            cal1.add(Calendar.MONTH, 1);
            nDiff++;
        }
        return nDiff;
    }

    /**
     * 去左边补的字符
     *
     * @param ss
     * @param c
     * @return
     */
    public static String leftUnPad(String ss, char c) {
        StringBuffer sb = new StringBuffer();
        int idx = -1;
        for (int i = 0; i < ss.length(); i++) {
            char cc = ss.charAt(i);
            if (cc != c) {
                idx = i;
                break;
            }
        }
        if (idx == -1) //all is char c
            return String.valueOf(c);
        else
            return ss.substring(idx);
    }

    /**
     * 当值为空时,返回defaultValue
     *
     * @param value
     * @param defaultVal
     * @return
     */
    public static Object getDefaultValue(Object value, Object defaultVal) {
        if (null == value || isEmpty(value.toString())) {
            return defaultVal;
        } else {
            return value;
        }
    }

    /**
     * 当值为空时,返回defaultValue
     *
     * @param value
     * @param defaultVal
     * @return
     */
    public static String getStringDefault(String value, String defaultVal) {
        if (isEmpty(value)) {
            return defaultVal;
        } else {
            return value;
        }
    }

    /**
     * 判断金额格式是否正确，正确格式为0.00
     *
     * @param sAmount
     * @return
     */
    public static boolean isAmount(String sAmount) {
        String sHead = null;
        String sEnd = null;
        if (sAmount != null && !sAmount.trim().equalsIgnoreCase("")) {
            int index = sAmount.indexOf(".");
            if (index >= 0) {
                sHead = sAmount.substring(0, index);
                sEnd = sAmount.substring(index + 1, sAmount.length());
                if (sHead.equalsIgnoreCase("") || sEnd.equalsIgnoreCase("")) {
                    return false;
                } else if (sEnd.indexOf(".") >= 0) {
                    return false;
                } else if (
                        !isNumeric(sHead)
                                || !isNumeric(sEnd)
                                || sEnd.length() > 2) {
                    return false;
                }
            } else if (!isNumeric(sAmount)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Replace all occurences of a substring within a string with
     * another string.
     *
     * @param inString   String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static String replace(
            String inString,
            String oldPattern,
            String newPattern) {
        if (inString == null) {
            return null;
        }
        if (oldPattern == null || newPattern == null) {
            return inString;
        }
        StringBuffer sbuf = new StringBuffer();
        // output StringBuffer we'll build up
        int pos = 0; // Our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sbuf.toString();
    }

    public static int count(String string, char character) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character)
                n++;
        }
        return n;
    }

    /**
     * @param params paramSql splited by ?
     * @param idx    the ? index in the paramSql,start with 1
     * @param val
     * @return
     */
    public static String replaceParamSql(
            String[] params,
            int idx,
            String val) {
        String sval = params[idx - 1];
        sval += q((String) val);
        return sval;
    }

    public static String replaceParamSql(String[] params, int idx, int val) {
        String sval = params[idx - 1];
        sval += val;
        return sval;
    }

    public static String replaceParamSql(String[] params, int idx, long val) {
        String sval = params[idx - 1];
        sval += val;
        return sval;
    }

    /**
     * 把数据库字段名转为BizComponent字段名 //cust_user_id->CustUserId
     */
    public static String getFieldName(String colName) {
        if (null == colName)
            return colName;
        String sName = null;
        StringBuffer sbTmp = null;
        if (colName.indexOf("_") >= 0) {
            String sTmp = null;
            StringBuffer sbName = new StringBuffer();
            StringTokenizer st = new StringTokenizer(colName, "_");
            while (st.hasMoreTokens()) {
                sTmp = st.nextToken();
                sbTmp = new StringBuffer(sTmp.toLowerCase());
                sbTmp.setCharAt(0, Character.toUpperCase(sbTmp.charAt(0)));
                sbName.append(sbTmp.toString());
            }
            sName = sbName.toString();
        } else {
            sbTmp = new StringBuffer(colName.toLowerCase());
            sbTmp.setCharAt(0, Character.toUpperCase(sbTmp.charAt(0)));
            sName = sbTmp.toString();
        }
        return sName;
    }

    /**
     * @param obj
     * @return
     */
    public static String getHtmlStr(Object obj) {
        String temp = "";
        if (null == obj)
            return "&nbsp;";
        temp = replace(obj.toString(), " ", "&nbsp;");
        temp = replace(temp, "\n", "<br>");
        return temp;
    }

    /**
     * @param sql
     * @return
     */
    public static String getOracleCountSql(String sql) {
        StringBuffer countSelect = new StringBuffer(sql.length() + 30);
        countSelect.append("select count(*) from ( ");
        countSelect.append(sql);
        countSelect.append(" )");
        return countSelect.toString();
    }

    /**
     * @param sql      the select sql
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static String getOraclePageSql(
            String sql,
            int pageNo,
            int pageSize) {
        boolean hasOffset = true;
        if (pageNo <= 1)
            hasOffset = false;
        int fromRowNum = (pageNo - 1) * pageSize;
        int toRowNum = (pageNo) * pageSize;
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        if (hasOffset) {
            pagingSelect.append(
                    "select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (hasOffset) {
            pagingSelect.append(
                    " ) row_ where rownum <= "
                            + toRowNum
                            + ") where rownum_ > "
                            + fromRowNum);
        } else {
            pagingSelect.append(" ) where rownum <= " + toRowNum);
        }
        return pagingSelect.toString();
    }

    /**
     * 舍最后一位
     *
     * @param samount
     * @return
     */
    public static long cutOff(String samount) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < samount.length(); i++) {
            if (i == samount.length() - 1) {
                sb.append('0');
            } else {
                sb.append(samount.charAt(i));
            }
        }
        return Long.parseLong(sb.toString());
    }

    /**
     * @param ss
     * @return
     */
    public static int unicodeLen(String ss) {
        return ss.length() + unicodeCount(ss);
    }

    /**
     * @param left
     * @param right
     * @param len
     * @return
     */
    public static String LRStr(String left, String right, int len) {
        StringBuffer sb = new StringBuffer(len);
        sb.append(left);
        int leftlen = left.length() + unicodeCount(left);
        int rightlen = right.length() + unicodeCount(right);
        int padlen = len - leftlen - rightlen;
        for (int i = 0; i < padlen; i++) {
            sb.append(" ");
        }
        sb.append(right);
        return sb.toString();
    }

    public static String LRStr(String left, String right, String str, int len) {
        StringBuffer sb = new StringBuffer(len);
        sb.append(left);
        int leftlen = left.length() + unicodeCount(left);
        int rightlen = right.length() + unicodeCount(right);
        int padlen = len - leftlen - rightlen;
        for (int i = 0; i < padlen; i++) {
            sb.append(str);
        }
        sb.append(right);
        return sb.toString();
    }

    public static String[] tokenizeToStringArray(
            String s,
            String delimiters,
            boolean trimTokens,
            boolean ignoreEmptyTokens) {
        StringTokenizer st = new StringTokenizer(s, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!(ignoreEmptyTokens && token.length() == 0)) {
                tokens.add(token);
            }
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    public static String[] preSplit(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List list = new ArrayList();
        boolean match = false;
        int start = 0;
        for (int j = 0; j < len; j++) {
            if (str.charAt(j) == separatorChar) {
                String ss = str.substring(start, j);
                start = j + 1;
                list.add(ss);
                match = false;
                continue;
            }
            match = true;
        }
        list.add(str.substring(start));
        return (String[]) list.toArray(new String[list.size()]);
    }


    //HTML字符实体（Character Entities），转义字符串（Escape Sequence）

    /**
     * @param escape
     * @return
     */
    public static String unescape(String escape) throws IOException {
        String[] array = split(escape, ';');

        StringBuffer chs_buffer = new StringBuffer();

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].trim().length() > 2) {
                String num = array[i].substring(2, array[i].length());
                int chs_int = Integer.parseInt(num);
                char chs_char = (char) chs_int;
                chs_buffer.append(chs_char);
            }
        }
        return chs_buffer.toString();
    }

    //将给定的时间以指定格式格式化
    public static String getFormatedDate(Date srcDate, String form) {

        java.text.SimpleDateFormat fmt = new SimpleDateFormat(form);
        String desDate = fmt.format(srcDate);
        return desDate;
    }

//    public static String nullToStr(String src, String defaultValue) {
//        if (StringUtils.isEmpty(src)) {
//            return defaultValue;
//        }
//        return src;
//    }


    public static String nullToStr(Object obj, String defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        return String.valueOf(obj);
    }

    public static int getLatnId(long acctId) {
        String accountId = Long.toString(acctId);
        return Integer.parseInt(accountId.substring(accountId.length() - 1));
    }

    /**
     * 得到当前日期到月底最后一天相隔天数
     *
     * @param date
     * @param pattern
     * @return
     */
    public static int getLastDays(String date, String pattern) {
        Date d = BaseUtil.parseDate(date, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        //日，设为一号
        cal.set(Calendar.DATE, 1);
        //月份加一，得到下个月的一号
        cal.add(Calendar.MONTH, 1);
        //下一个月减一为本月最后一天
        cal.add(Calendar.DATE, -1);
        return BaseUtil.diffDays(d, cal.getTime());
    }

    //小写金额转化为大写金额
    public static String NumberToChinese(Double amount) throws Exception {
        String s1 = "零壹贰叁肆伍陆柒捌玖";
        String s4 = "分角整圆拾佰仟万拾佰仟亿拾佰仟";
        String temp = "";
        String result = "";
        BigDecimal b = new BigDecimal(amount);
        String input = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        temp = input.trim();
        int len = temp.indexOf(".");
        if (len > 12) {
            throw new Exception("金额最高只能到仟亿");
        }
        int n1 = 0;
        String num = "";
        String unit = "";
        for (int i = 0; i < temp.length(); i++) {
            if (i > len + 2) {
                break;
            }
            if (i == len) {
                continue;
            }
            n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
            num = s1.substring(n1, n1 + 1);
            n1 = len - i + 2;
            unit = s4.substring(n1, n1 + 1);
            result = result.concat(num).concat(unit);
        }
        for (int i = 0; i < result.length() - 1; i++) {
            if (result.indexOf("零角零分") > 0) {
                result = result.replace("零角零分", "整");
            }
            if (result.indexOf("零分") > 0) {
                result = result.replace("零分", "");
            }
            if (result.indexOf("零角") > 0) {
                result = result.replace("零角", "零");
            }
            if (result.indexOf("零零") > 0) {
                result = result.replace("零零", "零");
            }
            if (result.indexOf("零拾") > 0) {
                result = result.replace("零拾", "零");
            }
            if (result.indexOf("零佰") > 0) {
                result = result.replace("零佰", "零");
            }
            if (result.indexOf("零仟") > 0) {
                result = result.replace("零仟", "零");
            }
            if (result.indexOf("零万") > 0) {
                result = result.replace("零万", "万");
            }
            if (result.indexOf("零亿") > 0) {
                result = result.replace("零亿", "亿");
            }
            if (result.indexOf("零圆") > 0) {
                result = result.replace("零圆", "圆");
            }
            if (result.indexOf("亿万") > 0) {
                result = result.replace("亿万", "亿");
            }
        }
        return result;
    }

    //如果字符串为空，返回一个空字符串
    public static String returnNullString(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        } else {
            return str.trim();
        }
    }


    /**
     * 列出起始月到终止月之间的月份
     */
    public static List<String> listFromTOMonth(int fromYear, int fromMonth, int toYear, int toMonth) {
        List<String> monthList = new ArrayList<String>();
        Calendar fromCal = Calendar.getInstance();
        fromCal.set(Calendar.YEAR, fromYear);
        fromCal.set(Calendar.MONTH, fromMonth - 1);
        Calendar toCal = Calendar.getInstance();
        toCal.set(Calendar.YEAR, toYear);
        toCal.set(Calendar.MONTH, toMonth - 1);
        int months = diffMonths(fromCal.getTime(), toCal.getTime());
        //System.out.println("MonthsCount:"+months);
        Calendar tempCal = Calendar.getInstance();
        for (int i = 0; i <= months; i++) {
            tempCal.set(Calendar.YEAR, fromYear);
            tempCal.set(Calendar.MONTH, (fromMonth + i) - 1);
            //System.out.println("Month:"+BaseUtil.format(tempCal.getTime(), "yyyy-MM"));
            monthList.add(BaseUtil.format(tempCal.getTime(), "yyyy-MM"));
        }

        return monthList;
    }

    /**
     *
     */
    public static boolean RightDate(String date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (date == null) {
            return false;
        }
        try {
            sdf.parse(date);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * unicode转汉字
     */
    public static String unicodeToGB(String s) {
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s, "\\u");
        while (st.hasMoreTokens()) {
            sb.append((char) Integer.parseInt(st.nextToken(), 16));
        }
        return sb.toString();
    }

    /**
     * 汉字转成unicode
     *
     * @param str
     * @return
     */
    public static String toUnicode(String str) {
        char[] arChar = str.toCharArray();
        int iValue = 0;
        String uStr = "";
        for (int i = 0; i < arChar.length; i++) {
            iValue = (int) str.charAt(i);
            if (iValue <= 256) {
                // uStr+="& "+Integer.toHexString(iValue)+";";
                uStr += "\\" + Integer.toHexString(iValue);
            } else {
                // uStr+="&#x"+Integer.toHexString(iValue)+";";
                uStr += "\\u" + Integer.toHexString(iValue);
            }
        }
        return uStr;
    }

    // 将字CLOB转成STRING类型
    public static String ClobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        java.io.Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }

    public static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher("2312");
        return isNum.matches();
    }

    /**
     * 获取请求流水号
     *
     * @return String
     */
    public static String generateRequestId() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return "IAM" + dateString + getRandom();
    }

    /**
     * 获取随机数:线程编号+随机数共10位
     *
     * @return String
     */
    private static String getRandom() {
        String randStr = "";
        String threadId = String.valueOf(Thread.currentThread().getId());
        int len = threadId.length();
        if (len >= 10)
            threadId = threadId.substring(len - 10);
        else {
            for (int i = 0; i < 10 - len; i++) {
                randStr = randStr + (int) (Math.random() * 10.0D);
            }
        }
        return threadId + randStr;
    }

    /**
     * 上月第一天
     *
     * @return
     */
    public static Date getPrefirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, -1);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTime();
    }

    /**
     * 本月第一天
     *
     * @return
     */
    public static Date getfirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTime();
    }

    /**
     * 本月第一天(绝对0点)
     * @return
     */
    public static Date getfirstDayAbs() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR, 0);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }

    /**
     * 将字符串型日期转换为日期型
     *
     * @param strDate       字符串型日期
     * @param srcDateFormat 源日期格式
     * @param dstDateFormat 目标日期格式
     * @return Date 返回的util.Date型日期
     */
    public static Date stringToDate(String strDate, String srcDateFormat, String dstDateFormat) {
        Date rtDate = null;
        Date tmpDate = (new SimpleDateFormat(srcDateFormat)).parse(strDate, new ParsePosition(0));
        String tmpString = null;
        if (tmpDate != null)
            tmpString = (new SimpleDateFormat(dstDateFormat)).format(tmpDate);
        if (tmpString != null)
            rtDate = (new SimpleDateFormat(dstDateFormat)).parse(tmpString, new ParsePosition(0));
        return rtDate;
    }

    /**
     * 返回指定格式的字符型日期
     *
     * @param date
     * @param formatString
     * @return
     */
    public static String Date2String(Date date, String formatString) {
        if (isEmpty(date)) {
            return null;
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat(formatString);
        String strDate = simpledateformat.format(date);
        return strDate;
    }

    /**
     * @param @param  tableName
     * @param @return
     * @return String
     * @throws
     * @Title: converPrintTable
     * @Description: 转换打印接口表到历史月份表
     */
    public static String converPrintTable(String tableName, String billMonth) {

        if (isNotEmpty(printTableMap.get(tableName.toUpperCase()))) {
            return "HIS_" + billMonth + "_"
                    + (String) printTableMap.get(tableName.toUpperCase());
        } else {
            return tableName;
        }
    }

    private static String converAbmAndLimitTable(String tableName, String billMonth) {

        if (isNotEmpty(tableName.toUpperCase())) {
            return tableName.toUpperCase() + "_" + billMonth;
        } else {
            return tableName;
        }
    }

//    public static String converTable(String tableName, String nowDate){
//        String date = nowDate.substring(4, 6);
//        switch (date) {
//            case "12":
//                tableName = converAbmAndLimitTable(tableName, "ONE");
//                break;
//            case "01":
//                tableName = converAbmAndLimitTable(tableName, "TWO");
//                break;
//            case "02":
//                tableName = converAbmAndLimitTable(tableName, "THREE");
//                break;
//            case "03":
//                tableName = converAbmAndLimitTable(tableName, "FOUR");
//                break;
//            case "04":
//                tableName = converAbmAndLimitTable(tableName, "FIVE");
//                break;
//            case "05":
//                tableName = converAbmAndLimitTable(tableName, "SIX");
//                break;
//            case "06":
//                tableName = converAbmAndLimitTable(tableName, "SEVEN");
//                break;
//            case "07":
//                tableName = converAbmAndLimitTable(tableName, "EIGHT");
//                break;
//            case "08":
//                tableName = converAbmAndLimitTable(tableName, "NINE");
//                break;
//            case "09":
//                tableName = converAbmAndLimitTable(tableName, "TEN");
//                break;
//            case "10":
//                tableName = converAbmAndLimitTable(tableName, "ELEVEN");
//                break;
//            case "11":
//                tableName = converAbmAndLimitTable(tableName, "TWELVE");
//                break;
//        }
//        return tableName;
//    }

    /**
     * 判断俩日期是否满12月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isOver12Month(Date startDate, Date endDate) {
        boolean flag = false;
        Calendar startCa = Calendar.getInstance();
        startCa.setTime(startDate);
        startCa.add(Calendar.MONTH, 12);
        if (startCa.getTime().before(endDate) || startCa.getTime().equals(endDate)) {
            flag = true;
        }
        return flag;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
        String format = null;
        try {
            format = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }



    public static void main(String[] args) {
        System.out.println(BaseUtil.RightDate("20142101", "yyyyMMdd"));
        System.out.println(BaseUtil.isNumeric("2014210.1"));
    }

    /**
     * 判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null(暂定)
     *
     * @param obj
     * @return boolean
     * @description
     * @author 张松
     * @date 2018年8月8日
     */
    public static boolean isAllFieldNull(Object obj){
        Class stuCla = (Class) obj.getClass();
        //得到属性集合
        Field[] fs = stuCla.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            if (!f.getName().equals(serialVersionUID)) {
                // 设置属性是可以访问的(私有的也可以)
                f.setAccessible(true);
                String name = f.getName();
                // 得到此属性的值
                Object val = null;
                try {
                    val = f.get(obj);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //只要有1个属性不为空,那么就不是所有的属性值都为空
                if (val != null) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static String getRequestTime() {
        String requestTime = null;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
        requestTime = sdf.format(now);
        return requestTime;
    }

	/**
	 * 获取请求ABM接口的流水号
	 *
	 * @return String
	 */
	public static String getAcctBalanceSeq() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 判断账本结转类型
	 *
	 * @param monthInterval
	 * @param isCycleDepend
	 * @return
	 */
	public static int balanceIntervalType(int monthInterval, int isCycleDepend) {
		int flag = 0;
		if (0 == monthInterval) {
			// 无分月返还，可结转
			flag = 0;
		}

		if (1 == monthInterval) {
			if (1 == isCycleDepend) {
				// 分月返还并且可结转
				flag = 1;
			} else {
				// 分月返回并且不可结转
				flag = 2;
			}
		}

		if (3 == monthInterval) {
			if (1 == isCycleDepend) {
				// 分季度返还并且可结转
				flag = 3;
			} else {
				// 分季度返还并且不可结转
				flag = 4;
			}
		}
		return flag;
	}
//
//	/**
//	 * 新建线程池
//	 *
//	 * @param threadNamePrefix 线程名称前缀 batch_payment-%d
//	 * */
//	public static ExecutorService getExecutorService(int corePoolSize, int maxPoolSize, int queueSize, String threadNamePrefix){
//		ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
//
//		ExecutorService execService = new ThreadPoolExecutor(corePoolSize,maxPoolSize,60,TimeUnit.SECONDS,
//				new LinkedBlockingDeque<>(queueSize),threadFactory,new ThreadPoolExecutor.AbortPolicy());
//
//		return execService;
//	}
//
//	public static String getExceptionDetailMessage(Throwable ex){
//		StringWriter sw = null;
//		PrintWriter pw = null;
//		try {
//			sw = new StringWriter();
//			pw = new PrintWriter(sw,true);
//			if (StringUtils.isNotBlank(ex.getMessage())) {
//				pw.write(ex.getMessage());
//			}else{
//				ex.printStackTrace(pw);
//			}
//			pw.flush();
//			sw.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (pw != null) {
//				pw.close();
//			}
//			if (sw != null) {
//				try {
//					sw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		return sw == null ? ex.getMessage():sw.toString();
//
//	}
//
//    public static Object map2Obj(Map<String, Object> map, Class<?> clz) throws Exception {
//        Object obj = clz.newInstance();
//        Field[] declaredFields = obj.getClass().getDeclaredFields();
//        for(Field field:declaredFields){
//          int mod = field.getModifiers();
//          if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
//          continue;
//          }
//          field.setAccessible(true);
//          field.set(obj, map.get(field.getName()));
//        }
//        return obj;
//  }
//
//    public static String kb2mg(String flowkb, int fixCount)
//    {
//        if("0".equals(flowkb)){
//            return "0.00KB";
//        }
//        StringBuffer partn = new StringBuffer();
//        if(fixCount > 0){
//            partn.append("#.");
//            for(int i = 0;i < fixCount; i++){
//                partn.append("0");
//            }
//        }else{
//            partn.append("#.00");
//        }
//        java.text.DecimalFormat   df   =new   java.text.DecimalFormat(partn.toString());
//        double munKb = Double.parseDouble(flowkb);
//        String strMG=munKb+"KB";
//        if (munKb<1024)//小于M则KB
//        {
//            strMG=munKb+"KB";
//        }else if (munKb<1024*1024)//小于G则M
//        {
//            strMG=df.format(((munKb/1024*100)/100))+"MB";
//        }
//        else{ //大于G为G
//            strMG=df.format(((munKb/1024/1024*100)/100))+"G";
//        }
//        return  strMG;
//    }
//
//    public static String getParamForGET(Map<String, Object> paramMap) {
//        String param = "";
//        for (Map.Entry<String, ?> m : paramMap.entrySet()) {
//            String key = m.getKey();
//            if(param.length() < 1){
//                param += "?";
//            }else {
//                param += "&";
//            }
//            param += key + "=" + paramMap.get(key) + "";
//        }
//        return param ;
//    }
////	/**
////	 * 获取随机数:线程编号+随机数共10位
////	 *
////	 * @return String
////	 */
////	private static String getRandom() {
////		String randStr = "";
////		String threadId = String.valueOf(Thread.currentThread().getId());
////		int len = threadId.length();
////		if (len >= 10) {
////			threadId = threadId.substring(len - 10);
////		}else {
////			for (int i = 0; i < 10 - len; i++) {
////				randStr = randStr + (int) (Math.random() * 10.0D);
////			}
////		}
////		return threadId + randStr;
////	}
//
//
//    public  static String getRequestSeq(){
//        StringBuffer sb=new StringBuffer();
//        sb.append(BaseUtil.format(new Date(),BaseUtil.DATETIME_PATTERN));
//        sb.append("IAM");
//        Random rd=new Random();
//        for(int i=0;i<6;i++){
//            sb.append(rd.nextInt(10));
//        }
//        return  sb.toString();
//    }
//
//    /**
//     * 返回当前字符串型日期
//     *
//     * @param format
//     *            格式规则
//     *
//     * @return String 返回的字符串型日期
//     */
//    public static String getCurDate(String format) {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
//        String strDate = simpledateformat.format(calendar.getTime());
//        return strDate;
//    }
//
//    /**
//	 * 解析ABM接口响应信息
//	 * */
//	public static ABMResponseInfo parseABMServiceResponse(String requestID, String interfaceName, ABMServiceResult abmResult){
//		ABMResponseInfo abmResponseInfo = new ABMResponseInfo();
//		if (abmResult == null) {
//			return abmResponseInfo;
//		}
//		if (200 == abmResult.getStatusCode()) {
//			String responXml = abmResult.getResultData();
//			if (StringUtils.isNotBlank(responXml)) {
//				String splitprefix = "<return>";
//				String splitsuffix = "</return>";
//				responXml = responXml.substring(responXml.indexOf(splitprefix) + splitprefix.length(),
//						responXml.indexOf(splitsuffix));
//				responXml = responXml.replaceAll("&lt;","<");
//				responXml = responXml.replaceAll("&gt;",">");
//				//abmResponseInfo = (ABMResponseInfo)xml2Bean(responXml,ABMResponseInfo.class);
//                abmResponseInfo = xml2BeanSingleton(responXml);
//			}
//
//		}else{
//			//abmResponseInfo.setResultCode(String.valueOf(abmResult.getStatusCode()));
//			abmResponseInfo.setResultCode("-1");
//			abmResponseInfo.setInterfaceName(interfaceName);
//			abmResponseInfo.setResultMsg(abmResult.getResultData());
//			abmResponseInfo.setResponseId(StringUtils.isBlank(requestID)?"0":requestID);
//
//		}
//		return abmResponseInfo;
//	}
//
//	public static ABMResponseInfo xml2BeanSingleton(String abmResponseXml){
//        //ABMResponseInfo abmResponseInfo = (ABMResponseInfo)SingletonXStreamHandler.getSingletonXtream().fromXML(abmResponseXml);
//		ABMResponseInfo abmResponseInfo = (ABMResponseInfo)SingletonXStreamHandler.SINGLETON_X_STREAM_HANDLER.getSingletonXtream().fromXML(abmResponseXml);
//		return abmResponseInfo;
//    }
//
//
//    /**
//     * 获取当前月份的前6个月，计算前6个月流程处理的平均时长
//     *
//     * @return String
//     */
//    public static List getLastSixMonths(){
//        List<String> list = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMM");
//        String thisMonth = "";
//        thisMonth = formatters.format(today);
//        today = today.minusMonths(1);
//        for(int i=0;i<6;i++){
//            list.add(formatters.format(today.minusMonths(i)));
//        }
//        return list;
//    }
//    /**
//     * 拼接查询节点
//     * \/*!HINT({"dn":["dn1","dn2",......,"dnx"]})*\/
//     * */
//    public static String joinTableNodes4QuerySql(List<String> tableNodes){
//
//        if (CollectionUtils.isEmpty(tableNodes)) {
//            return "";
//        }
//        // /*!HINT({"dn":["dev_billing_acct_1","dev_billing_acct_2","dev_billing_acct_3","dev_billing_acct_4","dev_billing_acct_5","dev_billing_acct_6","dev_billing_acct_7","dev_billing_acct_8"]})*/
//        StringBuffer qryPrefix = new StringBuffer();
//        qryPrefix.append("/*!HINT({\"dn\":[");
//        for (int i = 0; i < tableNodes.size(); i++) {
//            if (i == 0) {
//                qryPrefix.append("\"").append(tableNodes.get(i)).append("\"");
//            }else{
//                qryPrefix.append(",\"").append(tableNodes.get(i)).append("\"");
//            }
//        }
//        qryPrefix.append("]})*/");
//        return qryPrefix.toString();
//    }



    /**
     * @param corePoolSize    常驻核心线程，线程池初始化的时候池里是没有线程的，前面 corePoolSize 个任务是会创建线程，
     *                        当前线程池中的数量大于常驻核心线程数的时候，如果有空闲的线程则使用，没有的话就把任务放到
     *                        工作队列中
     * @param maximumPoolSize 线程池允许创建的最大线程数，如果队列满了，且线程数小于最大线程数，则新建临时线程（空闲超过时间会被销毁的），
     *                        如果队列为无界队列，则该参数无用
     * @param workQueueSize   工作队列，请求线程数大于常驻核心线程数的时候，将多余的任务放到工作队列
     * @param threadName      线程名称
     * @param handler         线程池拒绝策略，当线程池和队列都满了，则调用该策略，执行具体的逻辑
     * @desc: 自定义线程池的实现 总体逻辑就是 前corePoolSize个任务时，来一个任务就创建一个线程
     * 如果当前线程池的线程数大于了corePoolSize那么接下来再来的任务就会放入到我们上面设置的workQueue队列中
     * 如果此时workQueue也满了，那么再来任务时，就会新建临时线程，那么此时如果我们设置了keepAliveTime或者设置了allowCoreThreadTimeOut，那么系统就会进行线程的活性检查，一旦超时便销毁线程
     * 如果此时线程池中的当前线程大于了maximumPoolSize最大线程数，那么就会执行我们刚才设置的handler拒绝策略
     ***/

    public static ExecutorService createThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   int workQueueSize,
                                                   String threadName,
                                                   RejectedExecutionHandler handler) {
        BlockingQueue workQueue = new LinkedBlockingDeque(workQueueSize);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS, workQueue,
                handler == null ? new ThreadPoolExecutor.AbortPolicy() : handler);
        // 提前创建好核心线程
        //threadPoolExecutor.prestartAllCoreThreads();
        // 常驻核心线程的空闲时间超过 keepAliveTime 的时候要被回收
        //threadPoolExecutor.allowCoreThreadTimeOut(true);

        return threadPoolExecutor;
    }
}
