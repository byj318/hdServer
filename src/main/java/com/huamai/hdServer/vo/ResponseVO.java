package com.huamai.hdServer.vo;

import lombok.Data;

import java.io.Serializable;
/**
 * 响应   值 对象
 * @author byj
 *
 */
@Data
public class ResponseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean success;   //标志位
	private String message;//提示消息
	private Object body;//数据
	public ResponseVO(Boolean success, String message, Object body) {
		this.success = success;
		this.message = message;
		this.body = body;
	}
	public ResponseVO(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	public ResponseVO() {
	}
	
	

}
