package com.along101.microservice.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.along101")
public class CommonProperties {
    private static final Logger logger = LoggerFactory.getLogger(CommonProperties.class);
    private String appId;

    public CommonProperties() {
        logger.debug("constructing ..... ");
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
