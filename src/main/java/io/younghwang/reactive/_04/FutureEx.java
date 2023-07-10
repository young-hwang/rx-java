package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class FutureEx {
    interface SuccessCallback {
        void onSuccess(String result);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> result = es.submit(() -> {
            Thread.sleep(2000);
            log.info("Hello, world");
            return "Hello, world";
        });

        log.info(result.get());

        // FutureTask = 비동기 callback과 Future 를 포함
        FutureTask<String> ft = new FutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("Hello, world - futureTask");
            return "Hello, world";
        }) {
            @Override
            protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        es.execute(ft);
        log.info(ft.get());

        // CallbackFutureTask 활용
        CallbackFutureTask callbackFutureTask = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            log.info("Hello, world - callbackFutureTask");
            return "Hello, world";
        }, System.out::println);
        es.execute(callbackFutureTask);
        log.info("Exit");
    }
}
