package com.along101.microservice.starter.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by yinzuolong on 2017/4/24.
 */
@Slf4j
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(TestApplication.class, args);
        TimeUnit.SECONDS.sleep(3);
    }

    @PostConstruct
    public void init() {
        UUID u = UUID.randomUUID();
        log.info("test log {}", u.toString());
    }
}
