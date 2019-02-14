package com.zengg.miaosha.model.vo;

import com.zengg.miaosha.model.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @program: miaosha
 * @description: 商品拓展
 * @author: ZengGuangfu
 * @create 2019-02-12 13:35
 */

@Data
public class GoodsVO extends Goods {

    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
