package com.huamai.hdServer.controller;

import com.huamai.hdServer.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 旭燃
 * @Date: 2019/9/17 09:23
 * @Description:
 */
@Controller
@RequestMapping("statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("bfz")
    public String view() {
        return "statistics/bfz";
    }

    @GetMapping("getTableHeader")
    public ResponseEntity<List<Map<String, String>>> getTableHeader() {
        return ResponseEntity.ok(statisticsService.getTableHeader());
    }

    @GetMapping("getBfzData")
    public ResponseEntity<Map<String, Object>> getBfzData(String startTime, String endTime, String courtyardArea) {
        if (StringUtils.isEmpty(startTime) || "undefined".equals(startTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            startTime = "";
            startTime = year + "-01" + "-01";
        }
        if (StringUtils.isEmpty(endTime) || "undefined".equals(endTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            int day = now.getDayOfMonth();
            endTime = "";
            endTime += year;
            if (month < 10) {
                endTime = endTime + "-0" + month;
            } else {
                endTime = endTime + "-" + month;
            }
            if (day < 10) {
                endTime = endTime + "-0" + day;
            } else {
                endTime = endTime + "-" + day;
            }
        }
        return ResponseEntity.ok(statisticsService.statisticsBfz(startTime, endTime, courtyardArea));
    }

    @GetMapping("listPatientInfo")
    public ResponseEntity<Map<String, Object>> listPatientInfo(String startTime, String endTime, String courtyardArea, String type) {
        return ResponseEntity.ok(statisticsService.listPatientInfo(startTime, endTime, courtyardArea, type));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea) {
        statisticsService.exportExcel(request, response, startTime, endTime, courtyardArea);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/secondExportExcel")
    @ResponseBody
    public void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea, String type) {
        statisticsService.secondExportExcel(request, response, startTime, endTime, courtyardArea,type);
    }

    @GetMapping("ysjb")
    public String ysjbView() {
        return "statistics/ysjb";
    }

    /**
     * 医生交班
     * @param startTime
     * @param endTime
     * @param courtyardArea
     * @return
     */
    @GetMapping("getYsjbData")
    public ResponseEntity<Map<String, Object>> getYsjbData(String startTime, String endTime, String courtyardArea) {
        if (StringUtils.isEmpty(startTime) || "undefined".equals(startTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            startTime = "";
            startTime = year + "-01" + "-01";
        }
        if (StringUtils.isEmpty(endTime) || "undefined".equals(endTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            int day = now.getDayOfMonth();
            endTime = "";
            endTime += year;
            if (month < 10) {
                endTime = endTime + "-0" + month;
            } else {
                endTime = endTime + "-" + month;
            }
            if (day < 10) {
                endTime = endTime + "-0" + day;
            } else {
                endTime = endTime + "-" + day;
            }
        }
        return ResponseEntity.ok(statisticsService.statisticsYsjb(startTime, endTime, courtyardArea));
    }

    @GetMapping("hsjb")
    public String hsjbView() {
        return "statistics/hsjb";
    }

    /**
     * 护士交班
     * @param startTime
     * @param endTime
     * @param courtyardArea
     * @return
     */
    @GetMapping("getHsjbData")
    public ResponseEntity<Map<String, Object>> getHsjbData(String startTime, String endTime, String courtyardArea) {
        if (StringUtils.isEmpty(startTime) || "undefined".equals(startTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            startTime = "";
            startTime = year + "-01" + "-01";
        }
        if (StringUtils.isEmpty(endTime) || "undefined".equals(endTime)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            int day = now.getDayOfMonth();
            endTime = "";
            endTime += year;
            if (month < 10) {
                endTime = endTime + "-0" + month;
            } else {
                endTime = endTime + "-" + month;
            }
            if (day < 10) {
                endTime = endTime + "-0" + day;
            } else {
                endTime = endTime + "-" + day;
            }
        }
        return ResponseEntity.ok(statisticsService.statisticsHsjb(startTime, endTime, courtyardArea));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/exportExcelYsjb")
    @ResponseBody
    public void exportExcelYsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea) {
        statisticsService.exportExcelYsjb(request, response, startTime, endTime, courtyardArea);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/exportExcelHsjb")
    @ResponseBody
    public void exportExcelHsjb(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime, String courtyardArea) {
        statisticsService.exportExcelHsjb(request, response, startTime, endTime, courtyardArea);
    }
}
