package com.zengg.miaosha.config.adapter;

import com.zengg.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 有权限应返回 true 放行,执行 controller
 * 没有权限返回 false,拒绝后续执行
 * @program: miaosha
 * @description: 登录权限拦截器
 * @author: ZengGuangfu
 * @create 2019-02-14 15:12
 */

@Component
public class NeedLoginInterceptor implements HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 获取方法上的注解
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            NeedLogin needLogin = handlerMethod.getMethod().getAnnotation(NeedLogin.class);
            // 如果方法上的注解为空 则获取类的注解
            if (needLogin == null) {
                needLogin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NeedLogin.class);
            }

            // 如果标记了注解，则判断权限
            if ( needLogin != null ) {
                String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
                String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
                if (StringUtils.isAllBlank(paramToken,cookieToken)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 从cookie中获取token的值
     * @param request
     * @param cookieName
     * @return
     */
    public String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(),cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
