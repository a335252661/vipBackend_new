package helps;

import java.util.ArrayList;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/7/27
 */
public class ListHelp {
    public static ArrayList<Integer> fast(int num) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<num;i++){
            list.add(i);
        }
        return list;
    }
}
