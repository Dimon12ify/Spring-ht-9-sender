package com.example.task9;

import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

@Component
@Data
public class Receiver {

    private ValidAuth auth;
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(ValidAuth auth) {
        System.out.println(auth.getMessage());
        latch.countDown();
        this.auth = auth;
    }

}
