package com.saimo.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.hosp.HospitalSet;
import com.saimo.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {

    HospitalSet getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
