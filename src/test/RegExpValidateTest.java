package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by cld
 * @date 2020/5/19  21:35
 * @description:
 */
public class RegExpValidateTest {


    /**
     *  ^  : 表示xx开头
     *  \d+  匹配一个或多个数字  （ \\d+ ） 转义字符
     *  .	匹配除换行符以外的任意字符
     *  \w	匹配字母或数字或下划线或汉字
     *  \s	匹配任意的空白符
     *  \d	匹配数字
     *  ^	匹配字符串的开始
     *  $	匹配字符串的结束
     *  \b	匹配字符串的结束
     *
     *  *	重复零次或更多次
     *  +	重复一次或更多次
     *  ?	重复零次或一次
     *  {n}	重复n次
     *  {n,}	重复n次或更多次
     *  {n,m}	重复n到m次
     *  \W	匹配任意不是字母，数字，下划线，汉字的字符
     *  \S	匹配任意不是空白符的字符
     *  \D	匹配任意非数字的字符
     *  \B	匹配不是单词开头或结束的位置
     *  [^x]	匹配除了x以外的任意字符
     *  [^aeiou]	匹配除了aeiou这几个字母以外的任意字符
     *
     *  *?	重复任意次，但尽可能少重复
     *  +?	重复1次或更多次，但尽可能少重复
     *  ??	重复0次或1次，但尽可能少重复
     *  {n,m}?	重复n到m次，但尽可能少重复
     *  {n,}?	重复n次以上，但尽可能少重复
     *
     *  小写字母字符：[a-z]
     *  大写字母字符：[A-Z]
     *  十进制数字：[0-9]
     *  \p{Punct}	标点符号：!"#$%&'()*+,-./:;<=>?@[\]^_{|}~
     *
     *
     *    .*   =  .{0,}   表示任意字符出现任意次数   有点像like   '%%'
     * @param args
     */
    public static void main(String[] args) {
        String content = "I am noob from runoob.com";

//        String regex = ".*runoob.*";
//        String regex = "^I.*";
//        String regex = ".*com$";
        String regex = ".{0,}com$";



//        String regex = "\w*runoob\w*";
//        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        System.out.println(content.matches(regex));


//        boolean isMatch = Pattern.matches(regex, str);


//        System.out.println(str.matches(regex));

//        System.out.println(isMatch);

    }
    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
