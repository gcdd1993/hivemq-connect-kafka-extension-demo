package io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.IDeviceStatusMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * Created by gaochen on 2020/7/10.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusMessage
        extends AbstractMessage
        implements IDeviceStatusMessage {

    private DeviceStatus status;
    private DisconnectReason disconnectReason;

    @Override
    public DeviceStatus status() {
        return status;
    }

    @Override
    public Optional<DisconnectReason> disconnectReason() {
        return Optional.ofNullable(disconnectReason);
    }
}
