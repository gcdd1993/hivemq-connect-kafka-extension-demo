package io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors;

import com.google.inject.Inject;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.PayloadMessage;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
public class PublishInboundInterceptorImpl implements PublishInboundInterceptor {

    private static final String TOPIC = "hivemq-internal-message-from-client";
    private final MqProducer mqProducer;

    @Inject
    public PublishInboundInterceptorImpl(MqProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    @Override
    public void onInboundPublish(@NotNull PublishInboundInput publishInboundInput,
                                 @NotNull PublishInboundOutput publishInboundOutput) {
        publishInboundInput.getPublishPacket().getPayload()
                .ifPresent(byteBuffer -> {
                    var byteArray = new byte[byteBuffer.remaining()];
                    byteBuffer.get(byteArray);
                    var payload = new String(byteArray, StandardCharsets.UTF_8);

                    var clientInformation = publishInboundInput.getClientInformation();
                    var connectionInformation = publishInboundInput.getConnectionInformation();
                    var clientId = clientInformation.getClientId();
                    var hostAddress = connectionInformation.getInetAddress()
                            .map(InetAddress::getHostAddress);
                    var topic = publishInboundInput.getPublishPacket()
                            .getTopic();

                    var message = PayloadMessage.builder()
                            .clientId(clientId)
                            .hostAddress(hostAddress.orElse(null))
                            .topic(topic)
                            .payload(payload)
                            .timestamp(System.currentTimeMillis())
                            .build();
                    mqProducer.publish(TOPIC, message);
                });
    }
}
