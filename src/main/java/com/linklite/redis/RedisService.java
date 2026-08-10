package com.linklite.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveUrl(String shortCode, String originalUrl) {

        redisTemplate.opsForValue()
                .set(shortCode, originalUrl, Duration.ofHours(24));

    }

    public String getUrl(String shortCode) {

        return redisTemplate.opsForValue().get(shortCode);

    }

    public void deleteUrl(String shortCode) {

        redisTemplate.delete(shortCode);

    }

}