package vip.guoMan;

import helps.ListHelp;
import helps.Properties2Help;

import java.util.Properties;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/10
 */
public class cld {

    public static void main(String[] args) {
        System.out.println("===================运行时间");


        Properties init = Properties2Help.init("vip/guoMan/interNationRoamingFile.properties");
        String encoding = init.getProperty("encoding");

        System.out.println(encoding);

    }

}
