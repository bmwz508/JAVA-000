package com.example.redisDemo.controller;

import com.example.redisDemo.counter.RedisStockService;
import com.example.redisDemo.message.RedisPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    RedisPublisher redisPublisher;

    @PostMapping(value = "/add/{orderId}")
    public Boolean lockStock(@PathVariable String orderId){
        return redisPublisher.send(orderId);
    }
}
