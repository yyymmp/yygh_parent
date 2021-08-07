package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.saimo.yygh.hosp.repository.ScheduleRespository;
import com.saimo.yygh.hosp.service.ScheduleService;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.ScheduleQueryVo;
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
 * @ClassName ScheduleServiceImpl.java
 * @Description
 * @createTime 2021年08月07日 19:21:00
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRespository scheduleRespository;

    @Override
    public void save(Map<String, Object> map) {
        //map转json字符串
        String jsonString = JSONObject.toJSONString(map);
        //将json字符串转为json对象
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);

        //根据医院编号和科室编号查重
        Schedule exist = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (exist != null) {
            exist.setUpdateTime(new Date());
            exist.setIsDeleted(1);
            schedule.setStatus(1);
            scheduleRespository.save(exist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRespository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageSchedule(ScheduleQueryVo scheduleQueryVo, Integer page, Integer limit) {
        //同时传递page对象和条件对象
        //分页对象
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        //条件对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);
        //CONTAINING表示模糊查询 且不区分大小写
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);

        return scheduleRespository.findAll(example, pageRequest);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (null != schedule) {
            scheduleRespository.deleteById(schedule.getId());
        }
    }

}
