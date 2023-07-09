package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureEx {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();
        Future<String> result = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Hello, world");
            return "Hello, world";
        });
        log.info(result.get());
        log.info("Exit");
    }
}
