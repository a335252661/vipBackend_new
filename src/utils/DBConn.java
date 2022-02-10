package utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2019/12/12
 */
public class DBConn {

    public static Connection getLocalConn() {
        String dbUrl = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
        String dbUser = "system";
        String dbPwd = "root";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static Connection getIamConn()  {
        String dbUrl = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.145.196.101)(PORT = 1521))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = iamzwdb)))";
        String dbUser = "iam";
        String dbPwd = "iam";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
//#bss/jt#b2SS81
//jtbssdb =
//  (DESCRIPTION =
//    (ADDRESS_LIST =
//      (ADDRESS = (PROTOCOL = TCP)(HOST = 10.145.240.154)(PORT = 1521))
//    )
//    (CONNECT_DATA =
//      (SERVICE_NAME = jtbssdb)
//    )
//  )
    public static Connection getBssConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.240.154:1521:jtbssdb";
        String dbUser = "bss";
        String dbPwd = "jt#b2SS81";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getCopyProConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.132.244:1521:ACCTDB_REP1";
        String dbUser = "acct_app";
        String dbPwd = "g$N6FDS8";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static Connection getCopy235ProConn() {
        String dbUrl = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.145.132.243)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.145.132.244)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.145.132.235)(PORT = 1521))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = ACCTDB_REP1)))";
//        String dbUrl = "jdbc:oracle:thin:@10.145.132.235:1521:ACCTDB_REP1";
        String dbUser = "acct_app";
        String dbPwd = "g$N6FDS8";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static Connection getCopyTestConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.248.200:1521:IAMZWDB";
        String dbUser = "acct_app";
        String dbPwd = "acct_app_321";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getDbusr07ProConn() {
        String dbUrl = "jdbc:oracle:thin:@10.7.95.67:1521:iamzwdb";
        String dbUser = "dbusr07";
        String dbPwd = "K7=8]5lg";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static Connection getDbusr06ProConn()  {
        String dbUrl = "jdbc:oracle:thin:@10.7.95.67:1521:iamzwdb";
        String dbUser = "dbusr06";
        String dbPwd = "fqIuik1!";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getDbusr01ProConn()  {
        String dbUrl = "jdbc:oracle:thin:@10.7.95.67:1521:iamzwdb";
        String dbUser = "dbusr01";
        String dbPwd = "iamApp201302";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    //iamApp201302

    public static Connection getMqsql()  {
        String dbUrl = "jdbc:mysql://localhost:3306/maventest";
        String dbUser = "root";
        String dbPwd = "root";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    //----------------------------------------------
    public static Connection getDbusr01TestConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.196.101:1521:iamzwdb";
        String dbUser = "dbusr01";
        String dbPwd = "dbusr01123";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getDbusr06TestConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.196.101:1521:iamzwdb";
        String dbUser = "dbusr06";
        String dbPwd = "dbusr06123";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getDbusr07TestConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.196.101:1521:iamzwdb";
        String dbUser = "dbusr07";
        String dbPwd = "dbusr07123";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
             conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }



    /*--------------------------------*/
    //			<Resource name="jdbc/cqdb_test"
    //			auth="Container"
    //			type="javax.sql.DataSource"
    //			username="coll"
    //			password="coll"
    //			driverClassName="oracle.jdbc.driver.OracleDriver"
    //			url="jdbc:oracle:thin:@10.145.196.101:1521:cqdb"
    //			maxActive="8"
    //			maxIdle="4"/>
    public static Connection getCollTestConn() {
        String dbUrl = "jdbc:oracle:thin:@10.145.196.101:1521:cqdb";
        String dbUser = "coll";
        String dbPwd = "coll";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getColl04Conn() {
        String dbUrl = "jdbc:oracle:thin:@10.7.95.69:1521:cqdb";
        String dbUser = "dbusr04";
        String dbPwd = "mvqz4Ecr";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static Connection getLocalMySQLTestConn() {
        String dbUrl = "jdbc:mysql://localhost:3306/maventest";
        String dbUser = "root";
        String dbPwd = "root";
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //MySQL 8.0 以上版本的数据库连接有所不同：
//            Class.forName("com.mysql.cj.jdbc.Driver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getLocalMySQLTestConn(String name) {
        String dbUrl = "jdbc:mysql://localhost:3306/"+name+"?useUnicode=true&characterEncoding=UTF-8";
        String dbUser = "root";
        String dbPwd = "root";
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //MySQL 8.0 以上版本的数据库连接有所不同：
//            Class.forName("com.mysql.cj.jdbc.Driver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getCopyMySQLProConn() {
        String dbUrl = "jdbc:mysql://10.145.171.17:8331/ACCTDB?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&rewriteBatchedStatements=true";
        String dbUser = "acct_app";
        String dbPwd = "g$NEJ0IJ";
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //MySQL 8.0 以上版本的数据库连接有所不同：
//            Class.forName("com.mysql.cj.jdbc.Driver");
            conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    /*hss*/





}
