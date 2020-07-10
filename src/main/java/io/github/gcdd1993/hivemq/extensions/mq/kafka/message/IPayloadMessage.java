package io.github.gcdd1993.hivemq.extensions.mq.kafka.message;

/**
 * Created by gaochen on 2020/7/10.
 */
public interface IPayloadMessage extends IMessage {
    Object payload();
}
