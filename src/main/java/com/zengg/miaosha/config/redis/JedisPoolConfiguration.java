package com.zengg.miaosha.config.redis;

import com.zengg.miaosha.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: miaosha
 * @description: 连接池
 * @author: ZengGuangfu
 * @create 2019-02-11 12:14
 */
@Configuration
//@EnableConfigurationProperties(RedisConfig.class)
@ConditionalOnClass(RedisService.class)
public class JedisPoolConfiguration {

    @Autowired
    private RedisConfig properties;

    @Bean
    public JedisPool jedisPool(){
        JedisPool jedisPool = new JedisPool(jedisPoolConfig(), properties.getHost(),
                properties.getPort(), properties.getPoolMaxWait() * 1000, properties.getPassword());
        return jedisPool;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(properties.getPoolMaxWait() * 1000);
        config.setMaxTotal(properties.getPoolMaxTotal());
        config.setMinIdle(properties.getPoolMinIdel());
        config.setMaxIdle(properties.getPoolMaxIdel());
        return config;
    }

}
