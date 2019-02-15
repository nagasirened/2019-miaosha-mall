package com.zengg.miaosha.config.redis.mould.realize;

import com.zengg.miaosha.config.redis.mould.BasePrefix;

/**
 * @program: miaosha
 * @description: 商品redis前缀
 * @author: ZengGuangfu
 * @create 2019-02-11 13:49
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    /**
     * 默认页面缓存60s
     */
    public static GoodsKey getGoodsList = new GoodsKey(60,"goodsList");

    public static GoodsKey getGoodsDetail = new GoodsKey(60,"goodsDetail");

    public static GoodsKey miaoshaGoodsStock = new GoodsKey(0,"miaoshaStock");
}
