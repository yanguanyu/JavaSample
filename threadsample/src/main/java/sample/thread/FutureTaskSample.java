package sample.thread;

import java.util.concurrent.*;

/**
 * 通过 Future、FutureTask 来获得异步执行的线程中的计算结果
 * Created by guanyu on 2014/6/24.
 */
public class FutureTaskSample {

    ExecutorService threadPool = Executors.newFixedThreadPool(3);

    String getResult() {
        System.out.println("Long-Running Operations in getResult... ");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Long-Running Operations Finish...");
        return "finish";
    }

    Future<String> getResult2() {
        return threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getResult();
            }
        });
    }

    void execute() {
        System.out.println("getResult2 use Future...");
        Future<String> feture = getResult2();
        System.out.println("start doing other things...");

        try {
            System.out.println("waiting for result...");
            String result = feture.get();//会阻塞在这里直到线程执行结束返回
//            String result = feture.get(2, TimeUnit.SECONDS);//it will cause timeout error here
            System.out.println("finish getting the result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FutureTaskSample sample = new FutureTaskSample();
        sample.execute();
    }
}
