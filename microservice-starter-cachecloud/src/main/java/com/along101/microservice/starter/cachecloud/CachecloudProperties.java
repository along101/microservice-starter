package com.along101.microservice.starter.cachecloud;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by yinzuolong on 2017/9/20.
 */
@ConfigurationProperties(prefix = CachecloudProperties.PREFIX)
public class CachecloudProperties {

    public static final String PREFIX = "cachecloud";

    private String appId;

    private String domainUrl;

    private String type = "cluster";

    private int maxIdle = 20;

    private int minIdle = 10;

    private int maxActive = 50;

    private int maxWait = 120000;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}
