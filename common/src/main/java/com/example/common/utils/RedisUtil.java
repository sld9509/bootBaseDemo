package com.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author 孙灵达
 * @date 2021-02-07
 */
@Slf4j
@Component
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    public RedisUtil(@Qualifier(value = "redisTemplate") RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 检查给定 key,是否存在
     */
    public static boolean exists(Object key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis判断key存在出错", e);
        }
        return false;
    }

    /**
     * 赋值对应key对应的value
     * @param key       key
     * @param value     value
     */
    public static boolean setStr(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("缓存set出错", e);
            return false;
        }
    }

    /**
     * 赋值对应key对应的value
     * @param key       key
     * @param value     value
     * @param time      过期时间
     */
    public static boolean setStr(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setStr(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("缓存set出错", e);
            return false;
        }
    }

    /**
     * 获取对应key的值
     * @param key       key
     * @return          value
     */
    public static Object getStr(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除对应key的值
     * @param key 可以传一个值 或多个
     */
    public static void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length != 1) {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
            redisTemplate.delete(key);
        }
    }
}
