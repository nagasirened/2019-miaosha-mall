package com.zengg.miaosha.controller;

import com.zengg.miaosha.config.exception.GlobalException;
import com.zengg.miaosha.model.vo.LoginVO;
import com.zengg.miaosha.service.LoginService;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: miaosha
 * @description: 登陆控制类
 * @author: ZengGuangfu
 * @create 2019-02-11 14:55
 */

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) throws GlobalException {
        /*if (loginVO.getPassword() == null ){
            return Result.error(CodeMsg.PASSWPRD_EMPTY);
        }
        if (loginVO.getMobile() == null){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }

        if (!ValidatorUtil.isMobile(loginVO.getMobile())){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }*/

        String token = loginService.doLogin(response, loginVO);
        return Result.success(token);

    }
}
