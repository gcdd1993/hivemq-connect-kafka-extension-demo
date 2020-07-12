package io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.IPayloadMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaochen on 2020/7/10.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PayloadMessage
        extends AbstractMessage
        implements IPayloadMessage {
    private Object payload;

    @Builder.Default
    private Map<String, String> variables = new HashMap<>();

    @Override
    public Object payload() {
        return payload;
    }
}
