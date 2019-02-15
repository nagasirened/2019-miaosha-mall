package com.zengg.miaosha.service;


import com.zengg.miaosha.config.rabbitmq.RabbitmqConfig;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.vo.GoodsVO;
import com.zengg.miaosha.model.vo.MiaoshaMessageVO;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

@Service
public class RabbitmqReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaService miaoshaService;

    private static Logger log = LoggerFactory.getLogger(RabbitmqReceiver.class);

    @RabbitListener(queues = RabbitmqConfig.MIAOSHA_QUEUE)
    public void receiveMiaosha(String msg){
        MiaoshaMessageVO miaoshaMessageVO = RedisService.stringToBean(msg, MiaoshaMessageVO.class);
        long goodsId = miaoshaMessageVO.getGoodsId();
        MiaoshaUser miaoshaUser = miaoshaMessageVO.getMiaoshaUser();

        // 判断是否还有库存,从数据库中拿
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        if (goodsVO.getStockCount() <= 0){
            return;
        }

        // 开始减 库存和生成订单
        miaoshaService.miaosha(miaoshaUser,goodsVO);
    }


    /**
     * Derict模式接收测试  演示代码
     * @param message
     */
    //@RabbitListener(queues = RabbitmqConfig.QUEUENAME)
    public void receive(String message){
        log.info("receive message: " + message);
    }

    /**
     * 主题模式和广播模式的接收测试  演示代码
     * @param message
     */
    //@RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUENAME_1)
    public void receiveTopic1(String message){
        log.info("topicQueue 1 reveive: " + message);
    }

    //@RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUENAME_2)
    public void receiveTopic2(String message){
        log.info("topicQueue 2 reveive: " + message);
    }

    /**
     * Header模式发送和接收都是字节数组  演示代码
     * @param message
     */
    //@RabbitListener(queues = RabbitmqConfig.HEADER_QUEUENAME)
    public void receiveHeader(byte[] message){
        log.info("headerQueue reveive: " + new String(message));
    }

}
