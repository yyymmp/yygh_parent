package com.saimo.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.user.Patient;
import com.saimo.yygh.model.user.UserInfo;
import java.util.List;

public interface PatientService extends IService<Patient> {

    List<Patient> findAllUserId(Long userId);

    void packAge(Patient patient);

    Patient getPatientId(Long id);
}
