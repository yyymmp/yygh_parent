package com.saimo.yygh.hosp.service;

import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.vo.hosp.HospitalQueryVo;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface HosptialService {

    void save(Map<String, Object> map);

    Hospital getHospitalSetByHoscode(String hoscode);

    Page<Hospital> listHosp(long page, long limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, int status);

    Hospital getHospById(String id);
}
