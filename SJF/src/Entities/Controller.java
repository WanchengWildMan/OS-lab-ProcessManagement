package Entities;

import java.util.HashSet;

import static Sources.CriticalSources.jobQ;
import static Sources.CriticalSources.pidpool;

public class Controller {



    public static void main(String args[]) {
        JobYielder jobYielder = new JobYielder();
        pidpool=new HashSet<Integer>();
        for(int i=1;i<=65535;i++)pidpool.add(i);
        CPU cpu1=new CPU();
        //模拟多核
//        CPU cpu2=new CPU();
//        CPU cpu3=new CPU();
//        CPU cpu4=new CPU();
        new Thread(jobYielder).start();
        new Thread(cpu1).start();
        //模拟多个应用程序
//        new Thread(cpu2).start();
//        new Thread(cpu3).start();
//        new Thread(cpu4).start();
    }
}
