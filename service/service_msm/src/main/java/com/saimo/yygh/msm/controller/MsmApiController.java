package com.saimo.yygh.msm.controller;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.msm.service.MsmService;
import com.saimo.yygh.msm.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName MsmApiController.java
 * @Description
 * @createTime 2021年08月14日 16:00:00
 */
@RestController
@RequestMapping("/api/msm")
@Api(tags = "验证码")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送手机验证码
    @ApiOperation("发送手机验证码")
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        //redis中是否存有该手机号验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        //生成验证码
        String bitRandom = RandomUtil.getFourBitRandom();
        //
        boolean send = msmService.send(phone, bitRandom);
        if (send) {
            //2分钟有效
            redisTemplate.opsForValue().set(phone, bitRandom, 2, TimeUnit.MINUTES);
            return Result.ok(code);
        } else {
            return Result.fail().message("发送短信失败");
        }
    }
}
