package sample.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch的await()方法会阻塞到其countDown()方法的调用次数等于
 * CountDownLatch的count时，这些阻塞在await()的线程会被全部唤醒。
 * Created by guanyu on 2014/6/21.
 */
public class CountDownLatchSample {

    class PreTask implements Runnable {
        CountDownLatch countDownLatch;
        String taskName;
        public PreTask(CountDownLatch countDownLatch, String taskName) {
            this.countDownLatch = countDownLatch;
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println(taskName + " start!");
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(taskName + " finish!");
            this.countDownLatch.countDown();

        }
    }

    class PostTask implements Runnable {
        CountDownLatch countDownLatch;
        String taskName;
        public PostTask(CountDownLatch countDownLatch, String taskName) {
            this.countDownLatch = countDownLatch;
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println("wait all pre task finish...");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(taskName + " finish!");
        }
    }

    void execute() {
        int count = 3;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        System.out.println("execute start ...");
        for (int i = 0; i < count; i++) {
            threadPool.submit(new PreTask(countDownLatch,"pre task"+ i ));
        }
        for (int i = 0; i < 2; i++) {
            threadPool.submit(new PostTask(countDownLatch, "post task" + i));
        }
        threadPool.shutdown();//当线程池中的任务都执行完了，销毁掉线程池，如果不调用该方法，程序将一直运行下去。

        System.out.println("execute finish ...");
    }

    public static void main(String[] args) {
        CountDownLatchSample sample = new CountDownLatchSample();
        sample.execute();
    }
}
