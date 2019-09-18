package com.huamai.hdServer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A浓缩液配置记录  实体类
 *
 * @author bbd
 */

/**
 * A浓缩液配置记录 实体类
 * 
 * @author bbd
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Ansypzjl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5261372172514161599L;

	private BigDecimal id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@JsonFormat(
		    pattern = "yyyy-MM-dd HH:mm",
		    timezone = "GMT+8"
		)
	private LocalDateTime pzsj;// 配置时间
	private String pzfs;// 配置份数
	private String gfxh;// 干粉型号
	private String gfph;// 干粉批号

	private String jstj;// 加水体积
	private String drtj;// 定容体积
	private String zxddz;// 在线电导值
	private String czr;// 操作人
	private String fhr;// 复合人
	private String remark;// 备注
	@Column(name = "courtyardArea")
	private String courtyardArea;//院区
	@JsonIgnore
	private Boolean isDelete;
	@Transient
	private Integer number;
	
}
