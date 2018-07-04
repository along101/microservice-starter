package com.along101.microservice.starter.log;

import com.along101.logclient.logback.KafkaAppender;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/**
 * Created by yinzuolong on 2017/4/20.
 */
public class LogAutoConfiguration extends LoggingApplicationListener {

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return super.supportsEventType(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return super.supportsSourceType(sourceType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            onApplicationEnvironmentPreparedEvent(
                    (ApplicationEnvironmentPreparedEvent) event);
        }
    }

    private void onApplicationEnvironmentPreparedEvent(
            ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String appId = environment.getProperty("com.along101.appId");
        if (StringUtils.isEmpty(appId)) {
            throw new RuntimeException("appId is empty");
        }
        PropertySourcesBinder propertySourcesBinder = new PropertySourcesBinder(environment);
        KafkaAppenderProperties kafkaAppenderProperties = new KafkaAppenderProperties();
        propertySourcesBinder.bindTo("com.along101.logclient", kafkaAppenderProperties);

        for (KafkaAppender appender : KafkaAppender.getKafkaAppenders()) {
            if (!appender.isStarted()) {
                resetKafkaAppenderProperties(appender, kafkaAppenderProperties);
                appender.doStart();
            }
        }
    }

    private void resetKafkaAppenderProperties(KafkaAppender appender, KafkaAppenderProperties kafkaAppenderProperties) {
        String name = appender.getName();
        KafkaAppenderProperties.Appender properties = kafkaAppenderProperties.getAppender().get(name);
        if (properties != null) {
            appender.setAppId(properties.getAppId());
            appender.setBootstrapServers(properties.getBootstrapServers());
            appender.setTopic(properties.getTopic());
            appender.setReportTopic(properties.getReportTopic());
            appender.putProducerConfigs(properties.getProducerConfig());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
