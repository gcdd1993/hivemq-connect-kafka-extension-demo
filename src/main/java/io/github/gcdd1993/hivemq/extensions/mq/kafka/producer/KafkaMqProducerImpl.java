package io.github.gcdd1993.hivemq.extensions.mq.kafka.producer;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.config.ExtensionConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class KafkaMqProducerImpl implements MqProducer {

    private final KafkaSender<UUID, Object> sender;

    private final Queue<Tuple2<String, Object>> queue = new LinkedBlockingDeque<>();

    private final AtomicLong counter = new AtomicLong();

    public KafkaMqProducerImpl(ExtensionConfiguration configuration) {
        SenderOptions<UUID, Object> senderOptions = SenderOptions.create(configuration.producerConfig());

        sender = KafkaSender.create(senderOptions);
        init();
        counter(); // just for debug
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
