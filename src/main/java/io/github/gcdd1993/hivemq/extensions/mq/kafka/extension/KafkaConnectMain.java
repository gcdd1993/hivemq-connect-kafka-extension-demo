package io.github.gcdd1993.hivemq.extensions.mq.kafka.extension;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.MqttConnectKafkaConfig;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.MqttConnectKafkaConfigReader;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.events.ClientLifecycleEventListenerProviderImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.initializer.ClientInitializerImpl;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.KafkaMqProducerImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class KafkaConnectMain implements ExtensionMain {
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput,
                               @NotNull ExtensionStartOutput extensionStartOutput) {
        loadConfig(extensionStartInput, extensionStartOutput)
                .ifPresent(config -> {
                    var bootstrapServers = config.getBootstrapServers();
                    var mqProvider = new KafkaMqProducerImpl(bootstrapServers);
                    var initializer = new ClientInitializerImpl(mqProvider);
                    var clientLifecycleEventListenerProvider = new ClientLifecycleEventListenerProviderImpl(mqProvider);
                    Services.initializerRegistry().setClientInitializer(initializer);
                    Services.eventRegistry().setClientLifecycleEventListener(clientLifecycleEventListenerProvider);

                    log.info("{} started", extensionStartInput.getExtensionInformation().getName());
                });
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {
    }

    private Optional<MqttConnectKafkaConfig> loadConfig(@NotNull ExtensionStartInput extensionStartInput,
                                                        @NotNull ExtensionStartOutput extensionStartOutput) {
        try {
            final var configReader = new MqttConnectKafkaConfigReader(extensionStartInput.getExtensionInformation().getExtensionHomeFolder());
            return Optional.of(new MqttConnectKafkaConfig(configReader.readProperties()));
        } catch (final Exception e) {
            extensionStartOutput.preventExtensionStartup(extensionStartInput.getExtensionInformation().getName() + " cannot be started");
            log.error(
                    "{} could not be started. An exception was thrown!",
                    extensionStartInput.getExtensionInformation().getName(),
                    e
            );
            return Optional.empty();
        }
    }
}
