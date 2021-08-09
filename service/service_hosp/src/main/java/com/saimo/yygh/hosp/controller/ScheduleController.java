package com.saimo.yygh.hosp.controller;

import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.hosp.service.ScheduleService;
import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
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
@RequestMapping("admin/hosp/schedule")
@CrossOrigin
@Api(tags = "排班管理")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据医院编号与科室编号与工作日期查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(
            @PathVariable("hoscode") String hoscode
            , @PathVariable("depcode") String depcode
            , @PathVariable("workDate") String workDate
    ) {
        List<Schedule> schedules = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(schedules);
    }

    @ApiOperation(value = "根据医院编号与科室编号查询排班列表")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(
            @PathVariable("page") long page
            , @PathVariable("limit") long limit
            , @PathVariable("hoscode") String hoscode
            , @PathVariable("depcode") String depcode
    ) {
        Map<String, Object> map = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }


}
