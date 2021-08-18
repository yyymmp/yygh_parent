package com.saimo.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.model.acl.User;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.user.service.UserInfoService;
import com.saimo.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName UserController.java
 * @Description
 * @createTime 2021年08月18日 20:42:00
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    //添加就诊人
    @ApiOperation("用户列表")
    @PostMapping("/{page}/{limit}")
    public Result<IPage<UserInfo>> list(@PathVariable("page") long page
            , @PathVariable("limit") long limit
            , UserInfoQueryVo userInfoQueryVo
    ) {
        Page<UserInfo> page1 = new Page<>(page, limit);
        IPage<UserInfo> userInfoIPage = userInfoService.selectPage(page1,userInfoQueryVo);
        return Result.ok(userInfoIPage);
    }

    //用户锁定
    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId,@PathVariable Integer status) {
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    //用户详情
    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId) {
        Map<String,Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }

    //认证审批
    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,@PathVariable Integer authStatus) {
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }


}
