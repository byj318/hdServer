package com.huamai.hdServer.controller;

import com.huamai.hdServer.domain.Ybjcmpsdjl;
import com.huamai.hdServer.enums.CourtyardAreaEnum;
import com.huamai.hdServer.service.YbjcmpsdjlService;
import com.huamai.hdServer.vo.ResponseVO;
import com.huamai.hdServer.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 仪表及触摸屏设定记录  控制层
 *
 * @author bbd
 */
@Controller
@RequestMapping("/ybjcmpsdjl")
public class YbjcmpsdjlController {
    /**
     * 进入透析液电解质监测记录页面
     *
     * @param model
     * @return
     */
    @GetMapping("/view/{courtyardArea}")
    public String view(Model model, @PathVariable(name = "courtyardArea") String courtyardArea) {
        if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals(CourtyardAreaEnum.NEW_DISTRICT_EN_US.getValue())) {
            model.addAttribute("courtyardArea", CourtyardAreaEnum.NEW_DISTRICT_ZH_CN.getValue());
        } else if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals(CourtyardAreaEnum.OLD_DISTRICT_EN_US.getValue())) {
            model.addAttribute("courtyardArea", CourtyardAreaEnum.OLD_DISTRICT_ZH_CN.getValue());
        }
        model.addAttribute("title", "仪表及触摸屏设定记录");
        model.addAttribute("nowYear", LocalDate.now().getYear());
        return "ybjcmpsdjl/list";
    }

    @Autowired
    private YbjcmpsdjlService ybjcmpsdjlService;

    /**
     * 分页查询列表
     *
     * @param pageIndex 起始数据索引 非页数
     * @param pageSize  页大小
     * @return
     */
    @GetMapping("/listPage")
    @ResponseBody
    public ResponseEntity<ResultVO> listPage(Integer pageIndex, Integer pageSize, String startTime, String endTime,
                                             String courtyardArea) {
        ResultVO resultVO = ybjcmpsdjlService.listAll(pageIndex, pageSize, startTime, endTime, courtyardArea);
        return ResponseEntity.ok().body(resultVO);
    }

    /**
     * 删除数据
     *
     * @param ids 删除数据的id字符串 eg:1,2,3
     * @return
     */
    @DeleteMapping("/{ids}")
    @ResponseBody
    public ResponseEntity<ResponseVO> deleteData(@PathVariable(name = "ids") String ids) {
        ybjcmpsdjlService.deleteDate(ids);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(true);
        responseVO.setMessage("删除成功!");
        return ResponseEntity.ok(responseVO);
    }

    /**
     * 新增数据
     *
     * @param ybjcmpsdjl 数据源
     * @return
     */
    @PostMapping
    @ResponseBody
    public Object insertData(Ybjcmpsdjl ybjcmpsdjl) {
        ybjcmpsdjlService.insertData(ybjcmpsdjl);
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(true);
        responseVO.setMessage("添加成功!");
        return ResponseEntity.ok(responseVO);
    }

    /**
     * 修改数据
     *
     * @param ybjcmpsdjl 数据源
     * @return
     */
    @PutMapping
    @ResponseBody
    public Object updateData(Ybjcmpsdjl ybjcmpsdjl) {
        ResponseVO responseVO = new ResponseVO();
        try {
            ybjcmpsdjlService.updateData(ybjcmpsdjl);
            responseVO.setSuccess(true);
            responseVO.setMessage("修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            responseVO.setSuccess(false);
            responseVO.setMessage(e.getMessage());
        }
        return responseVO;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @GetMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
                            String courtyardArea) {
        ybjcmpsdjlService.exportExcel(request, response, startTime, endTime, courtyardArea);
    }
}
