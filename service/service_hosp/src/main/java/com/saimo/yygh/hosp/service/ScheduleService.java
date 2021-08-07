package com.saimo.yygh.hosp.service;

import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.ScheduleQueryVo;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    void save(Map<String, Object> map);

    Page<Schedule> findPageSchedule(ScheduleQueryVo scheduleQueryVo, Integer page, Integer limit);

    void remove(String hoscode, String hosScheduleId);
}
