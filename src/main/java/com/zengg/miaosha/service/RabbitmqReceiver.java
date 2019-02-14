package com.zengg.miaosha.service;


import com.zengg.miaosha.config.rabbitmq.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

@Service
public class RabbitmqReceiver {

    private static Logger log = LoggerFactory.getLogger(RabbitmqReceiver.class);

    /**
     * Derict模式接收测试
     * @param message
     */
    @RabbitListener(queues = RabbitmqConfig.QUEUENAME)
    public void receive(String message){
        log.info("receive message: " + message);
    }

    /**
     * 主题模式和广播模式的接收测试
     * @param message
     */
    @RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUENAME_1)
    public void receiveTopic1(String message){
        log.info("topicQueue 1 reveive: " + message);
    }

    @RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUENAME_2)
    public void receiveTopic2(String message){
        log.info("topicQueue 2 reveive: " + message);
    }

    /**
     * Header模式发送和接收都是字节数组
     * @param message
     */
    @RabbitListener(queues = RabbitmqConfig.HEADER_QUEUENAME)
    public void receiveHeader(byte[] message){
        log.info("headerQueue reveive: " + new String(message));
    }

}
