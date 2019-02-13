package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.OrderDao;
import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: miaosha
 * @description:订单服务类
 * @author: ZengGuangfu
 * @create 2019-02-12 15:29
 */

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVO goodsVO) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getMobile());
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goodsVO.getMiaoshaPrice());
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderDao.insertOrderInfo(orderInfo); //返回主键到对象中

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVO.getId());
        miaoshaOrder.setUserId(user.getMobile());
        miaoshaOrder.setOrderId(orderInfo.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        return orderInfo;
    }

    public MiaoshaOrder getMisoshaOrderByUserIdAndGoodsId(long mobile, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderDao.getMisoshaOrderByUserIdAndGoodsId(mobile,goodsId);
        return miaoshaOrder;
    }
}
