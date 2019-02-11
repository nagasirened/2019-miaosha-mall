package com.zengg.miaosha.config.redis.mould.realize;

import com.zengg.miaosha.config.redis.mould.BasePrefix;

/**
 * @program: miaosha
 * @description: 用户模块的redis储存
 * @author: ZengGuangfu
 * @create 2019-02-11 13:28
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix){
        super(prefix);
    }

    public static UserKey getById = new UserKey("userId");
    public static UserKey getByName = new UserKey("userName");

}
