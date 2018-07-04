package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.core.enums.Env;
import com.ctrip.apollo.core.enums.EnvUtils;
import com.ctrip.foundation.Foundation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/**
 * Created by yinzuolong on 2017/11/16.
 */
public class ApolloEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    //配置文件加载之后
    private int order = ConfigFileApplicationListener.DEFAULT_ORDER + 1;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String appId = environment.getProperty("com.along101.appId");
        if (StringUtils.isEmpty(appId)) {
            System.out.println("can not find appId property in spring environment.");
            return;
        }
        Env apolloEnv = EnvUtils.transformEnv(Foundation.server().getEnvType());
        environment.addActiveProfile(apolloEnv.name().toLowerCase());

        ApolloPropertySourcesHandler.initializePropertySources(environment);
    }
}
