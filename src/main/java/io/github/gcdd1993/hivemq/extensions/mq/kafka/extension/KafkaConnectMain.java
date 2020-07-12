package io.github.gcdd1993.hivemq.extensions.mq.kafka.extension;

import com.google.inject.Guice;
import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListenerProvider;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfigurationReader;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class KafkaConnectMain implements ExtensionMain {
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput,
                               @NotNull ExtensionStartOutput extensionStartOutput) {
        loadConfig(extensionStartInput, extensionStartOutput)
                .ifPresent(config -> {
                    var injector = Guice.createInjector(new ExtensionModule(config));

                    var clientInitializer = injector.getInstance(ClientInitializer.class);
                    var clientLifecycleEventListenerProvider = injector.getInstance(ClientLifecycleEventListenerProvider.class);
                    Services.initializerRegistry().setClientInitializer(clientInitializer);
                    Services.eventRegistry().setClientLifecycleEventListener(clientLifecycleEventListenerProvider);

                    log.info("{} started", extensionStartInput.getExtensionInformation().getName());
                });
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {
    }

    private Optional<ExtensionConfiguration> loadConfig(@NotNull ExtensionStartInput extensionStartInput,
                                                        @NotNull ExtensionStartOutput extensionStartOutput) {
        try {
            final var configReader = new ExtensionConfigurationReader(extensionStartInput.getExtensionInformation().getExtensionHomeFolder());
            return configReader.readConfigFromYamlFile();
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
