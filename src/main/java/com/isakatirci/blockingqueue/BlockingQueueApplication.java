package com.isakatirci.blockingqueue;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BlockingQueueApplication {
    //static int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
    static int numberOfThread = 200;
    static ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
    static List<Consumer> consumers = new ArrayList<>();
    static List<Future<String>> consumerFutures = new ArrayList<>();

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < numberOfThread; i++) {
            Consumer consumer = new Consumer(new LinkedBlockingDeque<String>());
            consumers.add(consumer);
            Future<String> consumerFuture = executorService.submit(consumer, "ok");
            consumerFutures.add(consumerFuture);
        }
        int j = 0;
        while (j < 1000) {
            try {
                j++;
                int mixSize = Integer.MAX_VALUE;
                int index = 0;
                for (int i = 0, length = numberOfThread; i < length; i++) {
                    if (i % 2 == 0) {
                        continue;
                    }
                    int size = consumers.get(i).getBlockingQueueList().size();
                    if (mixSize > size) {
                        mixSize = size;
                        index = i;
                    }
                }
                consumers.get(index).getBlockingQueueList().put("j => " + j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < numberOfThread; i++) {
            Consumer consumer = consumers.get(i);
            int size = consumer.getBlockingQueueList().size();
            if (size != 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
                continue;
            }
            consumer.setShutdown(true);
            consumers.get(i).run();
        }
        executorService.shutdownNow();
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds());
    }
}
