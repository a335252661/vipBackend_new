package test.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/22
 */
public class cld1 {
    public static void main(String[] args) {
//        ExecutorService service = Executors.newFixedThreadPool(10) ;
//        cld1Thread cld1Thread = new cld1Thread();
//        cld1Thread.passParams("hahah" , new ArrayList());
//
//
//        for(int i=0 ; i<12 ; i++){
//
//            service.submit(cld1Thread);
//
//        }
//
//        service.shutdown();
//        while (true) {
//            if (service.isTerminated()) {
//                System.out.println("所有线程运行结束");
//                break;
//            }
//        }

        ArrayList<String> ll =  new ArrayList<String>();
                ll.add("1");
                ll.add("2");
                ll.add("3");
                ll.add("4");

        List<String> mm =  ll.subList(0,ll.size()-1);
        System.out.println(mm);


    }
}
