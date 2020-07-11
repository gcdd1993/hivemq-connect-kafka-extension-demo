package io.github.gcdd1993.hivemq.extensions.mq.kafka.topics;

/**
 * Created by gaochen on 2020/7/11.
 */
public interface TopicMatcher {

    /**
     * Topic是否匹配给定的Topic名称规则
     *
     * @param topic        Topic
     * @param topicPattern Topic名称规则
     */
    boolean match(String topic, String topicPattern);
}
