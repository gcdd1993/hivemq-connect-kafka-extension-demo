package io.github.gcdd1993.hivemq.extensions.mq.kafka.events;

import com.google.inject.Inject;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener;
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListenerProvider;
import com.hivemq.extension.sdk.api.events.client.parameters.ClientLifecycleEventListenerProviderInput;

/**
 * Created by gaochen on 2020/7/10.
 */
public class ClientLifecycleEventListenerProviderImpl implements ClientLifecycleEventListenerProvider {

    private final ClientLifecycleEventListener clientLifecycleEventListener;

    @Inject
    public ClientLifecycleEventListenerProviderImpl(ClientLifecycleEventListener clientLifecycleEventListener) {
        this.clientLifecycleEventListener = clientLifecycleEventListener;
    }

    @Override
    public @Nullable ClientLifecycleEventListener getClientLifecycleEventListener(@NotNull ClientLifecycleEventListenerProviderInput clientLifecycleEventListenerProviderInput) {
        return clientLifecycleEventListener;
    }
}
