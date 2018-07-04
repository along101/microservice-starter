package com.along101.microservice.starter.cat.aspect;

import com.along101.microservice.starter.cat.annotation.CatTransaction;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by zhangyicong on 2017/4/25.
 */
@Aspect
public class CatTransactionAspect {

    private Gson gson = new Gson();
    private Type parameterType = new TypeToken<Object[]>() {}.getType();
    private Type returnValueType = new TypeToken<Object>() {}.getType();

    @Pointcut("@annotation(com.along101.microservice.starter.cat.annotation.CatTransaction)")
    public void catMethod() {
    }

    @Around("catMethod()")
    public Object catWrap(ProceedingJoinPoint pjp) throws Throwable {
        String type = "unknown";
        String name = "unknown";
        String parameters = null;
        boolean returnValue = false;

        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();

            CatTransaction catTransaction = method.getAnnotation(CatTransaction.class);

            type = catTransaction.type();
            name = catTransaction.name();

            if (StringUtils.isEmpty(type)) {
                type = pjp.getSignature().getDeclaringTypeName();
            }

            if (StringUtils.isEmpty(name)) {
                name = pjp.getSignature().getName();
            }

            if (catTransaction.parameters()) {
                Object[] args = pjp.getArgs();
                parameters = gson.toJson(args, parameterType);
            }

            returnValue = catTransaction.returnValue();
        } catch (Exception ex) {

        }

        Transaction transaction = Cat.newTransaction(type, name);
        Object ret = null;
        try {
            if (parameters != null) {
                Cat.logEvent("parameters", parameters);
            }

            ret = pjp.proceed();

            if (returnValue) {
                Cat.logEvent("returnValues", gson.toJson(ret, returnValueType));
            }

            transaction.setStatus(Message.SUCCESS);
        } catch (Exception ex) {
            Cat.logError(ex);
            transaction.setStatus(ex);
            throw ex;
        } finally {
            transaction.complete();
        }
        return ret;
    }
}
