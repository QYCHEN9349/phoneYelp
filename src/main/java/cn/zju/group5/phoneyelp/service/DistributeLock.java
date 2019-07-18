package cn.zju.group5.phoneyelp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * 分布式锁实现
 */
@Service
public class DistributeLock {
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public DistributeLock(RedisTemplate<String ,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    public Boolean lock(String key,String value, Duration expireTime){
        return redisTemplate.opsForValue().setIfAbsent(key,value,expireTime);
    }
    public Boolean unlock(String key,String value){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript=new DefaultRedisScript<>(script,Long.class);
        Long result=redisTemplate.execute(redisScript, Arrays.asList(key,value));
        return Long.valueOf(1).equals(result);
    }
}
