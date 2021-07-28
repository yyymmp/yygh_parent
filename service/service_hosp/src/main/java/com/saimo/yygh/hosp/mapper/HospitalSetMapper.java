package com.saimo.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saimo.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Repository;


@Repository
public interface HospitalSetMapper extends BaseMapper<HospitalSet> {
    //HospitalSet 是model包下面的 不能直接引入 已经在service的pom中引入了model包


}
