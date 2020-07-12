package io.github.gcdd1993.hivemq.extensions.mq.kafka.config;

import io.github.gcdd1993.hivemq.extensions.mq.kafka.codec.FstSerializer;
import lombok.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaochen on 2020/7/10.
 */
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionConfiguration {

    @Builder.Default
    private Map<String, Object> cluster = new HashMap<>();
    @Builder.Default
    private Map<String, Object> consumer = new HashMap<>();
    @Builder.Default
    private Map<String, Object> producer = new HashMap<>();

    @Getter
    @Builder.Default
    private Map<String, String> topicMappings = new HashMap<>();

    public Map<String, Object> consumerConfig() {
        mergeWith(consumer, defaultConsumerConfig());
        mergeWith(consumer, cluster);
        return consumer;
    }

    public Map<String, Object> producerConfig() {
        mergeWith(producer, defaultProducerConfig());
        mergeWith(producer, cluster);
        return producer;
    }

    private void mergeWith(Map<String, Object> c1, Map<String, Object> c2) {
        c2
                .forEach((k, v) -> {
                    if (!c1.containsKey(k)) {
                        c1.put(k, v);
                    }
                });
    }

    private Map<String, Object> defaultConsumerConfig() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "HiveMQ_consumers");
//        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, FstSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FstSerializer.class);

        return props;
    }

    private Map<String, Object> defaultProducerConfig() {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "HiveMQ_producers");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, FstSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FstSerializer.class);

        return props;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("cluster:\n");
        beautifyShow(cluster, sb);
        sb.append("consumer:\n");
        beautifyShow(consumer, sb);
        sb.append("producer:\n");
        beautifyShow(producer, sb);
        sb.append("topicMappings:\n");
        beautifyShow(topicMappings, sb);
        return sb.toString();
    }

    private void beautifyShow(Map<?, ?> values, StringBuilder sb) {
        values.forEach((k, v) ->
                sb
                        .append(k)
                        .append("=")
                        .append(v)
                        .append("\n"));
    }
}
