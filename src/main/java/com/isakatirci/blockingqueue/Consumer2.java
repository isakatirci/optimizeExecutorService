package com.isakatirci.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Consumer2 implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final Integer POISON;

    public Consumer2(BlockingQueue<Integer> queue, Integer poison) {
        this.queue = queue;
        this.POISON = poison;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer take = queue.take();
                process(take);
                if (take == POISON)
                {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void process(int take) throws InterruptedException {
        System.out.println("[Consumer] Take : " + take);
        Thread.sleep(500);
    }
}
