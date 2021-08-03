package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.saimo.yygh.hosp.repository.HosptialRepository;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.model.hosp.Hospital;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName HosptialServiceImpl.java
 * @Description
 * @createTime 2021年08月03日 21:25:00
 */
@Service
public class HosptialServiceImpl implements HosptialService {

    @Autowired
    private HosptialRepository hosptialRepository;

    @Override
    public void save(Map<String, Object> map) {
        //map转json字符串
        String jsonString = JSONObject.toJSONString(map);
        //将json字符串转为json对象
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        //是否存在
        Hospital hospitalExist = hosptialRepository.getHospitalByHoscode(hospital.getHoscode());

        if (hospitalExist != null) {
            //已存在 更新
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hosptialRepository.save(hospital);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hosptialRepository.save(hospital);
        }
    }
}
