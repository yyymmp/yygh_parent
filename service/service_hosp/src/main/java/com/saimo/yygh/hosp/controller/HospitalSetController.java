package com.saimo.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.common.utils.MD5;
import com.saimo.yygh.hosp.service.HospitalSetService;
import com.saimo.yygh.model.hosp.HospitalSet;
import com.saimo.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import java.util.List;
import java.util.Random;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author clearlove
 * @ClassName HospitalSetController.java
 * @Description
 * @createTime 2021年07月26日 23:49:00
 */
@RestController
@RequestMapping("admin/hosp/hospitalSet")
@Api(tags = "医院设置管理")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;


    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        return Result.ok(hospitalSetService.list());
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result<Boolean> removeHospitalSet(@PathVariable Long id) {
        return Result.ok(hospitalSetService.removeById(id));
    }

    @ApiOperation(value = "分页查询医院设置")
    @PostMapping("/findPageHospitalSet/{current}/{limit}")
    public Result findPageHospitalSet(@PathVariable long current
            , @PathVariable long limit
            , @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //分页条件
        Page<HospitalSet> page = new Page<>(current, limit);
        //查询条件
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())) {
            queryWrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())) {
            queryWrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, queryWrapper);
        return Result.ok(pageHospitalSet);
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("/saveHospitalSet")
    public Result<Boolean> saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 1:可用 0:不可用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        String encApiulr = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setApiUrl(encApiulr);
        return Result.ok(hospitalSetService.save(hospitalSet));
    }

    @ApiOperation(value = "通过id获取医院设置")
    @GetMapping("/getHospitalSet/{id}")
    public Result<HospitalSet> getHospitalSet(@PathVariable Long id) {
        return Result.ok(hospitalSetService.getById(id));
    }

    @ApiOperation(value = "根据id更新")
    @GetMapping("/updateHospitalSet/{id}")
    public Result<Boolean> getHospitalSet(@RequestBody HospitalSet hospitalSet) {
        return Result.ok(hospitalSetService.updateById(hospitalSet));
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/batchRemove")
    public Result<Boolean> batchRemove(@RequestBody List<Long> ids) {
        return Result.ok(hospitalSetService.removeByIds(ids));
    }

    @ApiOperation(value = "医院锁定/解锁  ")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result<Boolean> batchRemove(@PathVariable Long id, @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        return Result.ok(hospitalSetService.updateById(hospitalSet));
    }

    @ApiOperation(value = "发送密钥")
    @PutMapping("/sendKey/{id}")
    public Result<Void> batchRemove(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        // TODO: 2021/7/29 发送密钥
        return Result.ok();
    }
}
