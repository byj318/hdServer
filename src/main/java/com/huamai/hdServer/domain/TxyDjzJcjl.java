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
 * 透析液电解质监测记录  实体类
 * @author bbd
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class TxyDjzJcjl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039996419641412057L;
	
	
	private BigDecimal id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(
		    pattern = "yyyy-MM-dd",
		    timezone = "GMT+8"
		)
	private LocalDate jcrq;//日期
	private String weizhi;//位置
	private String txy;//透析液
	private String na;//纳
	private String k;//钾
	private String ca;//钙
	private String cl;//氯
	private String txyResult;//透析液结果
	private String sri22;//SRI_22
	private String sri26;//SRI_26
	private String ddz;//电导值
	private String jqjcResult;//机器监测结果
	private String sjr;//送检人
	private String remark;//备注
	private Integer number;//编号
	private String courtyardArea;//院区
	@JsonIgnore
	private Boolean isDelete;

}
