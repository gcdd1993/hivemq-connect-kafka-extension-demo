package io.github.gcdd1993.hivemq.extensions.mq.kafka.exceptions;

import java.text.MessageFormat;

public class TopicMappingNotFoundException extends RuntimeException {
    private static final String BASE_MESSAGE = "MQTT Topic {0} cannot find mapping mq topic";

    public TopicMappingNotFoundException(String topic) {
        super(MessageFormat.format(BASE_MESSAGE, topic));
    }
}
