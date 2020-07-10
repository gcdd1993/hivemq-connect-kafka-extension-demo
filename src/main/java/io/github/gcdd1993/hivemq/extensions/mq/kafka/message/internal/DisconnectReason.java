package io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal;

import com.hivemq.extension.sdk.api.packets.general.DisconnectedReasonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by gaochen on 2020/7/10.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisconnectReason implements Serializable {
    private DisconnectedReasonCode reasonCode;
    private String reasonString;
}
