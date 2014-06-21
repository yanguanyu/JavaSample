package sample.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 循环屏障，CyclicBarrier 可以协同多个线程，让多个线程在这个屏障前等待，直到所有线程都到达了这个屏障时，
 * 在一起继续执行后面的动作。
 * 当await的次数调用达到CyclicBarrier的count时，所有阻塞在await的线程都被唤醒，继续执行。
 * 与CountDownLatch的区别：CountDownLatch不能重复使用，CyclicBarrier可以重复使用。
 * CyclicBarrier使用时如果线程池的线程数过少会导致死锁。
 * Created by guanyu on 2014/6/21.
 */
public class CyclicBarrierSample {

    class MyTask implements Runnable {
        CyclicBarrier cyclicBarrier;
        String taskName;
        public MyTask(CyclicBarrier cyclicBarrier, String taskName) {
            this.cyclicBarrier = cyclicBarrier;
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 2);
                System.out.println(taskName + " arrived barrier, waiting...");
                cyclicBarrier.await();
                System.out.println(taskName + " continue execute and finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(taskName + " finish!");

        }
    }

    void execute() {
        int count = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count + 1);
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        System.out.println("execute start ...");
        for (int i = 0; i < count; i++) {
            threadPool.submit(new MyTask(cyclicBarrier,"task"+ i ));
        }

        //threadPool.submit(new MyTask(cyclicBarrier, "last task"));
        threadPool.shutdown();//当线程池中的任务都执行完了，销毁掉线程池，如果不调用该方法，程序将一直运行下去。
        System.out.println("wait other task...");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("execute finish ...");
    }

    public static void main(String[] args) {
        CyclicBarrierSample sample = new CyclicBarrierSample();
        sample.execute();
    }
}
