package com.example.redisDemo.lock;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author linmf
 *
 * 实现要点：
 * 1、原子操作
 * 2、互斥
 * 3、超时
 */
@Component
public class DistributedLock {

    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 锁过期时间（单位：秒）
     */
    private final static long EXPIRE_TIME = 5L;
    /**
     * 重新获取次数
     */
    private final static int TRY_COUNT = 5;
    /**
     * 重新获取的间隔时间（单位：毫秒）
     */
    private final static long TRY_INTERVAL = 10L;
    /**
     * 释放锁脚本
     */
    private static DefaultRedisScript<Long> releaseScript;

    @Autowired
    StringRedisTemplate redisTemplate;

    public DistributedLock() {
        releaseScript = new DefaultRedisScript<>();
        releaseScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis_unlock.lua")));
        releaseScript.setResultType(Long.class);
    }

    /**
     * 获取锁
     *
     * @param key   key
     * @param value value
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(String key, String value) {
        return getLock(key, value, EXPIRE_TIME, TRY_COUNT, TRY_INTERVAL);
    }

    /**
     * 获取锁
     *
     * @param key        key
     * @param value      value
     * @param expireTime 过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(String key, String value, Long expireTime) {
        return getLock(key, value, expireTime, TRY_COUNT, TRY_INTERVAL);
    }

    /**
     * 获取锁
     *
     * @param key      key
     * @param value    value
     * @param tryCount 重试次数
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(String key, String value, int tryCount) {
        return getLock(key, value, EXPIRE_TIME, tryCount, TRY_INTERVAL);
    }

    /**
     * 获取锁
     *
     * @param key         key
     * @param value       value
     * @param expireTime  过期时间
     * @param tryInterval 重试间隔时间
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(String key, String value, long expireTime, int tryCount, long tryInterval) {
        return getLock(key, value, expireTime, tryCount, tryInterval);
    }


    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁
     * 对于 Redis 集群则无法使用
     *
     * @param key         key
     * @param value       value
     * @param seconds     锁过期时间
     * @param tryCount    重试次数
     * @param tryInterval 重试间隔时间
     * @return true 获取成功，false获取失败
     */
    private boolean getLock(String key, String value, long seconds, long tryCount, long tryInterval) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return false;
        }
        int count = 0;
        do {
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
            if (Optional.ofNullable(aBoolean).orElse(Boolean.FALSE)) {
                return true;
            } else {
                try {
                    Thread.sleep(tryInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        while (count++ < tryCount);
        return false;
    }

    /**
     * 释放锁
     *
     * @param key   key
     * @param value value
     * @return true 获取成功，false获取失败
     */
    public boolean unLock(String key, String value) {
        Long result = redisTemplate.execute(releaseScript, Collections.singletonList(key), value);
        return RELEASE_SUCCESS.equals(result);
    }


}
