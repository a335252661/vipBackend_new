package small;

import utils.DBConn;
import helps.SQLHelp;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author by cld
 * @date 2020/5/3  15:03
 * @description:
 */
public class InsertData {
    public static void main(String[] args) {

        Connection  conn = DBConn.getLocalMySQLTestConn();

        String mm="select * from cld_code_hss_dis";

        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(conn, mm);


        List<String> all = new ArrayList<String>();

        for(LinkedHashMap<String, Object> map: linkedHashMaps){
            String id = map.get("id").toString();
           String code =  map.get("code").toString();

           if(code.contains(",")){
               String[] split = code.split(",");
               for(String str : split){
                    String data = id +"&"+str;
                    if(!all.contains(data)){
                        all.add(data);
                    }
               }

           }else if(code.contains("|")){
               String[] split = code.split("\\|");
               for(String str : split){
                   String data = id +"&"+str;
                   if(!all.contains(data)){
                       all.add(data);
                   }
               }
           }else {
               all.add(id +"&"+code);
           }



        }



        for(String str2 : all){
            String[] split = str2.split("&");
            String mm2 = "insert into cld_code_hss_dis2 values ('"+split[0]+"','"+split[1]+"')";
            SQLHelp.insertSQL(conn,mm2);
        }

        System.out.println(linkedHashMaps.size());

    }
}
