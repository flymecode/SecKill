package com.xupt.seckill.service;

/**
 * 封装本地缓存
 * @author maxu
 * @date 2019/6/24
 */
public interface CacheService {
    void setCommonCache(String key,Object value);

    Object getCommonCach(String key);
}
