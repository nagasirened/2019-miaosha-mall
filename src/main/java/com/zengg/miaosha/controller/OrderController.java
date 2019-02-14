package com.zengg.miaosha.controller;

import com.zengg.miaosha.config.adapter.NeedLogin;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.model.vo.OrderDetailVO;
import com.zengg.miaosha.service.GoodsService;
import com.zengg.miaosha.service.OrderService;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: miaosha
 * @description: 订单相关控制类
 * @author: ZengGuangfu
 * @create 2019-02-14 14:13
 */


@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/detail")
    @NeedLogin
    public Result<OrderDetailVO> getDetail(Model model, MiaoshaUser miaoshaUser
                            , @RequestParam("orderId")long orderId ){

        if (StringUtils.isBlank(""+orderId)){
            return Result.error(CodeMsg.ORDER_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderInfoByOrderId(orderId);
        if (orderInfo == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIT);
        }

        Long goodsId = orderInfo.getGoodsId();
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);

        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setGoodsVO(goodsVO);
        orderDetailVO.setOrderInfo(orderInfo);

        return Result.success(orderDetailVO);
    }
}
