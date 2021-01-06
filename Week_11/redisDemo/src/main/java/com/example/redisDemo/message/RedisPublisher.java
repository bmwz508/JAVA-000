package com.example.redisDemo.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author linmf
 * @Description
 * @date 2021/1/6 21:15
 */
@Component
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Boolean send(String data) {
        redisTemplate.convertAndSend("order", data);
        return true;
    }
}
