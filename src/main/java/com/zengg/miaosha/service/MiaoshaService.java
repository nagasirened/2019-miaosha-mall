package com.zengg.miaosha.service;

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

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goodsVO) {
        // 1、减库存 2、下订单  3、写入秒杀订单
        goodsService.reduceStock(goodsVO);
        OrderInfo orderInfo = orderService.createOrder(user ,goodsVO);




        return null;
    }
}
