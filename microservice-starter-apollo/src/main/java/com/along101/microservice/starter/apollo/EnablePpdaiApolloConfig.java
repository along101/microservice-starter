package com.along101.microservice.starter.apollo;

import com.ctrip.apollo.core.ConfigConsts;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(along101ApolloConfigRegistrar.class)
public @interface Enablealong101ApolloConfig {
    /**
     * Apollo namespaces to inject configuration into Spring Property Sources.
     */
    String[] value() default {ConfigConsts.NAMESPACE_APPLICATION};

    /**
     * The order of the apollo config, default is {@link Ordered#LOWEST_PRECEDENCE}, which is Integer.MAX_VALUE.
     * If there are properties with the same name in different apollo configs, the apollo config with smaller order wins.
     *
     * @return
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
