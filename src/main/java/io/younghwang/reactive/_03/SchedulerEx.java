package io.younghwang.reactive._03;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {
    // reactive programming 표준 scheduler를 이용한 관리
    // 1. publishOn: publisher는 느리나 subscriber가 빠른 경우 사용
    // 2. subscribeOn: publisher는 빠르나 subscriber가 느린 경우 사용
    public static void main(String[] args) {
        Publisher<Integer> pub = s -> {
            s.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    s.onNext(1);
                    s.onNext(2);
                    s.onNext(3);
                    s.onNext(4);
                    s.onNext(5);
                    s.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // 중간 publisher
        Publisher<Integer> pubSubOn = s -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                pub.subscribe(s);
            });
        };

        pubSubOn.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });
        System.out.println("exit");
    }
}
