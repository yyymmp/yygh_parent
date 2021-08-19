package com.saimo.yygh.hosp.service;

import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.vo.hosp.DepartmentQueryVo;
import com.saimo.yygh.vo.hosp.DepartmentVo;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface DepartmentService {

    void save(Map<String, Object> map);

    Page<Department> findPageDepartment(DepartmentQueryVo departmentVo, Integer page, Integer limit);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> getDeptList(String hoscode);

    String getDepName(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    Department getDepartment(String hoscode, String depcode);
}
