package com.zengg.miaosha.config.redis.mould.realize;

import com.zengg.miaosha.config.redis.mould.BasePrefix;

/**
 * @program: miaosha
 * @description: 用户模块的redis储存
 * @author: ZengGuangfu
 * @create 2019-02-11 13:28
 */
public class OrderKey extends BasePrefix {

    private OrderKey(String prefix){
        super(prefix);
    }

    public static OrderKey orderKey = new OrderKey("orderInfo");

}
