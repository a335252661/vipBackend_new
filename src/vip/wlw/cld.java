package vip.wlw;

//import cn.hutool.core.bean.BeanUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import utils.HttpClientHelps;
import utils.UtilTools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/10/29
 */
public class cld {
    public static void main(String[] args) {
        String mm = "http://10.145.221.42:8263/acct/acctbilling/monthlyBill";

        Map<String, String> map = new HashMap<String, String>();
        map.put("serialNumber", "18918589213");
        String json=JSON.toJSONString(map);
        String jsonArrayStr = HttpClientHelps.sendGet(mm, json);
        System.out.println(jsonArrayStr);
//        List<BillInvoiceDTO> tbPrdList = JSONArray.parseArray(jsonArrayStr, BillInvoiceDTO.class);


    }



}
