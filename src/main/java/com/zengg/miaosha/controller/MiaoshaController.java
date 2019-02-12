package com.zengg.miaosha.controller;

import com.zengg.miaosha.model.MiaoshaOrder;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.OrderInfo;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.service.GoodsService;
import com.zengg.miaosha.service.MiaoshaService;
import com.zengg.miaosha.service.OrderSrevice;
import com.zengg.miaosha.utils.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: miaosha
 * @description: 秒杀逻辑控制类
 * @author: ZengGuangfu
 * @create 2019-02-12 15:03
 */

@Controller
@RequestMapping(value = "/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderSrevice orderSrevice;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId){
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        // 判断是否还有库存
        if (goodsVO.getStockCount() <= 0){
            model.addAttribute("errorMsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_filed";
        }

        MiaoshaOrder miaoshaOrder = orderSrevice.getMisoshaOrderByUserIdAndGoodsId(user.getMobile(),goodsId);
        if (miaoshaOrder != null){
            // 判断是否已经成功秒杀过了
            if (goodsVO.getStockCount() <= 0){
                model.addAttribute("errorMsg", CodeMsg.MIAOSHA_REPET.getMsg());
                return "miaosha_filed";
            }
        }

        // 1、减库存 2、下订单  3、写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);
        return "order_detail";
    }
}
