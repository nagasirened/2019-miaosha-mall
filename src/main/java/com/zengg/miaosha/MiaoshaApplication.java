package com.zengg.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: miaosha
 * @description: 启动类
 * @author: ZengGuangfu
 * @create 2019-02-11 09:34
 */

@SpringBootApplication
public class MiaoshaApplication /*extends SpringBootServletInitializer*/{

    public static void main(String[] args) throws Exception{
        SpringApplication.run(MiaoshaApplication.class,args);
    }

    /* 打war的时候用
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MiaoshaApplication.class);
    }*/
}
