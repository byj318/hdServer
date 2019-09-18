package com.huamai.hdServer.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.huamai.hdServer.domain.TxyDjzJcjl;

/**
 * 透析液电解质监测记录 持久层
 *
 * @author byj
 */
@Mapper
public interface TxyDjzJcjlMapper {

    List<TxyDjzJcjl> getAll(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime,
                            @Param(value = "courtyardArea") String courtyardArea);

    @Update("update txydjzjcjl set is_delete = 1 where id = ${id}")
    void deleteDate(@Param(value = "id") BigDecimal id) throws Exception;

    @Insert("INSERT INTO txydjzjcjl(jcrq,weizhi,txy,na,k,ca,cl,txyResult,sri22,sri26,ddz,jqjcResult,sjr,remark,courtyardArea,is_delete) VALUES (#{jcrq},#{weizhi},#{txy},#{na},#{k},#{ca},#{cl},#{txyResult},#{sri22},#{sri26},#{ddz},#{jqjcResult},#{sjr},#{remark},#{courtyardArea},#{isDelete})")
    void insertData(TxyDjzJcjl txyDjzJcjl) throws Exception;

    @Update("update txydjzjcjl set jcrq = #{jcrq},weizhi = #{weizhi},txy = #{txy},na = #{na},k = #{k},ca=#{ca},cl=#{cl},txyResult = #{txyResult},sri22 = #{sri22},sri26=#{sri26},ddz=#{ddz},jqjcResult=#{jqjcResult},sjr=#{sjr},remark=#{remark} where id = ${id}")
    void updateData(TxyDjzJcjl txyDjzJcjl);

}
