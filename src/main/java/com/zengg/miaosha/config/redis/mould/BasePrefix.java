package com.zengg.miaosha.config.redis.mould;

/**
 * @program: miaosha
 * @description: redis前缀相关 抽象类
 * @author: ZengGuangfu
 * @create 2019-02-11 13:22
 */
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;

    private String prefix;

    // 使用private防止被实例化
    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 默认 0 代表永不过期
     *
     */
    public int expireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    /**
     * 获取不同的前缀
     */
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
