package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.LoginUserDao;
import com.zengg.miaosha.model.LoginUser;
import com.zengg.miaosha.model.vo.LoginVO;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * @program: miaosha
 * @description: 登录服务类
 * @author: ZengGuangfu
 * @create 2019-02-11 15:15
 */
@Service
@Slf4j
public class LoginService {

    @Autowired
    private LoginUserDao loginUserDao;

    public LoginUser getByMobile(String mobile){
        List<LoginUser> loginUserList = loginUserDao.getByMobile(mobile);
        if (loginUserList != null && !loginUserList.isEmpty()){
            return loginUserList.get(0);
        }
        return null;
    }

    public CodeMsg doLogin(LoginVO loginVO){
        log.info(loginVO.toString());
        String mobile = loginVO.getMobile();
        LoginUser user = getByMobile(mobile);
        if (user == null){
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        String vaildDBpass = Md5Utils.formpassToDBpass(loginVO.getPassword(), user.getSalt());
        if (StringUtils.equals(vaildDBpass,user.getPassword())){
            return CodeMsg.SUCCESS;
        }else{
            return CodeMsg.PASSWORD_ERROR;
        }
    }
}
