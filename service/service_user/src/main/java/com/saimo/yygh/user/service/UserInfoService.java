package com.saimo.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.vo.user.LoginVo;
import com.saimo.yygh.vo.user.UserAuthVo;
import com.saimo.yygh.vo.user.UserInfoQueryVo;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> loginUser(LoginVo loginVo);

    boolean save(UserInfo userInfo);

    UserInfo selectWxInfoOpenId(String openid);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    UserInfo getUserInfo(Long userId);

    IPage<UserInfo> selectPage(Page<UserInfo> page1, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String,Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
