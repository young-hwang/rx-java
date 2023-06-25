package io.younghwang.reactive._02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class DelegateSubscriber implements Subscriber<Integer> {
    private Subscriber sub;

    public DelegateSubscriber(Subscriber subscriber) {
        this.sub = subscriber;
    }

    @Override
    public void onSubscribe(Subscription s) {
        log.debug("onSubscribe map");
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(Integer integer) {
        log.debug("onNext map");
        sub.onNext(integer);
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
