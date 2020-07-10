package io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.IMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AbstractMessage implements IMessage {

    private String topic;
    private String clientId;
    private String hostAddress;
    private long timestamp;

    @Override
    public Optional<String> topic() {
        return Optional.ofNullable(topic);
    }

    @Override
    public String clientId() {
        return clientId;
    }

    @Override
    public Optional<String> hostAddress() {
        return Optional.ofNullable(hostAddress);
    }

    @Override
    public Long timestamp() {
        return timestamp;
    }
}
