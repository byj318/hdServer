package com.huamai.hdServer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 仪表及触摸屏设定记录  实体类
 *
 * @author bbd
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "ybjcmpsdjl")
public class Ybjcmpsdjl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2657998478790057880L;
	@Id
	//@KeySql(useGeneratedKeys = false)
	private BigDecimal id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(
		    pattern = "yyyy-MM-dd",
		    timezone = "GMT+8"
		)
	private LocalDate rqsj;// 日期时间
	private String sdxm;//设定项目
	private String ysdz;//原设定值
	private String xsdz;//新设定值
	private String remark;//备注
	@Column(name = "courtyardArea")
	private String courtyardArea;//院区
	private String czry;//操作人员
	@JsonIgnore
	private Boolean isDelete;
	@Transient
	private Integer number;//编号

}
