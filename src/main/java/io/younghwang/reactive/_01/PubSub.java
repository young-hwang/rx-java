package io.younghwang.reactive._01;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

public class PubSub {
    public static void main(String[] args) throws InterruptedException {
        // Publisher <- Observable
        // Subscriber <- Observer

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Publisher p = new Publisher() {
            Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);

            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = itr.iterator();

                subscriber.onSubscribe(new Subscription() {
                    // subscription 는 publisher와 subscriber의 중계자
                    // back pressure 역할


                    @Override
                    public void request(long n) {
                        executorService.execute(() -> {
                            int i = 0;
                            while (i++ < n) {
                                if (it.hasNext()) {
                                    subscriber.onNext(it.next());
                                } else {
                                    subscriber.onComplete();
                                    break;
                                }
                            }
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        p.subscribe(s);

        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        executorService.shutdown();
    }
}
