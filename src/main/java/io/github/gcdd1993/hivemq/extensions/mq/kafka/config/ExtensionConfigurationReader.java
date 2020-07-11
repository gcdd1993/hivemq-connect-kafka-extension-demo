package io.github.gcdd1993.hivemq.extensions.mq.kafka.config;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by gaochen on 2020/7/10.
 */
@Slf4j
@RequiredArgsConstructor
public class ExtensionConfigurationReader {

    @NotNull
    private static final String CONF_FILE_NAME = "mqttConnectKafka.yaml";

    @NotNull
    private final File extensionHomeFolder;

    @NotNull
    public Optional<ExtensionConfiguration> readConfigFromYamlFile() {
        final var yamlFile = new File(extensionHomeFolder, CONF_FILE_NAME);
        ExtensionConfiguration configuration = null;

        log.debug("HiveMQ MQTT Connect Kafka Extension: Will try to read config from {}", CONF_FILE_NAME);

        if (!yamlFile.canRead()) {
            log.info("HiveMQ MQTT Connect Kafka Extension: Cannot read config file {}", yamlFile.getAbsolutePath());
        } else {
            try (final InputStream is = new FileInputStream(yamlFile)) {
                var yaml = new Yaml(new Constructor(ExtensionConfiguration.class));
                configuration = yaml.load(is);
            } catch (final Exception e) {
                log.warn("HiveMQ MQTT Connect Kafka Extension: Could not load yaml file, reason {}", e.getMessage());
            }
        }
        log.info("HiveMQ MQTT Connect Kafka Extension: Properties initialized to: {}", configuration);
        return Optional.ofNullable(configuration);
    }

}
