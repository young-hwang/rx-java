package io.younghwang.rxjava._010502;

import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class SubscriptonCancelSample {
    public static void main(String[] args) throws Exception {
        Flowable.interval(200L, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Long>() {
                    private Subscription subscription;
                    private long startTime;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.startTime = System.currentTimeMillis();
                        this.subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long data) {
                        if ((System.currentTimeMillis() - startTime) > 500) {
                            subscription.cancel();
                            System.out.println("구독 해지");
                            return;
                        }
                        System.out.println("data=" + data);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Thread.sleep(2000L);
    }
}
