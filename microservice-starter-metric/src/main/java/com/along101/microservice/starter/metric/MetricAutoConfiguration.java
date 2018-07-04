package com.along101.microservice.starter.metric;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.along101.dropwizard.metrics.TaggedMetricRegistry;
import com.along101.dropwizard.metrics.reporter.KairosdbReporter;
import com.along101.dropwizard.metrics.transport.KafkaTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ExportMetricReader;
import org.springframework.boot.actuate.metrics.reader.MetricReader;
import org.springframework.boot.actuate.metrics.reader.MetricRegistryMetricReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyicong on 2017/4/21.
 */
@Configuration
@EnableConfigurationProperties(MetricProperties.class)
public class MetricAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MetricAutoConfiguration.class);

    @Autowired
    MetricProperties metricProperties;

    @Value("${spring.application.name}")
    private String applicationId;

    @Bean
    @ExportMetricReader
    public MetricReader metricReader(TaggedMetricRegistry metricRegistry) {
        return new MetricRegistryMetricReader(metricRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaggedMetricRegistry metricRegistry() {
        TaggedMetricRegistry taggedMetricRegistry = new TaggedMetricRegistry();

        taggedMetricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
        taggedMetricRegistry.register("jvm.mem", new MemoryUsageGaugeSet());
        taggedMetricRegistry.register("jvm.thread-states", new ThreadStatesGaugeSet());

        return taggedMetricRegistry;
    }

    @Bean
    public KairosdbReporter kairosdbReporter(MetricRegistry metricRegistry, KafkaTransport transport) {
        if (StringUtils.isEmpty(metricProperties.getKafkaTopic())
                || StringUtils.isEmpty(metricProperties.getKafkaServers())) {
            logger.info("Metrics KairosdbReporter not enabled");
            return null;
        }

        KairosdbReporter reporter = KairosdbReporter.forRegistry(metricRegistry)
                .prefixedWith(metricProperties.getPrefix())
                .withApplicationId(applicationId)
                .build(transport);

        reporter.start(metricProperties.getPeriod(), TimeUnit.SECONDS);

        logger.info("Metrics KairosdbReporter started, report metrics every " + metricProperties.getPeriod() + " seconds. kafkaTopic={}, kafkaServer={}", metricProperties.getKafkaTopic(), metricProperties.getKafkaServers());

        return reporter;
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaTransport createKafkaTransport() {
        return new KafkaTransport(metricProperties.getKafkaTopic(), metricProperties.getKafkaServers(),metricProperties.getKafkaConfig());
    }

    @Bean
    public MetricPointer createMetricPointer(KafkaTransport transport) {
        return new MetricPointer(transport);
    }
}