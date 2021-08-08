package com.saimo.yygh.hosp.controller;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName DepartmentController.java
 * @Description
 * @createTime 2021年08月08日 18:01:00
 */
@RestController
@RequestMapping("admin/hosp/department")
@CrossOrigin
@Api(tags = "科室管理")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @ApiOperation(value = "科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable("hoscode") String hoscode) {
        return Result.ok(departmentService.getDeptList(hoscode));
    }

    
}
