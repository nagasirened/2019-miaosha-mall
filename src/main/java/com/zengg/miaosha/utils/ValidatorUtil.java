package com.zengg.miaosha.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: miaosha
 * @description: 验证工具类
 * @author: ZengGuangfu
 * @create 2019-02-11 16:01
 */

public class ValidatorUtil {

    private static final Pattern mobile_pattern= Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(mobile);
        return m.matches();
    }
}
