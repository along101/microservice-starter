package com.along101.microservice.starter.cachecloud;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yinzuolong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class CachecloudAutoConfigurationTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testCache() throws InterruptedException {
        redisTemplate.opsForValue().set("key1", "value1");
        String cacheValue = redisTemplate.opsForValue().get("key1");
        Assert.assertEquals("value1", cacheValue);
    }
}
