package com.along101.microservice.starter.metric;

import com.codahale.metrics.Clock;
import com.along101.dropwizard.metrics.TaggedMetric;
import com.along101.dropwizard.metrics.TaggedMetricRegistry;
import com.along101.dropwizard.metrics.reporter.KairosdbReporter;
import com.along101.dropwizard.metrics.transport.KafkaTransport;

public class MetricPointer {
    private KafkaTransport transport;

    public MetricPointer(KafkaTransport transport) {
        this.transport = transport;
    }

    public void point(long timeMillis, String name, TaggedMetric taggedMetric) {
        TaggedMetricRegistry registry = new TaggedMetricRegistry();
        registry.getOrRegisterTaggedMetric(name, taggedMetric);
        KairosdbReporter reporter = KairosdbReporter.forRegistry(registry)
                .withClock(new FixTimeClock(timeMillis))
                .build(this.transport);
        reporter.report();
    }

    static class FixTimeClock extends Clock.UserTimeClock {
        private long timeMillis;

        public FixTimeClock(long timeMillis) {
            this.timeMillis = timeMillis;
        }

        @Override
        public long getTime() {
            return this.timeMillis;
        }
    }
}
