package org.doorip.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class LettuceLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(String token, String lockType) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(token, lockType, Duration.ofSeconds(3L));
    }

    public void unlock(String token) {
        redisTemplate.delete(token);
    }
}
