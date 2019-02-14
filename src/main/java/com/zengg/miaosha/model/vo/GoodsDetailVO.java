package com.zengg.miaosha.model.vo;


import com.zengg.miaosha.model.MiaoshaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetailVO {

    private GoodsVO goodsVO;

    private MiaoshaUser miaoshaUser;

    private int remainSeconds;

    private int miaoshaStatus;
}

