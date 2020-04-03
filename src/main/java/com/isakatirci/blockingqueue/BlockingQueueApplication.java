package com.isakatirci.blockingqueue;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class BlockingQueueApplication {
    //static int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
   static int N_CONSUMERS = 500;
    static ArrayList<LinkedBlockingDeque<String>> blockingQueueList = new ArrayList<>(N_CONSUMERS);
    static ExecutorService executorService = Executors.newFixedThreadPool(N_CONSUMERS);
    static List<Consumer> list = new ArrayList<>();

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < N_CONSUMERS; i++) {
            blockingQueueList.add(new LinkedBlockingDeque<String>());
        }
        for (int i = 0; i < N_CONSUMERS; i++) {
            Consumer consumer = new Consumer(blockingQueueList.get(i), i);
            list.add(consumer);
            executorService.submit(consumer);
        }

        int j = 0;
        while (j < 1000) {
            try {
                j++;
                int mixSize = Integer.MAX_VALUE;
                int index = 0;
                for (int i = 0, length = N_CONSUMERS; i < length; i++) {
                    int size = blockingQueueList.get(i).size();
                    if (mixSize > size) {
                        mixSize = size;
                        index = i;
                    }
                }
                blockingQueueList.get(index).put("j => " + j);
            } catch (Exception e) {

            }
        }
        while (true) {
            boolean all = true;
            for (int i = 0, length = list.size(); i < length; i++) {
                Consumer consumer = list.get(i);
                int size = consumer.getBlockingQueueList().size();
                if (size != 0) {
                    all = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                consumer.getShutdown().set(true);
            }
            if (all) {
                break;
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds()); // Prints: Time Elapsed: 2501
    }
}
