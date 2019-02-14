package com.zengg.miaosha.service;


import com.zengg.miaosha.config.rabbitmq.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * Direct 模式测试
     * @param message
     */
    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.QUEUENAME,msg);
    }

    /**
     * Topic模式测试
     * @param message
     */
    public void sendTopic(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE,"topic.key1",msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE,"topic.key20",msg);
    }

    /**
     * Fanout模式测试
     * @param message
     */
    public void sendFanout(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(RabbitmqConfig.FANOUT_EXCHANGE,msg);
    }

    /**
     * Header模式测试
     * @param message
     */
    public void sendHeader(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        // header条件
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");
        //要发送的消息为message对象，使用MessageProperties进行匹配，绕过了routing_key
        Message ob = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(RabbitmqConfig.HEADER_EXCHANGE,"",ob);
    }

}
