package com.saimo.yygh.hosp.repository;

import com.saimo.yygh.model.hosp.Schedule;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRespository extends MongoRepository<Schedule, String> {

    //根据springdata规范生成
    Schedule getScheduleByHoscodeAndDepcode(String hoscode, String depcode);

    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
