package com.example.redisDemo.controller;

import com.example.redisDemo.counter.RedisStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    RedisStockService redisStockService;

    @GetMapping(value = "lock/{goods}")
    public Boolean lockStock(@PathVariable String goods){
        return redisStockService.lockStock(goods, 1);
    }
}
