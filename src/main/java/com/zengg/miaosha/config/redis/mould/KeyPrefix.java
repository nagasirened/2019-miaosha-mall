package com.zengg.miaosha.config.redis.mould;

public interface KeyPrefix {

    public int expireSeconds();

    public void setExpireSeconds(int expireSeconds);

    public String getPrefix();
}
