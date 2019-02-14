package com.zengg.miaosha.controller;

import com.mysql.jdbc.StringUtils;
import com.zengg.miaosha.config.redis.mould.realize.GoodsKey;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.vo.GoodsDetailVO;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.service.GoodsService;
import com.zengg.miaosha.service.RedisService;
import com.zengg.miaosha.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: miaosha
 * @description: 商品相关控制类
 * @author: ZengGuangfu
 * @create 2019-02-12 09:40
 */

@Controller
@RequestMapping(value = "/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 商品列表页面，做缓存处理，默认60s
     * TODO 将cookie或param获取参数注册到了HandlerMethodArgumentResolver中，
     * TODO 这样就可以直接在参数中获取到MiaoshaUser
     * @param request
     * @param response
     * @param model
     * @param user
     * @return*/
    @RequestMapping(value = "to_goodsList", produces = "text/html")
    @ResponseBody
    public String toGoodsList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user){
        // 1、取页面缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isNullOrEmpty(html)){
            return html;
        }

        // 2、手动渲染页面
        model.addAttribute("user",user);
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        model.addAttribute("goodsVOList",goodsVOList);

        // 手动渲染（如果缓存里面没有这个页面，就手动渲染,返回前先缓存起来）
        SpringWebContext springWebContext = new SpringWebContext(request,response,
                            request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",springWebContext);
        if (!StringUtils.isNullOrEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    /**
     * 页面详情，做缓存60s处理
     * @param request
     * @param response
     * @param id
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_detail/{id}",produces = "text/html")
    @ResponseBody
    public String toGoodsDetail(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("id") long id,MiaoshaUser user,Model model){
        // 1、取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+id, String.class);
        if (!StringUtils.isNullOrEmpty(html)){
            return html;
        }

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(id);
        // 获取秒杀状态和秒杀倒计时
        Map<String, Integer> statusAndRemain = goodsService.getStatusAndremainSeconds(goodsVO);

        model.addAttribute("miaoshaStatus",statusAndRemain.get("miaoshaStatus"));
        model.addAttribute("remainSeconds",statusAndRemain.get("remainSeconds"));
        model.addAttribute("goods",goodsVO);
        model.addAttribute("user",user);



        // 缓存没有，就手动渲染（如果缓存里面没有这个页面，就手动渲染,返回前先缓存起来）
        SpringWebContext springWebContext = new SpringWebContext(request,response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",springWebContext);
        if (!StringUtils.isNullOrEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+id,html);
        }
        return html;
    }

    /**
     * 详情页面静态化改造
     * @return
     */
    @RequestMapping(value = "/to_detail2/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> toDetail(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable("goodsId") long goodsId, MiaoshaUser user, Model model){


        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        // 获取秒杀状态和秒杀倒计时
        Map<String, Integer> statusAndRemain = goodsService.getStatusAndremainSeconds(goodsVO);

        GoodsDetailVO goodsDetailVO = new GoodsDetailVO(goodsVO, user,
                statusAndRemain.get("miaoshaStatus"), statusAndRemain.get("remainSeconds"));

        return Result.success(goodsDetailVO);
    }

}
