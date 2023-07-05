package io.younghwang.reactive._03;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {
        // thread로 만드는 다양한 방법
        // user thread, daemon thread
        // daemon thread 생성을 하여 처리
        // subscribe만 지정한다고 실행 되지 않음
        Flux.interval(Duration.ofMillis(500))
                .subscribe(System.out::println);
        log.debug("exit");

        // 실행 실행을 위하여 필요
        TimeUnit.SECONDS.sleep(5);


        Flux.interval(Duration.ofMillis(500))
                .subscribe(System.out::println);
        log.debug("exit");
    }
}
