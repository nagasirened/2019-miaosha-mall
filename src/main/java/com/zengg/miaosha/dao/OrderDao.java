package com.zengg.miaosha.dao;

import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @program: miaosha
 * @description:
 * @author: ZengGuangfu
 * @create 2019-02-12 15:13
 */

@Mapper
public interface OrderDao {

    @Select("select * from order_info where user_id = #{mobile} and goods_id = #{goodsId}")
    public MiaoshaOrder getMisoshaOrderByUserIdAndGoodsId(@Param("mobile") long mobile, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values(" +
            "#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,
            before = false,statement = "SELECT LAST_INSERT_ID()")
    public long insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(user_id,goods_id,order_id) values (#{userId},#{goodsId},#{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
