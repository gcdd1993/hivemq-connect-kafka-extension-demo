package io.github.gcdd1993.hivemq.extensions.mq.kafka.message;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.DeviceStatus;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.DisconnectReason;

import java.util.Optional;

public interface IDeviceStatusMessage extends IMessage {

    DeviceStatus status();

    /**
     * 离线原因
     */
    Optional<DisconnectReason> disconnectReason();

}
