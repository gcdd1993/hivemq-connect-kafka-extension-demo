package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.topics.internal.TopicWrapper;

import java.util.Map;

/**
 * Created by gaochen on 2020/7/11.
 */
public interface TopicMapper {

    /**
     * 获取主题映射关系
     *
     * @return <mqtt-topic, kafka-topic>
     */
    Map<TopicWrapper, String> getMappings();

    /**
     * 转换MQTT Topic 到Kafka Topic
     *
     * @param topic     MQTT Topic
     * @param variables 可能存在的参数列表，示例：
     *                  topic mapping --> /sys/${device-name}/properties/upload
     *                  mqtt topic --> /sys/device-1/properties/upload
     *                  kafka topic --> /sys/properties/upload, variables --> {"device-name":"device-1"}
     */
    String convertMqttTopic2KafkaTopic(String topic, Map<String, String> variables);

    /**
     * 转换Kafka Topic到MQTT Topic
     *
     * @param topic     Kafka Topic
     * @param variables 可能存在的参数列表，示例：
     *                  topic mapping --> /sys/${device-name}/properties/upload
     *                  kafka topic --> /sys/properties/upload, variables --> {"device-name":"device-1"}
     *                  mqtt topic --> /sys/device-1/properties/upload
     */
    String convertKafkaTopicMqttTopic(String topic, Map<String, String> variables);

}
