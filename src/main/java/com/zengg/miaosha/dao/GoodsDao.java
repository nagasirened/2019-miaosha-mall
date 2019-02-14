package com.zengg.miaosha.dao;

import com.zengg.miaosha.model.MiaoshaGoods;
import com.zengg.miaosha.model.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: miaosha
 * @description: 商品DAO类
 * @author: ZengGuangfu
 * @create 2019-02-12 13:36
 */

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVO> getGoodsVOList();

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id= #{id}")
    public GoodsVO getGoodsVOByGoodsId(@Param("id") long id);

    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id=#{goodsId} and stock_count > 0")
    public int reduceStock(MiaoshaGoods miaoshaGoods);
}
