package com.saimo.yygh.user.client;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//远程调用的服务名
@FeignClient("service-user")
@Repository
public interface PatientFeignClient {

    @GetMapping("/api/user/patient/auth/get/{id}")
    public Result<Patient> getPatient(@PathVariable("id") Long id);

}
