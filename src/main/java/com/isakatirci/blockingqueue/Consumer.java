package com.isakatirci.blockingqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {

    private LinkedBlockingDeque<String> blockingQueueList;
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public Consumer(LinkedBlockingDeque<String> blockingQueueList) {
        this.blockingQueueList = blockingQueueList;
    }

    @Override
    public void run() {
        while (!this.getShutdown()) {
            try {
                String str = ThreadLocalRandom.current().nextInt(2) == 0
                        ? blockingQueueList.takeFirst()
                        : blockingQueueList.takeLast();
                System.out.println(
                        "thread:" + Thread.currentThread().getId() + " size => "
                                + blockingQueueList.size()
                                + " " + str);
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        System.out.println("thread:" + Thread.currentThread().getId() + " finish!"
                + " blockingQueueList.size() => " + blockingQueueList.size());
    }

    public LinkedBlockingDeque<String> getBlockingQueueList() {
        return blockingQueueList;
    }

    public boolean getShutdown() {
        return shutdown.get();
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown.set(shutdown);
    }
}
