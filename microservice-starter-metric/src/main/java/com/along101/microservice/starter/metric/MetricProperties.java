package com.along101.microservice.starter.metric;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * Created by zhangyicong on 2017/4/21.
 */
@ConfigurationProperties(prefix = MetricProperties.METRIC_PREFIX)
public class MetricProperties {

    public static final String METRIC_PREFIX = "metric";

    private String kafkaTopic;
    private String kafkaServers;
    private String prefix = "";
    private int period = 60;
    private Properties kafkaConfig = new Properties();

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getKafkaServers() {
        return kafkaServers;
    }

    public void setKafkaServers(String kafkaServers) {
        this.kafkaServers = kafkaServers;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Properties getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(Properties kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }
}
