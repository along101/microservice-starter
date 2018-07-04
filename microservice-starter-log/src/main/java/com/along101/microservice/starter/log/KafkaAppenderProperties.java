package com.along101.microservice.starter.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/6/9.
 */
@ConfigurationProperties(prefix = "com.along101.logclient")
public class KafkaAppenderProperties {

    private Map<String, Appender> appender = new HashMap<>();

    public Map<String, Appender> getAppender() {
        return appender;
    }

    public void setAppender(Map<String, Appender> appender) {
        this.appender = appender;
    }

    public static class Appender {
        private String topic;
        private String reportTopic;
        private String appId;
        private String bootstrapServers;
        private Map<String, String> producerConfig = new HashMap<String, String>();

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getReportTopic() {
            return reportTopic;
        }

        public void setReportTopic(String reportTopic) {
            this.reportTopic = reportTopic;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public Map<String, String> getProducerConfig() {
            return producerConfig;
        }

        public void setProducerConfig(Map<String, String> producerConfig) {
            this.producerConfig = producerConfig;
        }
    }
}
