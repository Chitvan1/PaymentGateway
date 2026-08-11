package com.paymentSystem.razorpay.common.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.rate-limit.method", havingValue = "sliding")
public class SlidingWindowRateLimiter implements RateLimiter {

    private final StringRedisTemplate redis;

    @Override
    public RateLimitResult check(String key, int maxRequestAllowed, long windowSeconds) {

        long nowMs = System.currentTimeMillis();
        long floorMS = nowMs - (windowSeconds * 1000);

        String redisKey = "ratelimit:sliding:"+key;
        var zset = redis.opsForZSet();
        zset.removeRangeByScore(redisKey, Double.NEGATIVE_INFINITY, floorMS);

        Long count = zset.zCard(redisKey);

        long current = count != null ? count : 0;

        if(current >= maxRequestAllowed) {

            return RateLimitResult.denied();
        }

        return null;
    }
}
