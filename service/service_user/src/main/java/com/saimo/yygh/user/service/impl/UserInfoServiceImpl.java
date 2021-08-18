package com.saimo.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saimo.yygh.common.exception.GlobalExceptionHandler;
import com.saimo.yygh.common.exception.HospitalException;
import com.saimo.yygh.common.helper.JwtHelper;
import com.saimo.yygh.common.result.ResultCodeEnum;
import com.saimo.yygh.enums.AuthStatusEnum;
import com.saimo.yygh.model.user.UserInfo;
import com.saimo.yygh.user.mapper.UserInforMapper;
import com.saimo.yygh.user.service.UserInfoService;
import com.saimo.yygh.vo.user.LoginVo;
import com.saimo.yygh.vo.user.UserAuthVo;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author clearlove
 * @ClassName UserInfoServiceImpl.java
 * @Description
 * @createTime 2021年08月12日 23:38:00
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInforMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        String code = loginVo.getCode();
        String phone = loginVo.getPhone();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(phone)) {
            throw new HospitalException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //验证验证码
        if (!Objects.equals(redisTemplate.opsForValue().get(phone), code)) {
            throw new HospitalException(ResultCodeEnum.CODE_ERROR);
        }
        UserInfo userInfo;
        //通过微信登录  绑定手机号
        //绑定手机号码
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            //此时已经在回调方法中写入了openid和用户信息
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if (null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new HospitalException(ResultCodeEnum.DATA_ERROR);
            }
        } else {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(queryWrapper);
            //第一次登录
            if (null == userInfo) {
                //添加到数据库
                //添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
            //校验是否被禁用
            if (userInfo.getStatus() == 0) {
                throw new HospitalException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
            }
        }

        //不是第一次 直接登录
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;

    }

    @Override
    public boolean save(UserInfo entity) {
        return this.baseMapper.insert(entity) > 0;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        return getById(userId);
    }
}
