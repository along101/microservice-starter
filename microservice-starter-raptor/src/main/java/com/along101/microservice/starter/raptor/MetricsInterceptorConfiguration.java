package com.along101.microservice.starter.raptor;

import com.along101.microservice.starter.raptor.service.MetricsServiceInterceptor;
import com.codahale.metrics.MetricRegistry;
import com.along101.microservice.starter.metric.TagName;
import com.along101.microservice.starter.raptor.client.MetricsClientInterceptor;
import com.along101.microservice.starter.raptor.service.MetricsServiceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinzuolong
 */
@Configuration
@ConditionalOnProperty(value = "raptor.metrics.enabled", havingValue = "true", matchIfMissing = true)
public class MetricsInterceptorConfiguration {

    @Configuration
    @ConditionalOnClass({MetricRegistry.class, TagName.class})
    @ConditionalOnWebApplication
    static class MetricsWebResourceConfiguration {

        @Bean
        MetricsServiceInterceptor raptorMetricsInterceptor() {
            return new MetricsServiceInterceptor();
        }
    }

    @Configuration
    @ConditionalOnClass({MetricRegistry.class, TagName.class})
    static class MetricsClientConfiguration {

        @Bean
        MetricsClientInterceptor createMetricsClientInterceptor() {
            return new MetricsClientInterceptor();
        }
    }
}
