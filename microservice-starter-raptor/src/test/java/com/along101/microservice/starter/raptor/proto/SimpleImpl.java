package com.along101.microservice.starter.raptor.proto;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinzuolong
 */
@RestController
public class SimpleImpl implements Simple {

    @Override
    public HelloReply getSayHello(HelloRequest request) {
        String hello = "Hello " + request.getName();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HelloReply(hello);
    }

    @Override
    public HelloReply postSayHello(HelloRequest request) {
        String hello = "Hello " + request.getName();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HelloReply(hello);
    }
}
