package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal.TopicMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Created by gaochen on 2020/7/12.
 */
class TopicMapperTest {

    private TopicMapper topicMapper;

    @BeforeEach
    public void setup() {
        var extensionConfiguration = new ExtensionConfiguration();
        extensionConfiguration.setTopicMappings(
                new HashMap<>() {{
                    put("/ota/device/inform/${productKey}/${deviceName}", "/ota/device/inform");
                    put("/ota/device/upgrade/${productKey}/${deviceName}", "/ota/device/upgrade");
                    put("/sys/${productKey}/${deviceName}/thing/deviceinfo/update", "/sys/thing/deviceinfo/update");
                    put("/sys/${productKey}/${deviceName}/thing/deviceinfo/update_reply", "/sys/thing/deviceinfo/update_reply");
                    put("/sys/${product-name}/${device-name}/thing/event/property/post", "/sys/thing/event/property/post");
                }}
        );
        topicMapper = new TopicMapperImpl(extensionConfiguration);
    }

    @Test
    void getMappings() {
        System.out.println(topicMapper.getMappings());
    }

    @Test
    void convertMqttTopic2KafkaTopic() {
        var variables = new HashMap<String, String>();
        var kafkaTopic = topicMapper.convertMqttTopic2KafkaTopic("/sys/client-test0006/client-test0006/thing/event/property/post", variables);
        System.out.println("kafkaTopic: " + kafkaTopic);
        System.out.println("variables: " + variables);
    }

    @Test
    void convertKafkaTopic2MqttTopic() {
        var variables = new HashMap<String, String>();
        variables.put("productKey", "a1GI5IafAM3");
        variables.put("deviceName", "device01");

        var mqttTopic = topicMapper.convertKafkaTopic2MqttTopic(
                "/ota/device/inform",
                variables
        );
        System.out.println("mqtt topic: " + mqttTopic);
    }
}