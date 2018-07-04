package com.along101.microservice.starter.cat;

import com.along101.microservice.starter.cat.annotation.CatTransaction;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyicong on 2017/4/26.
 */
@Component
public class CatTransactionTestClass {

    @CatTransaction(type = "CatAutoConfigurationTest", name = "testCatTransactionAnnotation")
    public void testCatMethodAnnotation() throws InterruptedException {
        Thread.sleep(500);
    }

    @CatTransaction
    public void testCatMethodAnnotation1() throws InterruptedException {
        Thread.sleep(500);
    }

    @CatTransaction(type = "CatAutoConfigurationTest", name = "testCatTransactionAnnotation2", returnValue = true)
    public int testCatMethodAnnotation2(String a, int b) throws InterruptedException {
        return 1;
    }
}
