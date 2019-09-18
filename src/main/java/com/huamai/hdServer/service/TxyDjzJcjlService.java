package com.huamai.hdServer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huamai.hdServer.domain.TxyDjzJcjl;
import com.huamai.hdServer.vo.ResultVO;

public interface TxyDjzJcjlService {
	ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea);

	void deleteDate(String inIds) throws Exception;// 删除数据

	void insertData(TxyDjzJcjl txyDjzJcjl) throws Exception;

	void updateData(TxyDjzJcjl txyDjzJcjl) throws Exception;

	void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea);
}
