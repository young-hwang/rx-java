package io.younghwang.reactive._03;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {
    // reactive programming 표준 scheduler를 이용한 관리
    // 1. publishOn: publisher는 빠르나 subscriber가 느린 경우 사용, subscriber에 스레드 처리
    // 2. subscribeOn: publisher는 느리나 subscriber가 빠른 경우 사용, publisher에 스레드 처리
    public static void main(String[] args) {
        Publisher<Integer> mainPublisher = subscriber -> {
            subscriber.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onNext(3);
                    subscriber.onNext(4);
                    subscriber.onNext(5);
                    subscriber.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // 중간 publisher
        // subscribeOn
        // Typically use for slow publisher, fast subscriber
//        Publisher<Integer> subscribeOn = subscriber -> {
//            ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
//                @Override
//                public String getThreadNamePrefix() {
//                    return "publisher thread";
//                }
//            });
//            executorService.execute(() -> {
//                mainPublisher.subscribe(subscriber);
//            });
//        };

        // 중간 publisher
        // publishOn
        // Typically use for fast publisher, slow subscriber
        Publisher<Integer> publishOn = subscriber -> {
            mainPublisher.subscribe(new Subscriber<Integer>() {
                ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                    @Override
                    public String getThreadNamePrefix() {
                        return "subscriber thread";
                    }
                });

                @Override
                public void onSubscribe(Subscription s) {
                    log.debug("publishOn onSubscribe");
                    subscriber.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    log.debug("publishOn onNext: {}", integer);
                    executorService.execute(() -> subscriber.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    log.debug("publishOn onError: {}", t);
                    subscriber.onError(t);
                }

                @Override
                public void onComplete() {
                    log.debug("publishOn onComplete");
                    executorService.execute(() -> subscriber.onComplete());
                    executorService.shutdown(); // graceful shutdown
//                    executorService.shutdownNow(); // immediate shutdown
                }
            });
        };

        publishOn.subscribe(new Subscriber<Integer>() {
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
