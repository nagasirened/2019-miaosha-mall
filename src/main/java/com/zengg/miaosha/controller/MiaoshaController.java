package com.zengg.miaosha.controller;

import com.zengg.miaosha.config.adapter.NeedLogin;
import com.zengg.miaosha.config.redis.mould.realize.GoodsKey;
import com.zengg.miaosha.config.redis.mould.realize.MiaoshaKey;
import com.zengg.miaosha.config.redis.mould.realize.OrderKey;
import com.zengg.miaosha.model.MiaoshaGoods;
import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.model.vo.MiaoshaMessageVO;
import com.zengg.miaosha.service.*;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Md5Utils;
import com.zengg.miaosha.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: miaosha
 * @description: 秒杀逻辑控制类
 * @author: ZengGuangfu
 * @create 2019-02-12 15:03
 */

@RestController
@RequestMapping(value = "/miaosha")
@Slf4j
@ConditionalOnBean(RedisService.class)
public class MiaoshaController implements InitializingBean{

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    private Map<Long,Boolean> localOverMap = new HashMap<>(2);

    /**
     * 系统初始化的时候查询秒杀商品数量，并加载到redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        if (goodsVOList == null || goodsVOList.isEmpty()){
            return;
        }
        for (GoodsVO goodsVO : goodsVOList){
            redisService.set(GoodsKey.miaoshaGoodsStock,"" + goodsVO.getId() ,goodsVO.getStockCount());
            localOverMap.put(goodsVO.getId(),false);
        }
    }

    /**
     * TODO 秒杀优化
     * 1、系统初始化，把秒杀商品库存数量加载到Redis
     * 2、收到请求后，Redis遇减库存，库存不足，直接返回，否则进入3
     * 3、请求入队，立即返回排队中
     * 4、请求出队，生成订单，减少库存
     * 5、客户端轮询，是否秒杀成功
     *
     * TODO 优化前 start
     * 1、查询是否还有库存 MYSQL
     * 2、查询是否秒杀过了
     * 3、开始秒杀
     * @param model
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    @PostMapping("/{path}/do_miaosha")
    @NeedLogin
    public Result<Integer> doMiaosha1(Model model,MiaoshaUser miaoshaUser
                    ,@RequestParam("goodsId")long goodsId
                    ,@PathVariable("path")String path){
        if (miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user",miaoshaUser);

        // 优化减少访问redis
        if (localOverMap.get(goodsId)){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        // 检查path是否正确，隐藏秒杀接口地址
        boolean checked = miaoshaService.checkRandomPath(miaoshaUser.getMobile(),goodsId,path);
        if( !checked ){
            return Result.error(CodeMsg.REQUEST_ILLEGLE);
        }

        // 内存标记，返回减少后的值
        Long stock = redisService.decrBy(GoodsKey.miaoshaGoodsStock, "" + goodsId, 1);
        if (stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        // 判断是否已经成功秒杀过了 从缓存中获取秒杀订单，因为这订单的存储时间没有限制
        MiaoshaOrder miaoshaOrder = orderService
                .getMisoshaOrderFromCache(miaoshaUser.getMobile(),goodsId);
        if (miaoshaOrder != null){
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_REPET.getMsg());
            return Result.error(CodeMsg.MIAOSHA_REPET);
        }

        // 加入队列
        MiaoshaMessageVO miaoshaMessageVO = new MiaoshaMessageVO(miaoshaUser , goodsId);
        rabbitMQSender.sendMiaoshaMessage(miaoshaMessageVO);

        // 返回0代表在排队中
        return Result.success(0);
    }

    /**
     * 轮询秒杀结果，即查询是否生成订单
     * @param model
     * @param miaoshaUser
     * @param goodsId
     * @return orderId:成功   1 秒杀失败 0 排队中
     */
    @GetMapping(value = "/msResult")
    @NeedLogin
    public Result<Long> msResult(Model model,MiaoshaUser miaoshaUser
            ,@RequestParam("goodsId")long goodsId){
        if (miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user",miaoshaUser);
        // orderId:成功   1 秒杀失败 0 排队中
        long result = miaoshaService.getMiaoshaResult(miaoshaUser.getMobile(),goodsId);

        return Result.success(result);
    }

    @GetMapping(value = "/path")
    @NeedLogin
    public Result<String> getMsPath(Model model,MiaoshaUser miaoshaUser
            ,@RequestParam("goodsId")long goodsId){
        if (miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String randomPath = miaoshaService.createPath(miaoshaUser.getMobile(),goodsId);
        return Result.success(randomPath);
    }


    /**
     * 回退方法  方便测试用
     * @param model
     * @param user
     */
    @RequestMapping(value = "/now/reset")
    public Result<String> reset(Model model ,MiaoshaUser user){
        // 清除缓存 token / miaoshaorder /
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        orderService.deleteOrders();

        for (GoodsVO goodsVO :goodsVOList){
            goodsVO.setStockCount(10);
            // 还原秒杀数
            redisService.set(GoodsKey.miaoshaGoodsStock,"" + goodsVO.getId(),goodsVO.getStockCount());
            localOverMap.put(goodsVO.getId(),false);
            MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
            miaoshaGoods.setStockCount(10);
            miaoshaGoods.setGoodsId(goodsVO.getId());
            goodsService.resetSotck(miaoshaGoods);

            // 删除秒杀订单缓存
            boolean exist1 = redisService.exist(OrderKey.orderKey, "" + user.getMobile() + " " + goodsVO.getId());
            if (exist1) {
                redisService.delete(OrderKey.orderKey, "" + user.getMobile() + " " + goodsVO.getId());
            }
            // 删除秒杀完成缓存
            boolean exist2 = redisService.exist(MiaoshaKey.isOver,""+goodsVO.getId());
            if (exist2) {
                redisService.delete(MiaoshaKey.isOver, "" + goodsVO.getId());
            }
        }

        return Result.success("Success");

    }

}
