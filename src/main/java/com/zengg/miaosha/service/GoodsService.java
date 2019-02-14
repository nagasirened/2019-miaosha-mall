package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.GoodsDao;
import com.zengg.miaosha.model.Goods;
import com.zengg.miaosha.model.MiaoshaGoods;
import com.zengg.miaosha.model.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public void reduceStock(GoodsVO goodsVO) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVO.getId());
        miaoshaGoods.setStockCount(goodsVO.getStockCount() - 1);
        goodsDao.reduceStock(miaoshaGoods);
    }

    public Map<String,Integer> getStatusAndremainSeconds(GoodsVO goodsVO){
        long endTime = goodsVO.getEndDate().getTime();
        long startTime = goodsVO.getStartDate().getTime();
        long nowTime = System.currentTimeMillis();
        // 倒计时时间
        int remainSeconds = 0;
        int miaoshaStatus = 0;

        if (nowTime < startTime){
            // 秒杀没有开始，需要进行倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startTime - nowTime )/1000);
        }else if(nowTime > endTime){
            // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            // 秒杀进行中
            miaoshaStatus = 1;
        }

        HashMap<String, Integer> map = new HashMap<>(2, 1f);
        map.put("miaoshaStatus",miaoshaStatus);
        map.put("remainSeconds",remainSeconds);
        return map;
    }
}
