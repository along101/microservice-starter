package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.Config;
import com.ctrip.apollo.ConfigChangeListener;
import com.ctrip.apollo.model.ConfigChange;
import com.ctrip.apollo.model.ConfigChangeEvent;
import com.ctrip.apollo.spring.annotation.ApolloConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yinzuolong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ApolloClientAutoConfigurationTest {

    @ApolloConfig
    private Config config;

    @Value("${test1}")//apollo中配置值为"test-1",application.properties中没有配置
    private String test1;

    @Value("${test2}")//apollo中配置值为"test-2",application.properties中配置为test-2a
    private String test2;


    @Value("${test3}")
    private String test3;//apollo中没有配置，application.properties中配置为test-3a

    @Value("${apollo.nameSpaces}")
    private String namespace;

    @Test
    public void testConfig() {

        Assert.assertEquals(config.getProperty("test1", "null"), "test-1");
        Assert.assertEquals(config.getProperty("test2", "null"), "test-2");
        Assert.assertEquals(config.getProperty("test3", "null"), "null");

        Assert.assertEquals(test1, "test-1");
        Assert.assertEquals(test2, "test-2");
        Assert.assertEquals(test3, "test-3a");

        Assert.assertEquals(namespace, "基础框架.microservice.starter.v1");
    }

    /**
     * 运行测试后，在apollo 中手工改一个配置，发布后查看控制台输出
     * @throws InterruptedException
     */
    @Test
    public void testConfigChange() throws InterruptedException {
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                System.out.println("Changes for namespace " + changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    System.out.println(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                }
            }
        });

//        TimeUnit.SECONDS.sleep(20);
    }
}
