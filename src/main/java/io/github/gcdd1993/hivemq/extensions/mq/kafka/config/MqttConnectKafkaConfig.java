package io.github.gcdd1993.hivemq.extensions.mq.kafka.config;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Created by gaochen on 2020/7/10.
 */
@Data
@Builder
@RequiredArgsConstructor
public class MqttConnectKafkaConfig {

    @NotNull
    private final Properties properties;

    @NotNull
    public String getBootstrapServers() {
        return properties.getProperty("bootstrapServers");
    }
}
