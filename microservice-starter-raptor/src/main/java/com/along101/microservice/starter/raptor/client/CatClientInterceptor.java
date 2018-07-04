package com.along101.microservice.starter.raptor.client;

import com.along101.microservice.starter.raptor.PropertyContext;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Transaction;
import com.along101.microservice.starter.raptor.PropertyContext;
import com.along101.raptor.common.RaptorConstants;
import com.along101.raptor.rpc.RaptorClientInterceptor;
import com.along101.raptor.rpc.RaptorContext;
import com.along101.raptor.rpc.RaptorRequest;
import com.along101.raptor.rpc.RaptorResponse;
import com.along101.raptor.utils.NetUtils;

/**
 * @author yinzuolong
 */
public class CatClientInterceptor implements RaptorClientInterceptor {
    public static final String CAT_ATTRIBUTE = "raptor-cat-client-transaction";
    public static final String CLIENT_APP = "x-raptor-app-name";
    public static final String TYPE_PREFIX = "Call.";

    @Override
    public void preHandle(RaptorRequest request, RaptorResponse response) {
        catTrace(request);

        String type = TYPE_PREFIX + RaptorConstants.RAPTOR + "." + RaptorConstants.NODE_TYPE_CLIENT;
        String name = request.getInterfaceName() + "#" + request.getMethodName();

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

    private void catTrace(RaptorRequest request) {
        Cat.logEvent("client.ip", NetUtils.getLocalIp());
        Cat.logEvent("client.app", Cat.getManager().getDomain());

        PropertyContext context = new PropertyContext();
        Cat.logRemoteCallClient(context);

        request.setAttachment(CatConstants.HTTP_HEADER_ROOT_MESSAGE_ID, context.getProperty(Cat.Context.ROOT));
        request.setAttachment(CatConstants.HTTP_HEADER_PARENT_MESSAGE_ID, context.getProperty(Cat.Context.PARENT));
        request.setAttachment(CatConstants.HTTP_HEADER_CHILD_MESSAGE_ID, context.getProperty(Cat.Context.CHILD));
        request.setAttachment(CLIENT_APP, Cat.getManager().getDomain());
    }
}
