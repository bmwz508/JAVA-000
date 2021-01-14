package com.example.activemq;

import com.example.activemq.publisher.TestPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.TextMessage;

@RestController
public class TestController {

    @Autowired
    TestPublisher testPublisher;

    @GetMapping(value = "/test")
    public String test(){
        int index = 0;
        while (index++ < 100) {
            String message = "message--" + index;
            testPublisher.publishQueue(message);
        }
        index = 0;
        while (index++ < 100) {
            String message = "message--" + index;
            testPublisher.publishTopic(message);
        }
        return "success";
    }
}
