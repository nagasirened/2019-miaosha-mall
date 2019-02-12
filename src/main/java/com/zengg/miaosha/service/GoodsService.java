package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.GoodsDao;
import com.zengg.miaosha.model.Goods;
import com.zengg.miaosha.model.MiaoshaGoods;
import com.zengg.miaosha.model.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: miaosha
 * @description: 商品服务类
 * @author: ZengGuangfu
 * @create 2019-02-12 13:42
 */

@Service
public class GoodsService  {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVO> getGoodsVOList(){
        return goodsDao.getGoodsVOList();
    }

    public GoodsVO getGoodsVOByGoodsId(long id) {
        return goodsDao.getGoodsVOByGoodsId(id);
    }

    public void reduceStock(GoodsVO goodsVO) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVO.getId());
        miaoshaGoods.setStockCount(goodsVO.getGoodsStock() - 1);
        goodsDao.reduceStock(miaoshaGoods);
    }
}
