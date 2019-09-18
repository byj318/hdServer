package com.huamai.hdServer.mapper;

import com.huamai.hdServer.domain.Ansypzjl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

/**
 * A浓缩液配置记录 持久层
 * 
 * @author byj
 *
 */
public interface AnsypzjlMapper extends Mapper<Ansypzjl> {

	@Update("update ansypzjl set is_delete = 1 where id = ${id}")
	void deleteDate(@Param(value = "id") BigDecimal id) throws Exception;

	@Update("update ansypzjl set pzsj = #{pzsj},pzfs = #{pzfs},gfxh = #{gfxh},gfph = #{gfph},jstj=#{jstj},drtj=#{drtj},zxddz = #{zxddz},czr = #{czr},fhr=#{fhr},remark=#{remark} where id = ${id}")
	void updateData(Ansypzjl ansypzjl);

}
