package com.along101.microservice.starter.raptor.service;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import com.along101.microservice.starter.raptor.PropertyContext;
import com.along101.raptor.common.RaptorConstants;
import com.along101.raptor.rpc.RaptorContext;
import com.along101.raptor.rpc.RaptorRequest;
import com.along101.raptor.rpc.RaptorResponse;
import com.along101.raptor.rpc.RaptorServiceInterceptor;

/**
 * @author yinzuolong
 */
public class CatServiceInterceptor implements RaptorServiceInterceptor {

    public static final String APP_NAME = "x-raptor-app-name";
    public static final String CAT_ATTRIBUTE = "raptor-cat-service-transaction";
    public static final String TYPE_PREFIX = "Call.";


    @Override
    public void preHandle(RaptorRequest request, RaptorResponse response) throws Exception {
        catTrace(request);

        String type = TYPE_PREFIX + RaptorConstants.RAPTOR + "." + RaptorConstants.NODE_TYPE_SERVICE;
        String interfaceName = request.getInterfaceName();
        String method = request.getMethodName();
        String name = interfaceName + "#" + method;
        Transaction transaction = Cat.newTransaction(type, name);
        RaptorContext.getContext().putAttribute(CAT_ATTRIBUTE, transaction);
    }

    @Override
    public void postHandle(RaptorRequest request, RaptorResponse response) throws Exception {

    }

    @Override
    public void afterCompletion(RaptorRequest request, RaptorResponse response) throws Exception {
        Transaction transaction = (Transaction) RaptorContext.getContext().getAttribute(CAT_ATTRIBUTE);
        if (transaction != null) {
            if (response.getException() == null) {
                transaction.setStatus(Transaction.SUCCESS);
            } else {
                Cat.logError(response.getException());
                transaction.setStatus(response.getException());
            }
            transaction.complete();
        }
    }

    protected void catTrace(RaptorRequest request) {
        Cat.logEvent("service.app", Cat.getManager().getDomain());
        PropertyContext propertyContext = new PropertyContext();
        propertyContext.addProperty(Cat.Context.ROOT, request.getAttachments().get(CatConstants.HTTP_HEADER_ROOT_MESSAGE_ID.toLowerCase()));
        propertyContext.addProperty(Cat.Context.PARENT, request.getAttachments().get(CatConstants.HTTP_HEADER_PARENT_MESSAGE_ID.toLowerCase()));
        propertyContext.addProperty(Cat.Context.CHILD, request.getAttachments().get(CatConstants.HTTP_HEADER_CHILD_MESSAGE_ID.toLowerCase()));
        Cat.logRemoteCallServer(propertyContext);
        String clientApp = request.getAttachments().get(APP_NAME.toLowerCase());
        Cat.logEvent("client.app", clientApp == null ? "unknown" : clientApp);
        Cat.logEvent("requestId", String.valueOf(request.getAttachments().get(RaptorConstants.HEADER_REQUEST_ID.toLowerCase())));
    }

}
