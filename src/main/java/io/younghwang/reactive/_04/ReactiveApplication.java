package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync // @Async 활성화
@SpringBootApplication
public class ReactiveApplication {
    @RestController
    public static class MyController {
        Queue<DeferredResult<String>> queue = new ConcurrentLinkedQueue<>();

        @GetMapping("/async")
        public String async() throws InterruptedException {
            TimeUnit.SECONDS.sleep(2);
            return "Hello";
        }

        @GetMapping("/async1")
        public Callable<String> async1() {
            log.info("async1");
            return () -> {
                log.debug("async1 callable");
                TimeUnit.SECONDS.sleep(2);
                return "Hello";
            };
        }

        @GetMapping("/deferred")
        public DeferredResult<String> deferred() {
            log.info("async1");
            DeferredResult<String> objectDeferredResult = new DeferredResult<>(6000000L);
            queue.add(objectDeferredResult);
            return objectDeferredResult;
        }

        @GetMapping("/deferred/count")
        public String deferredCount() {
            return String.valueOf(queue.size());
        }

        @GetMapping("/deferred/event")
        public String deferredEvent(String msg) {
            for (DeferredResult<String> result : queue) {
                result.setResult("Hello" + msg);
                queue.remove(result);
            }
            return "OK";
        }

        @GetMapping("/emitter")
        public ResponseBodyEmitter emitter() {
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        emitter.send("<p>Stream " + i + "</p>");
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (Exception e) {

                }
            });
            return emitter;
        }
    }

    @Component
    public static class MyService {
        @Async("myThreadPool")
        public Future<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }

        @Async("myThreadPool1")
        public ListenableFuture<String> hello1() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }
    }

    @Bean
    ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10); // 10개의 쓰레드를 미리 만들어 놓는다.
        te.setMaxPoolSize(100); // 큐가 다 차면 쓰레드가 100개까지 늘어날 수 있다.
        te.setQueueCapacity(200); // 200개의 요청을 큐에 담아둔다.
        te.setKeepAliveSeconds(10);
//        te.setTaskDecorator(); // 쓰레드에 decorator 추가
        te.setThreadNamePrefix("myThread");
        te.initialize();
        return te;
    }

    @Bean
    ThreadPoolTaskExecutor myThreadPool1() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(10); // 10개의 쓰레드를 미리 만들어 놓는다.
        te.setMaxPoolSize(100); // 큐가 다 차면 쓰레드가 100개까지 늘어날 수 있다.
        te.setQueueCapacity(200); // 200개의 요청을 큐에 담아둔다.
        te.setKeepAliveSeconds(10);
//        te.setTaskDecorator(); // 쓰레드에 decorator 추가
        te.setThreadNamePrefix("myThread-1");
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

//    @Autowired
//    MyService myService;

//    @Bean
//    ApplicationRunner run() {
//        return args -> {
//            log.info("run()");
//            Future<String> f = myService.hello();
//            log.info("exit: " + f.isDone());
//            log.info("result: " + f.get());
//            log.info("-------------------");
//            ListenableFuture<String> listenableFuture = myService.hello1();
//            listenableFuture.addCallback(s -> System.out.println(s), e -> System.out.println(e.getMessage()));
//            log.info("exit");
//        };
//    }
}
