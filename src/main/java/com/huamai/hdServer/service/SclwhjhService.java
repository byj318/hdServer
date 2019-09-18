package com.huamai.hdServer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huamai.hdServer.domain.Sclwhjh;
import com.huamai.hdServer.vo.ResultVO;

public interface SclwhjhService {
	ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea);

	void deleteDate(String inIds) throws Exception;// 删除数据

	void insertData(Sclwhjh sclwhjh) throws Exception;

	void updateData(Sclwhjh sclwhjh) throws Exception;

	void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea);
}
