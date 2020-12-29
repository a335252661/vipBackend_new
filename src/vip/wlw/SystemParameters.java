package vip.wlw;


import helps.SQLHelp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统参数类
 * @author zhangye
 *
 */
public class SystemParameters {

	private int paramType;
	
	private String paramName;
	
	private String defaultValue ; //longValue;
	
	private String enumValue ; //charValue;
	
	public static final String MODULE_PAY = "PAY";
	
	

	public int getParamType() {
		return paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}

	public static List<SystemParameters> query(Connection mysqlConn, int module, String paramName) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        List<SystemParameters> list = new ArrayList<SystemParameters>();
    	sql.append("select PARAM_TYPE,PARAM_NAME,DEFAULT_VALUE,ENUM_VALUE from param_config where PARAM_TYPE  = ? and PARAM_NAME = ? ");
    	try {
    		stmt = mysqlConn.prepareStatement(sql.toString());
    		stmt.setInt(1,module);
    		stmt.setString(2,paramName);
    		rs = stmt.executeQuery();
            list = fromResultSet(rs);
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return list;
    }
	
	public static void update(Connection mysqlConn, int module, String parameterName, String defaultValue, String enumValue) throws Exception {
//    	int index = 0;
//        PreparedStatement stmt = null;
//        StringBuffer sql = new StringBuffer();
//        sql.append("update param_config set DEFAULT_VALUE = ? ,ENUM_VALUE = ?");
//        sql.append(" where PARAM_TYPE=? and PARAM_NAME = ? ");
//        try {
//            stmt = mysqlConn.prepareStatement(sql.toString());
//            stmt.setString(++index,defaultValue);
//            stmt.setString(++index,enumValue);
//            stmt.setInt(++index,module);
//            stmt.setString(++index,parameterName);
//            stmt.executeUpdate();
//        } finally {
//        	WlwJdbcUtil.close(stmt);
//        }

        String sqlup = "update param_config set DEFAULT_VALUE = '"+defaultValue+"' ,ENUM_VALUE = '"+enumValue+"' where PARAM_TYPE='"+module+"' and PARAM_NAME = '"+parameterName+"' "+" limit 99999";


		SQLHelp.updateSQL(mysqlConn , sqlup);

    }
	
	public static void updateDefaultValue(Connection mysqlConn, int module, String parameterName, String defaultValue, String enumValue) throws Exception {
    	int index = 0;
        PreparedStatement stmt = null;
        StringBuffer sql = new StringBuffer();
        sql.append("update param_config set DEFAULT_VALUE = ? ");
        sql.append(" where ENUM_VALUE = ? and PARAM_TYPE=? and PARAM_NAME = ? ");
        try {
            stmt = mysqlConn.prepareStatement(sql.toString());
            stmt.setString(++index,defaultValue);
            stmt.setString(++index,enumValue);
            stmt.setInt(++index,module);
            stmt.setString(++index,parameterName);
            stmt.executeUpdate();
        } finally {
        	WlwJdbcUtil.close(stmt);
        }
    }
	  
	public static void insert(Connection mysqlConn, int module, String parameterName, String defaultValue, String enumValue) throws Exception {
//    	int index = 0;
//        PreparedStatement stmt = null;
//        StringBuffer sql = new StringBuffer();
//        sql.append("insert into param_config(PARAM_ID,PARAM_TYPE,PARAM_NAME,DEFAULT_VALUE,ENUM_VALUE)");
//        sql.append(" values(param_config_seq.nextval,?,?,?,?) ");
//        try {
//            stmt = mysqlConn.prepareStatement(sql.toString());
//            stmt.setInt(++index,module);
//            stmt.setString(++index,parameterName);
//            stmt.setString(++index,defaultValue);
//            stmt.setString(++index,enumValue);
//            stmt.executeUpdate();
//        } finally {
//        	WlwJdbcUtil.close(stmt);
//        }
        String in = "insert into param_config(PARAM_ID,PARAM_TYPE,PARAM_NAME,DEFAULT_VALUE,ENUM_VALUE) values(param_config_seq.nextval,'"+module+"','"+parameterName+"','"+defaultValue+"'," +
				"'"+enumValue+"')";
        SQLHelp.insertSQL(mysqlConn,in);

    }
	
	public static SystemParameters queryValue(Connection mysqlConn, String module, String parameterName) throws Exception {
		SystemParameters params = null ;
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
    	sql.append("select ENUM_VALUE,DEFAULT_VALUE from param_config where PARAM_TYPE  = ? and PARAM_NAME = ? ");
    	try {
    		stmt = mysqlConn.prepareStatement(sql.toString());
    		stmt.setString(1,module);
    		stmt.setString(2,parameterName);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			params = new SystemParameters() ;
    			params.setDefaultValue(rs.getString("DEFAULT_VALUE"));
    			params.setEnumValue(rs.getString("ENUM_VALUE"));
    		}
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return params;
    }
	
	
	
	public static Long queryIntValue(Connection mysqlConn, int module, String parameterName, String charValue) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        Long l = null;
    	sql.append("select DEFAULT_VALUE from param_config where PARAM_TYPE  = ? and PARAM_NAME = ? and ENUM_VALUE = ?");
    	try {
    		stmt = mysqlConn.prepareStatement(sql.toString());
    		stmt.setInt(1,module);
    		stmt.setString(2,parameterName);
    		stmt.setString(3,charValue);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			l = rs.getLong("DEFAULT_VALUE");
    		}
    	} finally {
    		WlwJdbcUtil.close(rs);
    		WlwJdbcUtil.close(stmt);
        }
    	
    	return l;
    }
	
	

	
    private static List<SystemParameters> fromResultSet(ResultSet rs) throws SQLException {
        List<SystemParameters> list = new ArrayList<SystemParameters>();
        SystemParameters entity = null;
        while (rs.next()) {
        	entity = new SystemParameters();
//        	entity.setModule(rs.getString("MODULE"));
//        	entity.setParameterName(rs.getString("PARAMETER_NAME"));
        	entity.setEnumValue(rs.getString("ENUM_VALUE"));
        	entity.setDefaultValue(rs.getString("DEFAULT_VALUE"));
            list.add(entity);
        }
        return list;
    }
}
