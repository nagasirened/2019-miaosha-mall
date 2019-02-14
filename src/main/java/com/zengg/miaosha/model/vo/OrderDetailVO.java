package com.zengg.miaosha.model.vo;


import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVO {

    private OrderInfo orderInfo;

    private GoodsVO goodsVO;

    private MiaoshaUser miaoshaUser;
}

