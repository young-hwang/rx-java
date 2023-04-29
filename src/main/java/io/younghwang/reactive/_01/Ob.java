package io.younghwang.reactive._01;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ob {
    public static void main(String[] args) {
        System.out.println("for");
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
        }

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("iterable for-each");
        for (Integer i : iterable) {
            System.out.println(i);
        }

        System.out.println("implimentation iterable");
        Iterable<Integer> iterableImpl = () -> {
            return new Iterator<Integer>() {
                int i = 0;
                final static int MAX = 10;

                @Override
                public boolean hasNext() {
                    return i < MAX;
                }

                @Override
                public Integer next() {
                    return ++i;
                }
            };
        };

        for (Integer i : iterableImpl) {
            System.out.println(i);
        }

        System.out.println("how to execute iterator");
        for (Iterator<Integer> it = iterableImpl.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }

        // Iterable <----> Observable (duality) 쌍대성
        // pull             push
        System.out.println("Observable");
        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        };

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(observer);
        intObservable.run();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(intObservable);
        intObservable.addObserver(observer);
        System.out.println(Thread.currentThread().getName() + " EXIT");
        executorService.shutdown();
    }

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }
}
