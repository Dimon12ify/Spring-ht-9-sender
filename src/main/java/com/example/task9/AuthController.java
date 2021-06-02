package com.example.task9;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {
    private RabbitTemplate rabbitTemplate;

    public AuthController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ValidAuth Post(@Valid @RequestBody Auth auth) throws InterruptedException {
        rabbitTemplate.convertAndSend(SimpleConfiguration.topicExchangeName, SimpleConfiguration.key, new Data(123, auth.getName(), auth.getPhoneNumber()));
        var validAuth = (ValidAuth) rabbitTemplate.receiveAndConvert(SimpleConfiguration.contollerQueueName, 10000);
        return validAuth;
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String, List<ErrorModel>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var fields = new HashMap<String, List<ErrorModel>>();
        var errors = new ArrayList<ErrorModel>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorModel(fieldName.toLowerCase(), errorMessage));
        });
        fields.put("errors", errors);
        return fields;
    }
}
