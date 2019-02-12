package com.zengg.miaosha.service;

import com.alibaba.fastjson.JSON;
import com.zengg.miaosha.config.redis.mould.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: miaosha
 * @description: Redis 服务类
 * @author: ZengGuangfu
 * @create 2019-02-11 11:29
 */

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * get方法
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnJedis(jedis);
        }
    }

    /**
     * set方法
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T t){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            String value = beanToString(t);
            if (StringUtils.isEmpty(value)){
                return false;
            }
            int expireSeconds = keyPrefix.expireSeconds();
            // <= 0 代表永不过期 否则包含过期时间
            if (expireSeconds <= 0){
                jedis.set(realKey, value);
            }else{
                jedis.setex(realKey,expireSeconds,value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnJedis(jedis);
        }
    }

    /**
     * 判断该key 是否存在
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exist(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnJedis(jedis);
        }
    }

    /**
     * 增加数值
     * @param keyPrefix key addNumber
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix keyPrefix,String key,long addNumber){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            if (addNumber < 1L){
                return jedis.incr(realKey);
            }
            return jedis.incrBy(realKey,addNumber);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }finally {
            returnJedis(jedis);
        }
    }

    /**
     * 减少数值
     * @param keyPrefix key addNumber
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix keyPrefix,String key,long deNumber){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            if (deNumber < 1L){
                return jedis.decr(realKey);
            }
            return jedis.decrBy(realKey,deNumber);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }finally {
            returnJedis(jedis);
        }
    }

    /**
     * 关闭连接
     * @param jedis
     */
    private void returnJedis(final Jedis jedis){
        if ( jedis != null){
            jedis.close();
        }
    }

    /**
     * 字符串转化为Bean对象
     */
    private <T> T stringToBean(String str, Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null ){
            return null;
        }
        if (clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if (clazz == String.class){
            return (T)str;
        }else if (clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    /**
     * Bean对象转化为String类型
     */
    private <T> String beanToString(T t){
        if (t == null){
            return null;
        }
        Class<?> clazz = t.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return "" + t;
        }else if (clazz == String.class){
            return (String)t;
        }else if (clazz == long.class || clazz == Long.class){
            return "" + t;
        }else {
            return JSON.toJSONString(t);
        }
    }

}
