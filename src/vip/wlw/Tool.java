package vip.wlw;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;


public class Tool {

    private static String encoding = null;

     public Tool(){
    }

    private static Hashtable<String,PrintWriter> fileHandleList = new Hashtable<String,PrintWriter>();

    public static void writeInfoLog(String filePath, String fileName, String content) {
        try {
            writeFile(filePath + fileName + "_" + BaseUtil.getCurrentDate(BaseUtil.DATE_PATTERN1),
                    BaseUtil.getCurrentDate(BaseUtil.DATETIME_PATTERN)
                            + "\t" + content,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeInfoLog(String fileName, String content) {
        try {
            /// bundle.getString("infoLogDir")
            writeFile( "infoLogDir" + fileName + "_" + BaseUtil.getCurrentDate(BaseUtil.DATE_PATTERN1),BaseUtil.getCurrentDate(BaseUtil.DATETIME_PATTERN)+ "\t" + content,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写文件
     * @throws IOException
     * @filePath 包含全路径的文件名
     * @content 写入文件的内容
     */
    public static void writeFile(String filePath, String content, boolean append) throws IOException {
        PrintWriter fileHandle = Tool.getFileHandle(filePath,append);
        fileHandle.println(content);
        fileHandle.flush();
    }

	 public static void writeFile(String filePath, List<String> logs, boolean append) throws IOException {
		 PrintWriter fileHandle = Tool.getFileHandle(filePath,append);
		 for(String log:logs){
			 fileHandle.println(log); 
		 }
		 fileHandle.flush();
	 }

    private static PrintWriter getFileHandle(String fileName, boolean append) throws IOException {
        PrintWriter fileHandle = fileHandleList.get(fileName);

        if(fileHandle == null){
            if(append){
                fileHandle = new PrintWriter(new FileWriter(new File(fileName), true));
            } else {
                fileHandle = new PrintWriter(fileName);
            }
            fileHandleList.put(fileName,fileHandle);
        }

        return fileHandle;
    }

    public static List<String> stringForList(String str, String delimiter){
        List<String> list = new ArrayList<String>();
        String[] keyArr = str.split(delimiter);
        for(String s:keyArr){
            list.add(s);
        }
        return list;
    }

    public static void writeErrorLog(String filpath, String fileName, String content) {
        try {
            writeFile(filpath + fileName + "_" + BaseUtil.getCurrentDate(BaseUtil.DATE_PATTERN1),BaseUtil.getCurrentDate(BaseUtil.DATETIME_PATTERN)+ "\t" + content,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeErrorLog(String fileName, String content) {
        try {
            /// bundle.getString("errorLogDir")
            writeFile("errorLogDir" + fileName + "_" + BaseUtil.getCurrentDate(BaseUtil.DATE_PATTERN1),BaseUtil.getCurrentDate(BaseUtil.DATETIME_PATTERN)+ "\t" + content,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Collection getMaps(ResultSet resultSet) throws SQLException {

        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();

        ArrayList list = new ArrayList();

        while (resultSet.next()) {
            HashMap row = new HashMap(cols, 1);
            for (int i = 1; i <= cols; i++) {
                putEntry(row, metaData, resultSet, i);
            }
            list.add(row);
        }

        return ((Collection) list);

    }

    public static void putEntry(Map properties, ResultSetMetaData metaData,
                                ResultSet resultSet, int i) throws SQLException {
        String columnName = metaData.getColumnLabel(i);
        String stemp = resultSet.getString(i);
        properties.put(columnName, stemp);

    }

    public static void close(Statement stat){
        if(null != stat){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs){
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn){
        if(null != conn){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfig(String fileName) throws Exception {
        /// bundle.getString("encoding")
        Tool.encoding = "UTF8";
    }

    public static void println(String msg){
        System.out.println(msg);
    }


//    public static Connection getIamConn(){
//        if(null == iamDsPool) {
//            Properties props = new Properties();
//            InputStream is = FinancialCheckTool.class.getClassLoader().getResourceAsStream("application-test.properties");
//            try {
//                props.load(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String iamOracleDriver = props.getProperty("spring.datasource.zwdb.druid.driver-class-name");
//            String iamOracleUrl = props.getProperty("spring.datasource.zwdb.druid.url");
//            String iamOracleUsername = props.getProperty("spring.datasource.zwdb.druid.username");
//            String iamOraclePassword = props.getProperty("spring.datasource.zwdb.druid.password");
//            String initialSize = props.getProperty("spring.datasource.zwdb.druid.initial-size");
//            int iamOracleInitialSize = Integer.parseInt(initialSize);
//            String minIdle = props.getProperty("spring.datasource.zwdb.druid.min-idle");
//            int iamOracleMinIdle = Integer.parseInt(minIdle);
//            String maxActive = props.getProperty("spring.datasource.zwdb.druid.max-active");
//            int iamOracleMaxActive = Integer.parseInt(maxActive);
//            String maxWait = props.getProperty("spring.datasource.zwdb.druid.max-wait");
//            int iamOracleMaxWait = Integer.parseInt(maxWait);
//            iamDsPool = new DbcpBean(iamOracleUrl,iamOracleUsername,iamOraclePassword,iamOracleDriver,
//                    iamOracleInitialSize,iamOracleMaxActive,iamOracleMinIdle,iamOracleMaxWait);
//        }
//
//        return iamDsPool.getConn();
//    }
}
