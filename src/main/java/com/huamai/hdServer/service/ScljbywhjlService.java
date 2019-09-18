package com.huamai.hdServer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huamai.hdServer.domain.Scljbywhjl;
import com.huamai.hdServer.vo.ResultVO;

public interface ScljbywhjlService {
	ResultVO listPage(int page, int size, String startTime, String endTime, String courtyardArea);

	void deleteDate(String inIds) throws Exception;// 删除数据

	void insertData(Scljbywhjl scljbywhjl) throws Exception;

	void updateData(Scljbywhjl scljbywhjl) throws Exception;

	void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea);
}
