package com.saimo.yygh.msm.service;

import com.saimo.yygh.vo.msm.MsmVo;

/**
 * @author clearlove
 * @ClassName MsmService.java
 * @Description
 * @createTime 2021年08月14日 16:00:00
 */
public interface MsmService {

    public boolean send(String phone, String bitRandom);

    public boolean send(MsmVo msmVo);
}
