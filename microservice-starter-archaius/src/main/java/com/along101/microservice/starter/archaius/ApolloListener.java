package com.along101.microservice.starter.archaius;

import com.ctrip.apollo.model.ConfigChangeEvent;
import com.ctrip.apollo.spring.annotation.ApolloConfigChangeListener;

/**
 * Created by yinzuolong on 2017/11/25.
 */
public class ApolloListener {

    private ArchaiusPollingScheduler scheduler;

    public ApolloListener(ArchaiusPollingScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        this.scheduler.poll();
    }
}
