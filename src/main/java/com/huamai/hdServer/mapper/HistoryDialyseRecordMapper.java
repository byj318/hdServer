package com.huamai.hdServer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 旭燃
 * @Date: 2019/9/16 17:07
 * @Description: 并发症统计
 */
@Mapper
public interface HistoryDialyseRecordMapper {

    List<String> statisticsBfz(@PathVariable(value = "startTime") String startTime, @PathVariable(value = "endTime") String endTime, @PathVariable(value = "courtyardArea") String courtyardArea);

    @Select("SELECT DISTINCT ItemName FROM DictionaryItem WHERE DictionaryTableID = 202 AND Description = '护士' AND Del_Mark = 0")
    List<String> getDictory();

    @Select("SELECT DISTINCT ItemName title,ItemName data FROM DictionaryItem WHERE DictionaryTableID = 202 AND Description = '护士' AND Del_Mark = 0")
    List<Map<String, String>> getDictoryToTableHeader();

    List<Map<String, Object>> listPatientInfo(@PathVariable(value = "startTime") String startTime, @PathVariable(value = "endTime") String endTime, @PathVariable(value = "courtyardArea") String courtyardArea, @PathVariable(value = "type") String type, @PathVariable(value = "dictoryList") List<String> dictoryList);

    List<Map<String, Object>> statisticsYsjb(String startTime, String endTime, String courtyardArea);

    List<Map<String, Object>> statisticsHsjb(String startTime, String endTime, String courtyardArea);
}
