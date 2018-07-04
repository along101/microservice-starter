package com.along101.microservice.starter.archaius;

import com.netflix.config.*;
import com.along101.microservice.starter.apollo.along101ConfigService;
import lombok.Setter;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Created by yinzuolong on 2017/9/24.
 */
@Configuration
@ConfigurationProperties("archaius")
public class ArchaiusConfig implements EnvironmentAware, InitializingBean {
    private static final String CONFIG_NAME = "springEnv";
    @Setter
    private int pollDelayMillis = 60000;
    @Setter
    private int poolInitialDelayMillis = 30000;
    @Setter
    private boolean pollIgnoreDeletesFromSource = false;

    private Environment environment;

    private ArchaiusPollingScheduler scheduler;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringEnvConfigurationSource springEnvConfigurationSource = new SpringEnvConfigurationSource((ConfigurableEnvironment) this.environment);
        scheduler = new ArchaiusPollingScheduler(poolInitialDelayMillis, pollDelayMillis, pollIgnoreDeletesFromSource);
        ConcurrentMapConfiguration configuration = new ConcurrentCompositeConfiguration();
        scheduler.startPolling(springEnvConfigurationSource, configuration);

        if (ConfigurationManager.isConfigurationInstalled()) {
            AbstractConfiguration installedConfiguration = ConfigurationManager.getConfigInstance();
            if (installedConfiguration instanceof ConcurrentCompositeConfiguration) {
                ConcurrentCompositeConfiguration configInstance = (ConcurrentCompositeConfiguration) installedConfiguration;
                if (configInstance.getConfiguration(CONFIG_NAME) == null)
                    configInstance.addConfigurationAtFront(configuration, CONFIG_NAME);
            }
        } else {
            ConcurrentCompositeConfiguration concurrentCompositeConfiguration = new ConcurrentCompositeConfiguration();
            concurrentCompositeConfiguration.addConfiguration(configuration, CONFIG_NAME);
            ConfigurationManager.install(concurrentCompositeConfiguration);
        }
    }

    @ConditionalOnClass(along101ConfigService.class)
    @Bean
    public ApolloListener createApolloListener() {
        return new ApolloListener(this.scheduler);
    }
}
