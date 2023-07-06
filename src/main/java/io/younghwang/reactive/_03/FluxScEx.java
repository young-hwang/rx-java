package io.younghwang.reactive._03;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {
        // thread로 만드는 다양한 방법
        // user thread - 하나 라도 있으면 종료하지 않음
        // daemon thread - user thread 가 없고 deamon thread 만 있으면 강제 종료
        // daemon thread 생성을 하여 처리
        // interval을 이용한 쓰레드 처리 - 쓰레드를 따로 선언하지 않아도 스레드 생성
        // subscribe만 지정한다고 실행 되지 않음
//        Flux.interval(Duration.ofMillis(500))
//                .subscribe(System.out::println);

        // 실행 실행을 위하여 필요
//        TimeUnit.SECONDS.sleep(5);


        // 원하는 개수 만큼 데이터를 받은 후 종료
//        Flux.interval(Duration.ofMillis(200))
//                .take(10)
//                .subscribe(System.out::println);
//
//        log.debug("exit");
//        TimeUnit.SECONDS.sleep(5);

        // 사용자 스레드 메인 스레드가 종료 되었다고 해서 종료 되지 않는다.
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            System.out.println("Hello");
        });

        System.out.println("exit");
    }
}
