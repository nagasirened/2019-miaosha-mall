package com.zengg.miaosha.controller;

import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: miaosha
 * @description: 商品相关控制类
 * @author: ZengGuangfu
 * @create 2019-02-12 09:40
 */

@Controller
@RequestMapping(value = "goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * @param model
     * @param user
     * @return
     */
    /** TODO 将cookie或param获取参数注册到了HandlerMethodArgumentResolver中，
     *  TODO 这样就可以直接在参数中获取到MiaoshaUser */
    @RequestMapping(value = "to_goodsList")
    public String toGoodsList(Model model,MiaoshaUser user){
        model.addAttribute("user",user);
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        model.addAttribute("goodsVOList",goodsVOList);
        return "goods_list";
    }

    @RequestMapping(value = "/to_detail/{id}")
    public String toGoodsDetail(@PathVariable("id") long id,MiaoshaUser user,Model model){
        model.addAttribute("user",user);
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(id);
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

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goodsVO);
        return "goods_detail";
    }
}
