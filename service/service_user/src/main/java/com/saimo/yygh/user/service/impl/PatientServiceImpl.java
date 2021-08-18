package com.saimo.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saimo.hospital.cmnclient.DictFeignClient;
import com.saimo.yygh.enums.DictEnum;
import com.saimo.yygh.model.cmn.Dict;
import com.saimo.yygh.model.user.Patient;
import com.saimo.yygh.user.mapper.PatientMapper;
import com.saimo.yygh.user.service.PatientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName PatientServiceImpl.java
 * @Description
 * @createTime 2021年08月17日 22:51:00
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllUserId(Long userId) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Patient> list = baseMapper.selectList(queryWrapper);
        //远程调用cmn模块
        list.forEach(this::packAge);

        return list;
    }

    @Override
    public void packAge(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());//联系人证件
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
    }

    @Override
    public Patient getPatientId(Long id) {
        Patient patient = this.baseMapper.selectById(id);
        packAge(patient);
        return patient;
    }
}
