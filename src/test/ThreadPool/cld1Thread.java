package test.ThreadPool;

import java.util.List;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/22
 */
public class cld1Thread extends  Thread{

    private String name;
    private List list;

    //该方法用于传输传递
    public void passParams(String name,List list) {
        this.name = name;
        this.list = list;
    }

    @Override
    public void run() {


        System.out.println(Thread.currentThread()+"=============");

    }


}
