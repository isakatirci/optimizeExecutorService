package com.isakatirci.blockingqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {

    private LinkedBlockingDeque<String> blockingQueueList;
    private int index;
    private AtomicBoolean shutdown = new AtomicBoolean(false);

    public LinkedBlockingDeque<String> getBlockingQueueList() {
        return blockingQueueList;
    }

    public void setBlockingQueueList(LinkedBlockingDeque<String> blockingQueueList) {
        this.blockingQueueList = blockingQueueList;
    }

    public AtomicBoolean isShutdown() {
        return shutdown;
    }

    public AtomicBoolean getShutdown() {
        return shutdown;
    }

    public void setShutdown(AtomicBoolean shutdown) {
        this.shutdown = shutdown;
    }

    public Consumer(LinkedBlockingDeque<String> blockingQueueList, int index) {
        this.blockingQueueList = blockingQueueList;
        this.index = index;
    }

    @Override
    public void run() {
        while (!shutdown.get()) {
            try {
                String str = blockingQueueList.take();
                System.out.println(
                        "thread:" + Thread.currentThread().getId() + " hub:" + index + " size => "
                                + blockingQueueList.size()
                                + " " + str);
                Thread.sleep(5000);
            } catch (Exception e) {

            }
        }
        System.out.println("thread:" + Thread.currentThread().getId() + " finish!");
    }
}
