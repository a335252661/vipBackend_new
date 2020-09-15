package test;

import helps.PropertiesHelp;

import java.util.Properties;

/**
 * @author by cld
 * @date 2020/5/3  18:21
 * @description:  java -cp D:\project\vipBackend\out\artifacts\vipBackend_jar\vipBackend.jar test.propertest D:\test.properties
 */
public class propertest {
    public static void main(String[] args) {
        String str = args[0];
        System.out.println(str);
        Properties init = PropertiesHelp.init(str);
        System.out.println(init.getProperty("name"));
    }
}
