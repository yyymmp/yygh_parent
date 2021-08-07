package com.saimo.yygh.hosp.controller.api;

import com.saimo.yygh.common.exception.HospitalException;
import com.saimo.yygh.common.helper.HttpRequestHelper;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.common.result.ResultCodeEnum;
import com.saimo.yygh.common.utils.MD5;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.hosp.service.HospitalSetService;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.hosp.service.ScheduleService;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.model.hosp.HospitalSet;
import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.DepartmentQueryVo;
import com.saimo.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName ApiController.java
 * @Description
 * @createTime 2021年08月03日 21:27:00
 */
@RestController
@RequestMapping("api/hosp")
@Api(tags = "Api")
public class ApiController {

    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);

        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation(value = "删除排班")
    @PostMapping("/remove")
    public Result<Void> removeSchedule(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }

    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result<Void> saveSchedule(HttpServletRequest request) {
        //获取科室信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        scheduleService.save(map);
        return Result.ok();
    }

    @ApiOperation(value = "查询排班")
    @PostMapping("/schedule/list")
    public Result<Page<Schedule>> findSchedule(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);

        //医院编号
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        Integer page = !StringUtils.isEmpty(map.get("page")) ? Integer.parseInt((String) map.get("page")) : 1;
        Integer limit = !StringUtils.isEmpty(map.get("limit")) ? Integer.parseInt((String) map.get("limit")) : 10;
        //签名校验省略
        //封装查询条件
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageList = scheduleService.findPageSchedule(scheduleQueryVo, page, limit);
        return Result.ok(pageList);
    }


    @ApiOperation(value = "查询科室")
    @PostMapping("/department/list")
    public Result<Page<Department>> findDepartment(HttpServletRequest request) {
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        //医院编号
        String hoscode = (String) map.get("hoscode");
        Integer page = !StringUtils.isEmpty(map.get("page")) ? Integer.parseInt((String) map.get("page")) : 1;
        Integer limit = !StringUtils.isEmpty(map.get("limit")) ? Integer.parseInt((String) map.get("limit")) : 10;
        //签名校验省略
        //封装查询条件
        DepartmentQueryVo departmentVo = new DepartmentQueryVo();
        departmentVo.setHoscode(hoscode);
        Page<Department> pageList = departmentService.findPageDepartment(departmentVo, page, limit);
        return Result.ok(pageList);
    }


    @ApiOperation(value = "上传科室")
    @PostMapping("/saveDepartment")
    public Result<Void> saveDepartment(HttpServletRequest request) {
        //获取科室信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        departmentService.save(map);
        return Result.ok();
    }

    @ApiOperation(value = "查询接口")
    @PostMapping("/hospital/show")
    public Result<Hospital> getHospital(HttpServletRequest request) {
        //获取医院信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        //验证签名
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        //获取hospcode
        String hoscode = (String) map.get("hoscode");
        //从mongo中获取
        Hospital hospital = hosptialService.getHospitalSetByHoscode(hoscode);

        return Result.ok(hospital);
    }

    @ApiOperation(value = "上传医院")
    @PostMapping("/saveHospital")
    public Result<Void> saveHosp(HttpServletRequest request) {
        //获取医院信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);
        //验证签名
        String hoscode = (String) map.get("hoscode");
        HospitalSet hospitalSet = hospitalSetService.getSignKey(hoscode);
        String sign = (String) map.get("sign");
        if (!MD5.encrypt(hospitalSet.getSignKey()).equals(sign)) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
        //处理+=>" "
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        map.put("logoData", logoData);
        hosptialService.save(map);
        return Result.ok();
    }
}
