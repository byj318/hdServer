package com.huamai.hdServer.mapper;

import com.huamai.hdServer.domain.Schedule;
import com.huamai.hdServer.domain.ScreenVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/9 16:19
 * @Description:
 */
public interface ScheduleMapper extends Mapper<Schedule> {
    @Select(value = "select bedcode from bed where del_mark = 0 and hospital_id= #{hospitalId} order by right('0000000000'+bedcode,10)")
    List<String> getBedCode(@Param(value = "hospitalId") Long hospitalId);

    List<ScreenVO> getScreen1(@Param(value = "banci")String banci,@Param(value = "scheduleId")String scheduleId,@Param(value = "sequence")String sequence);
    List<ScreenVO> getScreen2(@Param(value = "banci")String banci,@Param(value = "date")String date,@Param(value = "sequence")String sequence);
    List<ScreenVO> getScreen3(@Param(value = "banci")String banci,@Param(value = "date")String date,@Param(value = "scheduleId")String scheduleId,@Param(value = "sequence")String sequence);
    @Select("select Description from DictionaryItem where DictionaryTableID = (select ID from DictionaryTable where name='通知字典') and del_mark = 0")
    String getNotice();
    @Select("select COUNT(id) from RowScheduleExec where ScheduleExec_ID = #{scheduleExecId}")
    Integer getCount(@Param(value = "scheduleExecId") String scheduleExecId);
    @Select("select id ScheduleExecId from ScheduleExec where convert(varchar(10),executetime,120)= #{time} AND DailyNum = #{dailyNum}")
    Integer getScheduleExecId(@Param(value = "time") String time,@Param(value = "dailyNum") Integer dailyNum);
}
