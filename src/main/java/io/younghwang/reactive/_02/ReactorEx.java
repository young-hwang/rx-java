package io.younghwang.reactive._02;

import reactor.core.publisher.Flux;

public class ReactorEx {
    public static void main(String[] args) {
        Flux.create(s -> {
                    s.next(1);
                    s.next(2);
                    s.next(3);
                    s.complete();
                })
                .log()
                .subscribe(System.out::println);

        Flux.<Integer>create(s -> {
                    s.next(1);
                    s.next(2);
                    s.next(3);
                    s.complete();
                })
                .log()
                .map(s -> 10 * s)
                .reduce((a, b) -> a + b)
                .log()
                .subscribe(System.out::println);
    }
}
