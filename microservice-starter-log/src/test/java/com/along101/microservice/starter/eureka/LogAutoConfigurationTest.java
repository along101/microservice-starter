package com.along101.microservice.starter.eureka;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by yinzuolong on 2017/4/25.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class LogAutoConfigurationTest {

    @Test
    public void test() throws InterruptedException {
        UUID u = UUID.randomUUID();
        log.info("test log {}", u.toString());
        TimeUnit.SECONDS.sleep(3);
    }
}
