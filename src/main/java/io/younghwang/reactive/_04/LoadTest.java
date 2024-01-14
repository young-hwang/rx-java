package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/async";

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("thread {}", idx);
                StopWatch subStopWatch = new StopWatch();
                subStopWatch.start();
                String res = restTemplate.getForObject(url, String.class);
                subStopWatch.stop();
                log.info("Elapsed: {} / {}", subStopWatch.getTotalTimeSeconds(), res);
            });
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        stopWatch.stop();
        log.info("Total: {}", stopWatch.getTotalTimeSeconds());
    }
}
