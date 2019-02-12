package com.zengg.miaosha.service;

import com.zengg.miaosha.config.exception.GlobalException;
import com.zengg.miaosha.config.redis.mould.realize.MiaoshaUserKey;
import com.zengg.miaosha.dao.LoginUserDao;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.vo.LoginVO;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Md5Utils;
import com.zengg.miaosha.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private LoginUserDao loginUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getByMobile(long mobile){
        List<MiaoshaUser> loginUserList = loginUserDao.getByMobile(mobile);
        if (loginUserList != null && !loginUserList.isEmpty()){
            return loginUserList.get(0);
        }
        return null;
    }

    public boolean doLogin(HttpServletResponse response,LoginVO loginVO) throws GlobalException {
        if (loginVO == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        log.info(loginVO.toString());
        String phone = loginVO.getMobile();
        long mobile = Long.valueOf(phone);
        MiaoshaUser user = getByMobile(mobile);
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String vaildDBpass = Md5Utils.formpassToDBpass(loginVO.getPassword(), user.getSalt());
        if (!StringUtils.equals(vaildDBpass,user.getPassword())){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtils.uuid();
        //登录成功
        addCookie(response,token,user);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token){
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (miaoshaUser != null){
            addCookie(response,token,miaoshaUser);
        }
        return miaoshaUser;
    }

    public void addCookie(HttpServletResponse response, String token , MiaoshaUser user){
        //登录成功
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        // cookie有效期这儿保持与redis一致
        int i = MiaoshaUserKey.token.expireSeconds();
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
