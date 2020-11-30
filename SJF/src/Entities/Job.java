package Entities;


import static Sources.CriticalSources.jobQ;

public class Job implements Runnable {
    public Integer pid;
    public int dtime;//ms
    public int waittime;
    public Job(){this.pid=1000000;this.dtime=100000000;};
    public Job(int mypid,int dt){
        this.pid=mypid;
        this.dtime=dt;
        this.waittime=0;
    }

    @Override
    public void run() {
        jobQ.IOlock.lock();
        System.out.print("Job "+this.pid + " 正在运行...");
        System.out.println("服务时间"+dtime+"ms");
        jobQ.IOlock.unlock();
        try {
            Thread.sleep(dtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jobQ.IOlock.lock();
        System.out.println("Job "+this.pid + " 结束");
        jobQ.IOlock.unlock();
    }

}
