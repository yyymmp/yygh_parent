package com.saimo.yygh.hosp.service;

import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.ScheduleQueryVo;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    void save(Map<String, Object> map);

    Page<Schedule> findPageSchedule(ScheduleQueryVo scheduleQueryVo, Integer page, Integer limit);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getScheduleId(String scheduleId);
}
