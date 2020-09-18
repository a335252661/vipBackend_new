package helps;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author by cld
 * @date 2020/5/3  17:58
 * @description:
 */
public class PropertiesHelp {
    public static Properties init(String fullLocation) {
        Properties proper = new Properties();
        try {
            proper.load(new FileReader(fullLocation));  //配置文件读取uri
            Map<String, String> map = new HashMap<String, String>();
            Iterator<Object> keys = proper.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (proper.getProperty(key) != null) {
                    map.put(key, proper.getProperty(key).toString());
                }
            }
            proper.putAll(map);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return proper;
    }



    public static void main(String[] args) {
//        Properties init = PropertiesHelp.init("D:\\project\\vipBackend\\out\\artifacts\\vipBackend_jar\\test.properties");
//        System.out.println(init.getProperty("name"));



    }
}
