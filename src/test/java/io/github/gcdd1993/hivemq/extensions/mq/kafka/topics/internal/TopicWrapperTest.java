package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by gaochen on 2020/7/11.
 */
class TopicWrapperTest {

    @Test
    void withTopicName() {
        var topicWrapper = new TopicWrapper()
                .withTopic("/sys/properties/upload");

        Assertions
                .assertFalse(topicWrapper.isPattern());

        topicWrapper = new TopicWrapper()
                .withTopic("/sys/${device-name}/properties/upload");

        Assertions
                .assertEquals("/sys/([a-zA-Z0-9]+)/properties/upload", topicWrapper.getPattern().toString());
        Assertions
                .assertLinesMatch(
                        Collections.singletonList("device-name"),
                        topicWrapper.getVariables()
                );
        Assertions
                .assertTrue(topicWrapper.isPattern());

        topicWrapper = new TopicWrapper()
                .withTopic("/sys/${product-key}/${device-key}/properties/${index}/upload");

        Assertions
                .assertEquals(
                        "/sys/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)/properties/([a-zA-Z0-9]+)/upload",
                        topicWrapper.getPattern().toString()
                );

        Assertions
                .assertLinesMatch(
                        Arrays.asList("product-key", "device-key", "index"),
                        topicWrapper.getVariables()
                );
        Assertions
                .assertTrue(topicWrapper.isPattern());

    }

    @Test
    public void assembleTopicTest() {
        var topicWrapper = new TopicWrapper()
                .withTopic("/sys/${product-key}/${device-key}/properties/${index}/upload");
        var variables = new HashMap<String, String>() {{
            put("product-key", "product-AAA");
            put("device-key", "device-AAA");
            put("index", "10");
        }};
        var topic = topicWrapper.assembleTopic(variables);

        Assertions
                .assertEquals(
                        "/sys/product-AAA/device-AAA/properties/10/upload",
                        topic
                );
    }

}