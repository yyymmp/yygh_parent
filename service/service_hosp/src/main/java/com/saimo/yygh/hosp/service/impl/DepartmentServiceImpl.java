package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.saimo.yygh.hosp.repository.DepartmentRepository;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.vo.hosp.DepartmentQueryVo;
import com.saimo.yygh.vo.hosp.DepartmentVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
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

    @Override
    public List<DepartmentVo> getDeptList(String hoscode) {
        List<DepartmentVo> res = new ArrayList<>();
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> of = Example.of(department);
        List<Department> all = departmentRepository.findAll(of);
        //转为树形
        //按照大科室编号分组
        Map<String, List<Department>> depMap = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Entry<String, List<Department>> stringListEntry : depMap.entrySet()) {
            String depcode = stringListEntry.getKey();
            List<Department> depList = stringListEntry.getValue();

            //大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(depcode);
            departmentVo.setDepname(depList.get(0).getBigname());
            //多个小科室
            List<DepartmentVo> child = new ArrayList<>();
            for (Department department1 : depList) {
                DepartmentVo departmentVo1 = new DepartmentVo();
                departmentVo1.setDepname(department1.getDepname());
                departmentVo1.setDepcode(department1.getDepcode());
                child.add(departmentVo1);
            }
            departmentVo.setChildren(child);
            res.add(departmentVo);
        }

        return res;
    }
}
