package com.example.redisDemo.counter;

import com.example.redisDemo.lock.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RedisStockService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    DistributedLock distributedLock;

    private final static String LOCK_PREFIX = "LOCK:STOCK:";
    private static AtomicInteger SUCCESS_COUNT = new AtomicInteger(0);
    private static AtomicInteger FAIL_COUNT = new AtomicInteger(0);

    public Integer totalStock = 1000;

    public boolean lockStock(String key, Integer num) {
        Long stock = -1L;
        if (redisTemplate.hasKey(key)) {
            //存在减库存
            stock = redisTemplate.opsForValue().decrement(key, num);
        }else {
            //不存在添加库存
            String random = String.valueOf(new Random().nextInt(1000000));
            try {
                if (distributedLock.tryLock(LOCK_PREFIX + key, random)) {
                    //双重检验锁
                    if (!redisTemplate.hasKey(key)) {
                        redisTemplate.opsForValue().increment(key, totalStock);
                        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
                    }
                    stock = redisTemplate.opsForValue().decrement(key, num);
                }else {
                    if (redisTemplate.hasKey(key)) {
                        stock = redisTemplate.opsForValue().decrement(key, num);
                    }
                }
            }finally {
                distributedLock.unLock(key, random);
            }
        }
        boolean success = stock >= 0;
        if (success) {
            SUCCESS_COUNT.addAndGet(num);
            System.out.println("第" + SUCCESS_COUNT.get() + "个成功，剩余库存：" + stock);
        }else {
            FAIL_COUNT.addAndGet(num);
            System.out.println("第" + FAIL_COUNT.get() + "个失败，剩余库存：" + stock);
        }
        return success;
    }
}
