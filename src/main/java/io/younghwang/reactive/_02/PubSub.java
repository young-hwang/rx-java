package io.younghwang.reactive._02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * pub -> [Data1] -> mapPub -> [Data2] -> LogSub
 */
@Slf4j
public class PubSub {
    public static void main(String[] args) {
        Publisher<Integer> publisher = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
        Publisher<String> mapPublisher = mapPub(publisher, s -> "[" + s + "]");
//        Publisher<Integer> mapPublisher2 = mapPub(mapPublisher, s -> -s);
//        Publisher<Integer> sumPublisher = sumPub(publisher);
//        Publisher<Integer> reducePublisher = reducePub(publisher, 0, (a, b) -> a + b);

        mapPublisher.subscribe(logSub());
    }

//    private static Publisher<Integer> reducePub(Publisher<Integer> publisher, int i, BiFunction<Integer, Integer, Integer> biFunction) {
//        return new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> s) {
//                publisher.subscribe(new DelegateSubscriber(s) {
//                    private int value = i;
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        value = biFunction.apply(value, integer);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        s.onNext(value);
//                        s.onComplete();
//                    }
//                });
//            }
//        };
//    }
//
//    private static Publisher<Integer> sumPub(Publisher<Integer> publisher) {
//        return s -> publisher.subscribe(new DelegateSubscriber(s) {
//            int sum = 0;
//
//            @Override
//            public void onNext(Integer integer) {
//                sum += integer;
//            }
//
//            @Override
//            public void onComplete() {
//                s.onNext(sum);
//                s.onComplete();
//            }
//        });
//    }

    private static <T, R> Publisher<R> mapPub(Publisher<T> publisher, Function<T, R> func) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                publisher.subscribe(new DelegateSubscriber<T, R>(sub) {
                    @Override
                    public void onNext(T i) {
                        sub.onNext(func.apply(i));
                    }
                });
            }
        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T i) {
                log.debug("onNext: {}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(Iterable<Integer> iterable) {
        return new Publisher<Integer>() {
            Iterable<Integer> iter = iterable;

            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iter.forEach(s -> subscriber.onNext(s));
                            subscriber.onComplete();
                        } catch (Throwable throwable) {
                            subscriber.onError(throwable);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
