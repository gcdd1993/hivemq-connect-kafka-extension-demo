package io.github.gcdd1993.hivemq.extensions.mq.kafka.config;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by gaochen on 2020/7/10.
 */
@Slf4j
public class MqttConnectKafkaConfigReader {

    @NotNull
    private static final String PROPERTIES_FILE_NAME = "mqttConnectKafka.properties";

    @NotNull
    private final Properties properties;

    @NotNull
    private final File extensionHomeFolder;

    public MqttConnectKafkaConfigReader(final @NotNull File extensionHomeFolder) {
        this.extensionHomeFolder = extensionHomeFolder;
        this.properties = new Properties();
    }

    @NotNull
    public Properties readProperties() {
        final File propertiesFile = new File(extensionHomeFolder, PROPERTIES_FILE_NAME);

        log.debug("HiveMQ MQTT Connect Kafka Extension: Will try to read config properties from {}", PROPERTIES_FILE_NAME);

        if (!propertiesFile.canRead()) {
            log.info("HiveMQ MQTT Connect Kafka Extension: Cannot read properties file {}", propertiesFile.getAbsolutePath());
        } else {
            try (final InputStream is = new FileInputStream(propertiesFile)) {
                properties.load(is);
            } catch (final Exception e) {
                log.warn("HiveMQ MQTT Connect Kafka Extension: Could not load properties file, reason {}", e.getMessage());
            }
        }
        log.info("HiveMQ MQTT Connect Kafka Extension: Properties initialized to: {}", properties);
        return properties;
    }

}
