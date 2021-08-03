package com.saimo.yygh.hosp.controller.api;

import com.saimo.yygh.common.helper.HttpRequestHelper;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.hosp.service.HosptialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @ApiOperation(value = "上传医院")
    @PostMapping("/saveHospital")
    public Result<Void> saveHosp(HttpServletRequest request) {
        //获取医院信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        //
        Map<String, Object> map = HttpRequestHelper.switchMap(requestParameterMap);

        hosptialService.save(map);
        return Result.ok();
    }
}
