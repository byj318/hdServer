package com.huamai.hdServer.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.huamai.hdServer.domain.Sclwhjh;

/**
 * 水处理维护计划 持久层
 *
 * @author byj
 */
@Mapper
public interface SclwhjhMapper {
    /**
     * 删
     *
     * @param id
     * @throws Exception
     */
    @Update("update sclwhjh set is_delete = 1 where id = ${id}")
    void deleteDate(@Param(value = "id") BigDecimal id) throws Exception;

    /**
     * 增
     *
     * @param sclwhjh
     * @throws Exception
     */
    @Insert("INSERT INTO sclwhjh(whsj,jsy,hly,llyjcs,llyjns,llejcs,llejns,ddys,ddyjcs,ddejcs,ddejns,zl,yd,ph,ytjy,jly,courtyardArea,zdtsjjz,is_delete) VALUES (#{whsj},#{jsy},#{hly},#{llyjcs},#{llyjns},#{llejcs},#{llejns},#{ddys},#{ddyjcs},#{ddejcs},#{ddejns},#{zl},#{yd},#{ph},#{ytjy},#{jly},#{courtyardArea},#{zdtsjjz},#{isDelete})")
    void insertData(Sclwhjh sclwhjh) throws Exception;

    /**
     * 改
     *
     * @param sclwhjh
     */
    @Update("update sclwhjh set whsj = #{whsj},jsy = #{jsy},hly = #{hly},llyjcs = #{llyjcs},llyjns=#{llyjns},llejcs=#{llejcs},llejns = #{llejns},ddys = #{ddys},ddyjcs=#{ddyjcs},ddejcs=#{ddejcs},ddejns=#{ddejns},zl=#{zl},yd=#{yd},ytjy=#{ytjy},jly=#{jly},zdtsjjz=#{zdtsjjz},ph=#{ph} where id = ${id}")
    void updateData(Sclwhjh sclwhjh);

    List<Sclwhjh> getAll(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime,
                         @Param(value = "courtyardArea") String courtyardArea);
}
