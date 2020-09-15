
package Pro;

/**
 * @author zhangye
 *
 * 存储过程参数类型
 */
public interface PROC_PARAM_TYPE {
	
	//字符串类型
	public static final int VARCHAR = java.sql.Types.VARCHAR;
	
	//数字类型
	public static final int INTEGER = java.sql.Types.INTEGER;
	
	//日期类型
	public static final int DATE = java.sql.Types.DATE;
	
	//长日期类型 带时分秒
	public static final int TIMESTAMP = java.sql.Types.TIMESTAMP;
	
	//游标
	public static final int CURSOR = oracle.jdbc.OracleTypes.CURSOR;
	
	//结构
	public static final int STRUCT = oracle.jdbc.OracleTypes.STRUCT;
	
	public static final int ARRAY = oracle.jdbc.OracleTypes.ARRAY;
	
	
	//长数字类型
	public static final int LONG = java.sql.Types.NUMERIC;
}
