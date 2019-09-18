package com.huamai.hdServer.domain;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 水处理维护计划 实体类
 * 
 * @author bbd
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Sclwhjh implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1088550919575797936L;
	
	private BigDecimal id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(
		    pattern = "yyyy-MM-dd",
		    timezone = "GMT+8"
		)
	private LocalDate whsj;//维护时间
	private String jsy;//进水压
	private String hly;//回路压
	private String llyjcs;//在线流量显示   一级产水
	private String llyjns;//在线流量显示   一级浓水
	private String llejcs;//在线流量显示   二级产水
	private String llejns;//在线流量显示   二级浓水
	private String ddys;//在线电导显示   原水
	private String ddyjcs;//在线电导显示   一级纯水
	private String ddejcs;//在线电导显示   二级纯水
	private String ddejns;//在线电导显示   二级浓水
	private String zl;//总氯
	private String yd;//硬度
	private String ph;//PH值
	private String ytjy;//盐桶加盐
	private String jly;//记录员
	private Integer number;
	private String courtyardArea;//院区
	private Boolean zdtsjjz;//自动头时间校准
	@JsonIgnore
	private Boolean isDelete;
	
}
