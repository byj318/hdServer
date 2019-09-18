package com.huamai.hdServer.service;

import com.huamai.hdServer.domain.Ybjcmpsdjl;
import com.huamai.hdServer.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface YbjcmpsdjlService {
	ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea);

	void deleteDate(String inIds);// 删除数据

	void insertData(Ybjcmpsdjl ybjcmpsdjl);

	void updateData(Ybjcmpsdjl ybjcmpsdjl) throws Exception;

	void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea);
}
