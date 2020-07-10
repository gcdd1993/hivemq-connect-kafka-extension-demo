package io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.IPayloadMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Override
    public Object payload() {
        return payload;
    }
}
