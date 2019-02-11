package com.zengg.miaosha.config.exception;

import com.zengg.miaosha.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

/**
 * @program: miaosha
 * @description: 全局异常拦截类
 * @author: ZengGuangfu
 * @create 2019-02-11 16:59
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        if (e instanceof BindException){

        }
        return null;
    }
}
