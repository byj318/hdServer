package com.huamai.hdServer.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.huamai.hdServer.domain.Scljbywhjl;

/**
 * 水处理机半月维护记录 持久层
 *
 * @author byj
 */
@Mapper
public interface ScljbywhjlMapper {
    @Update("update scljbywhjl set is_delete = 1 where id = ${id}")
    void deleteDate(@Param(value = "id") BigDecimal id) throws Exception;

    @Insert("INSERT INTO scljbywhjl(rqsj,ysyl,sljk,slck,tlck,rhck,jlck,yjmj,yjnc,ejbqy,ejmj,ejnc,cshl,rxdkssj,rxdjssj,hxxdkssj,hxxdjssj,yjmc,yjnd,yjyl,cljc,jly,courtyardArea,is_delete) VALUES (#{rqsj},#{ysyl},#{sljk},#{slck},#{tlck},#{rhck},#{jlck},#{yjmj},#{yjnc},#{ejbqy},#{ejmj},#{ejnc},#{cshl},#{rxdkssj},#{rxdjssj},#{hxxdkssj},#{hxxdjssj},#{yjmc},#{yjnd},#{yjyl},#{cljc},#{jly},#{courtyardArea},#{isDelete})")
    void insertData(Scljbywhjl scljbywhjl) throws Exception;

    @Update("update scljbywhjl set rqsj = #{rqsj},ysyl=#{ysyl},sljk=#{sljk},slck=#{slck},tlck=#{tlck},rhck=#{rhck},jlck=#{jlck},yjmj=#{yjmj},yjnc=#{yjnc},ejbqy=#{ejbqy},ejmj=#{ejmj},ejnc=#{ejnc},cshl=#{cshl},rxdkssj=#{rxdkssj},rxdjssj=#{rxdjssj},hxxdkssj=#{hxxdkssj},hxxdjssj=#{hxxdjssj},yjmc=#{yjmc},yjnd=#{yjnd},yjyl=#{yjyl},cljc=#{cljc},jly=#{jly} where id = ${id}")
    void updateData(Scljbywhjl scljbywhjl);

    List<Scljbywhjl> getAll(@Param(value = "startTime") LocalDate startTime, @Param(value = "endTime") LocalDate endTime,
                            @Param(value = "courtyardArea") String courtyardArea);

}
