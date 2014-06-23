package sample.thread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Semaphor semaphore.acquire() 和 semaphore.release()方法之间的代码块只能有
 * count个线程同时执行，也就是说可以利用这个count来控制某个方法或者某块代码的并发数量，
 * 只有执行 semaphore.release() 后才会等待在 semaphore.acquire() 的线程才有机会可以
 * 执行，所以该方法要放在finally块中以保证都会被执行到。
 * Created by guanyu on 2014/6/23.
 */
public class SemaphorSample {

    AtomicInteger count = new AtomicInteger(0);

    class MyTask implements Runnable {
        Semaphore semaphore;
        String taskName;
        public MyTask(Semaphore semaphore, String taskName) {
            this.semaphore = semaphore;
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();//以下代码块只有两个线程能过同时进入，所以num的最大值不会超过2
                int num = count.incrementAndGet();
                System.out.println(taskName + " is running, the number of running task is " + num);
                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                count.decrementAndGet();
                System.out.println(taskName + " finish!");
                semaphore.release();
            }
        }
    }

    void execute() {
        int count = 10;
        Semaphore semaphore = new Semaphore(2);//最多只能有2个线程同时获得信号量的锁
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        System.out.println("execute start ...");
        for (int i = 0; i < count; i++) {
            threadPool.submit(new MyTask(semaphore,"task"+ i ));
        }
        threadPool.shutdown();//当线程池中的任务都执行完了，销毁掉线程池，如果不调用该方法，程序将一直运行下去。

        System.out.println("execute finish ...");
    }

    public static void main(String[] args) {
        SemaphorSample sample = new SemaphorSample();
        sample.execute();
    }
}
