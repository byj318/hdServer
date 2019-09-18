package com.huamai.hdServer.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.huamai.hdServer.domain.Grjcap;

/**
 * 感染监测安排 持久层
 *
 * @author byj
 */
@Mapper
public interface GrjcapMapper {

    @Update("update grjcap set is_delete = 1 where id = ${id}")
    void deleteDate(@Param(value = "id") BigDecimal id) throws Exception;

    void insertData(Grjcap grjcap) throws Exception;

    void updateData(Grjcap grjcap);

    List<Grjcap> getAll(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime,
                        @Param(value = "courtyardArea") String courtyardArea);

    List<String> queryDictionary(@Param(value = "dictionaryClassName") String dictionaryClassName,
                                 @Param(value = "dictionaryTableName") String dictionaryTableName);
}
