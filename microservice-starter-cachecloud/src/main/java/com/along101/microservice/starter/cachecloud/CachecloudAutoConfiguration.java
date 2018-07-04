package com.along101.microservice.starter.cachecloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yinzuolong on 2017/9/20.
 */
@Configuration
@Order(-100)
@EnableConfigurationProperties(CachecloudProperties.class)
public class CachecloudAutoConfiguration {

    @Autowired
    private CachecloudProperties cachecloudProperties;

    private final String CACHECLOUD_TYPE_STANDALONE = "standalone";

    private final String CACHECLOUD_TYPE_CLUSTER = "cluster";

    @Bean
    protected JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory;
        if (CACHECLOUD_TYPE_CLUSTER.equals(cachecloudProperties.getType())) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(cachecloudProperties.getMaxIdle());
            poolConfig.setMinIdle(cachecloudProperties.getMinIdle());
            poolConfig.setMaxWaitMillis(cachecloudProperties.getMaxWait());
            redisConnectionFactory = new CachecloudJedisFactory(cachecloudProperties.getAppId(), cachecloudProperties.getDomainUrl(), poolConfig);
        } else if (CACHECLOUD_TYPE_STANDALONE.equals(cachecloudProperties.getType())) {
            redisConnectionFactory = new CachecloudJedisFactory(cachecloudProperties.getAppId(), cachecloudProperties.getDomainUrl());
        } else {
            throw new UnsupportedOperationException();
        }
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
