package com.xupt.seckill.redis;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(prefix.getPrefix() + key);
            T bean = transformToBean(str, clazz);
            return bean;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置对象
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = transformString(value);
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(prefix.getPrefix() + key, str);
            } else {
                jedis.setex(prefix.getPrefix() + key, seconds, str);
            }
            return true;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 判断是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(prefix.getPrefix() + key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 减操作
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(prefix.getPrefix() + key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 加操作
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(prefix.getPrefix() + key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    private <T> String transformString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T transformToBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

}
