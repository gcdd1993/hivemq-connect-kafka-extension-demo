package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by gaochen on 2020/7/11.
 */
class TopicWrapperTest {

    @Test
    void withTopicName() {
        var topicWrapper = new TopicWrapper()
                .withTopicName("/sys/properties/upload");

        Assertions
                .assertFalse(topicWrapper.isPattern());

        topicWrapper = new TopicWrapper()
                .withTopicName("/sys/${device-name}/properties/upload");

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
                .withTopicName("/sys/${product-key}/${device-key}/properties/${index}/upload");

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
}