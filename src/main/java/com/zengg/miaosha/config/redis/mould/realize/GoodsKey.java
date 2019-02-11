package com.zengg.miaosha.config.redis.mould.realize;

import com.zengg.miaosha.config.redis.mould.BasePrefix;

/**
 * @program: miaosha
 * @description: 商品redis前缀
 * @author: ZengGuangfu
 * @create 2019-02-11 13:49
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(String prefix){
        super(prefix);
    }

    public static GoodsKey getByName = new GoodsKey("goodsName");
    public static GoodsKey getByTitle = new GoodsKey("goodsTitle");
}
