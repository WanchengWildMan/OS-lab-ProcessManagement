package Entities;

import java.util.Random;

import static Sources.CriticalSources.*;

public class CPU implements Runnable {

    @Override
    public void run() {
        final boolean POP=true;
        Job nowjob = new Job();
        while (true) {
            nowjob = jobQ.front(POP);
            //保证为原子操作，防止第二个CPU也取
            jobQ.IOlock.lock();
            try {
                //多核时，只允许一个核输出！！！
                //也可以直接this）
                System.out.println("调度"+"pid"+nowjob.pid+","+"现在还剩"+jobQ.getItemsNum()+"个任务" );
                jobQ.printQ();
            }finally {
                jobQ.IOlock.unlock();
            }
            nowjob.run();
            jobQ.IOlock.lock();
            try {
                System.out.println("Job " + nowjob.pid + " 服务时间： "+nowjob.dtime + "ms");
                System.out.println("Job " + nowjob.pid + " 等待时间： "+nowjob.waittime + "ms");
                System.out.println("Job " + nowjob.pid + " 周转时间： "+ (nowjob.waittime+nowjob.dtime) + "ms");
                System.out.println("Job " + nowjob.pid + " 带权周转时间： "+1.0*nowjob.waittime/nowjob.dtime+1 );
                System.out.println("还剩"+jobQ.getItemsNum()+"个任务" );
            }finally {
                jobQ.IOlock.unlock();
            }
            jobQ.addwaittime(nowjob);
            jobQ.printQ();
            pidpool.add(nowjob.pid);
            try {
                Thread.sleep(new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
