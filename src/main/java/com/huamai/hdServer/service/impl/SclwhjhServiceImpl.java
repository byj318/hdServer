package com.huamai.hdServer.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.huamai.hdServer.domain.Sclwhjh;
import com.huamai.hdServer.mapper.SclwhjhMapper;
import com.huamai.hdServer.service.SclwhjhService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.ResultVO;

/**
 * 水处理维护计划 业务逻辑层
 * 
 * @author bbd
 *
 */
@Service
public class SclwhjhServiceImpl implements SclwhjhService {
	@Autowired
	private SclwhjhMapper sclwhjhMapper;

	/**
	 * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
	 */
	public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea) {
		PageHelper.startPage(page / size + 1, size);
		Page<Sclwhjh> data = (Page<Sclwhjh>) sclwhjhMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
				DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
		List<Sclwhjh> result = data.getResult();
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
			sclwhjhMapper.deleteDate(new BigDecimal(id));
		}
	}

	/**
	 * 新增数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void insertData(Sclwhjh sclwhjh) throws Exception {
		sclwhjh.setIsDelete(false);
		sclwhjhMapper.insertData(sclwhjh);
	}

	/**
	 * 修改数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateData(Sclwhjh sclwhjh) throws Exception {
		sclwhjhMapper.updateData(sclwhjh);
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
			if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
				path = "static/excel/水处理维护计划（新院）.xls";
			} else if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
				path = "static/excel/水处理维护计划（老院）.xls";
			}
			is = this.getClass().getClassLoader().getResourceAsStream(path);
			tpWorkbook = new HSSFWorkbook(is);
			workBook = new HSSFWorkbook();
			workBook = tpWorkbook;
			CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
			Sheet sheet = workBook.getSheetAt(0);
			workBook.setSheetName(0, "水处理维护计划");

			int line = 2;
			Row row;
			Cell cell;
			List<Sclwhjh> datas = sclwhjhMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
					DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
			CellStyle cellStyle = PoiUtil.getCellStyle(workBook);

			if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
				for (Sclwhjh data : datas) {
					row = sheet.createRow(line++);
					cell = row.createCell(0);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getWhsj()));

					cell = row.createCell(1);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJsy());

					cell = row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getHly());

					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlyjcs());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlyjns());

					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlejcs());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlejns());

					cell = row.createCell(7);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdys());

					cell = row.createCell(8);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdyjcs());

					cell = row.createCell(9);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdejcs());

					cell = row.createCell(10);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdejns());

					cell = row.createCell(11);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getZl());

					cell = row.createCell(12);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYd());

					cell = row.createCell(13);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getPh());

					cell = row.createCell(14);
					cell.setCellStyle(cellStyle);
					if (data.getZdtsjjz()) {
						cell.setCellValue("√");
					} else {
						cell.setCellValue("×");
					}

					cell = row.createCell(15);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYtjy());

					cell = row.createCell(16);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJly());
				}
			} else if (!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
				for (Sclwhjh data : datas) {
					row = sheet.createRow(line++);
					cell = row.createCell(0);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getWhsj()));

					cell = row.createCell(1);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJsy());

					cell = row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getHly());

					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlyjns());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlejns());

					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlyjcs());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getLlejcs());

					cell = row.createCell(7);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdys());

					cell = row.createCell(8);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdyjcs());

					cell = row.createCell(9);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getDdejcs());

					cell = row.createCell(10);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getZl());

					cell = row.createCell(11);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYd());

					cell = row.createCell(12);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getPh());

					cell = row.createCell(13);
					cell.setCellStyle(cellStyle);
					if (data.getZdtsjjz()) {
						cell.setCellValue("√");
					} else {
						cell.setCellValue("×");
					}

					cell = row.createCell(14);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYtjy());

					cell = row.createCell(15);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJly());
				}
			}

			outputStream = new ByteArrayOutputStream();

			workBook.write(outputStream);
			DownloadUtil downloadUtil = new DownloadUtil();
			String fileName = "水处理维护计划.xls";
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
