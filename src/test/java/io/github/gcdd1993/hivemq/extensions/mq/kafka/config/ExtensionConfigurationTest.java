package io.github.gcdd1993.hivemq.extensions.mq.kafka.config;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by gaochen on 2020/7/11.
 */
public class ExtensionConfigurationTest {

    @Test
    public void loadConfigTest() throws URISyntaxException {
        var extensionConfigurationReader = new ExtensionConfigurationReader(new File("D:\\WorkSpace\\Personal\\Iot\\hivemq-extensions\\hivemq-connect-kafka-extension\\src\\test\\resources"));
        var extensionConfiguration = extensionConfigurationReader.readConfigFromYamlFile();

        extensionConfiguration.ifPresent(
                config -> {
                    System.out.println(config.producerConfig());
                }
        );
    }
}
