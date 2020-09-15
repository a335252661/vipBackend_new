package helps;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/4/21
 */
public class StringHelp {
    public static String strSubString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            System.out.println("字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串");
            return "";
        }
        if (strEndIndex < 0) {
            System.out.println("字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串");
            return "";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    public static void main(String[] args) {
        String mm = "https://cn.bing.com/search?q=windows+聚焦+小测" +
                "验&filters=IsConversation:%22True%22+BTWLKey:%22ValdesPeninsulaArgentina%22+BTWLType:%22Quiz%22&ensearch=0&FORM=M401CK";
    }

}
