package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.saimo.hospital.cmnclient.DictFeignClient;
import com.saimo.yygh.hosp.repository.HosptialRepository;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.vo.hosp.HospitalQueryVo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private HosptialRepository hosptialRepsitory;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> map) {
        //map转json字符串
        String jsonString = JSONObject.toJSONString(map);
        //将json字符串转为json对象
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        //是否存在
        Hospital hospitalExist = hosptialRepsitory.getHospitalByHoscode(hospital.getHoscode());

        if (hospitalExist != null) {
            //已存在 更新
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hosptialRepsitory.save(hospital);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hosptialRepsitory.save(hospital);
        }
    }

    @Override
    public Hospital getHospitalSetByHoscode(String hoscode) {
        return hosptialRepsitory.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> listHosp(long page, long limit, HospitalQueryVo hospitalQueryVo) {
//同时传递page对象和条件对象
        //分页对象
        PageRequest pageRequest = PageRequest.of((int) page - 1, (int) limit);
        //条件对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        hospital.setStatus(0);
        hospital.setIsDeleted(0);
        //CONTAINING表示模糊查询 且不区分大小写
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, matcher);

        //todo 需要远程调用cnm接口
        Page<Hospital> all = hosptialRepsitory.findAll(example, pageRequest);
        all.getContent().forEach(hospital1 -> {
            //远程调用
            String hostypeString = dictFeignClient.getName("Hostype", hospital1.getHostype());
            String provinceString = dictFeignClient.getName(hospital1.getProvinceCode());
            String cityString = dictFeignClient.getName(hospital1.getCityCode());
            String districtString = dictFeignClient.getName(hospital1.getDistrictCode());

            hospital1.getParam().put("hostypeString", hostypeString);
            hospital1.getParam().put("fullAddress", provinceString + cityString + districtString);
        });

        return all;

    }

    @Override
    public void updateStatus(String id, int status) {
        Hospital hospital = hosptialRepsitory.findById(id).get();
        hospital.setUpdateTime(new Date());
        hospital.setStatus(status);
        hosptialRepsitory.save(hospital);
    }

    @Override
    public Hospital getHospById(String id) {
        Hospital hospital = hosptialRepsitory.findById(id).get();
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString);

        return hospital;
    }

    @Override
    public String getHospName(String hoscode) {
        return hosptialRepsitory.getHospitalByHoscode(hoscode).getHosname();
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hosptialRepsitory.findHospitalByHosnameLike(hosname);
    }
}
