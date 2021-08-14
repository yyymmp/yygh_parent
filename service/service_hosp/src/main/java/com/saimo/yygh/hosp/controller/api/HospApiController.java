package com.saimo.yygh.hosp.controller.api;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.vo.hosp.DepartmentVo;
import com.saimo.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
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
@Api(tags = "前台接口")
public class HospApiController {

    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result<Page<Hospital>> findHospList(@PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hosptialService.listHosp(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    public Result<List<Hospital>> findByHosName(@PathVariable String hosname) {
        List<Hospital> list = hosptialService.findByHosname(hosname);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDeptList(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取医院预约挂号详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode) {
        Map<String, Object> map = hosptialService.item(hoscode);
        return Result.ok(map);
    }

}
