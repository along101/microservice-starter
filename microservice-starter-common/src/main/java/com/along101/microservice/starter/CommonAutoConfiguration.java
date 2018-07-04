package com.along101.microservice.starter;

import com.along101.microservice.error.CommonErrorMessage;
import com.along101.microservice.error.CommonErrorCode;
import com.along101.microservice.error.CommonErrorMessage;
import com.along101.microservice.util.exception.NoStrackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(CommonProperties.class)
public class CommonAutoConfiguration {
    Logger logger = LoggerFactory.getLogger(CommonAutoConfiguration.class);
    private CommonProperties commonProperties;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public CommonAutoConfiguration(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
        logger.debug("constructing ....");
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(this.commonProperties.getAppId())) {
            logger.error("appid is null");
            throw new NoStrackException(CommonErrorMessage.APPID_NOT_FOUND, CommonErrorCode.APPID_NOT_FOUND,
                    CommonAutoConfiguration.class.getCanonicalName(), "newCommonProperties");
        }
        if (StringUtils.isEmpty(applicationContext.getId())) {
            throw new NoStrackException(CommonErrorMessage.SERVICE_NAME_NOT_FOUND,
                    CommonErrorCode.SERVICE_NAME_NOT_FOUND, CommonAutoConfiguration.class.getCanonicalName(),
                    "newCommonProperties");
        }
    }

}
