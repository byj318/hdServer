package com.huamai.hdServer.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huamai.hdServer.enums.CourtyardAreaEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huamai.hdServer.domain.Sclwhjh;
import com.huamai.hdServer.service.SclwhjhService;
import com.huamai.hdServer.vo.ResponseVO;

/**
 * 水处理维护计划 控制层
 * 
 * @author bbd
 *
 */
@Controller
@RequestMapping("/sclwhjh")
public class SclwhjhController {
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
		model.addAttribute("title", "水处理维护计划");
		model.addAttribute("nowYear", LocalDate.now().getYear());
		return "sclwhjh/list";
	}

	@Autowired
	private SclwhjhService sclwhjhService;

	/**
	 * 分页查询列表
	 * 
	 * @param pageIndex 起始数据索引 非页数
	 * @param pageSize  页大小
	 * @return
	 */
	@GetMapping("/listPage")
	@ResponseBody
	public Object listPage(Integer pageIndex, Integer pageSize, String startTime, String endTime,
			String courtyardArea) {
		return sclwhjhService.listAll(pageIndex, pageSize, startTime, endTime, courtyardArea);
	}

	/**
	 * 删除数据
	 * 
	 * @param ids 删除数据的id字符串 eg:1,2,3
	 * @return
	 */
	@DeleteMapping("/{ids}")
	@ResponseBody
	public Object deleteData(@PathVariable(name = "ids") String ids) {
		ResponseVO responseVO = new ResponseVO();
		try {
			sclwhjhService.deleteDate(ids);
			responseVO.setSuccess(true);
			responseVO.setMessage("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			responseVO.setSuccess(false);
			responseVO.setMessage(e.getMessage());
		}
		return responseVO;
	}

	/**
	 * 新增数据
	 * 
	 * @param txyDjzJcjl 数据源
	 * @return
	 */
	@PostMapping
	@ResponseBody
	public Object insertData(Sclwhjh sclwhjh) {
		ResponseVO responseVO = new ResponseVO();
		try {
			sclwhjhService.insertData(sclwhjh);
			responseVO.setSuccess(true);
			responseVO.setMessage("添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			responseVO.setSuccess(false);
			responseVO.setMessage(e.getMessage());
		}
		return responseVO;
	}

	/**
	 * 修改数据
	 * 
	 * @param txyDjzJcjl 数据源
	 * @return
	 */
	@PutMapping
	@ResponseBody
	public Object updateData(Sclwhjh sclwhjh) {
		ResponseVO responseVO = new ResponseVO();
		try {
			sclwhjhService.updateData(sclwhjh);
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
		sclwhjhService.exportExcel(request, response, startTime, endTime, courtyardArea);
	}
}
