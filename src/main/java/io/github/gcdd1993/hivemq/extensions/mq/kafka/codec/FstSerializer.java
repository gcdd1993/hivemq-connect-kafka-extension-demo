package io.github.gcdd1993.hivemq.extensions.mq.kafka.codec;

import org.apache.kafka.common.serialization.Serializer;
import org.nustaq.serialization.FSTConfiguration;

import java.util.Map;

public class FstSerializer implements Serializer<Object> {

    private static final FSTConfiguration FST_CONFIGURATION = FSTConfiguration.createDefaultConfiguration();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        return FST_CONFIGURATION.asByteArray(data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
