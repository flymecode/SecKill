package com.xupt.seckill;

import com.xupt.seckill.config.RedisConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 启动类
 *
 * @author maxu
 */
@MapperScan("com.xupt.seckill.mapper")
@SpringBootApplication
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    @Bean
    public RedisConfig redisConfig() {
        return new RedisConfig();
    }

    @Bean
    public JedisPool jedisPoolFactory(@Qualifier("redisConfig") RedisConfig redisConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(),
                redisConfig.getPort(), redisConfig.getTimeout());
        return jedisPool;
    }
}
