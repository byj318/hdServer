package com.huamai.hdServer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * jquery datatable 返回页面展示类
 * 
 * @author 逼逼叨
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultVO implements Serializable {
	private Long recordsTotal;// 总记录数
	private List data;
}
