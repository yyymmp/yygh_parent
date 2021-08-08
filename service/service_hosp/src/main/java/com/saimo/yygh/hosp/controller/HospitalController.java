package com.saimo.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saimo.yygh.common.exception.HospitalException;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.common.result.ResultCodeEnum;
import com.saimo.yygh.common.utils.MD5;
import com.saimo.yygh.hosp.service.HospitalSetService;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.model.hosp.HospitalSet;
import com.saimo.yygh.vo.hosp.HospitalQueryVo;
import com.saimo.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName HospitalSetController.java
 * @Description
 * @createTime 2021年07月26日 23:49:00
 */
@RestController
@RequestMapping("admin/hosp/hospital")
@CrossOrigin
@Api(tags = "医院设置管理")
public class HospitalController {

    @Autowired
    private HosptialService hosptialService;

    @ApiOperation(value = "医院列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(
            @PathVariable long page,
            @PathVariable long limit,
            HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hosptialService.listHosp(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }
}
