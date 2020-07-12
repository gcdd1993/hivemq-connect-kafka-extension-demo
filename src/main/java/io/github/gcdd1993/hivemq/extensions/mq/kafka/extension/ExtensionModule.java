package io.github.gcdd1993.hivemq.extensions.mq.kafka.extension;

import com.google.inject.AbstractModule;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListenerProvider;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.subscribe.SubscribeInboundInterceptor;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.events.ClientLifecycleEventListenerImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.events.ClientLifecycleEventListenerProviderImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.initializer.ClientInitializerImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors.PublishInboundInterceptorImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors.SubscribeInboundInterceptorImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.KafkaMqProducerImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.TopicMapper;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal.TopicMapperImpl;
import lombok.RequiredArgsConstructor;

/**
 * Created by gaochen on 2020/7/11.
 */
@RequiredArgsConstructor
public class ExtensionModule extends AbstractModule {

    private final ExtensionConfiguration extensionConfiguration;

    @Override
    protected void configure() {
        bind(ExtensionConfiguration.class)
                .toInstance(extensionConfiguration);
        bind(ClientLifecycleEventListenerProvider.class)
                .to(ClientLifecycleEventListenerProviderImpl.class);
        bind(ClientLifecycleEventListener.class)
                .to(ClientLifecycleEventListenerImpl.class);
        bind(ClientInitializer.class)
                .to(ClientInitializerImpl.class);
        bind(PublishInboundInterceptor.class)
                .to(PublishInboundInterceptorImpl.class);
        bind(SubscribeInboundInterceptor.class)
                .to(SubscribeInboundInterceptorImpl.class);
        bind(MqProducer.class)
                .to(KafkaMqProducerImpl.class);
        bind(TopicMapper.class)
                .to(TopicMapperImpl.class);
    }
}
