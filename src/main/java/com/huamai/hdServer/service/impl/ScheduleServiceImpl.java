package com.huamai.hdServer.service.impl;

import com.huamai.hdServer.domain.Schedule;
import com.huamai.hdServer.domain.ScreenVO;
import com.huamai.hdServer.mapper.ScheduleMapper;
import com.huamai.hdServer.service.ScheduleService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.vo.OutDataVO;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/9 16:43
 * @Description:
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public OutDataVO getDataAjax(Long hospitalId) {
        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Schedule schedule = new Schedule();
            schedule.setCurrentMark(1).setHospitalId(hospitalId);
            Schedule schedule1 = scheduleMapper.select(schedule).get(0);
            String paiban = "";
            String scheduleId = String.valueOf(schedule1.getId());
            long seq = getDateSeq(Calendar.getInstance(), schedule1);

            if(hospitalId == 1){
                Integer scheduleExecId = scheduleMapper.getScheduleExecId(date, 2);
                if(scheduleExecId == null){
                    paiban = "上午班";
                }else{
                    Integer count = scheduleMapper.getCount(scheduleExecId+"");
                    if(count == 0 || count == 1){
                        paiban = "上午班";
                    }else if (count == 2){
                        paiban = "下午班";
                        seq += 1;
                    }
                }
            }else if(hospitalId == 2){
                Integer scheduleExecId = scheduleMapper.getScheduleExecId(date, 3);
                if(scheduleExecId == null){
                    paiban = "上午班";
                }else{
                    Integer count = scheduleMapper.getCount(scheduleExecId+"");
                    if(count == 0 || count == 1){
                        paiban = "上午班";
                    }else if (count == 2){
                        paiban = "下午班";
                        seq += 1;
                    }
                }
            }

            String sequence = seq + "";

            LinkedBlockingQueue<Runnable> objects = new LinkedBlockingQueue<Runnable>(3);
            ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 3, 3L, TimeUnit.SECONDS, objects);
            executorService.prestartAllCoreThreads();
            //data1
            Future<List<ScreenVO>> data1 = executorService.submit(() -> {
                return scheduleMapper.getScreen1("1", scheduleId, sequence);
            });
            //data2
            Future<List<ScreenVO>> data2 = executorService.submit(() -> {
                return scheduleMapper.getScreen2("1", date, sequence);
            });
            //data3
            Future<List<ScreenVO>> data3 = executorService.submit(() -> {
                return scheduleMapper.getScreen3("1", date, scheduleId, sequence);
            });
            List<ScreenVO> screenVOS = data1.get();
            List<ScreenVO> screenVOS1 = data2.get();
            List<ScreenVO> screenVOS2 = data3.get();
            if (!CollectionUtils.isEmpty(screenVOS1)) {
                Map<String, ScreenVO> collect = screenVOS.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                Map<String, ScreenVO> collect2 = screenVOS1.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                collect.putAll(collect2);
                screenVOS = new ArrayList<>(collect.values());
            }
            if (!CollectionUtils.isEmpty(screenVOS2)) {
                Map<String, ScreenVO> collect = screenVOS.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                Map<String, ScreenVO> collect2 = screenVOS2.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                collect.putAll(collect2);
                screenVOS = new ArrayList<>(collect.values());
            }
            Pattern pattern = Pattern.compile("[0-9]*");
            Set<ScreenVO> ts = new TreeSet<ScreenVO>(new Comparator<ScreenVO>() {

                @Override
                public int compare(ScreenVO o1, ScreenVO o2) {
                    Matcher isNum = pattern.matcher(o1.getBedcode());
                    Matcher isNum2 = pattern.matcher(o2.getBedcode());
                    if( isNum.matches() && isNum2.matches()){
                        return Integer.parseInt(o1.getBedcode())-Integer.parseInt(o2.getBedcode());
                    }
                    int length1 = o1.getBedcode().length();
                    int length2 = o2.getBedcode().length();
                    int char1 = o1.getBedcode().charAt(0);
                    int char2 = o2.getBedcode().charAt(0);
                    if(char1 > char2) {
                        return 1;
                    }else if(char1 < char2) {
                        return -1;
                    }else {
                        String msg1 = o1.getBedcode().substring(1, length1);
                        String msg2 = o2.getBedcode().substring(1, length2);
                        if(msg1.startsWith("+")) {
                            return 1;
                        }
                        if(msg2.startsWith("+")) {
                            return -1;
                        }
                        return Integer.parseInt(msg1)-Integer.parseInt(msg2);
                    }
                }

            });
            OutDataVO outDataVO = new OutDataVO();
            ts.addAll(screenVOS);
            outDataVO.setBanci(paiban).setRiqi(date).setData(ListUtils.partition(ts.stream().collect(Collectors.toList()), 11)).setNotice(scheduleMapper.getNotice());
            return outDataVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //	 得到当前日期相对于起始日期的序号
    private long getDateSeq(Calendar curentDate, Schedule tmpSchedule)
    {
        long between = DateUtil.getDistanceOfDates_edit(getStartDate(
                tmpSchedule, curentDate), curentDate);
        if (between < 0)
        {
            return 0;
        }
        long seq = between * tmpSchedule.getDailyNum() + 1;

        return seq;
    }

    @Override
    public void  getData(Long hospitalId) {
        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Schedule schedule = new Schedule();
            schedule.setCurrentMark(1).setHospitalId(hospitalId);
            Schedule schedule1 = scheduleMapper.select(schedule).get(0);
            String scheduleId = String.valueOf(schedule1.getId());
            long seq = getDateSeq(Calendar.getInstance(), schedule1);
            String paiban = "";

            if(hospitalId == 1){
                Integer scheduleExecId = scheduleMapper.getScheduleExecId(date, 2);
                if(scheduleExecId == null){
                    paiban = "上午班";
                }else{
                    Integer count = scheduleMapper.getCount(scheduleExecId+"");
                    if(count == 0 || count == 1){
                        paiban = "上午班";
                    }else if (count == 2){
                        paiban = "下午班";
                        seq += 1;
                    }
                }
            }else if(hospitalId == 2){
                Integer scheduleExecId = scheduleMapper.getScheduleExecId(date, 3);
                if(scheduleExecId == null){
                    paiban = "上午班";
                }else{
                    Integer count = scheduleMapper.getCount(scheduleExecId+"");
                    if(count == 0 || count == 1){
                        paiban = "上午班";
                    }else if (count == 2){
                        paiban = "下午班";
                        seq += 1;
                    }
                }
            }

            String sequence = seq + "";

            LinkedBlockingQueue<Runnable> objects = new LinkedBlockingQueue<Runnable>(3);
            ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 3, 3L, TimeUnit.SECONDS, objects);
            executorService.prestartAllCoreThreads();
            //data1
            Future<List<ScreenVO>> data1 = executorService.submit(() -> {
                return scheduleMapper.getScreen1("1", scheduleId, sequence);
            });
            //data2
            Future<List<ScreenVO>> data2 = executorService.submit(() -> {
                return scheduleMapper.getScreen2("1", date, sequence);
            });
            //data3
            Future<List<ScreenVO>> data3 = executorService.submit(() -> {
                return scheduleMapper.getScreen3("1", date, scheduleId, sequence);
            });
            List<ScreenVO> screenVOS = data1.get();
            List<ScreenVO> screenVOS1 = data2.get();
            List<ScreenVO> screenVOS2 = data3.get();
            if (!CollectionUtils.isEmpty(screenVOS1)) {
                Map<String, ScreenVO> collect = screenVOS.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                Map<String, ScreenVO> collect2 = screenVOS1.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                collect.putAll(collect2);
                screenVOS = new ArrayList<>(collect.values());
            }
            if (!CollectionUtils.isEmpty(screenVOS2)) {
                Map<String, ScreenVO> collect = screenVOS.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                Map<String, ScreenVO> collect2 = screenVOS2.stream().collect(Collectors.toMap(ScreenVO::getBedcode, ScreenVO -> ScreenVO));
                collect.putAll(collect2);
                screenVOS = new ArrayList<>(collect.values());
            }
            Pattern pattern = Pattern.compile("[0-9]*");
            Set<ScreenVO> ts = new TreeSet<ScreenVO>(new Comparator<ScreenVO>() {

                @Override
                public int compare(ScreenVO o1, ScreenVO o2) {
                    Matcher isNum = pattern.matcher(o1.getBedcode());
                    Matcher isNum2 = pattern.matcher(o2.getBedcode());
                    if( isNum.matches() && isNum2.matches()){
                        return Integer.parseInt(o1.getBedcode())-Integer.parseInt(o2.getBedcode());
                    }
                    int length1 = o1.getBedcode().length();
                    int length2 = o2.getBedcode().length();
                    int char1 = o1.getBedcode().charAt(0);
                    int char2 = o2.getBedcode().charAt(0);
                    if(char1 > char2) {
                        return 1;
                    }else if(char1 < char2) {
                        return -1;
                    }else {
                        String msg1 = o1.getBedcode().substring(1, length1);
                        String msg2 = o2.getBedcode().substring(1, length2);
                        if(msg1.startsWith("+")) {
                            return 1;
                        }
                        if(msg2.startsWith("+")) {
                            return -1;
                        }
                        return Integer.parseInt(msg1)-Integer.parseInt(msg2);
                    }
                }

            });
            OutDataVO outDataVO = new OutDataVO();
            ts.addAll(screenVOS);
            outDataVO.setBanci(paiban).setRiqi(date).setData(ListUtils.partition(ts.stream().collect(Collectors.toList()), 11)).setNotice(scheduleMapper.getNotice());
            if(hospitalId == 1){
                simpMessagingTemplate.convertAndSend("/topic/game_chat/new", outDataVO);
            }else if(hospitalId == 2){
                simpMessagingTemplate.convertAndSend("/topic/game_chat/old", outDataVO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //	得到当前大排班下的相对于任一日期的起始日期
    private Calendar getStartDate(Schedule tmpSchedule, Calendar date)
    {
        Calendar tmpdate = (Calendar) date.clone();
        if (tmpSchedule != null)
        {
            Calendar start = Calendar.getInstance();
            start.setTime(tmpSchedule.getStartTime());
            if (tmpdate.after(start))
            {
                // 得到当前日期和起始日期之间的天数
                long betweenDays = DateUtil.getDistanceOfDates_edit(start,
                        tmpdate);

                // 得到一个排班周期的天数
                long days = tmpSchedule.getFrequence();

                if (betweenDays < days)
                {
                    tmpdate = start;
                }
                else
                {
                    if (betweenDays % days == 0)
                    {
                        tmpdate = date;
                    }
                    else
                    {
                        tmpdate.add(Calendar.DATE, -(int) (betweenDays % days));
                    }
                }
            }
            else
            {
                tmpdate = start;
            }
        }
        return tmpdate;
    }
}
