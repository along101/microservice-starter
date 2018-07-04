package com.along101.microservice.starter.raptor;

import com.codahale.metrics.MetricRegistry;
import com.along101.dropwizard.metrics.transport.KafkaTransport;
import com.along101.microservice.starter.metric.MetricProperties;
import com.along101.microservice.starter.metric.UriTagMetricReporter;
import com.along101.raptor.common.RaptorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Configuration
@Import({CatInterceptorConfiguration.class, MetricsInterceptorConfiguration.class})
public class RaptorStarterAutoConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public RaptorInfo raptorInfoBean() {
        RaptorInfo raptorInfo = RaptorInfo.getInstance();
        String appId = environment.getProperty("com.along101.appId");
        if (!StringUtils.isEmpty(appId)) {
            raptorInfo.setAppId(appId);
        }
        String prefix = environment.getProperty("raptor.metrics.prefix");
        if (!StringUtils.isEmpty(prefix)) {
            raptorInfo.setMetricPrefix(prefix);
        }
        return raptorInfo;
    }

    @Configuration
    @ConditionalOnClass(KafkaTransport.class)
    @EnableConfigurationProperties(MetricProperties.class)
    static class metricAutoConfiguration {

        @Autowired
        private Environment environment;

        @Autowired
        private MetricProperties metricProperties;

        @Bean
        @ConditionalOnBean(KafkaTransport.class)
        public UriTagMetricReporter raptorMetricReporter(KafkaTransport transport) {
            String appId = environment.getProperty("com.along101.appId", "unknow");
            MetricRegistry metricRegistry = MetricContext.getMetricRegistry();
            UriTagMetricReporter reporter = UriTagMetricReporter.forRegistry(metricRegistry)
                    .withApplicationId(appId)
                    .build(transport);
            reporter.start(metricProperties.getPeriod(), TimeUnit.SECONDS);
            return reporter;
        }
    }

}
