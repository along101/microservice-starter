package com.along101.microservice.starter.metric;

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.along101.dropwizard.metrics.TaggedCounter;
import com.along101.dropwizard.metrics.TaggedHistogram;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhangyicong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TestMetricPointer {

    @Autowired
    MetricPointer MetricPointer;

    @Test
    public void testMetricPointer() throws InterruptedException {
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "t1");
        TaggedCounter taggedCounter = new TaggedCounter(tags);
        MetricPointer.point(System.currentTimeMillis() - 10 * 60 * 1000, "test.metricPointer.counter", taggedCounter);

        TaggedHistogram histogram = new TaggedHistogram(new ExponentiallyDecayingReservoir(), tags);
        for (int i = 0; i < 100; i++) {
            histogram.update(new Random().nextInt(100));
        }
        MetricPointer.point(System.currentTimeMillis() - 10 * 60 * 1000, "test.metricPointer.histogram", histogram);

        Thread.sleep(2000);
    }
}
