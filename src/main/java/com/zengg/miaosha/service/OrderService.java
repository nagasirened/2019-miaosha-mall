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
 * @description:
 * @author: ZengGuangfu
 * @create 2019-02-12 15:29
 */

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVO goodsVO) {

       /* private Long id;
        private Long userId;
        private Long goodsId;
        private Long  deliveryAddrId;
        private String goodsName;
        private Integer goodsCount;
        private Double goodsPrice;
        private Integer orderChannel;
        private Integer status;
        private Date createDate;
        private Date payDate;*/
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getMobile());
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goodsVO.getMiaoshaPrice());
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        long orderInfoId = orderDao.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVO.getId());
        miaoshaOrder.setUserId(user.getMobile());
        miaoshaOrder.setOrderId(orderInfoId);
        orderDao.insert(miaoshaOrder);

        return orderInfo;
    }
}
