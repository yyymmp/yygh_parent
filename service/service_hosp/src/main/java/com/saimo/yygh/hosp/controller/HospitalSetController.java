package com.saimo.yygh.hosp.controller;

import com.saimo.yygh.hosp.service.HospitalSetService;
import com.saimo.yygh.model.hosp.HospitalSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName HospitalSetController.java
 * @Description
 * @createTime 2021年07月26日 23:49:00
 */
@RestController("admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        return hospitalSetService.list();
    }

    @DeleteMapping("{id}")
    public boolean removeHospitalSet(@PathVariable Long id) {
        return hospitalSetService.removeById(id);
    }
}
