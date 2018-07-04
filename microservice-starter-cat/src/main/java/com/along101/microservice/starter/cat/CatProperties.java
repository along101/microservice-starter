package com.along101.microservice.starter.cat;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyicong on 2017/4/19.
 */
@ConfigurationProperties(prefix= CatProperties.CAT_PREFIX)
public class CatProperties {
    public static final String CAT_PREFIX = "along101.cat";

    private List<CatServer> servers = new ArrayList<CatServer>();
    private List<String> filterUrlPatterns = new ArrayList<>();
    private List<String> filterUrlAggregatorPatterns = new ArrayList<>();
    private int filterOrder = 1;

    public List<CatServer> getServers() {
        return servers;
    }

    public void setServers(List<CatServer> servers) {
        this.servers = servers;
    }

    public int getFilterOrder() {
        return filterOrder;
    }

    public void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public List<String> getFilterUrlPatterns() {
        return filterUrlPatterns;
    }

    public void setFilterUrlPatterns(List<String> filterUrlPatterns) {
        this.filterUrlPatterns = filterUrlPatterns;
    }

    public List<String> getFilterUrlAggregatorPatterns() {
        return filterUrlAggregatorPatterns;
    }

    public void setFilterUrlAggregatorPatterns(List<String> filterUrlAggregatorPatterns) {
        this.filterUrlAggregatorPatterns = filterUrlAggregatorPatterns;
    }

    public static class CatServer {
        private String ip;
        private Integer port;
        private Integer httpPort;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer getHttpPort() {
            return httpPort;
        }

        public void setHttpPort(Integer httpPort) {
            this.httpPort = httpPort;
        }
    }
}
