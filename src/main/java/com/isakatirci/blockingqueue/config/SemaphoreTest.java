package com.isakatirci.blockingqueue.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    //static Semaphore semaphore = new Semaphore(1);
    static int numberOfThread = 50;
    static Semaphore semaphore = new Semaphore(1);
    static ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> {
                String name = Thread.currentThread().toString();
                try {
                    semaphore.acquire();
                    System.out.println(name + " : acquiring lock...");
                    System.out.println(name + " : available Semaphore permits now: "
                            + semaphore.availablePermits());

                    System.out.println(name + " : got the permit!");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // calling release() after a successful acquire()
                    System.out.println(name + " : releasing lock...");
                    semaphore.release();
                    System.out.println(name + " : available Semaphore permits now: "
                            + semaphore.availablePermits());
                }
            });
        }
    }
}
