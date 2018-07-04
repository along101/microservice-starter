package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.Config;
import com.ctrip.apollo.spring.config.ConfigPropertySource;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Created by yinzuolong on 2017/11/16.
 */
public class ApolloPropertySourcesHandler {
    private static final String APOLLO_PROPERTY_SOURCE_NAME = "ApolloPropertySources";
    private static final Multimap<Integer, String> NAMESPACE_NAMES = LinkedHashMultimap.create();

    private static ConfigurableEnvironment ENVIRONMENT;

    public static boolean addNamespaces(Collection<String> namespaces, int order) {
        return NAMESPACE_NAMES.putAll(order, namespaces);
    }

    public static ConfigurableEnvironment getEnvironment() {
        return ENVIRONMENT;
    }

    public static void initializePropertySources(ConfigurableEnvironment environment) {
        ENVIRONMENT = environment;
        CompositePropertySource composite = (CompositePropertySource) environment.getPropertySources().get(APOLLO_PROPERTY_SOURCE_NAME);
        if (composite == null) {
            composite = new CompositePropertySource(APOLLO_PROPERTY_SOURCE_NAME);
            environment.getPropertySources().addFirst(composite);
        }
        loadConfigNamespaces(environment);
        //sort by order asc
        ImmutableSortedSet<Integer> orders = ImmutableSortedSet.copyOf(NAMESPACE_NAMES.keySet());
        composite.getPropertySources().clear();
        for (Integer order : orders) {
            for (String namespace : NAMESPACE_NAMES.get(order)) {
                Config config = along101ConfigService.getConfig(namespace);
                composite.addPropertySource(new ConfigPropertySource(namespace, config));
            }
        }
    }

    private static void loadConfigNamespaces(ConfigurableEnvironment environment) {
        try {
            String configNameSpaces = environment.getProperty(along101ConfigUtil.CONFIG_NAMESPACES);
            if (StringUtils.isEmpty(configNameSpaces)) {
                return;
            }
            String[] namespaces = configNameSpaces.split(",");
            NAMESPACE_NAMES.putAll(Ordered.LOWEST_PRECEDENCE, Lists.newArrayList(namespaces));
        } catch (Exception e) {
            throw new RuntimeException("load apollo config namespace error.", e);
        }
    }
}
