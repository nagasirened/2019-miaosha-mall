package com.zengg.miaosha.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: miaosha
 * @description: Redis 配置读取
 * @author: ZengGuangfu
 * @create 2019-02-11 11:14
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;
    private int poolMaxTotal;
    private int poolMaxIdel;
    private int poolMaxWait;
    private int poolMinIdel;

}
