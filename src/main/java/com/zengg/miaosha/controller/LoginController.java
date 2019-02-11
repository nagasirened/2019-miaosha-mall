package com.zengg.miaosha.controller;

import com.zengg.miaosha.model.LoginUser;
import com.zengg.miaosha.model.vo.LoginVO;
import com.zengg.miaosha.service.LoginService;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Result;
import com.zengg.miaosha.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result<Boolean> doLogin(@Valid LoginVO loginVO){
        /*if (loginVO.getPassword() == null ){
            return Result.error(CodeMsg.PASSWPRD_EMPTY);
        }
        if (loginVO.getMobile() == null){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }

        if (!ValidatorUtil.isMobile(loginVO.getMobile())){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }*/

        CodeMsg codeMsg = loginService.doLogin(loginVO);
        if (codeMsg.getCode() == 0) {
            return Result.success(true);
        }else{
            return Result.error(codeMsg);
        }
    }
}
