package test;

import helps.DateTimeHelp;

import java.util.Random;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/22
 */
public class test1 {

    public static void main(String[] args) {
        String timeString = DateTimeHelp.getDateTimeString("yyyy-MM-dd HH:mm:ss");
        System.out.println(timeString);

        Random random1 = new Random();
        int i = random1.nextInt(100);
        System.out.println(i);

    }

}
