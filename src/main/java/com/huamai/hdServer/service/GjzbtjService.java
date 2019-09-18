package com.huamai.hdServer.service;

import com.huamai.hdServer.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GjzbtjService {
    ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea, HttpServletRequest request, String loginName) throws Exception;

    void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                     String courtyardArea, String loginName);

    ResultVO listPatientInfo(HttpServletRequest request,String s, String s1, String sa, String searchType, String courtyardArea);

    void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea, String loginName, String searchType);
}
