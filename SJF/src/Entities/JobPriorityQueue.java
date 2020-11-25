package Entities;

import java.util.concurrent.locks.*;

import static Sources.ConstVar.QSIZE;
import static Sources.CriticalSources.jobQ;


public class JobPriorityQueue {
    private static Job[] q;
    public static int head = 0, tail = QSIZE - 1;
    public Lock IOlock;
    public Lock mutex;
    public Condition notfull, notempty;
    private boolean isempty;

    public JobPriorityQueue() {
        this.q = new Job[QSIZE + 3];
        for (int i = 0; i <= QSIZE + 2; i++) q[i] = new Job();
        this.IOlock = new ReentrantLock();//输出锁，对于任何输出均需取得
        this.isempty = true;
        this.mutex = new ReentrantLock();
        this.notempty = mutex.newCondition();
        this.notfull = mutex.newCondition();//从队列中存取元素的互斥信号量
        this.tail = QSIZE - 1;
        this.head = 0;
    }

    public boolean empty() {
        return isempty;
    }

    public int getItemsNum() {
        if (empty()) return 0;
        return ((tail + QSIZE - head) % QSIZE + 1);
    }

    public void printQ() {
        IOlock.lock();
        try {
            System.out.println("Jobs in queue:");
            if (isempty) System.out.println("Queue empty!");
            else {
                boolean isfirst = true;
                for (int i = head; (i != (tail + 1) % QSIZE) || isfirst; i = (i + 1) % QSIZE) {
                    System.out.print(q[i].pid + " ");
                    isfirst = false;
                }
            }
            //如果队满则必须先让第一个输出
            System.out.println("");
            System.out.println("Job during time in queue:");
            if (isempty) System.out.println("Queue empty!");
            else {
                boolean isfirst = true;
                for (int i = head; (i != (tail + 1) % QSIZE) || isfirst; i = (i + 1) % QSIZE) {
                    System.out.print(q[i].dtime + "ms ");
                    isfirst = false;
                }
            }
            System.out.println("");
        } finally {
            IOlock.unlock();
        }
    }

    public void push(Job job) {
        mutex.lock();
        try {

            if (getItemsNum() >= QSIZE && !isempty)
                notfull.await();
            //必须获得重入锁后才能调用
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int chkpos = tail;

        while (chkpos != (head - 1 + QSIZE) % QSIZE && q[chkpos].dtime > job.dtime) {
            q[(chkpos + 1) % QSIZE] = q[chkpos];
            chkpos = (chkpos - 1 + QSIZE) % QSIZE;
        }
        q[(chkpos + 1) % QSIZE] = job;
        tail = (tail + 1) % QSIZE;
        isempty = false;
        notempty.signal();
        mutex.unlock();
    }

    public void pop() {
        mutex.lock();
        try {
            if (isempty) {
                notempty.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        head = (head + 1) % QSIZE;
        if (head == (tail + 1) % QSIZE) {
            isempty = true;
        }
        notfull.signal();
        mutex.unlock();
    }

    public Job front(boolean POP) {
        mutex.lock();
        try {
            if (jobQ.isempty)
                notempty.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int head0 = head;
        if (POP) {
            head = (head + 1) % QSIZE;
            if (head == (tail + 1) % QSIZE) {
                isempty = true;
            }
            notfull.signal();
            //注意此时也要释放！
        }
        mutex.unlock();
        //弹了之后head就变了！！！！！
        return q[head0];
    }


}
