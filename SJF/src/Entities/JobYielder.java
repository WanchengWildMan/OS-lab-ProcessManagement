package Entities;

import java.util.*;

import static Sources.CriticalSources.*;

public class JobYielder implements Runnable {
    @Override
    public void run() {
        Integer nowpid = null;

        while (true) {
                while (pidpool.isEmpty()) ;
                Iterator<Integer> iterator = pidpool.iterator();
                if (iterator.hasNext()) {
                    nowpid = iterator.next();
                    pidpool.remove(nowpid);
                }

            Job newjob = new Job(nowpid, new Random().nextInt(50));
            jobQ.push(newjob);
            System.out.println("产生新任务，其pid为"+newjob.pid);
            try {
                Thread.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
