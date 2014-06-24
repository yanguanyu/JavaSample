package sample.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger用于在两个线程之间进行数据交换，线程会阻塞在Exchanger的exchange方法上，直到
 * 另外一个线程也到了同一个Exchanger的exchange方法时，二者进行交换，然后两个线程会继续执行
 * 自身相关的代码
 * 使用Exchanger要注意线程数一定要为偶数，否则肯定会有一个线程永远阻塞在exchange方法上。
 * Created by guanyu on 2014/6/23.
 */
public class ExchangerSample {

    class Task implements Runnable {
        String taskName;
        Exchanger<String> exchanger;
        String value;
        public Task(String taskName, Exchanger<String> exchanger, String value) {
            this.taskName = taskName;
            this.exchanger = exchanger;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " before exchange, value = [" + value + "]");
                value = exchanger.exchange(value);//先到这里的线程会阻塞直到另一个线程也执行到这里，交换数据后再继续执行
                System.out.println(threadName + " after exchange, value = [" + value + "]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void execute() {
        Exchanger<String> exchanger = new Exchanger<String>();
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            threadPool.submit(new Task("task" + i, exchanger, "I am from task" + i));
        }
        threadPool.shutdown();//当线程池中的任务都执行完了，销毁掉线程池，如果不调用该方法，程序将一直运行下去。
    }

    public static void main(String[] args) {
        ExchangerSample sample = new ExchangerSample();
        sample.execute();
    }
}
