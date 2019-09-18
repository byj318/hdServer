package com.huamai.hdServer.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huamai.hdServer.domain.TxyDjzJcjl;
import com.huamai.hdServer.mapper.TxyDjzJcjlMapper;
import com.huamai.hdServer.service.TxyDjzJcjlService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.ResultVO;

/**
 * 透析液电解质监测记录 业务逻辑层
 * 
 * @author bbd
 *
 */
@Service
public class TxyDjzJcjlServiceImpl implements TxyDjzJcjlService {
	@Autowired
	private TxyDjzJcjlMapper txyDjzJcjlMapper;

	/**
	 * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
	 */
	public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea) {
		PageHelper.startPage(page / size + 1, size);
		Page<TxyDjzJcjl> data = (Page<TxyDjzJcjl>) txyDjzJcjlMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
				DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
		List<TxyDjzJcjl> result = data.getResult();
		AtomicInteger atomicInteger = new AtomicInteger();
		atomicInteger.set(page);
		result.forEach((e) -> {
			e.setNumber(atomicInteger.incrementAndGet());
		});
		return new ResultVO(data.getTotal(), result);
	}

	/**
	 * 删除数据 删除数据的inIds字符串 eg:1,2,3
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class) // 事务
	public void deleteDate(String inIds) throws Exception {
		String[] ids = inIds.split(",");
		for (String id : ids) {
			txyDjzJcjlMapper.deleteDate(new BigDecimal(id));
		}
	}

	/**
	 * 新增数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void insertData(TxyDjzJcjl txyDjzJcjl) throws Exception {
		txyDjzJcjl.setIsDelete(false);
		txyDjzJcjlMapper.insertData(txyDjzJcjl);
	}

	/**
	 * 修改数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateData(TxyDjzJcjl txyDjzJcjl) throws Exception {
		txyDjzJcjlMapper.updateData(txyDjzJcjl);
	}

	@Override
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea) {
		InputStream is = null;
		HSSFWorkbook tpWorkbook = null;
		HSSFWorkbook workBook = null;
		ByteArrayOutputStream outputStream = null;
		try {
			String path = "";
			if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
				path = "static/excel/透析液电解质监测登记（新院）.xls";
			}else if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
				path = "static/excel/透析液电解质监测登记（老院）.xls";
			}
			is = this.getClass().getClassLoader().getResourceAsStream(path);
			tpWorkbook = new HSSFWorkbook(is);
			workBook = new HSSFWorkbook();
			workBook = tpWorkbook;
			CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
			Sheet sheet = workBook.getSheetAt(0);
			workBook.setSheetName(0, "透析液电解质监测登记");

			int line = 2;
			Row row;
			Cell cell;
			List<TxyDjzJcjl> datas = txyDjzJcjlMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
					DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
			CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
			for (TxyDjzJcjl data : datas) {
				row = sheet.createRow(line++);
				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getJcrq()));

				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getWeizhi());

				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getTxy());

				if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getNa());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getK());
				}else if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getK());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getNa());
				}
				
				if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCa());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCl());

				}else if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCl());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCa());

				}

				cell = row.createCell(7);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getTxyResult());

				cell = row.createCell(8);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getSri22());

				cell = row.createCell(9);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getSri26());

				cell = row.createCell(10);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getDdz());

				cell = row.createCell(11);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getJqjcResult());

				cell = row.createCell(12);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getSjr());

				cell = row.createCell(13);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getRemark());

			}

			outputStream = new ByteArrayOutputStream();

			workBook.write(outputStream);
			DownloadUtil downloadUtil = new DownloadUtil();
			String fileName = "透析液电解质监测登记.xls";
			downloadUtil.download(outputStream, response, fileName, request.getHeader("user-agent"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (workBook != null) {
				try {
					workBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (tpWorkbook != null) {
				try {
					tpWorkbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
