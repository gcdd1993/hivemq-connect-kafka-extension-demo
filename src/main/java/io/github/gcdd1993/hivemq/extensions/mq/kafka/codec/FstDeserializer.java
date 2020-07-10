package io.github.gcdd1993.hivemq.extensions.mq.kafka.codec;

import org.apache.kafka.common.serialization.Deserializer;
import org.nustaq.serialization.FSTConfiguration;

import java.util.Map;

/**
 * Created by gaochen on 2020/7/7.
 */
public class FstDeserializer implements Deserializer<Object> {

    private static final FSTConfiguration FST_CONFIGURATION = FSTConfiguration.createDefaultConfiguration();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        return FST_CONFIGURATION.asObject(data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
