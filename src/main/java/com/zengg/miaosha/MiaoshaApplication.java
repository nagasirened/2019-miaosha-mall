package com.zengg.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: miaosha
 * @description: 启动类
 * @author: ZengGuangfu
 * @create 2019-02-11 09:34
 */

@SpringBootApplication
@MapperScan(value = "com.zengg.miaosha.dao")
public class MiaoshaApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(MiaoshaApplication.class,args);
    }

}