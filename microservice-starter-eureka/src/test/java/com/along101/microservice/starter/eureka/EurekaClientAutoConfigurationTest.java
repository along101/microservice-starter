package com.along101.microservice.starter.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by yinzuolong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class EurekaClientAutoConfigurationTest {

    @Autowired
    private EurekaClient eurekaClient;

    @Test
    public void testDsicovery() throws InterruptedException {
        boolean registed = false;
        for (int i = 0; i < 120; i++) {
            InstanceInfo.InstanceStatus status = eurekaClient.getInstanceRemoteStatus();
            if (status == InstanceInfo.InstanceStatus.UP || status == InstanceInfo.InstanceStatus.STARTING) {
                registed = true;
                break;
            }
            TimeUnit.SECONDS.sleep(1);
        }
        if (!registed) {
            Assert.fail();
        }
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("EUREKA-SERVER", false);
        System.out.println(instance.getHomePageUrl());
    }
}
