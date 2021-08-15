package com.saimo.yygh.user.service;

import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.vo.user.LoginVo;
import java.util.Map;

public interface UserInfoService {

    Map<String, Object> loginUser(LoginVo loginVo);

    boolean save(UserInfo userInfo);

    UserInfo selectWxInfoOpenId(String openid);
}
