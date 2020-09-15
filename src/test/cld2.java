package test;

import utils.DBConn;
import helps.SQLHelp;
import helps.WriterExcelHelp;
import java.sql.Connection;
import java.util.List;

/**
 * @author ³ÌÁõµÂ
 * @version 1.0
 * @Description TODO
 * @date 2020/4/16
 */
public class cld2 {

    public static void main(String[] args) {
        System.out.println("222222222");

        Connection conn = DBConn.getLocalConn();
        List list = SQLHelp.querySQLReturnList(conn, "select * from CLD_ERR");
        System.out.println(list.size());

//        WriterExcelHelp.generateExcelFile("D:\\tmp22222" , "2222.xlsx",list,null);

    }

}
