package io.github.gcdd1993.hivemq.extensions.mq.kafka.producer;

import com.hivemq.extension.sdk.api.annotations.NotNull;

public interface MqProducer {

    void publish(@NotNull String topic,
                 @NotNull Object message);
}
