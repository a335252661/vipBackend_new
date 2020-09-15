package test.demo;

/**
 * @author ³ΜΑυµΒ
 * @version 1.0
 * @Description TODO
 * @date 2020/7/13
 */
public class Thread01 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(50000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            }.start();
        }
    }
}
