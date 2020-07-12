package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal;

import com.google.inject.Inject;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.exceptions.TopicMappingNotFoundException;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.TopicMapper;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gaochen on 2020/7/11.
 */
public class TopicMapperImpl implements TopicMapper {

    /**
     * 已经匹配过一次的Topic列表
     * <Mqtt Topic, <Kafka Topic, variables>>
     */
    private final Map<String, Tuple2<String, Map<String, String>>> mqttTopicMappingCache = new ConcurrentHashMap<>();

    /**
     * <mqtt topic, kafka topic>
     */
    private final Map<TopicWrapper, String> topicMappings = new HashMap<>();

    /**
     * <mqtt topic, kafka topic>
     */
    private final Map<String, TopicWrapper> reversedTopicMappings = new HashMap<>();

    @Inject
    public TopicMapperImpl(ExtensionConfiguration extensionConfiguration) {
        extensionConfiguration.getTopicMappings()
                .forEach((mqttTopic, kafkaTopic) -> {
                    var topicWrapper = new TopicWrapper()
                            .withTopic(mqttTopic);
                    topicMappings.put(topicWrapper, kafkaTopic);
                    reversedTopicMappings.put(kafkaTopic, topicWrapper);
                });
    }

    @Override
    public Map<TopicWrapper, String> getMappings() {
        return topicMappings;
    }

    @Override
    public String convertMqttTopic2KafkaTopic(String topic, Map<String, String> variables) {
        Tuple2<String, Map<String, String>> tuple2;
        if (mqttTopicMappingCache.containsKey(topic)) {
            tuple2 = mqttTopicMappingCache.get(topic);
        } else {
            tuple2 = lookup(topic);
            mqttTopicMappingCache.put(topic, tuple2);
        }
        variables.putAll(tuple2.getT2());
        return tuple2.getT1();
    }

    @Override
    public String convertKafkaTopic2MqttTopic(String topic, Map<String, String> variables) {
        if (reversedTopicMappings.containsKey(topic)) {
            var topicWrapper = reversedTopicMappings.get(topic);
            return topicWrapper.assembleTopic(variables);
        }
        return null;
    }

    private Tuple2<String, Map<String, String>> lookup(String topic) {
        for (var entrySet : topicMappings.entrySet()) {
            var topicWrapper = entrySet.getKey();
            var kafkaTopic = entrySet.getValue();
            if (topicWrapper.isPattern()) {
                var matcher = topicWrapper.getPattern().matcher(topic);
                if (matcher.matches()) {
                    var variables = new HashMap<String, String>();
                    for (var index = 0; index < matcher.groupCount(); index++) {
                        variables.put(topicWrapper.getVariables().get(index), matcher.group(index + 1));
                    }
                    return Tuples.of(kafkaTopic, variables);
                }
            } else {
                if (topicWrapper.getTopic().equals(topic)) {
                    return Tuples.of(kafkaTopic, Collections.emptyMap());
                }
            }
        }
        throw new TopicMappingNotFoundException(topic);
    }

}
