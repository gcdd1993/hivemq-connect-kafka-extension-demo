package io.github.gcdd1993.hivemq.extensions.mq.kafka.message;

import java.io.Serializable;
import java.util.Optional;

public interface IMessage extends Serializable {

    /**
     * 来源主题
     */
    Optional<String> topic();

    /**
     * 客户端ID
     */
    String clientId();

    /**
     * 客户端IP
     */
    Optional<String> hostAddress();

    /**
     * 消息发生时间
     */
    Long timestamp();
}
