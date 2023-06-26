package io.younghwang.reactive._02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class DelegateSubscriber<T, R> implements Subscriber<T> {
    private Subscriber sub;

    public DelegateSubscriber(Subscriber<? super R> subscriber) {
        this.sub = subscriber;
    }

    @Override
    public void onSubscribe(Subscription s) {
        log.debug("onSubscribe map");
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(T i) {
        log.debug("onNext map");
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        log.debug("onError map");
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        log.debug("onComplete map");
        sub.onComplete();
    }
}
