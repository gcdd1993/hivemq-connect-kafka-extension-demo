package io.github.gcdd1993.hivemq.extensions.mq.kafka.events;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListenerProvider;
import com.hivemq.extension.sdk.api.events.client.parameters.ClientLifecycleEventListenerProviderInput;
import io.github.gcdd1993.hivemq.extensions.mq.kafka.producer.MqProducer;
import lombok.RequiredArgsConstructor;

/**
 * Created by gaochen on 2020/7/10.
 */
@RequiredArgsConstructor
public class ClientLifecycleEventListenerProviderImpl implements ClientLifecycleEventListenerProvider {

    private final MqProducer mqProducer;

    @Override
    public @Nullable ClientLifecycleEventListener getClientLifecycleEventListener(@NotNull ClientLifecycleEventListenerProviderInput clientLifecycleEventListenerProviderInput) {
        return new ClientLifecycleEventListenerImpl(mqProducer);
    }
}
