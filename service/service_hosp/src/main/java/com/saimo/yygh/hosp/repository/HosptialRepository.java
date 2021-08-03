package com.saimo.yygh.hosp.repository;

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
public interface HosptialRepository extends MongoRepository<Hospital, String> {

    //getHosptialByHoscode 按照规范写 springdata会自动生成该方法
    Hospital getHospitalByHoscode(String hoscode);
}
