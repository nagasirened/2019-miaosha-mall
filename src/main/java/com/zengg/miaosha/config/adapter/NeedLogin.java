package com.zengg.miaosha.config.adapter;

import java.lang.annotation.*;

/**
 * @program: miaosha
 * @description: 拦截器，表示必须登录
 * @author: ZengGuangfu
 * @create 2019-02-14 15:03
 */


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeedLogin {
}
