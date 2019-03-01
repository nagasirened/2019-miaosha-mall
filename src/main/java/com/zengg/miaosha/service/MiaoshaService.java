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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

    /**
     * 创建图片验证码
     * @param miaoshaUser
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId) {
        if ( goodsId < 0 || miaoshaUser == null ){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Graphics 操作图片的类
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));                         //设置画笔颜色
        g.fillRect(0, 0, width, height);                        //背景色填充
        // draw the border
        g.setColor(Color.black);                                     //重新设置画笔颜色
        g.drawRect(0, 0, width - 1, height - 1);   // 画矩形框
        Random rdm = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);                     // 五十个随机位置生成干扰点
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);                // 生成验证码
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);                       // 将验证码写在图片上
        g.dispose();                                                // 销毁画笔
        //把验证码存到redis中
        int rnd = calc(verifyCode);                                 // 计算验证码结果
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getMobile()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    public boolean checkVerifyCode(MiaoshaUser miaoshaUser, long goodsId, int verifyCode) {
        if(miaoshaUser == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getMobile()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getMobile()+","+goodsId);
        return true;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return -100;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
