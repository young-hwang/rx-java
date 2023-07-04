package io.younghwang.reactive._03;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxEx {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("pub"))
                .log()
                .subscribeOn(Schedulers.newSingle("sub"))
                .subscribe(System.out::println);

        System.out.println("exit");
    }
}
