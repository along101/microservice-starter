package com.along101.microservice.starter.raptor;

import com.codahale.metrics.MetricRegistry;

public class MetricContext {

    private final static MetricRegistry METRIC_REGISTRY = new MetricRegistry();

    public static MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }
}
