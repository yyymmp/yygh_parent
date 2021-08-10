package com.saimo.yygh.hosp.controller.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName HospApiController.java
 * @Description
 * @createTime 2021年08月10日 22:36:00
 */
@RestController
@RequestMapping("api/hosp/hospital")
@Api(tags = "Api")
public class HospApiController {

    @Autowired
    private HosptialService hosptialService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hosptialService.listHosp(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname) {
        List<Hospital> list = hosptialService.findByHosname(hosname);
        return Result.ok(list);
    }
}
