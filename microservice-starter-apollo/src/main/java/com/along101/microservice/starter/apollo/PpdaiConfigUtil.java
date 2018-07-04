package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.util.ConfigUtil;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.unidal.lookup.annotation.Named;

@Named(type = ConfigUtil.class)
public class along101ConfigUtil extends ConfigUtil {
    public static final String APPID_KEY = "com.along101.appId";
    public static final String CONFIG_NAMESPACES = "apollo.nameSpaces";
    private String appId;
    private String domain;


    public along101ConfigUtil() {
        super();
        initConfig();
    }

    private void initConfig() {
        try {
            ConfigurableEnvironment env = ApolloPropertySourcesHandler.getEnvironment();
            if (env == null) {
                throw new RuntimeException("init apollo error, spring envrionment is not inited.");
            }
            this.appId = env.getProperty(APPID_KEY);
            if (StringUtils.isEmpty(getApolloEnv())) {
                throw new RuntimeException("'along101.appId' is empty , check config in spring envrionment.");
            }
            String domainKey = "apollo." + getApolloEnv().name().toLowerCase();
            this.domain = env.getProperty(domainKey);
            if (StringUtils.isEmpty(this.domain)) {
                throw new RuntimeException(String.format("'%s' is empty, check config in spring envrionment.", domainKey));
            }
        } catch (Exception e) {
            throw new RuntimeException("init apollo config error.", e);
        }
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

    @Override
    public String getMetaServerDomainName() {
        return this.domain;
    }
}
