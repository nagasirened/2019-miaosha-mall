package com.zengg.miaosha.controller;

import com.zengg.miaosha.config.redis.mould.realize.OrderKey;
import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.service.GoodsService;
import com.zengg.miaosha.service.MiaoshaService;
import com.zengg.miaosha.service.OrderService;
import com.zengg.miaosha.service.RedisService;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: miaosha
 * @description: 秒杀逻辑控制类
 * @author: ZengGuangfu
 * @create 2019-02-12 15:03
 */

@Controller
@RequestMapping(value = "/miaosha")
@Slf4j
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @PostMapping("/do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaosha1(Model model,MiaoshaUser miaoshaUser
                    ,@RequestParam("goodsId")long goodsId){
        if (miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        // 判断是否还有库存
        if (goodsVO.getStockCount() <= 0){
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        // 判断是否已经成功秒杀过了 从缓存中获取秒杀订单，因为这订单的存储时间没有限制
        MiaoshaOrder miaoshaOrder = orderService
                .getMisoshaOrderFromCache(miaoshaUser.getMobile(),goodsId);
        if (miaoshaOrder != null){
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_REPET.getMsg());
            return Result.error(CodeMsg.MIAOSHA_REPET);
        }

        // 开始秒杀
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser,goodsVO);
        return Result.success(orderInfo);
    }
}
