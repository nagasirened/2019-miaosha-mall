package com.zengg.miaosha.service;

import com.zengg.miaosha.config.redis.mould.realize.MiaoshaKey;
import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.utils.Md5Utils;
import com.zengg.miaosha.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: miaosha
 * @description: 商品服务类
 * @author: ZengGuangfu
 * @create 2019-02-12 13:42
 */

@Service
public class MiaoshaService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goodsVO) {
        // 1、减库存
        boolean bool = goodsService.reduceStock(goodsVO);
        // 2、下订单,写入秒杀订单
        if (bool) {
            return orderService.createOrder(user, goodsVO);
        }else{
            setMiaoshaOver(goodsVO.getStockCount());
            return null;
        }

    }


    /**
     * orderId:成功   1 秒杀失败 0 排队中
     * 如果有秒杀订单，返回订单号，表示秒杀成功
     * 如果没有，那么判断库存，如果库存为0，代表秒杀失败
     *                      如果库存还有，返回还在排队中
     * @param mobile
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(long mobile, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMisoshaOrderFromCache(mobile, goodsId);
        if (miaoshaOrder != null){
            return miaoshaOrder.getOrderId();
        }else{
            boolean isOver = getMiaoshaOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }
        }
    }

    /**
     * 标记改产品秒杀完了
     * @param goodsId
     */
    public void setMiaoshaOver(long goodsId) {
        redisService.set(MiaoshaKey.isOver, "" + goodsId, true);
    }

    /**
     * 获取某产品是否秒杀完了
     * @param goodsId
     * @return
     */
    private boolean getMiaoshaOver(long goodsId) {
        boolean exist = redisService.exist(MiaoshaKey.isOver, "" + goodsId);
        if (exist){
            return true;
        }
        return false;
    }

    /**
     * 生成秒杀随机路径
     * @param mobile
     * @param goodsId
     * @return
     */
    public String createPath(long mobile, long goodsId) {
        String randomPath = Md5Utils.md5(UUIDUtils.uuid() + "1a2b");
        redisService.set(MiaoshaKey.getRandomPath,""+mobile +" "+ goodsId ,randomPath);
        return randomPath;
    }

    /**
     * 验证秒杀随机路径
     * @param mobile
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkRandomPath(long mobile, long goodsId, String path) {
        if (StringUtils.isAllBlank(""+mobile , path)){
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getRandomPath, "" + mobile + " " + goodsId, String.class);
        return StringUtils.equals(pathOld,path);
    }
}
