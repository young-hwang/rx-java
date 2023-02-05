package io.younghwang.rxjava._010602;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SingleSample {
    public static void main(String[] args) {
        // Single 생성
        Single<DayOfWeek> single = Single.create(emitter -> {
            emitter.onSuccess(LocalDate.now().getDayOfWeek());
        });

        // 구독
        single.subscribe(new SingleObserver<DayOfWeek>() {
            // 구독 준비가 됐을 때의 처리
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // 아무것도 하지 않음
            }

            // 데이터 통지를 받았을 때의 처리
            @Override
            public void onSuccess(@NonNull DayOfWeek value) {
                System.out.println(value);
            }

            // 에러 통지를 받았을 때의 처리
            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("에러 = " + e);
            }
        });
    }
}
