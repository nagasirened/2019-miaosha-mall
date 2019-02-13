package com.zengg.miaosha.config.exception;

import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        // e.printStackTrace();
        if (e instanceof GlobalException){
            GlobalException exception = (GlobalException)e;
            return Result.error(exception.getCm());
        }else if (e instanceof BindException){
            BindException bindException = (BindException)e;
            List<ObjectError> errorList = bindException.getAllErrors();
            ObjectError error = errorList.get(0);
            String message = error.getDefaultMessage();
            return Result.error(CodeMsg.VALIDATION_ERROR.fillmsg(message));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

}
