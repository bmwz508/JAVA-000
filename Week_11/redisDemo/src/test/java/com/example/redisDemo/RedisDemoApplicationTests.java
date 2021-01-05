package com.example.redisDemo;

import com.example.redisDemo.counter.RedisStockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisDemoApplicationTests {

    @Autowired
    RedisStockService redisStockService;

    @Test
    public void distributedCounter() {
        Boolean test = redisStockService.lockStock("test11", 1);
        System.out.println(test);
    }

}
