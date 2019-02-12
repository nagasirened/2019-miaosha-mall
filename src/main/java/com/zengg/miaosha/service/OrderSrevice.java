package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.OrderDao;
import com.zengg.miaosha.model.MiaoshaOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: miaosha
 * @description: 订单服务类
 * @author: ZengGuangfu
 * @create 2019-02-12 15:10
 */

@Service
public class OrderSrevice {

    @Autowired
    private OrderDao orderDao;

    public MiaoshaOrder getMisoshaOrderByUserIdAndGoodsId(long mobile, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderDao.getMisoshaOrderByUserIdAndGoodsId(mobile,goodsId);
        return miaoshaOrder;
    }
}
