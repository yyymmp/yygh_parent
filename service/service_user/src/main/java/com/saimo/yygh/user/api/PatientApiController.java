package com.saimo.yygh.user.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.common.utils.AuthContextHolder;
import com.saimo.yygh.model.user.Patient;
import com.saimo.yygh.user.service.PatientService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.protocol.http.AuthenticationHeader;

/**
 * @author clearlove
 * @ClassName PatientApiController.java
 * @Description
 * @createTime 2021年08月17日 22:50:00
 */
@RestController("/api/user/patient")
@Slf4j
public class PatientApiController {

    @Autowired
    private PatientService patientService;


    @ApiOperation("获取就诊人列表")
    @GetMapping("/auth/findAll")
    public Result<List<Patient>> findAll(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    //添加就诊人
    @PostMapping("auth/save")
    public Result<Void> savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result<Patient> getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    //修改就诊人
    @PostMapping("auth/update")
    public Result<Void> updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    //删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result<Void> removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }


}
