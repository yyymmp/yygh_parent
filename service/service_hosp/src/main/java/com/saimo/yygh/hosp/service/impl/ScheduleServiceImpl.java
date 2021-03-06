package com.saimo.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saimo.yygh.common.exception.HospitalException;
import com.saimo.yygh.common.result.ResultCodeEnum;
import com.saimo.yygh.hosp.mapper.HospitalSetMapper;
import com.saimo.yygh.hosp.repository.ScheduleRespository;
import com.saimo.yygh.hosp.service.DepartmentService;
import com.saimo.yygh.hosp.service.HosptialService;
import com.saimo.yygh.hosp.service.ScheduleService;
import com.saimo.yygh.model.hosp.BookingRule;
import com.saimo.yygh.model.hosp.Department;
import com.saimo.yygh.model.hosp.Hospital;
import com.saimo.yygh.model.hosp.HospitalSet;
import com.saimo.yygh.model.hosp.Schedule;
import com.saimo.yygh.vo.hosp.BookingScheduleRuleVo;
import com.saimo.yygh.vo.hosp.ScheduleOrderVo;
import com.saimo.yygh.vo.hosp.ScheduleQueryVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName ScheduleServiceImpl.java
 * @Description
 * @createTime 2021???08???07??? 19:21:00
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRespository scheduleRespository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> map) {
        //map???json?????????
        String jsonString = JSONObject.toJSONString(map);
        //???json???????????????json??????
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);

        //???????????????????????????????????????
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
        //????????????page?????????????????????
        //????????????
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        //????????????
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);
        //CONTAINING?????????????????? ?????????????????????
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

    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode) {
        //??????workdate??????
        //????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),  //????????????
                Aggregation.group("workDate") //????????????
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //??????
                Aggregation.sort(Direction.DESC, "workDate"),
                //??????
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggRes = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggRes.getMappedResults();

        //????????????
        Aggregation totalAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),  //????????????
                Aggregation.group("workDate") //????????????
        );
        AggregationResults<BookingScheduleRuleVo> totalAggRes = mongoTemplate.aggregate(totalAggregation, Schedule.class, BookingScheduleRuleVo.class);
        int size = totalAggRes.getMappedResults().size();
        //????????????????????????
        for (BookingScheduleRuleVo mappedResult : mappedResults) {
            String dayOfWeek = getDayOfWeek(new DateTime(mappedResult.getDayOfWeek()));
            mappedResult.setDayOfWeek(dayOfWeek);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bookingScheduleRuleVoList", mappedResults);
        map.put("size", size);
        //??????????????????
        String hospName = hosptialService.getHospName(hoscode);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("hosname", hospName);
        map.put("baseMap", nameMap);
        return map;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {

        List<Schedule> schedules = scheduleRespository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        schedules.forEach(this::packageSchedule);
        return schedules;
    }

    //????????????
    private Schedule packageSchedule(Schedule schedule) {
        String hospName = hosptialService.getHospName(schedule.getHoscode());
        schedule.getParam().put("hostname", hospName);

        String depName = departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode());
        schedule.getParam().put("depName", depName);

        String dayOfWeek = getDayOfWeek(new DateTime(schedule.getWorkDate()));
        schedule.getParam().put("dayOfWeek", dayOfWeek);

        return schedule;
    }

    /**
     * ??????????????????????????????
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "??????";
            default:
                break;
        }
        return dayOfWeek;
    }


    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Map<String, Object> result = new HashMap<>();
        //??????????????????
        Hospital hospital = hosptialService.getHospitalByHoscode(hoscode);
        if (null == hospital) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        //??????????????????????????????
        IPage<Date> iPage = this.getListDate(page, limit, bookingRule);
        List<Date> dateList = iPage.getRecords();

        //????????????????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode)
                .and("workDate").in(dateList);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")

        );

        AggregationResults<BookingScheduleRuleVo> aggregateResult =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregateResult.getMappedResults();

        //????????????  map?????? key??????  value??????????????????????????????  list ???map
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().
                    collect(
                            Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
        //???????????????????????????
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);
            //???map????????????key????????????value???
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            //??????????????????????????????
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //??????????????????
                bookingScheduleRuleVo.setDocCount(0);
                //?????????????????????  -1????????????
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //????????????????????????????????????
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //?????????????????????????????????????????????   ?????? 0????????? 1??????????????? -1????????????????????????
            if (i == len - 1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //??????????????????????????????????????? ????????????
            if (i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    //????????????
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //???????????????????????????
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());

        //??????????????????
        Map<String, String> baseMap = new HashMap<>();
        //????????????
        baseMap.put("hosname", hosptialService.getHospName(hoscode));
        //??????
        Department department = departmentService.getDepartment(hoscode, depcode);
        //???????????????
        baseMap.put("bigname", department.getBigname());
        //????????????
        baseMap.put("depname", department.getDepname());
//???
        baseMap.put("workDateString", new DateTime().toString("yyyy???MM???"));
//????????????
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
//????????????
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }

    @Override
    public Schedule getScheduleId(String scheduleId) {
        return packageSchedule(scheduleRespository.findById(scheduleId).get());
    }

    private IPage<Date> getListDate(Integer page, Integer limit, BookingRule bookingRule) {
        //?????????????????? ????????? ??????
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());

        //????????????
        Integer cycle = bookingRule.getCycle();
        //????????????????????????????????? ??????????????????????????????
        if (releaseTime.isBeforeNow()) {
            cycle++;
        }
        //??????????????????????????? ????????????????????????
        List<Date> datesList = new ArrayList<>();
        for (int integer = 0; integer < cycle; integer++) {
            DateTime curDate = new DateTime().plusDays(cycle);
            String dateString = curDate.toString("yyyy-MM-dd");
            datesList.add(new DateTime(dateString).toDate());
        }
        //??????????????????????????????????????????????????????7??????????????????7?????????
        List<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        //??????????????????????????????7???????????????
        if (end > datesList.size()) {
            end = datesList.size();
        }
        for (int i = start; i < end; i++) {
            pageDateList.add(datesList.get(i));
        }
        //??????????????????????????????7???????????????
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, 7, datesList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    /**
     * ???Date?????????yyyy-MM-dd HH:mm????????????DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Schedule schedule = scheduleRespository.findById(scheduleId).get();

        //????????????????????????
        Hospital hospital = hosptialService.getHospitalByHoscode(schedule.getHoscode());

        if (null == hospital) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        //????????????????????????scheduleOrderVo
        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hosptialService.getHospName(schedule.getHoscode()));
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        BookingRule bookingRule = hospital.getBookingRule();
        //?????????????????????????????????????????????-1????????????0???
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //??????????????????
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //??????????????????
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //????????????????????????
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStartTime(startTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        scheduleRespository.save(schedule);
    }
}
