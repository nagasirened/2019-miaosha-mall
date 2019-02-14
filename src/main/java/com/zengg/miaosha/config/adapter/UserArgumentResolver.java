package com.zengg.miaosha.config.adapter;

import com.zengg.miaosha.model.MiaoshaUser;
import com.zengg.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: miaosha
 * @description:
 * @author: ZengGuangfu
 * @create 2019-02-12 10:32
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    /**
     * 如果方法参数类型含有 MiaoshaUser ，就返回true，执行下面的参数整合
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;

        // 查找参数中是否含有@RequestBody注解
        // return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    /**
     *
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(miaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,miaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isAllBlank(paramToken,cookieToken)){
            return null;
        }
        String token = StringUtils.isBlank(cookieToken) ?  paramToken : cookieToken;
        MiaoshaUser user = miaoshaUserService.getByToken(response, token);
        return user;
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
