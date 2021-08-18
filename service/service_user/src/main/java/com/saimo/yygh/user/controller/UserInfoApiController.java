package com.saimo.yygh.user.controller;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.common.utils.AuthContextHolder;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.user.service.UserInfoService;
import com.saimo.yygh.vo.user.LoginVo;
import com.saimo.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName UserInfoApiController.java
 * @Description
 * @createTime 2021年08月12日 23:38:00
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("用户手机号登录接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }


    @ApiOperation("用户手机号登录接口")
    @PostMapping("/auth/userAuth")
    public Result<Void> userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        userInfoService.userAuth(userId, userAuthVo);
        return Result.ok();
    }

    @ApiOperation("根据用户id获取信息")
    @GetMapping("/auth/getUserInfo")
    public Result<UserInfo> getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        return Result.ok(userInfoService.getUserInfo(userId));
    }

}
