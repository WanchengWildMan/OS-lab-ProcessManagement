package Sources;
import Entities.JobPriorityQueue;

import java.util.Set;
import java.util.concurrent.locks.Lock;

public class CriticalSources {
    public static JobPriorityQueue jobQ=new JobPriorityQueue();//任务优先队列，实现SJF
    public static Set<Integer>pidpool;//分配pid的令牌池
    public static Lock pidReqLock;
    //类静态成员，保证多个实例共有一个资源
    //不加public 默认private！
}
