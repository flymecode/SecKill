package com.xupt.seckill.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xupt.seckill.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author maxu
 * @date 2019/6/24
 */
@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String, Object> commonCache = null;

    @PostConstruct
    public void init() {
        commonCache = CacheBuilder.newBuilder()
                // 设置缓存容器的初始容量为 10
                .initialCapacity(10)
                // 设置缓存中可以存储100个 key，超过之后会按照 LRU 的策略移除缓存。
                .maximumSize(100)
                // 设置缓存多少秒之后过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getCommonCach(String key) {
        return commonCache.getIfPresent(key);
    }
}
