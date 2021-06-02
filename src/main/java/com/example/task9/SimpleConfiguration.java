package com.example.task9;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class SimpleConfiguration {
    static final String topicExchangeName = "my-exchange";

    static final String receiverQueueName = "toReceiver";

    static final String contollerQueueName = "toController";

    static final String key = "receiver.receive";

    static final String auth = "auth.receive";

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(receiverQueueName, false);
        Queue topicQueue2 = new Queue(contollerQueueName, false);
        TopicExchange topicExchange = new TopicExchange(topicExchangeName);
        return new Declarables(
                topicQueue1,
                topicQueue2,
                topicExchange,
                BindingBuilder
                        .bind(topicQueue1)
                        .to(topicExchange).with(key),
                BindingBuilder
                        .bind(topicQueue2)
                        .to(topicExchange).with(auth));
    }
}
