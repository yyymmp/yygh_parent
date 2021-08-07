package com.saimo.yygh.hosp.repository;

import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author clearlove
 * @ClassName HosptialRepository.java
 * @Description
 * @createTime 2021年08月03日 21:23:00
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    //根据Hoscode和Depcode获取Department  根据springData规范生成
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
