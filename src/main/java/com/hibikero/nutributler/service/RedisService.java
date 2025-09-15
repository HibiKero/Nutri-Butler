package com.hibikero.nutributler.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务接口
 */
public interface RedisService {

    /**
     * 设置键值对
     */
    void set(String key, Object value);

    /**
     * 设置键值对，带过期时间
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取值
     */
    Object get(String key);

    /**
     * 获取指定类型的值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除键
     */
    void delete(String key);

    /**
     * 批量删除键
     */
    void delete(List<String> keys);

    /**
     * 检查键是否存在
     */
    boolean hasKey(String key);

    /**
     * 设置过期时间
     */
    boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 获取过期时间
     */
    long getExpire(String key);

    /**
     * 获取所有匹配的键
     */
    Set<String> keys(String pattern);

    /**
     * 原子递增
     */
    long increment(String key, long delta);

    /**
     * 原子递减
     */
    long decrement(String key, long delta);

    /**
     * 获取列表
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 设置列表
     */
    <T> void setList(String key, List<T> list);

    /**
     * 设置列表，带过期时间
     */
    <T> void setList(String key, List<T> list, long timeout, TimeUnit unit);

    /**
     * 左推入列表
     */
    <T> void leftPush(String key, T value);

    /**
     * 右推入列表
     */
    <T> void rightPush(String key, T value);

    /**
     * 左弹出列表
     */
    <T> T leftPop(String key, Class<T> clazz);

    /**
     * 右弹出列表
     */
    <T> T rightPop(String key, Class<T> clazz);

    /**
     * 获取列表大小
     */
    long getListSize(String key);

    /**
     * 设置哈希字段
     */
    void hSet(String key, String field, Object value);

    /**
     * 获取哈希字段
     */
    Object hGet(String key, String field);

    /**
     * 获取哈希字段（指定类型）
     */
    <T> T hGet(String key, String field, Class<T> clazz);

    /**
     * 删除哈希字段
     */
    void hDelete(String key, String field);

    /**
     * 检查哈希字段是否存在
     */
    boolean hHasKey(String key, String field);

    /**
     * 获取哈希所有字段
     */
    Set<String> hKeys(String key);

    /**
     * 获取哈希大小
     */
    long hSize(String key);
}