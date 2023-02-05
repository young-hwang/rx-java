package io.younghwang.rxjava._010602;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.time.LocalDate;

public class CompletableSample {
    public static void main(String[] args) throws InterruptedException {
        Completable completable = Completable.create(emitter -> {
            emitter.onComplete();
        });

        completable.subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {

                    // 구독 준비가 됐을 때의 처리
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 아무것도 하지 않는다
                    }

                    // 완료 통지를 받을 때의 처리
                    @Override
                    public void onComplete() {
                        System.out.println("완료");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("에러 = " + e);
                    }
                });

        Thread.sleep(100L);
    }
}
