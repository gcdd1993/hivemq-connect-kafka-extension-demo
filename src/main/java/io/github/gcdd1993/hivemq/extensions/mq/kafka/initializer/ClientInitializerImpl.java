package io.github.gcdd1993.hivemq.extensions.mq.kafka.initializer;

import com.google.inject.Inject;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.client.ClientContext;
import com.hivemq.extension.sdk.api.client.parameter.InitializerInput;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.subscribe.SubscribeInboundInterceptor;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;

/**
 * Created by gaochen on 2020/7/7.
 */
public class ClientInitializerImpl implements ClientInitializer {

    private final SubscribeInboundInterceptor subscribeInboundInterceptor;
    private final PublishInboundInterceptor publishInboundInterceptor;

    @Inject
    public ClientInitializerImpl(SubscribeInboundInterceptor subscribeInboundInterceptor,
                                 PublishInboundInterceptor publishInboundInterceptor) {
        this.subscribeInboundInterceptor = subscribeInboundInterceptor;
        this.publishInboundInterceptor = publishInboundInterceptor;
    }

    @Override
    public void initialize(@NotNull InitializerInput initializerInput,
                           @NotNull ClientContext clientContext) {
        clientContext
                .addSubscribeInboundInterceptor(subscribeInboundInterceptor);
        clientContext
                .addPublishInboundInterceptor(publishInboundInterceptor);
    }
}
