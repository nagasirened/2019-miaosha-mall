package com.zengg.miaosha.config.redis.mould.realize;

import com.zengg.miaosha.config.redis.mould.BasePrefix;

/**
 * @program: miaosha
 * @description: 储存用户信息
 * @author: ZengGuangfu
 * @create 2019-02-12 09:30
 */
public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey isOver = new MiaoshaKey(0,"msOver ");

    public static MiaoshaKey getRandomPath = new MiaoshaKey(60,"msOver ");

    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(60,"verifyCode ");


}
