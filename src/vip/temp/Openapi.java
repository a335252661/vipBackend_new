package vip.temp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import helps.SQLHelp;
import utils.DBConn;
import utils.HttpClientHelps;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2021/1/6
 */
public class Openapi {

//    private static Connection conn = DBConn.getDbusr01TestConn();
    private static Connection conn = DBConn.getCopyProConn();
    String pro = "http://10.145.167.212:8063";
//    String pro = "http://10.145.221.42:8263";

    String taocanurl  ="http://10.145.167.201:8052/qcdr/qcdrshaccuuseqry/AccuUseQry";

    public static void main(String[] args) {
        Openapi  openapi = new Openapi();
        String all = "select * from openapi2";
//        String all = "select * from openapi2@to_iamzw_new";
        List<LinkedHashMap<String, Object>> linkedHashMaps = SQLHelp.querySQLReturnList(conn, all);
        for(LinkedHashMap<String, Object> map : linkedHashMaps){
            String SERVICE_NBR = map.get("SERVICE_NBR").toString();
            String YUE10 = openapi.fun(SERVICE_NBR, "202010");
            String YUE11 = openapi.fun(SERVICE_NBR, "202011");


            String YUEDETAIL11 = openapi.YUEDETAIL11(SERVICE_NBR, "202011");

//            List<HashMap<String, String>> hashMaps = openapi.fun3(SERVICE_NBR);
//            String taocan1="";
//            String taocan2="";
//            String taocan3="";
//            for(HashMap<String, String> map2 :hashMaps){
//                taocan1 = map2.get("语音");
//                taocan2 = map2.get("短信");
//                taocan3 = map2.get("流量");
//            }
//            String update ="update openapi2@to_iamzw_new set YUE10='"+YUE10+"' , YUE11 = '"+YUE11+"'  ,YUEDETAIL11='"+YUEDETAIL11+"' " +
            String update ="update openapi2 set YUE10='"+YUE10+"' , YUE11 = '"+YUE11+"'  ,YUEDETAIL11='"+YUEDETAIL11+"' " +
//                    " ,taocan1 = '"+taocan1+"'  " + " ,taocan2 = '"+taocan2+"'  "+ " ,taocan3 = '"+taocan3+"'  "+
                    " where   SERVICE_NBR ='"+SERVICE_NBR+"'";



            SQLHelp.updateSQL(conn,update);
        }

    }

