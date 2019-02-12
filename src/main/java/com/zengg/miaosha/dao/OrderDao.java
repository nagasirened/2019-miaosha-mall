package com.zengg.miaosha.dao;

import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @program: miaosha
 * @description:
 * @author: ZengGuangfu
 * @create 2019-02-12 15:13
 */
public interface OrderDao {

    @Select("select * from order_info where user_id = #{mobile} and goods_id = #{goodsId}")
    public MiaoshaOrder getMisoshaOrderByUserIdAndGoodsId(@Param("mobile") long mobile, @Param("goodsId") long goodsId);


    public long insert(OrderInfo orderInfo);

    public long insert(MiaoshaOrder miaoshaOrder);
}
