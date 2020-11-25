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
                System.out.println("Took a job,"+"pid"+nowjob.pid+","+jobQ.getItemsNum() + "jobs left now");
            }finally {
                jobQ.IOlock.unlock();
            }
            nowjob.run();
            jobQ.IOlock.lock();
            try {
                System.out.print("Job " + nowjob.pid + " finished,go ");
                System.out.println(nowjob.dtime + "ms");
                System.out.println(jobQ.getItemsNum() + "jobs left now");
            }finally {
                jobQ.IOlock.unlock();
            }
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
