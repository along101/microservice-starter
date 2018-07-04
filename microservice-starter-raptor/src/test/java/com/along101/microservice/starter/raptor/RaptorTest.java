package com.along101.microservice.starter.raptor;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.along101.microservice.starter.raptor.proto.HelloReply;
import com.along101.microservice.starter.raptor.proto.HelloRequest;
import com.along101.microservice.starter.raptor.proto.Simple;
import com.along101.raptor.spring.annotation.RaptorClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RaptorTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("server.port");
    }

    @RaptorClient
    private Simple simple;

    @Test
    public void testRpcCall() throws InterruptedException {
        HelloReply reply = simple.getSayHello(new HelloRequest("along101"));
        System.out.println("getSayHello: " + reply.getMessage());
        Thread.sleep(100);

        reply = simple.postSayHello(new HelloRequest("along101"));
        System.out.println("postSayHello: " + reply.getMessage());
        Thread.sleep(100);

        MetricRegistry metricRegistry = MetricContext.getMetricRegistry();
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.report();
    }
}
