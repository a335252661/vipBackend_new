package test.demo;

/**
 * @author ������
 * @version 1.0
 * @Description TODO
 * @date 2020/7/13
 */
public class Thread02 extends Thread{
    private static int count = 5;
    public Thread02(String name) {
//        super();
        this.setName(name);//�����߳�����
    }
    public static void main(String[] args) {
        Thread02 a=new Thread02("A");
        Thread02 b=new Thread02("B");
        Thread02 c=new Thread02("C");


//        a.count = 100;
//        System.out.println(b.count);

        a.start();
        b.start();
        c.start();
    }



    @Override
    public void run() {
//        super.run();
//        while (count > 0) {
            count--;
            System.out.println("�� " + this.currentThread().getName()
                    + " ���㣬count=" + count);
//        }
    }



}
