package io.github.gcdd1993.hivemq.extensions.mq.kafka.interceptors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.PayloadMessage;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.TopicMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
@Singleton
public class PublishInboundInterceptorImpl implements PublishInboundInterceptor {

    //    private static final String TOPIC = "hivemq-internal-message-from-client";
    private final MqProducer mqProducer;
    private final TopicMapper topicMapper;

    @Inject
    public PublishInboundInterceptorImpl(MqProducer mqProducer,
                                         TopicMapper topicMapper) {
        this.mqProducer = mqProducer;
        this.topicMapper = topicMapper;
    }

    @Override
    public void onInboundPublish(@NotNull PublishInboundInput publishInboundInput,
                                 @NotNull PublishInboundOutput publishInboundOutput) {
        Mono
                .justOrEmpty(publishInboundInput.getPublishPacket().getPayload())
                .doOnNext(byteBuffer -> {
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

                    var kafkaTopic = topicMapper.convertMqttTopic2KafkaTopic(
                            topic,
                            message.getVariables()
                    );
                    mqProducer.publish(kafkaTopic, message);

                })
                .doOnError(error -> log.error("caught error {}", error.getMessage(), error))
                .subscribe()
        ;
    }
}
