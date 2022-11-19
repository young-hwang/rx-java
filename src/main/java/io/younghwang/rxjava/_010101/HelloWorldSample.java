package io.younghwang.rxjava._010101;

import io.reactivex.rxjava3.core.Flowable;

import java.util.Arrays;

public class HelloWorldSample {
    public static void main(String[] args) {
        // 데이터를 통지하는 생산자를 생성
        Flowable<String> flowable = Flowable.just("Hello", "World");
        // 통지받은 데이터를 출력
        flowable.subscribe(data -> System.out.println(data));
    }
}
