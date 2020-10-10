package vip.sendMsg;

import helps.LogHelp;
import utils.DBConn;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 程刘德
 * @version 1.0
 * @Description TODO
 * @date 2020/9/30
 */
public class sendMsgs implements Runnable {

    private Connection conn = DBConn.getDbusr07TestConn();

    //最后一次程序是否执行完成 true为执行完毕 false为还在执行
    private volatile boolean isFinish = true;

    // 定时线程对象
    private ScheduledExecutorService scheduler=Executors.newSingleThreadScheduledExecutor();

    public void startService(){
        this.scheduler.scheduleAtFixedRate(this,0,10,TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new sendMsgs().startService();
    }

    @Override
    public void run() {

        if(isFinish){
            isFinish=false;
            LogHelp.sendMsg(conn);
            System.out.println(1);
            isFinish=true;
        }

    }
}
