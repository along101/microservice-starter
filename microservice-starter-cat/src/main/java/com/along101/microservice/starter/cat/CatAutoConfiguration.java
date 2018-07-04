package com.along101.microservice.starter.cat;

import com.dianping.cat.Cat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.along101.cat.servlet.CatServletFilter;
import com.along101.microservice.starter.cat.aspect.CatTransactionAspect;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zhangyicong on 2017/4/19.
 */
@Configuration
@EnableConfigurationProperties(CatProperties.class)
@EnableAspectJAutoProxy
public class CatAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CatAutoConfiguration.class);
    @Autowired
    private CatProperties catProperties;

    @Value("${spring.application.name}")
    private String domain;

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean catFilterRegistration(CatServletFilter catFilter) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(catFilter);
        registration.addUrlPatterns(catProperties.getFilterUrlPatterns().toArray(new String[0]));
        registration.addInitParameter("CatHttpModuleUrlPatterns", StringUtils.join(catProperties.getFilterUrlAggregatorPatterns().toArray(new String[0]), ","));
        registration.setName("catFilter");
        registration.setOrder(catProperties.getFilterOrder());
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public CatServletFilter catFilter() {
        return new CatServletFilter();
    }

    @Bean
    public CatTransactionAspect catTransactionAspect() {
        return new CatTransactionAspect();
    }

    @PostConstruct
    public void applyConfigurationProperties() {
        System.setProperty("cat.domain", domain);
        System.setProperty("cat.servers",
                new Gson().toJson(catProperties.getServers(), new TypeToken<List<CatProperties.CatServer>>() {
                }.getType()));

        try {
            Cat.destroy();
        } catch (Exception ex) {
        }
        try {
            Cat.getManager();
        } catch (Exception ex) {
            logger.error("init cat error.", ex);
        }
    }
}
