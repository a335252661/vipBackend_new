package utils;


import org.apache.log4j.spi.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期工具类
 *
 * @author Tommy
 *
 */
public class DateTimeUtils {
	/**
	 * 日期时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateTimeString = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期时间格式 yyyyMMdd
	 */
	public static String dateTimeString2="yyyyMMdd";

	/**
	 * 日期格式 yyyy-MM-dd
	 */
	public static String dateString = "yyyy-MM-dd";

	/**
	 * 日期时间格式 yyyy-MM-dd'T'HH:mm:ssXXX 例如 2016-05-06T15:46:31+08:00
	 */
	public static String dateTimeString4 = "yyyy-MM-dd'T'HH:mm:ssXXX";

	/**
	 * 日期时间格式For 文件名 yyyy_MM_dd_HH_mm_ss
	 */
	public static String dateTimeString4FileName = "yyyy_MM_dd_HH_mm_ss";

	public final static String YYYY = "yyyy";
	public final static String YYYY_MM = "yyyy-MM";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public final static String YYYY_MM_DD_HH_mm = "yyyy-MM-dd HH:mm";
	public final static String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYY_MM_DD_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public final static String YY_MM_DD_HH_mm = "yyMMddHHmm";
	public final static String MM = "MM";
	public final static String YYYYMM = "yyyyMM";
	public final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public final static String dateString_D = "d";
	public final static String yyyyMMdd = "yyyy/MM/dd";
	/**
	 * 日期时间格式 yyyy/MM/dd HH:mm:ss
	 */
	public static String dateTimeString3 = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 日
	 */
	public final static int INTERVAL_DAY = 1;
	/**
	 * 周
	 */
	public final static int INTERVAL_WEEK = 2;
	/**
	 * 月
	 */
	public final static int INTERVAL_MONTH = 3;
	/**
	 * 年
	 */
	public final static int INTERVAL_YEAR = 4;
	/**
	 * 小时
	 */
	public final static int INTERVAL_HOUR = 5;
	/**
	 * 分钟
	 */
	public final static int INTERVAL_MINUTE = 6;
	/**
	 * 秒
	 */
	public final static int INTERVAL_SECOND = 7;

//	private static final Logger logger = LoggerFactory
//			.getLogger(DateTimeUtils.class);
//
	/**
	 * 解决日期字符串,日期格式为： yyyy-MM-dd HH:mm:ss
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStr(String dateStr) {
		return parseStr(dateStr, dateTimeString);
	}


	/**
	 * 解决日期字符串
	 *
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parseStr(String dateStr, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date resultDate = null;
		try {
			resultDate = df.parse(dateStr);
		} catch (ParseException e) {
			resultDate = null;
		}
		return resultDate;
	}

	/**
	 * 解决日期字符串，异常抛出不做处理
	 *
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws ParseException 异常
	 */
	public static Date parseStrThrowException(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date resultDate = null;
		resultDate = df.parse(dateStr);
		return resultDate;
	}

	/**
	 * 格式化
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null || pattern == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		String result = df.format(date);
		if (result.equalsIgnoreCase("0001-01-01 00:00:00")) {
			result = "";
		}
		return result;
	}

	/**
	 * 格式化，yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date) {
		return format(date, dateTimeString);
	}

	/**
	 * 格式化，yyyy-MM-dd
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatWithoutTime(Date date) {
		return format(date, dateString);
	}

	/**
	 * 两date比较
	 *
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static int compareDate(Date beforeDate, Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		Calendar afterCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		afterCalendar.setTime(afterDate);
		return beforeCalendar.compareTo(afterCalendar);
	}

	/**
	 * 判断目标日期是否在时间段类
	 *
	 * @param beforeDate
	 * @param afterDate
	 * @param targetDate
	 * @return
	 */
	public static boolean isBetweenDate(Date beforeDate, Date afterDate,
                                        Date targetDate) {
		if (targetDate == null) {
			throw new RuntimeException("targetDate should not be null!");
		}
		if (beforeDate == null && afterDate == null) {
			return false;
		}
		if (afterDate == null) {
			return (compareDate(beforeDate, targetDate) <= 0);
		}
		if (beforeDate == null) {
			return (compareDate(targetDate, afterDate) <= 0);
		}
		return (compareDate(beforeDate, targetDate) <= 0)
				&& (compareDate(targetDate, afterDate) <= 0);
	}

