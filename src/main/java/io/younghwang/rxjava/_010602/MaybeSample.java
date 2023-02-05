package io.younghwang.rxjava._010602;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MaybeSample {
    public static void main(String[] args) {
        // Maybe 생성
        Maybe<DayOfWeek> maybe = Maybe.create(emitter -> {
            emitter.onSuccess(LocalDate.now().getDayOfWeek());
        });

        // 구독
        maybe.subscribe(new MaybeObserver<DayOfWeek>() {
            // 구독 준비가 됬을 때 처리
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // 아무것도 하지 않는다
            }

            // 데이터 통지를 받을 때의 처리
            @Override
            public void onSuccess(@NonNull DayOfWeek value) {
                System.out.println(value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("에러 = " + e);
            }

            // 완료 통지를 받을 때의 처리
            @Override
            public void onComplete() {
                System.out.println("완료");
            }
        });
    }
}
