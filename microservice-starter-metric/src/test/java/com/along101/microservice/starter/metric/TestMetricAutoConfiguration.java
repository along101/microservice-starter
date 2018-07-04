package com.along101.microservice.starter.metric;

import com.codahale.metrics.MetricRegistry;
import com.along101.dropwizard.metrics.TaggedMetricRegistry;
import com.along101.dropwizard.metrics.TaggedTimer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyicong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TestMetricAutoConfiguration {

    @Autowired
    MetricRegistry metricRegistry;

    @Test
    public void testMetric() throws InterruptedException {
        final Map<String, String> tags = new HashMap<String, String>();
        tags.put("a", "b");

        TaggedTimer timer = ((TaggedMetricRegistry) metricRegistry).taggedTimer("microservicetest1.testMetric.time", tags);
        TaggedTimer.Context context = timer.time();
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        } finally {
            context.stop();
        }

        Thread.sleep(90000);
    }
}
