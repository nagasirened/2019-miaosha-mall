package com.zengg.miaosha.service;

import com.zengg.miaosha.config.exception.GlobalException;
import com.zengg.miaosha.config.redis.mould.realize.MiaoshaUserKey;
import com.zengg.miaosha.dao.LoginUserDao;
import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.model.vo.LoginVO;
import com.zengg.miaosha.utils.CodeMsg;
import com.zengg.miaosha.utils.Md5Utils;
import com.zengg.miaosha.utils.Result;
import com.zengg.miaosha.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private LoginUserDao loginUserDao;

    @Autowired
    private RedisService redisService;

    /**
     * 获取登录对象的方法
     * @param mobile
     * @return
     */
    public MiaoshaUser getByMobile(long mobile){
        // 取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getByMobile, "" + mobile, MiaoshaUser.class);
        if (user != null ){
            return user;
        }
        // 缓存没有则取数据库，并缓存
        user = loginUserDao.getByMobile(mobile);
        if (user != null ){
            redisService.set(MiaoshaUserKey.getByMobile,"" + mobile, user);
        }
        return user;
    }

    /**
     * 用户修改密码
     * @param mobile
     * @param passwordNew
     * @return
     * @throws GlobalException
     */
    @Transactional
    public boolean updateMiaoshaUserPassword(long mobile,String passwordNew) throws GlobalException {
        // 取user
        MiaoshaUser oldUser = loginUserDao.getByMobile(mobile);
        if (oldUser == null ){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 更新数据库
        MiaoshaUser user = new MiaoshaUser();
        user.setMobile(mobile);
        user.setPassword(Md5Utils.formpassToDBpass(passwordNew,Md5Utils.SALT));
        int i = loginUserDao.updateMiaoshaUserPassword(user);
        if (i > 0){
            // 处理缓存,包括Cookie中的token
            redisService.delete(MiaoshaUserKey.getByMobile,"" + mobile);

            oldUser.setPassword(user.getPassword());
            redisService.set(MiaoshaUserKey.token,"" + mobile , oldUser);
        }
        return true;
    }

    /**
     * 登录方法，完成后添加Cookie以及token的缓存
     * @param response
     * @param loginVO
     * @return
     * @throws GlobalException
     */
    public String doLogin(HttpServletResponse response,LoginVO loginVO) throws GlobalException {
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
        return token;
    }

    /**
     * 查看缓存中改用户是否登录，用于分布式session
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response,String token){
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (miaoshaUser != null){
            addCookie(response,token,miaoshaUser);
        }
        return miaoshaUser;
    }

    /**
     * 添加用户信息到Cookie中
     * @param response
     * @param token
     * @param user
     */
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
