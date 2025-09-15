package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        try {
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value to " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    @Override
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null ? expire : -1;
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public long increment(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0;
    }

    @Override
    public long decrement(String key, long delta) {
        Long result = redisTemplate.opsForValue().decrement(key, delta);
        return result != null ? result : 0;
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream()
                .map(obj -> {
                    if (clazz.isInstance(obj)) {
                        return clazz.cast(obj);
                    }
                    try {
                        return objectMapper.convertValue(obj, clazz);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to convert list item to " + clazz.getSimpleName(), e);
                    }
                })
                .toList();
    }

    @Override
    public <T> void setList(String key, List<T> list) {
        redisTemplate.delete(key);
        if (list != null && !list.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, list.toArray());
        }
    }

    @Override
    public <T> void setList(String key, List<T> list, long timeout, TimeUnit unit) {
        setList(key, list);
        expire(key, timeout, unit);
    }

    @Override
    public <T> void leftPush(String key, T value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public <T> void rightPush(String key, T value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public <T> T leftPop(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForList().leftPop(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        try {
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value to " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public <T> T rightPop(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForList().rightPop(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        try {
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value to " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public long getListSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size != null ? size : 0;
    }

    @Override
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public <T> T hGet(String key, String field, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        try {
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value to " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public void hDelete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public boolean hHasKey(String key, String field) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, field));
    }

    @Override
    public Set<String> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key).stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public long hSize(String key) {
        Long size = redisTemplate.opsForHash().size(key);
        return size != null ? size : 0;
    }
}
