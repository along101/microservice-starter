package com.along101.microservice.starter.raptor.client;

import com.along101.microservice.starter.raptor.MetricContext;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.along101.microservice.starter.metric.TagName;
import com.along101.microservice.starter.raptor.MetricContext;
import com.along101.raptor.common.RaptorConstants;
import com.along101.raptor.common.RaptorInfo;
import com.along101.raptor.rpc.RaptorClientInterceptor;
import com.along101.raptor.rpc.RaptorContext;
import com.along101.raptor.rpc.RaptorRequest;
import com.along101.raptor.rpc.RaptorResponse;
import com.along101.raptor.spring.client.feign.RaptorFeignClient;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yinzuolong
 */
public class MetricsClientInterceptor implements RaptorClientInterceptor {
    public static final String REQUEST_START_TIME = "raptor-client-RequestStartTime";
    public static final String TIME_NAME = RaptorInfo.getInstance().getMetricPrefix() + ".client.time";
    public static final String COUNT_NAME = RaptorInfo.getInstance().getMetricPrefix() + ".client.count";

    protected MetricRegistry metricRegistry = MetricContext.getMetricRegistry();


    @Override
    public void preHandle(RaptorRequest request, RaptorResponse response) {
        RaptorContext.getContext().putAttribute(REQUEST_START_TIME, System.nanoTime());
    }

    @Override
    public void postHandle(RaptorRequest request, RaptorResponse response) throws Exception {

    }

    @Override
    public void afterCompletion(RaptorRequest request, RaptorResponse response) throws Exception {
        Map<String, String> tags = createTags(request, response);

        metricRegistry.counter(new TagName(COUNT_NAME, tags).toString()).inc();

        Long startTime = (Long) RaptorContext.getContext().getAttribute(REQUEST_START_TIME);
        if (startTime != null) {
            Timer timer = metricRegistry.timer(new TagName(TIME_NAME, tags).toString());
            timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        }
    }

    private Map<String, String> createTags(RaptorRequest request, RaptorResponse response) {
        Map<String, String> tags = new HashMap<>();
        tags.put("raptorVersion", RaptorInfo.getInstance().getVersion());
        tags.put("interface", request.getInterfaceName());
        tags.put("method", request.getMethodName());
        String serverHost = response.getAttachments().get(RaptorConstants.HEADER_HOST_SERVER);

        if (!StringUtils.isEmpty(serverHost)) {
            tags.put("serverHost", serverHost);
        }

        if (response.getException() != null) {
            tags.put("exception", response.getException().getClass().getSimpleName());
        }

        tags.put("httpUri", (String) RaptorContext.getContext().getAttribute(RaptorFeignClient.NAME_HTTP_URI));
        tags.put("httpMethod", (String) RaptorContext.getContext().getAttribute(RaptorFeignClient.NAME_HTTP_METHOD));
        tags.put("status", String.valueOf(response.getCode()));

        return tags;
    }
}
