package com.saimo.yygh.user.service;

import com.saimo.yygh.vo.user.LoginVo;
import java.util.Map;

public interface UserInfoService {

    Map<String, Object> loginUser(LoginVo loginVo);
}