    public String fun(String SERVICE_NBR,String mon) {//202010
        String url = pro+"/acct/acctbizgroup/QryBalanceRecord";
        String re="{\n" +
                "    \"operAttrStruct\": {\n" +
                "        \"staffId\": 123,\n" +
                "        \"operOrgId\": 123,\n" +
                "        \"operTime\": \"11\",\n" +
                "        \"operPost\": 123,\n" +
                "        \"operServiceId\": \"11\",\n" +
                "        \"lanId\": 123\n" +
                "    },\n" +
                "  \"svcObjectStruct\": {\n" +
                "    \"objType\": \"2\",\n" +
                "    \"objValue\": \""+SERVICE_NBR+"\",\n" +
                "    \"objAttr\": \"\",\n" +
                "    \"dataArea\": \"\"\n" +
                "  },\n" +
                "     \"billingCycleId\": "+mon+",\n" +
                "    \"balanceTypeFlag\": \"0\"\n" +
                "}";
        BigDecimal balanceBegin = null;
        BigDecimal balanceIn = null;
        BigDecimal balanceOut = null;
        BigDecimal balanceEnd = null;
        String data = "";
        try {
            JSONObject post = HttpClientHelps.post(url, re);
            JSONArray balanceTypeFlagQuery = post.getJSONArray("balanceTypeFlagQuery");
            JSONObject o = balanceTypeFlagQuery.getJSONObject(0);
//            balanceBegin = o.getInteger("balanceBegin")/100+"";

             balanceBegin = new BigDecimal(o.getInteger("balanceBegin")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

//            balanceIn = o.getInteger("balanceIn")/100+"";
            balanceIn =new BigDecimal(o.getInteger("balanceIn")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//            balanceOut = o.getInteger("balanceOut")/-100+"";
            balanceOut = new BigDecimal(o.getInteger("balanceOut")).divide(new BigDecimal(-100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//            balanceEnd = o.getInteger("balanceEnd")/100+"";
            balanceEnd = new BigDecimal(o.getInteger("balanceEnd")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
             data = balanceBegin+":"+balanceIn+":"+balanceOut+":"+balanceEnd;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    public String YUEDETAIL11(String SERVICE_NBR,String mon) {//202010
        String url = pro+"/acct/acctbizgroup/QryBalanceRecordDetail";
        String re="{\n" +
                "    \"operAttrStruct\": {\n" +
                "        \"staffId\": 123,\n" +
                "        \"operOrgId\": 123,\n" +
                "        \"operTime\": \"11\",\n" +
                "        \"operPost\": 123,\n" +
                "        \"operServiceId\": \"11\",\n" +
                "        \"lanId\": 123\n" +
                "    },\n" +
                "  \"svcObjectStruct\": {\n" +
                "    \"objType\": \"2\",\n" +
                "    \"objValue\": \""+SERVICE_NBR+"\",\n" +
                "    \"objAttr\": \"\",\n" +
                "    \"dataArea\": \"\"\n" +
                "  },\n" +
                "     \"billingCycleId\": "+mon+",\n" +
                "    \"balanceTypeFlag\": \"0\"\n" +
                "}";
        String data = "";
        try {
            JSONObject post = HttpClientHelps.post(url, re);
            JSONArray balanceTypeFlagQuery = post.getJSONArray("balanceChangeList");

            for(int i=0;i<balanceTypeFlagQuery.size();i++){
                JSONObject o = balanceTypeFlagQuery.getJSONObject(i);
                //话费支出:201.86:0.00:129.00:72.86                             现金充值:72.86:100.00:0.00:172.86
//                String beforeBalanceVal = o.getInteger("beforeBalanceVal")/100+"";
                BigDecimal beforeBalanceVal = new BigDecimal(o.getInteger("beforeBalanceVal")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//                String balanceIn = o.getInteger("balanceIn")/100+"";
                BigDecimal balanceIn = new BigDecimal(o.getInteger("balanceIn")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//                String balanceOut = o.getInteger("balanceOut")/-100+"";
                BigDecimal balanceOut = new BigDecimal(o.getInteger("balanceOut")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//                String balanceEnd =  o.getInteger("balanceEnd")/100+"";
                BigDecimal balanceEnd =  new BigDecimal(o.getInteger("balanceEnd")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                String balanceChangeType = o.getInteger("balanceChangeType")+"";
                if(balanceChangeType.equals("0")){
                    balanceChangeType = "现金充值";
                }else if (balanceChangeType.equals("5")){
                    balanceChangeType = "话费支出";
                }
                data +=balanceChangeType+"： " +beforeBalanceVal+":"+balanceIn+":"+balanceOut+":"+balanceEnd+"\r\n";
                System.out.println(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  data;
    }

    public List<HashMap<String , String>>  fun3(String SERVICE_NBR) {
        String da = "{\n" +
                "  \"accNbr\": \""+SERVICE_NBR+"\",\n" +
                "  \"billingCycle\": 202011,\n" +
                "  \"destinationAttr\": \"1\",\n" +
                "  \"offerId\": 0,\n" +
                "  \"operAttrStruct\": {\n" +
                "    \"lanId\": \"1\",\n" +
                "    \"operOrgId\": \"2\",\n" +
                "    \"operPost\": \"3\",\n" +
                "    \"operServiceId\": \"5\",\n" +
                "    \"operTime\": \"5\",\n" +
                "    \"staffId\": \"6\"\n" +
                "  }\n" +
                "}";

        List<HashMap<String , String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String , String> map = new HashMap<String, String>();
        StringBuilder st = new StringBuilder();
        String data = "";

        JSONObject parse = HttpClientHelps.post(taocanurl, da);

        JSONArray offerInstInfo = parse.getJSONArray("offerInstInfo");
        for(int i=0 ; i<offerInstInfo.size();i++){
            JSONObject jsonObject = offerInstInfo.getJSONObject(i);
            JSONArray accuQryList = jsonObject.getJSONArray("accuQryList");
            for(int m=0;m<accuQryList.size();m++){
                JSONObject jsonObject1 = accuQryList.getJSONObject(m);
                String unitTypeId =  jsonObject1.getString("unitTypeId");
                if(unitTypeId.equals("1")){//语音-分钟
                    int initVal  =   jsonObject1.getInteger("initVal");
                    int usageVal  =   jsonObject1.getInteger("usageVal");
                    st.append(initVal).append(":").append(usageVal).append("\r\n");
                    map.put("语音" , st.toString());
                }else if(unitTypeId.equals("2")){//短信，次数
                    int initVal  =   jsonObject1.getInteger("initVal");
                    int usageVal  =   jsonObject1.getInteger("usageVal");
                    st.append(initVal).append(":").append(usageVal).append("\r\n");
                    map.put("短信" , st.toString());
                }else if(unitTypeId.equals("3")){ //流量 kb
                    int initVal  =   jsonObject1.getInteger("initVal");
                    int usageVal  =   jsonObject1.getInteger("usageVal");
                    BigDecimal bbinitVal=  new BigDecimal(initVal).divide(new BigDecimal(1048576)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal  bbusageVal =  new BigDecimal(usageVal).divide(new BigDecimal(1048576)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    st.append(bbinitVal).append(":").append(bbusageVal).append("\r\n");
                    data+=bbinitVal+":"+bbusageVal+":"+"\r\n";
                    map.put("流量" , data);
                }
            }
        }
        System.out.println(st.toString());

        return list;

    }
}
