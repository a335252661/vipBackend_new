package vip.yuechu07;

import helps.SQLHelp;
import utils.DBConn;

import java.sql.Connection;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/2/24
 */
public class BackUPprodInst {
    public static void main(String[] args) {
        //1、清除cp库数据
        Connection copyProConn = DBConn.getCopyProConn();
        SQLHelp.truncate(copyProConn , "prod_inst");
        SQLHelp.exec(copyProConn , "insert into prod_inst select distinct prod_inst_id , acc_num from cus.prod_inst@CRM_COPY");

        Connection conn07 = DBConn.getDbusr07ProConn();
        SQLHelp.dropTable(conn07,"prod_inst_temp");
        SQLHelp.exec(conn07 , "create table prod_inst_temp as select * from prod_inst@to_iamzw_new");
    }
}
