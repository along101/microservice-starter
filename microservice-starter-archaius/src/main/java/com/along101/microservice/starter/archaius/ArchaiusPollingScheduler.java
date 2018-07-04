package com.along101.microservice.starter.archaius;

import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import org.apache.commons.configuration.Configuration;

/**
 * Created by yinzuolong on 2017/11/25.
 */
public class ArchaiusPollingScheduler extends FixedDelayPollingScheduler {

    private PolledConfigurationSource source;
    private Configuration config;

    public ArchaiusPollingScheduler() {
        super();
    }

    public ArchaiusPollingScheduler(int initialDelayMillis, int delayMillis, boolean ignoreDeletesFromSource) {
        super(initialDelayMillis, delayMillis, ignoreDeletesFromSource);
    }

    @Override
    public void startPolling(PolledConfigurationSource source, Configuration config) {
        this.source = source;
        this.config = config;
        super.startPolling(source, config);
    }

    public void poll() {
        if (this.source != null && this.config != null) {
            Runnable runnable = this.getPollingRunnable(this.source, this.config);
            runnable.run();
        }
    }
}
