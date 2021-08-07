package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.saimo.yygh.hosp.repository.DepartmentRepository;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.vo.hosp.DepartmentQueryVo;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName DepartmentServiceImpl.java
 * @Description
 * @createTime 2021年08月04日 23:25:00
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> map) {
        //map转json字符串
        String jsonString = JSONObject.toJSONString(map);
        //将json字符串转为json对象
        Department department = JSONObject.parseObject(jsonString, Department.class);

        //根据医院编号和科室编号查重
        Department exist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (exist != null) {
            exist.setUpdateTime(new Date());
            exist.setIsDeleted(1);
            departmentRepository.save(exist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(DepartmentQueryVo departmentVo, Integer page, Integer limit) {
        //同时传递page对象和条件对象
        //分页对象
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        //条件对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentVo, department);
        //CONTAINING表示模糊查询 且不区分大小写
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);

        return departmentRepository.findAll(example, pageRequest);
    }

    @Override
    public void remove(String hoscode, String depcode) {
        //查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }

    }
}
