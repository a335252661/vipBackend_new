package vip.guoMan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * �����ĸ�����
 * @author Administrator
 *
 */
public class BaseUtils {

	private static Log log = LogFactory.getLog(BaseUtils.class);

	/**
	 * DES�㷨��Կ
	 */
	private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };

	private static String HanDigiStr[] = new String[] { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };

	private static String HanDiviStr[] = new String[] { "", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ" };	

	/**
	 * �ж϶����Ƿ�Empty(null��Ԫ��Ϊ0)<br>
	 * ʵ���ڶ����¶������ж�:String Collection�������� Map��������
	 * 
	 * @param pObj
	 *            ��������
	 * @return boolean ���صĲ���ֵ
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
	 * �ж϶����Ƿ�ΪNotEmpty(!null��Ԫ��>0)<br>
	 * ʵ���ڶ����¶������ж�:String Collection�������� Map��������
	 * 
	 * @param pObj
	 *            ��������
	 * @return boolean ���صĲ���ֵ
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
	 * ������ת��Ϊ��д��ʽ(���ڲ�����)
	 * 
	 * @param val
	 * @return String
	 */
	private static String PositiveIntegerToHanStr(String NumStr) {
		// �����ַ���������������ֻ����ǰ���ո�(�����Ҷ���)��������ǰ����
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // �ڡ����λǰ����ֵ���
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "��ֵ����!";
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "���뺬�������ַ�!";

			if (n != 0) {
				if (lastzero)
					RMBStr += HanDigiStr[0]; // ���������������ֵ��ֻ��ʾһ����
				// ��������ǰ���㲻��������
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )
				// ��ʮ��λǰ����Ҳ����Ҽ���ô���
				if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // ʮ��λ���ڵ�һλ����Ҽ��
					RMBStr += HanDigiStr[n];
				RMBStr += HanDiviStr[i]; // ����ֵ��ӽ�λ����λΪ��
				hasvalue = true; // �����λǰ��ֵ���

			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // ����֮������з���ֵ����ʾ��
					RMBStr += HanDiviStr[i]; // ���ڡ�����
			}
			if (i % 8 == 0)
				hasvalue = false; // ���λǰ��ֵ��Ƿ��ڸ�λ
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0)
			return HanDigiStr[0]; // ������ַ���"0"������"��"
		return RMBStr;
	}

	/**
	 * ������ת��Ϊ��д��ʽ
	 * 
	 * @param val
	 *            ���������
	 * @return String ���ص�����Ҵ�д��ʽ�ַ���
	 */
	public static String numToRMBStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "��";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "��ֵλ������!";
		// �������뵽��
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "��";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "��";
			// ��Ԫ��д�㼸��
			if (integer == 0 && jiao == 0)
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "��";
		}
		// ��һ�п����ڷ�������ڳ��ϣ�0.03ֻ��ʾ�����֡������ǡ���Ԫ���֡�
		// if( !integer ) return SignStr+TailStr;
		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "Ԫ" + TailStr;
	}

	/**
	 * ��ȡָ����ݺ��·ݶ�Ӧ������
	 * 
	 * @param year
	 *            ָ�������
	 * @param month
	 *            ָ�����·�
	 * @return int ��������
	 */
	public static int getDaysInMonth(int year, int month) {
		if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
				|| (month == 12)) {
			return 31;
		} else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			return 30;
		} else {
			if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {
				return 29;
			} else {
				return 28;
			}
		}
	}

	/**
	 * ������������ֹʱ����������������
	 * 
	 * @param startDate
	 *            ��ʼʱ��
	 * @param endDate
	 *            ����ʱ��
	 * @return int ���ؼ������
	 */
	public static int getIntervalDays(java.sql.Date startDate, java.sql.Date endDate) {
		long startdate = startDate.getTime();
		long enddate = endDate.getTime();
		long interval = enddate - startdate;
		int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
		return intervalday;
	}

	/**
	 * ������������ֹʱ����������������
	 * 
	 * @param startDate
	 *            ��ʼʱ��
	 * @param endDate
	 *            ����ʱ��
	 * @return int ���ؼ������
	 */
