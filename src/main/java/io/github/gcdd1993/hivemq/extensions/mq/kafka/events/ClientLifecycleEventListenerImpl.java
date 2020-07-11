package io.github.gcdd1993.hivemq.extensions.mq.kafka.events;

import com.google.inject.Inject;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener;
import com.hivemq.extension.sdk.api.events.client.parameters.AuthenticationSuccessfulInput;
import com.hivemq.extension.sdk.api.events.client.parameters.ConnectionStartInput;
import com.hivemq.extension.sdk.api.events.client.parameters.DisconnectEventInput;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.DeviceStatus;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.DeviceStatusMessage;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.message.internal.DisconnectReason;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * Created by gaochen on 2020/7/8.
 */
@Slf4j
public class ClientLifecycleEventListenerImpl implements ClientLifecycleEventListener {

    private static final String TOPIC = "hivemq-internal-device-status";
    private final MqProducer mqProducer;

    @Inject
    public ClientLifecycleEventListenerImpl(MqProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    @Override
    public void onMqttConnectionStart(@NotNull ConnectionStartInput connectionStartInput) {
    }

    @Override
    public void onAuthenticationSuccessful(@NotNull AuthenticationSuccessfulInput authenticationSuccessfulInput) {
        var clientInformation = authenticationSuccessfulInput.getClientInformation();
        var connectionInformation = authenticationSuccessfulInput.getConnectionInformation();
        var clientId = clientInformation.getClientId();
        var hostAddress = connectionInformation.getInetAddress()
                .map(InetAddress::getHostAddress);
        log.info(
                "client with id {}, ip {} connect success",
                clientId,
                hostAddress
        );

        var message = DeviceStatusMessage.builder()
                .clientId(clientId)
                .hostAddress(hostAddress.orElse(null))
                .status(DeviceStatus.CONNECT)
                .timestamp(System.currentTimeMillis())
                .build();

        mqProducer
                .publish(TOPIC, message);
    }

    @Override
    public void onDisconnect(@NotNull DisconnectEventInput disconnectEventInput) {
        var clientInformation = disconnectEventInput.getClientInformation();
        var connectionInformation = disconnectEventInput.getConnectionInformation();
        var clientId = clientInformation.getClientId();
        var hostAddress = connectionInformation.getInetAddress()
                .map(InetAddress::getHostAddress);
        var reasonCode = disconnectEventInput.getReasonCode();
        var reasonString = disconnectEventInput.getReasonString();

        log.info(
                "client with id {}, ip {} disconnect, reasonCode {}, reasonString {}",
                clientId,
                hostAddress,
                reasonCode,
                reasonString
        );

        var message = DeviceStatusMessage.builder()
                .clientId(clientId)
                .hostAddress(hostAddress.orElse(null))
                .status(DeviceStatus.DISCONNECT)
                .timestamp(System.currentTimeMillis())
                .disconnectReason(
                        DisconnectReason.builder()
                                .reasonCode(reasonCode.orElse(null))
                                .reasonString(reasonString.orElse(null))
                                .build()
                )
                .build();

        mqProducer
                .publish(TOPIC, message);
    }
}
