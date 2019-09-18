package com.huamai.hdServer.domain;

import java.io.Serializable;
/**
 * A浓缩液配置记录  实体类
 * @author bbd
 *
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 水处理机半月维护记录   实体类
 * 
 * @author bbd
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Scljbywhjl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3943737598840501248L;
	/**
	 * 
	 */

	private BigDecimal id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private LocalDate rqsj;// 日期时间
	private String ysyl;// 原水压力
	private String sljk;// 砂虑进口
	private String slck;// 砂虑出口
	private String tlck;// 碳虑出口
	private String rhck;// 软化出口
	private String jlck;// 精滤出口
	private String yjmj;// 一级膜进
	private String yjnc;// 一级浓出
	private String ejbqy;// 二级泵前压
	private String ejmj;// 二级膜进
	private String ejnc;// 二级浓出
	private String cshl;// 纯水回路
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date rxdkssj;// 热消毒开始时间
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date rxdjssj;// 热消毒结束时间
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date hxxdkssj;// 化学消毒开始时间
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date hxxdjssj;// 化学消毒结束时间
	private String yjmc;// 药剂名称
	private String yjnd;// 药剂浓度
	private String yjyl;// 药剂用量
	private String cljc;// 残留检测
	private String jly;// 记录员
	private Integer number;
	private String courtyardArea;//院区
	@JsonIgnore
	private Boolean isDelete;
}
