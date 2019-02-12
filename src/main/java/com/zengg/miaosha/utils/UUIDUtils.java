package com.zengg.miaosha.utils;

import java.util.UUID;

/**
 * @program: miaosha
 * @description: UUID工具类
 * @author: ZengGuangfu
 * @create 2019-02-12 09:27
 */
public class UUIDUtils {

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
