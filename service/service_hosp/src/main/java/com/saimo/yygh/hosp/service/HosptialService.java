package com.saimo.yygh.hosp.service;

import com.saimo.yygh.model.hosp.Hospital;
import java.util.Map;

public interface HosptialService {

    void save(Map<String, Object> map);

    Hospital getHospitalSetByHoscode(String hoscode);
}
