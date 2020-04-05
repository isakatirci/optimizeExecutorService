package com.isakatirci.blockingqueue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {

    private final LinkedBlockingQueue<String> blockingQueueList;
    private WebDriver webDriver;
    private AtomicBoolean running = new AtomicBoolean(false);

    public boolean getRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public Consumer(LinkedBlockingQueue<String> blockingQueueList, WebDriver webDriver) {
        this.blockingQueueList = blockingQueueList;
        this.webDriver = webDriver;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("START..");
                String str = blockingQueueList.take();
                running.set(true);
                System.out.println("TAKEN..");
                if (str == "POISON") {
                    System.out.println("thread:" + Thread.currentThread().getId() + " finish!"
                            + " blockingQueueList.size() => " + blockingQueueList.size());
                    if (this.webDriver != null) {
                        this.webDriver.close();
                        if (this.webDriver != null) {
                            this.webDriver.quit();
                            this.webDriver = null;
                        }
                    }
                    break;
                }
                System.out.println(
                        "thread:" + Thread.currentThread().getId() + " size => "
                                + blockingQueueList.size()
                                + " " + str);
                System.out.println("webDriver.get(str)");
                webDriver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
                webDriver.get(str);
                System.out.println(webDriver.findElement(By.cssSelector("h3.description")).getText());
                System.out.println("System.out.println(webDriver.findElement(By.cssSelector(\"h3.description\")).getText())");
                running.set(false);
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
