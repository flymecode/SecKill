package com.xupt.seckill.redis;

/**
 * 前缀接口
 */
public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
