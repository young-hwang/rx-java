package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FutureEx {
    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();

        // callable 값을 리턴할 수 있음, exception도 던질 수 있음
        // runnable 리턴값을 받을 수 없음
        Future<String> result = es.submit(() -> {
            TimeUnit.SECONDS.sleep(2);
            log.info("Hello, world");
            return "Hello, world";
        });

        log.info(result.get());

        // FutureTask = 비동기 callback과 Future 를 포함
        FutureTask<String> ft = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(2);
            log.info("Hello, world - futureTask");
            return "Hello, world";
        }) {
            @Override
            protected void done() {
                try {
                    System.out.println("future task done " + get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        es.execute(ft);

        // CallbackFutureTask 활용
        CallbackFutureTask callbackFutureTask = new CallbackFutureTask(() -> {
            TimeUnit.SECONDS.sleep(2);
            if (1 == 1) throw new RuntimeException("Error");
            log.info("Hello, world - callbackFutureTask");
            return "Hello, world";
        }, System.out::println, e -> System.out.println("Error: " + e.getMessage()));
        es.execute(callbackFutureTask);
        log.info("Exit");
    }
}
