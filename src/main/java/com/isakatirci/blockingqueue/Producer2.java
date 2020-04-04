package com.isakatirci.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Producer2 implements Runnable {

    public static void main(String[] args) {
        Integer poison = -1;
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        new Thread(new Producer2(queue, poison)).start();
        new Thread(new Consumer2(queue, poison)).start();
        new Thread(new Producer2(queue, poison)).start();
        new Thread(new Consumer2(queue, poison)).start();
    }

    private final BlockingQueue<Integer> queue;
    private final Integer POISON;


    public Producer2(BlockingQueue<Integer> queue, Integer POISON) {
        this.queue = queue;
        this.POISON = POISON;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            while (true) {
                try {
                    queue.put(POISON);
                    break;
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private void process() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            System.out.println("[Producer] Put : " + i);
            queue.put(i);
            System.out.println("[Producer] Queue remainingCapacity : " + queue.remainingCapacity());
            Thread.sleep(100);
        }
    }
}
