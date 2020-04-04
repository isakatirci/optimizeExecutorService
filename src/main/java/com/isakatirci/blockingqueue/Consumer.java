package com.isakatirci.blockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable {

    private LinkedBlockingQueue<String> blockingQueueList;

    public Consumer(LinkedBlockingQueue<String> blockingQueueList) {
        this.blockingQueueList = blockingQueueList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String str = blockingQueueList.take();
                if (str == "POISON") {
                    System.out.println("thread:" + Thread.currentThread().getId() + " finish!"
                            + " blockingQueueList.size() => " + blockingQueueList.size());
                    break;
                }
                System.out.println(
                        "thread:" + Thread.currentThread().getId() + " size => "
                                + blockingQueueList.size()
                                + " " + str);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error: size => " + blockingQueueList.size());
        }
    }

    public LinkedBlockingQueue<String> getBlockingQueueList() {
        return blockingQueueList;
    }
}
