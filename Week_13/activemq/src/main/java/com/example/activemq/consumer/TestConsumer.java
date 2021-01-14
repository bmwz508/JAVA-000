package com.example.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

    @JmsListener(destination = "test.queue", containerFactory = "queueListenerFactory")
    public void receiveQueue(String msg) {
        System.out.println("监听到的消息内容为(queue): " + msg);
    }

    @JmsListener(destination = "test.topic", containerFactory = "topicListenerFactory")
    public void receiveTopic(String msg) {
        System.out.println("监听到的消息内容为(topic): " + msg);
    }

}
