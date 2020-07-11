package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal;

import com.google.inject.Inject;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.TopicMapper;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.TopicMatcher;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaochen on 2020/7/11.
 */
public class TopicMapperImpl implements TopicMapper {

    private final Map<String, Tuple2<String, TopicWrapper>> topicMappingCache = new ConcurrentHashMap<>();

    /**
     * <mqtt topic, kafka topic>
     */
    private final Map<TopicWrapper, String> topicMappings = new HashMap<>();

    /**
     * <mqtt topic, kafka topic>
     */
    private final Map<String, TopicWrapper> reversedTopicMappings = new HashMap<>();

    private final TopicMatcher topicMatcher;

    @Inject
    public TopicMapperImpl(ExtensionConfiguration extensionConfiguration,
                           TopicMatcher topicMatcher) {
        extensionConfiguration.getTopicMappings()
                .forEach((mqttTopic, kafkaTopic) -> {
                    var topicWrapper = new TopicWrapper()
                            .withTopicName(mqttTopic);
                    topicMappings.put(topicWrapper, kafkaTopic);
                    reversedTopicMappings.put(kafkaTopic, topicWrapper);
                });
        this.topicMatcher = topicMatcher;
    }

    @Override
    public Map<TopicWrapper, String> getMappings() {
        return topicMappings;
    }

    @Override
    public String convertMqttTopic2KafkaTopic(String topic, Map<String, String> variables) {
        Tuple2<String, TopicWrapper> tuple2;
        if (topicMappingCache.containsKey(topic)) {
            tuple2 = topicMappingCache.get(topic);
        } else {
            tuple2 = lookup(topic);
            topicMappingCache.put(topic, tuple2);
        }
        parseVariables(tuple2.getT2(), topic, variables);
        return tuple2.getT1();
    }

    @Override
    public String convertKafkaTopicMqttTopic(String topic, Map<String, String> variables) {
        return null;
    }

    private Tuple2<String, TopicWrapper> lookup(String topic) {
        for (var entrySet : topicMappings.entrySet()) {
            var topicWrapper = entrySet.getKey();
            var kafkaTopic = entrySet.getValue();
            if (topicWrapper.isPattern()) {
                var matcher = topicWrapper.getPattern().matcher(topic);
                if (matcher.matches()) {
                    return Tuples.of(topic, topicWrapper);
                }
            } else {
                if (topicWrapper.getTopic().equals(topic)) {
                    return Tuples.of(topic, topicWrapper);
                }
            }
        }
        return null;
    }

    private void parseVariables(TopicWrapper topicWrapper, String topic, Map<String, String> variables) {
        var matcher = topicWrapper.getPattern().matcher(topic);
        for (var index = 0; index < matcher.groupCount(); index++) {
            variables.put(topicWrapper.getVariables().get(index), matcher.group(index));
        }
    }

}
