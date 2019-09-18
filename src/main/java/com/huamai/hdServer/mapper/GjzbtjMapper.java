package com.huamai.hdServer.mapper;

import com.huamai.hdServer.domain.Gjzb;
import com.huamai.hdServer.domain.GjzbPatientTj;
import com.huamai.hdServer.domain.Xgtl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @Auther: byj
 * @Date: 2019/3/13 15:40
 * @Description: 关键指标统计  持久层
 */
@Mapper
public interface GjzbtjMapper {

    List<Gjzb> listObjects2(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime, @Param(value = "minValue") Double minValue, @Param(value = "maxValue") Double maxValue, @Param(value = "componentdefId") Integer componentdefId, @Param(value = "hospitalId") Integer hospitalId);

    List<Xgtl> listXgtls2(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime, @Param(value = "hospitalId") Integer hospitalId);

    List<GjzbPatientTj> getXgtlPatientinfo(@Param(value = "patientIdList") List<String> patientIdList);

    List<Gjzb> getHy(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime, @Param(value = "minValue") Double minValue, @Param(value = "maxValue") Double maxValue, @Param(value = "componentdefId") Integer componentdefId, @Param(value = "patientIdList") List<String> patientIdList);
    List<GjzbPatientTj> getPatientinfo(@Param(value = "patientIdList") List<String> patientIdList);

}
