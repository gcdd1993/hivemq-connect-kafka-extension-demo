package io.github.gcdd1993.hivemq.extensions.mq.kafka.producer;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.codec.FstSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class KafkaMqProducerImpl implements MqProducer {

    private final KafkaSender<UUID, Object> sender;

    private final Queue<Tuple2<String, Object>> queue = new LinkedBlockingDeque<>();

    private final AtomicLong counter = new AtomicLong();

    public KafkaMqProducerImpl(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, FstSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FstSerializer.class);
        SenderOptions<UUID, Object> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
        init();
        counter();
    }

    private void init() {
        sender.send(
                Flux
                        .<Tuple2<String, Object>>generate(sink -> {
                            var message = queue.poll();
                            if (message != null) {
                                sink.next(message);
                            } else {
                                sink.complete();
                            }
                        })
                        .repeatWhen(it -> it.delayElements(Duration.of(1, ChronoUnit.SECONDS)))
                        .map(tuple2 -> {
                            var uuid = UUID.randomUUID();
                            var producerRecord = new ProducerRecord<>(tuple2.getT1(), uuid, tuple2.getT2());
                            return SenderRecord.create(producerRecord, uuid);
                        }))
                .doOnError(e -> log.error("Send failed", e))
                .subscribe()
        ;
    }

    private void counter() {
        Flux
                .interval(Duration.ofMillis(1000))
                .doOnEach(__ -> {
                    log.info("published message {}", counter.get());
                })
                .subscribe()
        ;
    }

    @Override
    public void publish(@NotNull String topic, @NotNull Object message) {
        counter.incrementAndGet();
        queue.offer(Tuples.of(topic, message));
    }
}
