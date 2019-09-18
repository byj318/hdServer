package com.huamai.hdServer.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 旭燃
 * @Date: 2019/9/17 09:24
 * @Description:
 */
public interface StatisticsService {
    Map<String, Object> statisticsBfz(String startTime, String endTime, String courtyardArea);

    List<Map<String, String>> getTableHeader();

    Map<String, Object> listPatientInfo(String startTime, String endTime, String courtyardArea, String type);

    void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea);

    void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea, String type);

    Map<String, Object> statisticsYsjb(String startTime, String endTime, String courtyardArea);

    Map<String, Object> statisticsHsjb(String startTime, String endTime, String courtyardArea);

    void exportExcelYsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea);

    void exportExcelHsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea);
}
