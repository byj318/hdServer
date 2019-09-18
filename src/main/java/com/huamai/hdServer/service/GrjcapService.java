package com.huamai.hdServer.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huamai.hdServer.domain.Grjcap;
import com.huamai.hdServer.vo.ResultVO;

public interface GrjcapService {
	ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea);

	void deleteDate(String inIds) throws Exception;// 删除数据

	void insertData(Grjcap grjcap) throws Exception;

	void updateData(Grjcap grjcap) throws Exception;

	void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea);

	List<String> queryDictionary(String dictionaryClassName, String dictionaryTableName);
}
