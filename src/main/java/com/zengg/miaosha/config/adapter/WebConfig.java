package com.zengg.miaosha.config.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @program: miaosha
 * @description: 全局配置
 * @author: ZengGuangfu
 * @create 2019-02-12 10:29
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    private NeedLoginInterceptor needLoginInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(needLoginInterceptor);
    }
}
