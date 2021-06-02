package com.example.task9;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("auth")
public class AuthController {
    private RabbitTemplate rabbitTemplate;
    private Receiver receiver;

    public AuthController(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ValidAuth Post(@RequestBody Auth auth) throws InterruptedException {
        rabbitTemplate.convertAndSend(SimpleConfiguration.topicExchangeName, SimpleConfiguration.key, auth);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        return receiver.getAuth();
    }
}
