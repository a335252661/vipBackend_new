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
     *  ^  : ��ʾxx��ͷ
     *  \d+  ƥ��һ����������  �� \\d+ �� ת���ַ�
     *  .	ƥ������з�����������ַ�
     *  \w	ƥ����ĸ�����ֻ��»��߻���
     *  \s	ƥ������Ŀհ׷�
     *  \d	ƥ������
     *  ^	ƥ���ַ����Ŀ�ʼ
     *  $	ƥ���ַ����Ľ���
     *  \b	ƥ���ַ����Ľ���
     *
     *  *	�ظ���λ�����
     *  +	�ظ�һ�λ�����
     *  ?	�ظ���λ�һ��
     *  {n}	�ظ�n��
     *  {n,}	�ظ�n�λ�����
     *  {n,m}	�ظ�n��m��
     *  \W	ƥ�����ⲻ����ĸ�����֣��»��ߣ����ֵ��ַ�
     *  \S	ƥ�����ⲻ�ǿհ׷����ַ�
     *  \D	ƥ����������ֵ��ַ�
     *  \B	ƥ�䲻�ǵ��ʿ�ͷ�������λ��
     *  [^x]	ƥ�����x����������ַ�
     *  [^aeiou]	ƥ�����aeiou�⼸����ĸ����������ַ�
     *
     *  *?	�ظ�����Σ������������ظ�
     *  +?	�ظ�1�λ����Σ������������ظ�
     *  ??	�ظ�0�λ�1�Σ������������ظ�
     *  {n,m}?	�ظ�n��m�Σ������������ظ�
     *  {n,}?	�ظ�n�����ϣ������������ظ�
     *
     *  Сд��ĸ�ַ���[a-z]
     *  ��д��ĸ�ַ���[A-Z]
     *  ʮ�������֣�[0-9]
     *  \p{Punct}	�����ţ�!"#$%&'()*+,-./:;<=>?@[\]^_{|}~
     *
     *
     *    .*   =  .{0,}   ��ʾ�����ַ������������   �е���like   '%%'
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
     * ������ʽ�ַ���
     * @param str
     * Ҫƥ����ַ���
     * @return ���str ���� regex��������ʽ��ʽ,����true, ���򷵻� false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
