package com.huamai.hdServer.controller;

import com.huamai.hdServer.enums.CourtyardAreaEnum;
import com.huamai.hdServer.service.GjzbtjService;
import com.huamai.hdServer.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 关键指标统计 控制层
 *
 * @author bbd
 */
@Controller
@RequestMapping("/gjzbtj")
public class GjzbtjController {
    @Autowired
    private GjzbtjService gjzbtjService;

    /**
     * 进入关键指标统计页面
     *
     * @param model
     * @return
     */
    @GetMapping("/{loginName}/view/{courtyardArea}")
    public String view(@PathVariable(name = "loginName") String loginName, Model model, @PathVariable(name = "courtyardArea") String courtyardArea) {
        if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals(CourtyardAreaEnum.NEW_DISTRICT_EN_US.getValue())) {
            model.addAttribute("courtyardArea", CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue());
        } else if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals(CourtyardAreaEnum.OLD_DISTRICT_EN_US.getValue())) {
            model.addAttribute("courtyardArea", CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue());
        }
        model.addAttribute("loginName", loginName);
        model.addAttribute("title", "关键指标质量统计");
        model.addAttribute("nowYear", LocalDate.now().getYear());
        return "gjzbtj/list";
    }


    /**
     * 分页查询列表
     *
     * @param pageIndex 起始数据索引 非页数
     * @param pageSize  页大小
     * @return
     */
    @GetMapping("/listPage")
    @ResponseBody
    public Object listPage(Integer pageIndex, Integer pageSize, String startTime, String endTime, String courtyardArea, HttpServletRequest request, String loginName) {
        try {
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
            return gjzbtjService.listAll(pageIndex, pageSize, startTime, endTime, courtyardArea, request, loginName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime,
                            String endTime, String courtyardArea, String loginName) {
        gjzbtjService.exportExcel(request, response, startTime, endTime, courtyardArea, loginName);
    }

    @GetMapping("/listPatientInfo")
    @ResponseBody
    public ResultVO listPatientInfo(HttpServletRequest request, String searchType, String startTime,
                                    String endTime, String loginName, String courtyardArea) {
        return gjzbtjService.listPatientInfo(request, startTime, endTime, loginName, searchType, courtyardArea);
    }


    @GetMapping("/secondExportExcel")
    @ResponseBody
    public void secondExportExcel(HttpServletRequest request, HttpServletResponse response, String searchType, String startTime,
                                  String endTime, String loginName, String courtyardArea) {
        gjzbtjService.secondExportExcel(request, response, startTime, endTime, courtyardArea, loginName, searchType);
    }
}
