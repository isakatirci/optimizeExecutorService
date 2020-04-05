package com.isakatirci.blockingqueue;

import com.isakatirci.blockingqueue.config.CustomerVoiceProxy;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

import org.apache.log4j.Logger;


public class BlockingQueueApplication {
    private static final Logger LOGGER = Logger.getLogger(BlockingQueueApplication.class);

    static int numberOfThread = 50;
    static ExecutorService executorService = Executors.newFixedThreadPool(numberOfThread);
    static List<Consumer> consumers = new ArrayList<>(numberOfThread);
    static List<Callable<Boolean>> consumersFutures = new ArrayList<>(numberOfThread);
    static List<Consumer> consumersFutures2 = new ArrayList<>(numberOfThread);
    static List<WebDriver> webDrivers = new ArrayList<>(numberOfThread);

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < numberOfThread; i++) {
            System.out.println(" webDrivers.add(createDriver()) => " + i);
            webDrivers.add(createDriver());
        }
        for (int i = 0; i < numberOfThread; i++) {
            Consumer consumer = new Consumer(new LinkedBlockingQueue<>(), webDrivers.get(i));
            System.out.println("executorService.submit(consumer) => " + i);
            consumers.add(consumer);
            executorService.submit(consumer);
        }
        System.out.println("continue 1...");
        for (int j = 0; j < 100; j++) {
            System.out.println("consumersFutures.add(new CallableParameters(consumers.get(j)));");
            try {
                consumers.get(j % numberOfThread).getBlockingQueueList().put("https://www.milliyet.com.tr/cadde/galeri/nilgun-kasapoglunun-yardimina-feyza-cipa-kostu-6181571");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("continue 2...");
        sendPoisons();
        for (int i = 0; i < numberOfThread; i++) {
            WebDriver webDriver = webDrivers.get(i);
            webDrivers.get(i).close();
            if (webDriver != null) {
                webDriver.quit();
                webDriver = null;
            }
        }
        watch.stop();
        System.out.println("Time Elapsed: " + watch.getTotalTimeSeconds());
    }

    private static void sendPoisons() {
        while (true) {
            boolean allTaskFinished = true;
            for (int i = 0; i < numberOfThread; i++) {
                try {
                    Consumer consumer = consumers.get(i);
                    LinkedBlockingQueue queue = consumer.getBlockingQueueList();
                    int size = queue.size();
                    if (size != 0 || consumer.getRunning()) {
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
    }

    private static WebDriver createDriver() {
        return createDriver(null, null, null);
    }

    ;

    private static WebDriver createDriver(String url, CustomerVoiceProxy proxy, String userAgent) {
        System.setProperty("webdriver.chrome.driver",
                "D:\\28 Mart Backup\\browser-mob-test\\src\\main\\resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1680,945");
        options.addArguments("--disable-extensions");
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--start-maximized");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-reading-from-canvas");
        options.addArguments("--disable-webgl");
        options.addArguments("--incognito");
        options.setExperimentalOption("detach", true);
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        //LoggingPreferences logPrefs = new LoggingPreferences();
        //logPrefs.enable("performance", Level.ALL);
        //options.setCapability("goog:loggingPrefs", logPrefs);
        //this most important options
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
/*        if (proxy != null && !proxy.isNonProxy(url)) {
            options.addArguments(new String[]{"--proxy-server==" + proxy.getProxyHost() + ":" + proxy.getProxyPort()});
            LOGGER.info("Proxy Server: " + proxy.getProxyHost() + " for " + url);
        }*/

/*        if (StringUtils.isNotBlank(userAgent)) {
            this.userAgent = userAgent;
        }

        if (StringUtils.isNotBlank(this.userAgent)) {
            options.addArguments(new String[]{"--user-agent=" + this.userAgent});
        }*/

        ChromeDriver driver = new ChromeDriver(options);
        WebDriver.Options manager = driver.manage();
/*        if (!this.getCookieList().isEmpty()) {
            driver.navigate().to("http://127.0.0.1:8080");
            Iterator var8 = this.getCookieList().iterator();

            while(var8.hasNext()) {
                Cookie cookie = (Cookie)var8.next();
                manager.addCookie(new org.openqa.selenium.Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), (Date)null));
            }
        }*/

        return driver;
    }
}