	/**
	 *
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateOperateByMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date dateOperateByDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 邮箱激活，时间操作，对验证时间+24H
	 *
	 * @param date
	 *            需要操作的时间，如果为null,就去当前时间
	 * @param hour
	 *            小时，对操作时间增加或减少的量
	 * @author chj
	 */
	public static Date dateOperateByHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date == null ? new Date() : date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}

	/**
	 * 验证手机验证码过期，时间操作，对验证时间+有效时间
	 *
	 * @param date
	 *            需要操作的时间，如果为null,就去当前时间
	 * @param minute
	 *            分钟，对操作时间增加或减少的量
	 * @author chj
	 */
	public static Date dateOperateByMinute(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date == null ? new Date() : date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static Date dateOperateBySecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date getDateWithoutTime(Date date) {
		Date result = null;
		String dateStr = format(date, YYYY_MM_DD_HH_mm_ss);
		result = parseStr(dateStr, YYYY_MM_DD_HH_mm_ss);
		return result;
	}

	/**
	 * 增加时间
	 *
	 * @param interval
	 *            [INTERVAL_DAY,INTERVAL_WEEK,INTERVAL_MONTH,INTERVAL_YEAR,
	 *            INTERVAL_HOUR,INTERVAL_MINUTE]
	 * @param date
	 * @param n
	 *            可以为负数
	 * @return
	 */
	public static Date dateAdd(int interval, Date date, int n) {
		long time = (date.getTime() / 1000); // 单位秒
		switch (interval) {
		case INTERVAL_DAY:
			time = time + n * 86400;// 60 * 60 * 24;
			break;
		case INTERVAL_WEEK:
			time = time + n * 604800;// 60 * 60 * 24 * 7;
			break;
		case INTERVAL_MONTH:
			time = time + n * 2678400;// 60 * 60 * 24 * 31;
			break;
		case INTERVAL_YEAR:
			time = time + n * 31536000;// 60 * 60 * 24 * 365;
			break;
		case INTERVAL_HOUR:
			time = time + n * 3600;// 60 * 60 ;
			break;
		case INTERVAL_MINUTE:
			time = time + n * 60;
			break;
		case INTERVAL_SECOND:
			time = time + n;
			break;
		default:
		}

		Date result = new Date();
		result.setTime(time * 1000);
		return result;
	}

	/**
	 * 计算两个时间间隔
	 *
	 * @param interval
	 *            [INTERVAL_DAY,INTERVAL_WEEK,INTERVAL_MONTH,INTERVAL_YEAR,
	 *            INTERVAL_HOUR,INTERVAL_MINUTE]
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int dateDiff(int interval, Date begin, Date end) {
		long beginTime = (begin.getTime() / 1000); // 单位：秒
		long endTime = (end.getTime() / 1000); // 单位: 秒
		long tmp = 0;
		if (endTime == beginTime) {
			return 0;
		}

		// 确定endTime 大于 beginTime 结束时间秒数 大于 开始时间秒数
		if (endTime < beginTime) {
			tmp = beginTime;
			beginTime = endTime;
			endTime = tmp;
		}

		long intervalTime = endTime - beginTime;
		long result = 0;
		switch (interval) {
		case INTERVAL_DAY:
			result = intervalTime / 86400;// 60 * 60 * 24;
			break;
		case INTERVAL_WEEK:
			result = intervalTime / 604800;// 60 * 60 * 24 * 7;
			break;
		case INTERVAL_MONTH:
			result = intervalTime / 2678400;// 60 * 60 * 24 * 31;
			break;
		case INTERVAL_YEAR:
			result = intervalTime / 31536000;// 60 * 60 * 24 * 365;
			break;
		case INTERVAL_HOUR:
			result = intervalTime / 3600;// 60 * 60 ;
			break;
		case INTERVAL_MINUTE:
			result = intervalTime / 60;
			break;
		case INTERVAL_SECOND:
			result = intervalTime / 1;
			break;
		default:
		}

		// 做过交换
		if (tmp > 0) {
			result = 0 - result;
		}
		return (int) result;
	}

	/**
	 * date转long
	 *
	 * @param date
	 * @return
	 */
	public static Long convertToLong(Date date) {
		if(date == null) return null;
		return date.getTime();
	}

	/**
	 * 获得当前时间X个月的一天
	 *
	 * @param x
	 * @return
	 */
	public static Date getBreforeDate(int x) {
		Date currentTime = new Date();// 得到当前系统时间
		long now = currentTime.getTime();// 返回自 1970 年 1 月 1 日 00:00:00 GMT
											// 以来此Date 对象表示毫秒数
		for (int i = 0; i < x; i++) {
			currentTime = new Date(now - (long) (1000l * 3600l * 24l * 30l)); // 减去24天
			now = currentTime.getTime();
		}
		return currentTime;
	}


	/**
	 * 获取上月第一天
	 *
	 * @return
	 */
	public static Date getBeforeMouthFirstDay(){
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
	}

	/**
	 * 获取上月最后天
	 *
	 * @return
	 */
	public static Date getBeforeMouthLastDay(){
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
	}

	/**
	 * 获取当前日期的时间戳
	 *
	 * @return
	 */
	public static String getDateTimeString(String formate){
		SimpleDateFormat format = new SimpleDateFormat(formate);
		return format.format(new Date());
	}
	public static String getDateTimeString(){
		SimpleDateFormat format = new SimpleDateFormat(DateTimeUtils.yyyyMMddHHmmss);
		return format.format(new Date());
	}

	/**
	 * 获取当前日期的时间戳:DAS下发用，没有微秒
	 *
	 * @return
	 */
	public static String getDateTimeStringDas(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}

	/**
	 * 获取当前日期的时间戳:年月日到分钟
	 *
	 * @return
	 */
	public static String getLotNoForDateTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
		return format.format(new Date());
	}



	/**
	* 计算两个日期的时间差
	* @param maxDate
	* @param minDate
	* @return
	*/
	public static String getTimeDifference(Date maxDate, Date minDate) {
		long maxDateTime = maxDate.getTime();
		long minDateTime = minDate.getTime();
		// 因为maxDateTime-minDateTime得到的是毫秒级,所以要初3600000得出小时.算天数或秒同理
		int hours = (int) ((maxDateTime - minDateTime) / 3600000);
		int minutes = (int) (((maxDateTime - minDateTime) / 1000 - hours * 3600) / 60);
		int second = (int) ((maxDateTime - minDateTime) / 1000 - hours * 3600 - minutes * 60);

		//小时<10，补0
		String hourStr = ""+hours;
		if(hours<10){
			hourStr = "0"+hours;
		}

		//分钟<10，补0
		String minuteStr = ""+minutes;
		if(minutes<10){
			minuteStr = "0"+minutes;
		}

		//秒<10，补0
		String secondStr = ""+second;
		if(second<10){
			secondStr = "0"+second;
		}

		//秒为0，时就不显示秒
//		if(second==0){
//			return hourStr + ":" + minuteStr;
//		}
		return hourStr + ":" + minuteStr + ":" + secondStr;
	}

	/**
	* 计算反馈日期是否在反馈周期内
	* @param feedbackDate
	* @return
	*/
	public static boolean isFeedbackInDate(Date feedbackDate){
		Date start = getDateWithoutTime(new Date());
		Date end = dateAdd(INTERVAL_DAY,start,1);
		if(feedbackDate.compareTo(start) >= 0 && feedbackDate.compareTo(end) == -1){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * 得到当前时间的时分秒
	 * @return
	 */
	public static String getNowTime(){
		String timeStr = DateTimeUtils.format(new Date(), "HH:mm:ss");
		return timeStr;
	}

	public static String getNowTimeSSS(){
		String timeStr = DateTimeUtils.format(new Date(), "HHmmssSSS");
		return timeStr;
	}
}
