package com.zengg.miaosha.service;

import com.zengg.miaosha.config.redis.mould.realize.GoodsKey;
import com.zengg.miaosha.config.redis.mould.realize.MiaoshaKey;
import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: miaosha
 * @description: 商品服务类
 * @author: ZengGuangfu
 * @create 2019-02-12 13:42
 */

@Service
public class MiaoshaService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goodsVO) {
        // 1、减库存
        boolean bool = goodsService.reduceStock(goodsVO);
        OrderInfo orderInfo = null;
        // 2、下订单,写入秒杀订单
        if (bool) {
            orderInfo = orderService.createOrder(user, goodsVO);
        }
        setMiaoshaOver(goodsVO.getStockCount());
        return orderInfo;
    }


    /**
     * orderId:成功   1 秒杀失败 0 排队中
     * 如果有秒杀订单，返回订单号，表示秒杀成功
     * 如果没有，那么判断库存，如果库存为0，代表秒杀失败
     *                      如果库存还有，返回还在排队中
     * @param mobile
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(long mobile, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMisoshaOrderFromCache(mobile, goodsId);
        if (miaoshaOrder != null){
            return miaoshaOrder.getOrderId();
        }else{
            boolean isOver = getMiaoshaOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }


    public void setMiaoshaOver(long goodsId) {
        redisService.set(MiaoshaKey.isOver, "" + goodsId, true);
    }

    private boolean getMiaoshaOver(long goodsId) {
        boolean exist = redisService.exist(MiaoshaKey.isOver, "" + goodsId);
        if (exist){
            return true;
        }
        return false;
    }
}