//	public static int getIntervalMonths(java.sql.Date startDate, java.sql.Date endDate) {
//		Calendar startCal = Calendar.getInstance();
//		startCal.setTime(startDate);
//		Calendar endCal = Calendar.getInstance();
//		endCal.setTime(endDate);
//		int startDateM = startCal.MONTH;
//		int startDateY = startCal.YEAR;
//		int enddatem = endCal.MONTH;
//		int enddatey = endCal.YEAR;
//		int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
//		return interval;
//	}
	
	/**
	 * ������������ֹʱ����������������
	 * 
	 * @param startDate
	 *            ��ʼʱ��
	 * @param endDate
	 *            ����ʱ��
	 * @return int ���ؼ����������ֵ
	 */
	public static int getIntervalMonths(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int startDateM = startCal.get(Calendar.MONTH)+1;
		int startDateY = startCal.YEAR;
		int enddatem = endCal.get(Calendar.MONTH)+1;
		int enddatey = endCal.YEAR;
		int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
		
		return Math.abs(interval);
	}

	/**
	 * ���ص�ǰ����ʱ���ַ���<br>
	 * Ĭ�ϸ�ʽ:yyyy-mm-dd hh:mm:ss
	 * 
	 * @return String ���ص�ǰ�ַ���������ʱ��
	 */
	public static String getCurrentTime() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}
	
	/**
	 * ���ص�ǰ����ʱ���ַ���<br>
	 * Ĭ�ϸ�ʽ:yyyymmddhhmmss
	 * 
	 * @return String ���ص�ǰ�ַ���������ʱ��
	 */
	public static BigDecimal getCurrentTimeAsNumber() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		returnStr = f.format(date);
		return new BigDecimal(returnStr);
	}


	/**
	 * �����Զ����ʽ�ĵ�ǰ����ʱ���ַ���
	 * 
	 * @param format
	 *            ��ʽ����
	 * @return String ���ص�ǰ�ַ���������ʱ��
	 */
	public static String getCurrentTime(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	/**
	 * ���ص�ǰ�ַ���������
	 * 
	 * @return String ���ص��ַ���������
	 */
	public static String getCurDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = simpledateformat.format(calendar.getTime());
		return strDate;
	}

	/**
	 * ����ָ����ʽ���ַ�������
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String Date2String(Date date, String formatString) {
		if (BaseUtils.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat(formatString);
		String strDate = simpledateformat.format(date);
		return strDate;
	}
	
	/**
	 * ���ص�ǰ�ַ���������
	 * 
	 * @param format
	 *            ��ʽ����
	 * 
	 * @return String ���ص��ַ���������
	 */
	public static String getCurDate(String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
		String strDate = simpledateformat.format(calendar.getTime());
		return strDate;
	}



	/**
	 * ���ַ���������ת��Ϊ������
	 * 
	 * @param strDate
	 *            �ַ���������
	 * @param srcDateFormat
	 *            Դ���ڸ�ʽ
	 * @param dstDateFormat
	 *            Ŀ�����ڸ�ʽ
	 * @return Date ���ص�util.Date������
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
	 * �ϲ��ַ�������
	 * 
	 * @param a
	 *            �ַ�������0
	 * @param b
	 *            �ַ�������1
	 * @return ���غϲ�����ַ�������
	 */
	public static String[] mergeStringArray(String[] a, String[] b) {
		if (a.length == 0 || isEmpty(a))
			return b;
		if (b.length == 0 || isEmpty(b))
			return a;
		String[] c = new String[a.length + b.length];
		for (int m = 0; m < a.length; m++) {
			c[m] = a[m];
		}
		for (int i = 0; i < b.length; i++) {
			c[a.length + i] = b[i];
		}
		return c;
	}

	/**
	 * ���ļ���������ص������ļ������б��� ���θ���������汾�Ĳ�����
	 */
	public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) {
		String agent = request.getHeader("USER-AGENT");
		try {
			if (null != agent && -1 != agent.indexOf("MSIE")) {
				pFileName = URLEncoder.encode(pFileName, "utf-8");
			} else {
				pFileName = new String(pFileName.getBytes("utf-8"), "iso8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pFileName;
	}

	/**
	 * �������ڻ�ȡ����
	 * 
	 * @param strdate
	 * @return
	 */
	public static String getWeekDayByDate(String strdate) {
		final String dayNames[] = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	/**
	 * �ж��Ƿ���IE�����
	 * 
	 * @param userAgent
	 * @return
	 */
	public static boolean isIE(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		boolean isIe = true;
		int index = userAgent.indexOf("msie");
		if (index == -1) {
			isIe = false;
		}
		return isIe;
	}

	/**
	 * �ж��Ƿ���Chrome�����
	 * 
	 * @param userAgent
	 * @return
	 */
	public static boolean isChrome(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		boolean isChrome = true;
		int index = userAgent.indexOf("chrome");
		if (index == -1) {
			isChrome = false;
		}
		return isChrome;
	}

	/**
	 * �ж��Ƿ���Firefox�����
	 * 
	 * @param userAgent
	 * @return
	 */
	public static boolean isFirefox(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		boolean isFirefox = true;
		int index = userAgent.indexOf("firefox");
		if (index == -1) {
			isFirefox = false;
		}
		return isFirefox;
	}

	/**
	 * ��ȡ�ͻ�������
	 * 
	 * @param userAgent
	 * @return
	 */
	public static String getClientExplorerType(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String explorer = "�����������";
		if (isIE(request)) {
			int index = userAgent.indexOf("msie");
			explorer = userAgent.substring(index, index + 8);
		} else if (isChrome(request)) {
			int index = userAgent.indexOf("chrome");
			explorer = userAgent.substring(index, index + 12);
		} else if (isFirefox(request)) {
			int index = userAgent.indexOf("firefox");
			explorer = userAgent.substring(index, index + 11);
		}
		return explorer.toUpperCase();
	}

	/**
	 * ����MD5�㷨�ĵ������
	 * 
	 * @param strSrc
	 *            ����
	 * @return ��������
	 */
	public static String encryptBasedMd5(String strSrc) {
		String outString = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] outByte = md5.digest(strSrc.getBytes("UTF-8"));
			outString = outByte.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outString;
	}

	/**
	 * ���ݼ��ܣ��㷨��DES��
	 * 
	 * @param data
	 *            Ҫ���м��ܵ�����
	 * @return ���ܺ������
	 */
	public static String encryptBasedDes(String data) {
		String encryptedData = null;
		try {
			// DES�㷨Ҫ����һ�������ε������Դ
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			// ����һ���ܳ׹�����Ȼ��������DESKeySpecת����һ��SecretKey����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			// ���ܶ���
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// ���ܣ������ֽ����������ַ���
			encryptedData = new BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
		} catch (Exception e) {
			log.error("���ܴ��󣬴�����Ϣ��", e);
			throw new RuntimeException("���ܴ��󣬴�����Ϣ��", e);
		}
		return encryptedData;
	}

	/**
	 * ���ݽ��ܣ��㷨��DES��
	 * 
	 * @param cryptData
	 *            ��������
	 * @return ���ܺ������
	 */
	public static String decryptBasedDes(String cryptData) {
		String decryptedData = null;
		try {
			// DES�㷨Ҫ����һ�������ε������Դ
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			// ����һ���ܳ׹�����Ȼ��������DESKeySpecת����һ��SecretKey����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			// ���ܶ���
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// ���ַ�������Ϊ�ֽ����飬������
			decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
		} catch (Exception e) {
			log.error("���ܴ��󣬴�����Ϣ��", e);
			throw new RuntimeException("���ܴ��󣬴�����Ϣ��", e);
		}
		return decryptedData;
	}

	/**
	 * JS�������\n�����⴦��
	 * 
	 * @param pStr
	 * @return
	 */
	public static String replace4JsOutput(String pStr) {
		pStr = pStr.replace("\r\n", "<br/>&nbsp;&nbsp;");
		pStr = pStr.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		pStr = pStr.replace(" ", "&nbsp;");
		return pStr;
	}
	
	/**
	 * ��ȡclass�ļ����ھ���·��
	 * 
	 * @param cls
	 * @return
	 * @throws IOException
	 */
	public static String getPathFromClass(Class cls) {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}

	/**
	 * �����������ͨ����ĳ�����class�ļ������·������ȡ�ļ���Ŀ¼�ľ���·���� ͨ���ڳ����к��Ѷ�λĳ�����·�����ر�����B/SӦ���С�
	 * ͨ��������������ǿ��Ը������ǳ�����������ļ���λ������λĳ�����·����
	 * ���磺ĳ��txt�ļ�����ڳ����Test���ļ���·����../../resource/test.txt��
	 * ��ôʹ�ñ�����Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
	 * �õ��Ľ����txt�ļ�����ϵͳ�еľ���·����
	 * 
	 * @param relatedPath
	 *            ���·��
	 * @param cls
	 *            ������λ����
	 * @return ���·������Ӧ�ľ���·��
	 * @throws IOException
	 *             ��Ϊ����������ѯ�ļ�ϵͳ�����Կ����׳�IO�쳣
	 */
	public static String getFullPathRelateClass(String relatedPath, Class cls) {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		try {
			path = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * ��ȡ���class�ļ�λ�õ�URL
	 * 
	 * @param cls
	 * @return
	 */
	private static URL getClassLocationURL(final Class cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			if (cs != null)
				result = cs.getLocation();
			if (result != null) {
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}
		if (result == null) {
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader
					.getSystemResource(clsAsResource);
		}
		return result;
	}

	/**
	 * ��ȡstart��end����������,������start+end
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static BigDecimal getRandom(int start, int end) {
		return new BigDecimal(start + Math.random() * end);
	}

	/**
	 * ���ַ���д��ָ���ļ� (��ָ���ĸ�·�����ļ��в�����ʱ��������޶�ȥ�������Ա�֤����ɹ���)
	 * 
	 * @param res
	 *            ԭ�ַ���
	 * @param filePath
	 *            �ļ�·��
	 * @return �ɹ����
	 */
	public static boolean writeString2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024];
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * �ı��ļ�ת��Ϊָ��������ַ���
	 * 
	 * @param file
	 *            �ı��ļ�
	 * @param encoding
	 *            ��������
	 * @return ת������ַ���
	 * @throws IOException
	 */
	public static String readStringFromFile(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file), encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			char[] buffer = new char[1024];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if (writer != null)
			return writer.toString();
		else
			return null;
	}
	
	/**
	 * �ַ�������ת��������
	 * 
	 * @param pString
	 * @return
	 */
	public static String getGBK(String pString){
		if (isEmpty(pString)) {
			return "";
		}
		try {
			pString = new String(pString.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pString;
	}
	
	/**
	 * ���µ�һ��
	 * @return
	 */
	public static Date getPrefirstDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -1);
		ca.set(Calendar.DAY_OF_MONTH, 1);// ����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
		return ca.getTime();
	}
	
	/**
	 * �������һ��
	 * @return
	 */
	public static Date getPrelastDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 0);// ����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
		return ca.getTime();
	}
	
	/**
	 * ���µ�һ��
	 * @return
	 */
	public static Date getfirstDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, 0);
		ca.set(Calendar.DAY_OF_MONTH, 1);// ����Ϊ1��,��ǰ���ڼ�Ϊ���µ�һ��
		return ca.getTime();
	}
	
	/**
	 * �������һ��
	 * @return
	 */
	public static Date getlastDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return ca.getTime();
	}
	
	/**
	 * �ж��������Ƿ���N��
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isOver3Month(Date startDate,Date endDate,int month) {
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startCa = Calendar.getInstance();
		startCa.setTime(startDate);
		startCa.add(Calendar.MONTH, month);
		//��ʼʱ��С�ڵ��ڽ���ʱ�䷵��false,��ȡ����ʾ����3��
		if(!startCa.getTime().after(endDate)){
			flag = true;
		}
		return flag;
	}
	
	
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");	
		System.out.println(isOver3Month(stringToDate("2015-02-01","yyyy-MM-dd","yyyy-MM-dd"),getfirstDay(),3));
		//System.out.println(stringToDate("2015-01-01","yyyy-MM-dd","yyyy-MM-dd").after(stringToDate("2014-12-01","yyyy-MM-dd","yyyy-MM-dd")));
	}

}