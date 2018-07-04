package com.along101.microservice.starter.raptor;

import com.along101.microservice.starter.raptor.client.CatClientInterceptor;
import com.along101.microservice.starter.raptor.service.CatServiceInterceptor;
import com.dianping.cat.Cat;
import com.along101.microservice.starter.raptor.client.CatClientInterceptor;
import com.along101.microservice.starter.raptor.service.CatServiceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinzuolong
 */
@Configuration
@ConditionalOnProperty(value = "raptor.cat.enabled", havingValue = "true", matchIfMissing = true)
public class CatInterceptorConfiguration {

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(Cat.class)
    static class CatWebResourceConfiguration {
        @Bean
        CatServiceInterceptor raptorCatInterceptor() {
            return new CatServiceInterceptor();
        }

    }

    @Configuration
    @ConditionalOnClass(Cat.class)
    static class CatClientConfiguration {

        @Bean
        CatClientInterceptor createCatClientInterceptor() {
            return new CatClientInterceptor();
        }
    }
}
