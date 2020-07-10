package io.github.gcdd1993.hivemq.extensions.mq.kafka.initializer;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.client.ClientContext;
import com.hivemq.extension.sdk.api.client.parameter.InitializerInput;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors.PublishInboundInterceptorImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors.SubscribeInboundInterceptorImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import lombok.RequiredArgsConstructor;

/**
 * Created by gaochen on 2020/7/7.
 */
@RequiredArgsConstructor
public class ClientInitializerImpl implements ClientInitializer {

    private final MqProducer mqProducer;

    @Override
    public void initialize(@NotNull InitializerInput initializerInput,
                           @NotNull ClientContext clientContext) {
        clientContext
                .addSubscribeInboundInterceptor(new SubscribeInboundInterceptorImpl(mqProducer));
        clientContext
                .addPublishInboundInterceptor(new PublishInboundInterceptorImpl(mqProducer));
    }
}
