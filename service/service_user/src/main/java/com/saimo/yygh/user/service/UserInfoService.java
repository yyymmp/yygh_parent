package com.saimo.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.vo.user.LoginVo;
import com.saimo.yygh.vo.user.UserAuthVo;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> loginUser(LoginVo loginVo);

    boolean save(UserInfo userInfo);

    UserInfo selectWxInfoOpenId(String openid);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    UserInfo getUserInfo(Long userId);
}
