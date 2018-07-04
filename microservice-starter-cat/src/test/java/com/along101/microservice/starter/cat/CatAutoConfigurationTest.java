package com.along101.microservice.starter.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhangyicong on 2017/4/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class CatAutoConfigurationTest {

    @Autowired
    CatTransactionTestClass catTransactionTestClass;

    @Test
    public void testCatMethod() throws InterruptedException {

        int i = catTransactionTestClass.testCatMethodAnnotation2("zhangyicong", 10);

        Thread.sleep(30000);
    }

    @Test
    public void testCat() throws InterruptedException {
        Transaction transaction = Cat.newTransaction("Service", "testCat");

        try {
            Thread.sleep(500);
            transaction.setStatus(Transaction.SUCCESS);
        } catch (Exception ex) {
            Cat.logError(ex);
            transaction.setStatus(ex);
            throw ex;
        } finally {
            transaction.complete();
        }

        Thread.sleep(30000);
    }
}
