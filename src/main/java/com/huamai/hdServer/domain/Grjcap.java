package com.huamai.hdServer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 感染监测安排 实体类
 * 
 * @author bbd
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Grjcap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4461442494396683224L;
	private BigDecimal id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private LocalDate rqsj;// 日期时间
	private String swsResult;// 手卫生 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date swsTime;// 手卫生 时间
	private String jqbmResult;// 物表 机器表面 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date jqbmTime;// 物表 机器表面 时间

	private String zls1Result;// 物表 治疗室1 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date zls1Time;// 物表 治疗室1 时间

	private String zls2Result;// 物表 治疗室2 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date zls2Time;// 物表 治疗室2 时间

	private String fssaRysResult;// 反渗水A 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date fssaRysTime;// 反渗水A 热源数 时间
	private String fssaXjsResult;// 反渗水A 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date fssaXjsTime;// 反渗水A 细菌数 时间

	private String fssbRysResult;// 反渗水B 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date fssbRysTime;// 反渗水B 热源数 时间
	private String fssbXjsResult;// 反渗水B 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date fssbXjsTime;// 反渗水B 细菌数 时间

	private String txyaRysResult;// 透析液A 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyaRysTime;// 透析液A 热源数 时间
	private String txyaXjsResult;// 透析液A 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyaXjsTime;// 透析液A 细菌数 时间

	private String txybRysResult;// 透析液B 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txybRysTime;// 透析液B 热源数 时间
	private String txybXjsResult;// 透析液B 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txybXjsTime;// 透析液B 细菌数 时间

	private String txycRysResult;// 透析液C 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txycRysTime;// 透析液C 热源数 时间
	private String txycXjsResult;// 透析液C 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txycXjsTime;// 透析液C 细菌数 时间

	private String txydRysResult;// 透析液D 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txydRysTime;// 透析液D 热源数 时间
	private String txydXjsResult;// 透析液D 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txydXjsTime;// 透析液D 细菌数 时间

	private String txyeRysResult;// 透析液E 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyeRysTime;// 透析液E 热源数 时间
	private String txyeXjsResult;// 透析液E 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyeXjsTime;// 透析液E 细菌数 时间

	private String txyfRysResult;// 透析液F 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyfRysTime;// 透析液F 热源数 时间
	private String txyfXjsResult;// 透析液F 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyfXjsTime;// 透析液F 细菌数 时间

	private String txygRysResult;// 透析液G 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txygRysTime;// 透析液G 热源数 时间
	private String txygXjsResult;// 透析液G 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txygXjsTime;// 透析液G 细菌数 时间

	private String txyhRysResult;// 透析液H 热源数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyhRysTime;// 透析液H 热源数 时间
	private String txyhXjsResult;// 透析液H 细菌数 结果
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date txyhXjsTime;// 透析液H 细菌数 时间

	private String cccyr;// 初查采样人
	private String fccyr;// 复查采样人
	private String courtyardArea;//院区
	private Integer number;
	private String remark;
	@JsonIgnore
	private Boolean isDelete;
	private String swsResultTemp;
	private String txyaRysResultTemp;
	private String txybRysResultTemp;
	private String txycRysResultTemp;
	private String txydRysResultTemp;
	private String txyeRysResultTemp;
	private String txyfRysResultTemp;
	private String txygRysResultTemp;
	private String txyhRysResultTemp;

	
}
