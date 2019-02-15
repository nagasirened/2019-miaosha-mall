package com.zengg.miaosha.config.rabbitmq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * Rabbitmq的配置文件
 */

@Configuration
public class RabbitmqConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";

    public static final String QUEUENAME = "queue";
    public static final String TOPIC_QUEUENAME_1 = "topic.queue1";
    public static final String TOPIC_QUEUENAME_2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String ROUNTING_KEY1 = "topic.key1";
    public static final String ROUNTING_KEY2 = "topic.#";

    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    public static final String HEADER_QUEUENAME = "header.queue";
    public static final String HEADER_EXCHANGE = "header.exchange";


    /**
     * Direct 模式
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUENAME,true);
    }

    /**
     * Topic 模式
     * @return
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUENAME_1,true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUENAME_2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUNTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUNTING_KEY2);
    }

    /**
     * 广播模式fanout
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }


    /**
     * Header模式
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADER_EXCHANGE);
    }

    @Bean
    public Queue headerQueue(){
        return new Queue(HEADER_QUEUENAME,true);
    }

    @Bean
    public Binding headerBinding(){
        Map<String,Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        // whereAll 表示同时满足两个条件才行
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
        //return BindingBuilder.bind(headerQueue()).to(headersExchange()).where((String)map.get("header1")).exists();
    }
}
