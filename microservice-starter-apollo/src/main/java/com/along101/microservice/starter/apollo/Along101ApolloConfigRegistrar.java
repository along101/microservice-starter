package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.spring.util.BeanRegistrationUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

public class Along101ApolloConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(Enablealong101ApolloConfig.class.getName()));
        String[] namespaces = attributes.getStringArray("value");
        int order = attributes.getNumber("order");
        ApolloPropertySourcesHandler.addNamespaces(Lists.newArrayList(namespaces), order);
        ApolloPropertySourcesHandler.initializePropertySources(this.environment);

        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, PropertySourcesPlaceholderConfigurer.class.getName(),
                PropertySourcesPlaceholderConfigurer.class);

        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, ApolloAnnotationProcessor.class.getName(),
                ApolloAnnotationProcessor.class);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

}
