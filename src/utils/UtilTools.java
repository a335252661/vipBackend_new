package utils;



import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具
 * 
 * @author mark
 *
 */
public class UtilTools {
	
	// 系统路径 分隔符
	public static String fileSeparator = System.getProperty("file.separator");
	
	//常量
	public static String start = "start";
	public static String end = "end";
	public static String erro = "erro";
	
	
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static final double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	

	/**
	 * 字符串转换为日期 yyyyMMddHHmmss
	 */
	public static Date string2Date(String fromat, String strDate) throws ParseException {
		SimpleDateFormat dateTimeSf = new SimpleDateFormat(fromat);
		return dateTimeSf.parse(strDate);
	}
	
	/**
	 * 日期转换为字符串（yyyyMMddHHmmss）
	 */
	public static String date2String(String fromat, Date date){
		SimpleDateFormat dateTimeSf = new SimpleDateFormat(fromat);
		return dateTimeSf.format(date);
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
	 * 返回当前日期
	 * @param s
	 * @return
	 */
	public static String printSysDate(){
		return date2String("yyyy-MM-dd HH:mm:ss",new Date());
	}

	public static boolean isNotEmpty(String s){
        if(null != s && s.trim().length() > 0){
            return true;
        }
        return false;
    }
	
	public static boolean isEmpty(String s){
		return !isNotEmpty(s);
	}
	
	/**
	 * 字符串转数值
	 */
	public static int stringToInt(String str){
		int num = 0;
		try {
			num = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return num;
	}
	
	/**
	 * 字符串数组 转 数值
	 */
	public static String stringArrayToString(String[] array, int i){
		String str = "";
		try {
			str = array[i];
		} catch (Exception e) {
		}
		if (str == null || str.equalsIgnoreCase("null")) {
			return "";
		}
		return str;
	}
	
	/**
	 * 字符串数组 转 字符串
	 */
	public static int stringArrayToInt(String[] array, int i){
		int num = 0;
		try {
			num = Integer.parseInt(array[i]);
		} catch (Exception e) {
		}
		return num;
	}
	
	/**
	 * 字符串数组 转 字符串
	 */
	public static long stringArrayToLong(String[] array, int i){
		long num = 0;
		try {
			num = Long.parseLong(array[i]);
		} catch (Exception e) {
		}
		return num;
	}
	
/**
	 * 删除文件
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file){
		int count = 0;
		while (count < 3) {
			file.delete();
			if (!file.exists()) {
				return true;
			}
			count++;
		}
		return false;
	}

	/**
	 * rename文件
	 */
	public static boolean rename(File srcFile, File destFile){
		int count = 0;
		while(!(srcFile.renameTo(destFile))){
			count++;
			if (count > 10) {
				break;
			}
		}
		if (!destFile.exists()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 数字转化为定长的字符串
	 * @param numStr 要转换的数字
	 * @param prefixStr 填充的字符
	 * @param num 长度
	 * @return
	 */
	public static String fromatNumberStr(String numStr, String prefixStr, int num){
		int strNum = numStr.length();
		for (int i = 0; i < num - strNum; i++) {
			numStr = prefixStr + numStr;
		}
		return numStr;
	}
	
	/**
	 * 匹配正则表达式
	 */
	public static boolean checkExpression(String expression, String str){
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static void creatSmsFile(String srcPath, String destPath, String filename, String str)
	{
		File srcFile = new File(srcPath, filename);
		OutputStreamWriter fileWriter = null ;
		try {
			fileWriter = new OutputStreamWriter(new FileOutputStream(srcFile), "GBK");
			fileWriter.write(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fileWriter != null)
			{
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(srcFile.exists())
		{
			File destFile = new File(destPath, filename);
			rename(srcFile, destFile);
		}
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String formatDateToString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		
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

	/**
	 * 从大集合中取等量数据
	 *
	 * @param list 大集合
	 * @param num  每次取的个数
	 * @return
	 */
	public static List<List> subList(List list, int num) {
		List<List> returnList = new ArrayList();
		for (int i = 0; i < list.size(); i = i + num) {
			//最后一次截取集合
			if (i + num > list.size()) {
				num = list.size() - i;
			}
			List newList = list.subList(i, i + num);
			returnList.add(newList);
		}
		return returnList;
	}

	/**
	 *
	 * @param list
	 * @param num  分成 多少个集合
	 *
	 *             3490
	 *             分成100个集合
	 *
	 * @return
	 */
	public static List<List> subList2(List list, int num) {

		Integer total  = list.size();
		BigDecimal mm =  new BigDecimal(total).divide(new BigDecimal(num),0);
		List<List> lists = subList(list, mm.intValue());
		List<List> returnList = new ArrayList();
		return lists;
	}


	// 判断文件是否存在
	public static void judeFileExists(String location) {
		File file = new File(location);
		if (file.exists()) {
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static String makeFF() {
		String format1 = new SimpleDateFormat("dd").format(new Date());
		String format2 = new SimpleDateFormat("hh").format(new Date());
		return format1.substring(1) + format2.substring(1);
	}

	public static String zeroPadding(int m) {
		String cuf = "";
		String mm = m + "";
		if (mm.length() == 1) {
			cuf = ".00" + m;
		} else if (mm.length() == 2) {
			cuf = ".0" + m;
		} else if (mm.length() == 3) {
			cuf = "." + m;
		}else if (mm.length() == 4) {
			cuf = "." + m;
		}
		return cuf;
	}

	/** */
	/**
	 * 文件重命名
	 *
	 * @param path    文件目录
	 * @param oldname 原来的文件名
	 * @param newname 新文件名
	 */
	public static void renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists()) {
				return;//重命名文件不存在
			}
			if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else {
				oldfile.renameTo(newfile);
			}
		} else {
			System.out.println("新文件名和旧文件名相同...");
		}
	}

	public static String getLastYearAndMonth() {
		//文件名时间为当前月份减一个月
		Date date = new Date();//获取当前时间 ? ?
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间 ? ?
		calendar.getTime();//获取一年前的时间，或者一个月前的时间 ? ?
		Date dt1 = calendar.getTime();
		String sys_datetime = new SimpleDateFormat("yyyyMM").format(dt1);
		return sys_datetime;
	}
	
}

