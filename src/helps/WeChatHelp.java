package helps;

import com.alibaba.fastjson.JSONObject;
import utils.HttpClientUtils;

import java.util.HashMap;

/**
 * @author by cld
 * @date 2020/3/22  9:26
 * @description:
 */
public  class WeChatHelp {
    //企业微信id
    private static String corpid="wwcda20ad5423e09b1";
    //bug应用的   secret
    public static String bug_secret = "v_sWaLf1a-gcNj6JsvBkTpJNVuGFbYu1QQxSlL8PZUU";
    //日志测试应用的   secret
    public static String log_secret = "qHToh622zGu94dqbI4tsGGHSeNJyyO1iOhz1Lxo3yBM";
    private static String access_token = "";
    private static HashMap<String , Integer> map = new HashMap();
    static {
        map.put(WeChatHelp.bug_secret ,1000002 );
        map.put(WeChatHelp.log_secret ,1000003 );
    }
    public static String get_access_token(String currSecret){
        String acceptAccessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid
                +"&corpsecret="+currSecret;
        String s = HttpClientUtils.sendPostOrGet(acceptAccessTokenUrl, "");
        JSONObject jsonObject = JSONObject.parseObject(s);
        String access_token = jsonObject.getString("access_token");

        return  access_token;
    }
    public static JSONObject makeMsg(String currSecret , String msg){
        JSONObject js = new JSONObject();
        js.put("touser","ChengLiuDe");
        js.put("msgtype","text");
        js.put("agentid",map.get(currSecret));
        js.put("safe","safe");
        JSONObject jsText = new JSONObject();
        jsText.put("content" , msg);
        js.put("text",jsText);
        return js;
    }
    synchronized  public static void sendCompanyWeChatMsg(String secret , String msg){
        try{

//            String str = new String(msg.getBytes("GBK"));

            String access_token = WeChatHelp.get_access_token(secret);
            WeChatHelp.access_token = access_token;
            String sendMessurl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
                    +WeChatHelp.access_token;
            JSONObject jsonMsg = WeChatHelp.makeMsg(secret, msg);
            HttpClientUtils.sendPostOrGet(sendMessurl,JSONObject.toJSONString(jsonMsg));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WeChatHelp.sendCompanyWeChatMsg(log_secret,"日志信息");
        WeChatHelp.sendCompanyWeChatMsg(log_secret,"日志信息");
//        System.out.println("日志信息");
    }

}
