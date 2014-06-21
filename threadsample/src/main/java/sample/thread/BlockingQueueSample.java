package sample.thread;

import java.util.concurrent.*;

/**
 * BlockingQueueSample
 * Created by guanyu on 2014/6/8.
 */
public class BlockingQueueSample {

//    final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(3);
    final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(3);
    int i = 0;
    Object lock = new Object();
    class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    String threadName = Thread.currentThread().getName();
                    int temp = 0;
                    synchronized (lock) {
                        temp = i++;
                    }
                    System.out.println("produce " + temp + " by " + threadName);
                    queue.put(temp);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    TimeUnit.SECONDS.sleep(1);
                    int temp = queue.take();
                    String threadName = Thread.currentThread().getName();
                    System.out.println("consume " + temp + " by " + threadName);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void execute() {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 2; ++ i) {
            threadPool.submit(new Producer());
        }

        for (int i = 0; i < 3; i++) {
            threadPool.submit(new Consumer());
        }

        threadPool.shutdown();
        //threadPool.shutdownNow();
    }

    public static void main(String[] args) {
        BlockingQueueSample sample = new BlockingQueueSample();
        sample.execute();

    }
}
