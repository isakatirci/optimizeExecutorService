package com.isakatirci.blockingqueue;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BlockingQueueApplication {
    //static int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
    static int numberOfThread = 500;
    static ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
    static List<Consumer> consumers = new ArrayList<>();

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < numberOfThread; i++) {
            Consumer consumer = new Consumer(new LinkedBlockingDeque<String>());
            consumers.add(consumer);
            executorService.submit(consumer);
        }
        int j = 0;
        while (j < 5000) {
            try {
                j++;
                int mixSize = Integer.MAX_VALUE;
                int index = 0;
                for (int i = 0, length = numberOfThread; i < length; i++) {
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
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds());
    }
}
