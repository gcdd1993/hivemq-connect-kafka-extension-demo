package io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors;

import com.google.inject.Inject;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.subscribe.SubscribeInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.subscribe.parameter.SubscribeInboundInput;
import com.hivemq.extension.sdk.api.interceptor.subscribe.parameter.SubscribeInboundOutput;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscribeInboundInterceptorImpl implements SubscribeInboundInterceptor {

    private static final String TOPIC = "hivemq-extension-subscription";
    private final MqProducer mqProducer;

    @Inject
    public SubscribeInboundInterceptorImpl(MqProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    @Override
    public void onInboundSubscribe(@NotNull SubscribeInboundInput subscribeInboundInput,
                                   @NotNull SubscribeInboundOutput subscribeInboundOutput) {
//        Mono.just(subscribeInboundInput.getSubscribePacket())
//                .flatMap()
//
//
//        var clientInformation = subscribeInboundInput.getClientInformation();
//        var connectionInformation = subscribeInboundInput.getConnectionInformation();
//        subscribeInboundInput.getSubscribePacket().getSubscriptionIdentifier()
//        final var clientId = subscribeInboundInput.getClientInformation().getClientId();
//        final var subscribePacket = subscribeInboundInput.getSubscribePacket();
//        final var subscriptionIdentifier = subscribePacket.getSubscriptionIdentifier().orElse(null);
//
//        final var str = MessageFormat.format(
//                "Received SUBSCRIBE from client '{0}', Subscription Identifier: '{1}'",
//                clientId,
//                subscriptionIdentifier
//        );
//        log.info(str);
//        final var message = new StringMessage(TOPIC, str);
//
//        mqProducer.publish(TOPIC, message);
    }
}
