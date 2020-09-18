package utils;

import helps.Properties2Help;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2019/12/12
 *
 * 使用jdbctemplte  必须一下jar
 * spring-beans-3.2.3.RELEASE.jar
 * spring-core-3.2.3.RELEASE.jar
 * spring-jdbc-3.2.3.RELEASE.jar
 * spring-tx-3.2.3.RELEASE.jar
 *
 */
public class DBConnProper {
    private static String url = "";
    private static String user ="";
    private static String pwd = "";
    private static String driver="";
    public static void main(String[] args) {
//        Connection oracle_local = DBConn.getConnection("oracle_local");
        JdbcTemplate jdbcTemplate = DBConnProper.getJdbcTemplate("oracle_local");
        String sql = " select cust from cld_err where rownum =1"   ;
        String cust = jdbcTemplate.queryForObject(sql, String.class) ;
        System.out.println(cust);
    }
    
    public static void init(String name) {
        Properties init = Properties2Help.init("jdbc.properties");
         url = init.getProperty(name+".url");
         user = init.getProperty(name+".user");
         pwd = init.getProperty(name+".pwd");
         driver = init.getProperty(name+".driver");
        System.out.println("------------------------加载数据库连接----------------------------");
        System.out.println("url="+url);
        System.out.println("user="+user);
        System.out.println("pwd="+pwd);
        System.out.println("------------------------------------------------------------------");
    }


    public static Connection getConnection(String name) {
        DriverManagerDataSource dataSource = DBConnProper.getDataSource(name);
        Connection connection =null;
        try {
             connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static JdbcTemplate getJdbcTemplate(String name) {
        DriverManagerDataSource dataSource = DBConnProper.getDataSource(name);
        JdbcTemplate jdbcTemplate= new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    public static DriverManagerDataSource getDataSource(String name) {
        DBConnProper.init(name);
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setUrl(url);
        datasource.setUsername(user);
        datasource.setPassword(pwd);
        datasource.setDriverClassName(driver);
        return datasource;
    }


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


    /*hss*/





}
