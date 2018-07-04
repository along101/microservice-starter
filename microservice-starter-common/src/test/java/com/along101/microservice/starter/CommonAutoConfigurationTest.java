package com.along101.microservice.starter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yinzuolong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class CommonAutoConfigurationTest {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void init() {
        String id = applicationContext.getId();
        System.out.printf("DemoApplication '%s' started !", id);
    }
}
