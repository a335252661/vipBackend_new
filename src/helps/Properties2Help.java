package helps;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/18
 */
public class Properties2Help {
    public static Properties init(String name) {
        Properties2Help c = new Properties2Help();
        Properties fun = c.fun(name);
        return fun;
    }
    public  Properties fun(String name) {
            Properties props = new Properties();
            InputStream in;
            try {
                in = getClass().getResourceAsStream("/"+name);
                props.load(in);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        return props;
    }
}
