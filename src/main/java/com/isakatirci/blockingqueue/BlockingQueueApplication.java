package com.isakatirci.blockingqueue;

import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BlockingQueueApplication {
    static int numberOfThread = 500;
    static ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
    static List<Consumer> consumers = new ArrayList<>();

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < numberOfThread; i++) {
            Consumer consumer = new Consumer(new LinkedBlockingQueue<>());
            consumers.add(consumer);
            executorService.submit(consumer);
        }
        for (int j = 0; j < 5000; j++) {
            try {
                j++;
                int index = j % numberOfThread;
                consumers.get(index).getBlockingQueueList().put("j => " + j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while (true) {
            boolean allTaskFinished = true;
            for (int i = 0; i < numberOfThread; i++) {
                try {
                    LinkedBlockingQueue queue = consumers.get(i).getBlockingQueueList();
                    int size = queue.size();
                    if (size != 0) {
                        Thread.sleep(1000);
                        allTaskFinished = false;
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!allTaskFinished) {
                continue;
            }
            for (int i = 0; i < numberOfThread; i++) {
                try {
                    consumers.get(i).getBlockingQueueList().put("POISON");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds());
    }
}
