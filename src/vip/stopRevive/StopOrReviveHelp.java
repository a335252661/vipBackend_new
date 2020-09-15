package vip.stopRevive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/7/30
 */
public class StopOrReviveHelp {
    public static String sendPostOrGet(String url, String param) {
        System.out.println("请求报文内容=================================");
        System.out.println(param);
        System.out.println("=============================================");
        OutputStreamWriter out = null;
        BufferedReader in = null;

        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

//            ResourceBundle bundle = ResourceBundle.getBundle("interface");
//            String id = bundle.getString("x-app-id");
//            String key = bundle.getString("x-app-key");

//            conn.setRequestProperty("x-app-id", id);
//            conn.setRequestProperty("x-app-key", key);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);


            // 获取URLConnection对象对应的输出流
            //out = new PrintWriter(conn.getOutputStream());
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param.toString());
            //out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            //System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static JSONArray append(String HANDLE_TYPE, int stopOrRevive, String name) {
        JSONArray js = new JSONArray();
        JSONObject jslist = new JSONObject();//操作类型

        if (stopOrRevive == 11) { //停机
            if("停机请求CRM-短流程".equals(name)){
                if (HANDLE_TYPE.equals("1")) {
                    jslist.put("stopType", "130098");
                    jslist.put("stopDirection", "1000");
                }
                if (HANDLE_TYPE.equals("2")) {
                    jslist.put("stopType", "130098");
                    jslist.put("stopDirection", "1200");
                }
            }else {
                if (HANDLE_TYPE.equals("1")) {
                    jslist.put("stopType", "130001");
                    jslist.put("stopDirection", "1000");
                }
                if (HANDLE_TYPE.equals("2")) {
                    jslist.put("stopType", "130002");
                    jslist.put("stopDirection", "1200");
                }
            }
        }


        if (stopOrRevive == 16) { //复机



            if ("紧急复机转复机同步ABP".equals(name) || "紧急复机同步ABP".equals(name) || "同步ABP".equals(name)||"同步ABP_500".equals(name)) { //紧急复机转复机 和 紧急复机 只会同步给abp
                jslist.put("stopType", "");
                jslist.put("stopDirection", HANDLE_TYPE);//HANDLE_TYPE
            } else {

                if("复机请求CRM-短流程".equals(name)){
                    jslist.put("stopType", "130098");//账务停机
                    jslist.put("stopDirection", "");

                    //高额停机
                    JSONObject jsob3 = new JSONObject();
                    jsob3.put("stopType", "160099");
                    jsob3.put("stopDirection", "");
                    js.add(jsob3);

                }else {
                    //一般复机
                    jslist.put("stopType", "130001");
                    jslist.put("stopDirection", "");

                    JSONObject jsob2 = new JSONObject();
                    jsob2.put("stopType", "130002");
                    jsob2.put("stopDirection", "");

                    JSONObject jsob3 = new JSONObject();
                    jsob3.put("stopType", "160099");
                    jsob3.put("stopDirection", "");

                    js.add(jsob2);
                    js.add(jsob3);
                }


            }


        }
        js.add(jslist);
        return js;
    }

    public static void insertLogs(Connection conn, String seq, String name, String code, String json, String responseStr) {
        try {
            String sqlStr = "insert into coll_order_log values ('" + seq + "'," +
                    "'" + name + "'," +
                    "'" + code + "'," +
                    "sysdate," +
                    "'" + json + "'," +
                    "'" + responseStr + "'" +
                    " )";
            Statement statement = conn.createStatement();
            statement.execute(sqlStr);
            statement.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static HashMap sendRequest(Connection conn,
                                      String SERVICE_EXTERNAL_ID,
                                      String operType,
                                      String seq,
                                      String HANDLE_TYPE,
                                      int stopOrRevive,
                                      String name,
                                      String processType,
                                      String remarks,
                                      String staff,
                                      String url

    ) {
        //执行复机
        Boolean flag = true;
        HashMap map = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("accNum", SERVICE_EXTERNAL_ID);//设备号码
            jsonObject.put("operType", operType);//操作类型
            jsonObject.put("requestNbr", seq);//请求流水号

            JSONArray js = StopOrReviveHelp.append(HANDLE_TYPE, stopOrRevive, name);
            jsonObject.put("stopList", js);

            jsonObject.put("stopReason", remarks);//停机原因
            jsonObject.put("processType", processType);
            jsonObject.put("source", "IAM");//调用方来源入参
            if(url.equals("request_crm_url")){
                jsonObject.put("operateStaff", "");//请求员工
            }else {
                jsonObject.put("operateId", staff);//请求员工
            }

//            ResourceBundle bundle = ResourceBundle.getBundle("interface");
//            String questUrl = "http://10.145.205.69:9011/Ducc/stopRevive";
            //生产
            String questUrl = "http://10.145.196.13:9011/Ducc/stopRevive";

            String responseStr = StopOrReviveHelp.sendPostOrGet(questUrl, jsonObject.toJSONString());

            String code = "";
            String msg = "";
            if(url.equals("request_crm_url")){
                JSONArray parse = JSONArray.parseArray(responseStr);
                JSONObject JS = parse.getJSONObject(0);
                 code = JS.get("code").toString();//0
                msg = JS.get("reason").toString();//0
            }else {
                JSONObject jsres = JSONObject.parseObject(responseStr);
                code = jsres.getString("resultCode");//0
                msg = jsres.getString("resultMsg");//0
            }
            System.out.println("==============================================");
            System.out.println("流水号 ==" + seq + "    返回参数==" + responseStr);
            //记录日志
            StopOrReviveHelp.insertLogs(conn, seq, name, code, jsonObject.toJSONString(), responseStr);
            System.out.println("==============================================");
            map.put("code" , code);
            map.put("flag" , flag);
            map.put("msg" , msg);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return map;
    }


}
