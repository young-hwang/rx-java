package io.younghwang.reactive._04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@SpringBootApplication
public class ReactiveApplication {
    @Component
    public static class MyService {
        public String hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(1000);
            return "Hello";
        }
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(ReactiveApplication.class, args)) {

        }
    }

    @Autowired
    MyService myService;

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");
            String hello = myService.hello();
            log.info(hello);
        };
    }
}
