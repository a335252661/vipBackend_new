package helps;

import oracle.jdbc.OracleTypes;
import org.apache.commons.beanutils.BeanUtils;
import utils.DBConn;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/4/10
 */
public class SQLHelp {

    public static String fieldNameToPropName(String columnName) throws Exception {
        StringBuffer propName = new StringBuffer();
        boolean upper = false;
        for (int i = 0; i < columnName.length(); i++) {
            String ch = columnName.substring(i, i + 1);
            if (ch.equals("_")) {
                upper = true;
            } else if (i != 0 && upper) {
                propName.append(ch.toUpperCase());
                upper = false;
            } else {
                propName.append(ch);
            }
        }
        return propName.toString();
    }

    private static List resultSet2MapList(ResultSet rs) throws Exception {
        ArrayList resultList = new ArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            LinkedHashMap resultCol = new LinkedHashMap();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

//                System.out.println(metaData.getColumnName(i));
                String propName = metaData.getColumnName(i);
//                String propName = fieldNameToPropName(metaData.getColumnName(i)
//                        .toLowerCase());
                Object dbValue = rs.getObject(metaData.getColumnName(i));
                if (dbValue != null) {
                    if (dbValue instanceof java.sql.Date) {
                        resultCol.put(propName, new java.util.Date(
                                ((java.sql.Date) dbValue).getTime()));
                    } else {
                        resultCol.put(propName, dbValue);
                    }
                } else {
                    resultCol.put(propName, dbValue);
                }
            }
            resultList.add(resultCol);
        }
        return resultList;
    }

    private static ArrayList<String> resultSet2List(ResultSet rs) throws Exception {
        ArrayList<String> resultList = new ArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String propName =  metaData.getColumnName(i);
                Object dbValue = rs.getObject(metaData.getColumnName(i));
                if (dbValue != null) {
                    resultList.add(dbValue.toString());
                }else {
                    resultList.add("");
                }
            }
        }
        return resultList;
    }

    private static Map resultSet2Map(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        HashMap resultMap = new HashMap();
        if (rs != null && rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String propName = fieldNameToPropName(metaData.getColumnName(i)
                        .toLowerCase());
                Object dbValue = rs.getObject(metaData.getColumnName(i));
                if (dbValue != null) {
                    if (dbValue instanceof java.sql.Date) {
                        resultMap.put(propName, new java.util.Date(
                                ((java.sql.Date) dbValue).getTime()));
                    } else {
                        resultMap.put(propName, dbValue);
                    }
                }
            }
        }
        return resultMap;
    }

    public static List<LinkedHashMap<String, Object>> querySQLReturnList(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        Statement state = null;
        ResultSet rs = null;
        List<LinkedHashMap<String, Object>> lst = new ArrayList();
        try {
            System.out.println(sqlStr);
            state = conn.createStatement();
            rs = state.executeQuery(sqlStr);
            lst = resultSet2MapList(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DateTimeHelp.end();
        return lst;
    }

    public static  ArrayList<String> querySQLReturnList2(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        Statement state = null;
        ResultSet rs = null;
        ArrayList<String> lst = new ArrayList();
        try{
            System.out.println(sqlStr);
            state = conn.createStatement();
            rs =state.executeQuery(sqlStr);
            lst = resultSet2List(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DateTimeHelp.end();
        return lst;
    }

    public static Map querySQLReturnMap(Connection conn, String sqlStr) {
        Statement state = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            System.out.println(sqlStr);
            state = conn.createStatement();
            rs = state.executeQuery(sqlStr);
            map = resultSet2Map(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public static  void updateSQL(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        try{
            PreparedStatement pre = conn.prepareStatement(sqlStr);
            System.out.println(sqlStr);
            pre.execute();
            pre.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateTimeHelp.end();
    }

    public static String querySQLReturnField(Connection conn, String sqlStr) {
        System.out.println(sqlStr);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String coun = "";
        try {
            ps = conn.prepareStatement(sqlStr);
            rs = ps.executeQuery();
            while (rs.next()) {
                coun = rs.getString("FIELD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != ps)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return coun;
    }

    public static void insertSQL(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        try {
            System.out.println(sqlStr);
            Statement statement = conn.createStatement();
            statement.execute(sqlStr);
            statement.close();
            conn.commit();

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateTimeHelp.end();
    }

    public static void insertSQL(Connection conn, String sqlStr , Boolean println) {
        DateTimeHelp.start();
        try {
            if(println){
                System.out.println(sqlStr);
            }
            Statement statement = conn.createStatement();
            statement.execute(sqlStr);
            statement.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateTimeHelp.end();
    }

    public static void deleteSQL(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        Statement state = null;
        ResultSet rs = null;
        try {
            System.out.println(sqlStr);
            state = conn.createStatement();
            int i = state.executeUpdate(sqlStr);
            System.out.println("该语句成功删除条数 ===="+i);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DateTimeHelp.end();
    }

    public static Boolean isExistTable(Connection conn, String tableName) {
        String sql = "select count(*) coun from user_tables where table_name =upper('" + tableName + "')";
        Statement state = null;
        ResultSet rs = null;
        Boolean flag = false;
        try {
            System.out.println(sql);
            state = conn.createStatement();
            rs = state.executeQuery(sql);
            conn.commit();
            while (rs.next()) {
                String coun = rs.getString("coun");
                if ("0".equals(coun)) {
                    flag = false;
                } else {
                    flag = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void dropTable(Connection conn, String tableName) {
        Boolean existTable = SQLHelp.isExistTable(conn, tableName);
        if (existTable) {
            SQLHelp.deleteSQL(conn, "drop table " + tableName +" purge");
            System.out.println(tableName +" 存在，删除成功！");
        }
    }


    public ArrayList<String> queryTableComments(Connection conn, String tableName) {
        String sql = "select column_name from user_col_comments  where table_name = '"+tableName.toUpperCase()+"'";
        Statement state = null;
        ResultSet rs = null;
        ArrayList<String> commentsList = new ArrayList<String>();
        try {
            System.out.println(sql);
            state = conn.createStatement();
            rs = state.executeQuery(sql);
            while (rs.next()) {
                String coun = rs.getString("column_name");
                commentsList.add(coun);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs)
                    rs.close();
                if (null != state)
                    state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return commentsList;
    }

    public static ResultSet querySQLReturnResultSet(Connection conn, String sqlStr) {
        DateTimeHelp.start();
        Statement state = null;
        ResultSet rs = null;
        try {
            System.out.println(sqlStr);
            state = conn.createStatement();
            rs = state.executeQuery(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (null != state)
//                    state.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        DateTimeHelp.end();
        return rs;
    }



    public static void call(Connection conn, String str) {
        DateTimeHelp.start();
        CallableStatement cstat = null;
        try {
            System.out.println(str);
            cstat = conn.prepareCall(str);
            cstat.execute();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (cstat != null) {
                    cstat.close();
                }
            } catch (Exception se) {
                se.printStackTrace();
            }
        }
        DateTimeHelp.end();
    }

    public static String toSQLin(Object ... objects ) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            if(i == objects.length - 1) {
                sb.append("'");
                sb.append(objects[i]);
                sb.append("'");
            }else {
                sb.append("'");
                sb.append(objects[i]);
                sb.append("'");
                sb.append(",");
            }
        }
        return sb.toString().trim();
    }

    /**
     * 获取字段名称
     * @param resultSet
     * @return
     */
    public static List<String> getColumnName(ResultSet resultSet) {
        List<String> cols = new ArrayList<String>();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String columnName = rsmd.getColumnName(i).toLowerCase();
                cols.add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cols;
    }

    public static void main(String[] args) {
        Connection collTestConn = DBConn.getDbusr01TestConn();
        String dumpInvoiceSql = "{call cld_test('4')}";
        SQLHelp.call(collTestConn, dumpInvoiceSql);
    }

}
