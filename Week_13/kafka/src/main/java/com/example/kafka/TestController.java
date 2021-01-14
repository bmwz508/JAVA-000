package com.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    KafkaProducer kafkaProducer;

    @GetMapping("/test")
    public String test(){
        for (int i = 0; i < 100; i++) {
            kafkaProducer.sendMessage("message--" + i);
        }
        return "success";
    }
}
