package com.saimo.yygh.common.utils;

import com.saimo.yygh.common.helper.JwtHelper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;

/**
 * @author clearlove
 * @ClassName AuthContextHolder.java
 * @Description 获取当前用户信息
 * @createTime 2021年08月17日 21:56:00
 */
public class AuthContextHolder {

    public static Long getUserId(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getParameter("token");
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    public static String getUserName(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getParameter("token");
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
