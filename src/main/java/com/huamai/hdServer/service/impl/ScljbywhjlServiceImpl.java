package com.huamai.hdServer.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.huamai.hdServer.domain.Scljbywhjl;
import com.huamai.hdServer.mapper.ScljbywhjlMapper;
import com.huamai.hdServer.service.ScljbywhjlService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.ResultVO;

/**
 * 水处理机半月维护 业务逻辑层
 * 
 * @author bbd
 *
 */
@Service
public class ScljbywhjlServiceImpl implements ScljbywhjlService {
	@Autowired
	private ScljbywhjlMapper scljbywhjlMapper;
	private static final SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
	 */
	public ResultVO listPage(int page, int size, String startTime, String endTime, String courtyardArea) {
		PageHelper.startPage(page / size + 1, size);
		Page<Scljbywhjl> data = (Page<Scljbywhjl>) scljbywhjlMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
				DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
		List<Scljbywhjl> result = data.getResult();
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
			scljbywhjlMapper.deleteDate(new BigDecimal(id));
		}
	}

	/**
	 * 新增数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void insertData(Scljbywhjl scljbywhjl) throws Exception {
		scljbywhjl.setIsDelete(false);
		LocalDate rqsj = scljbywhjl.getRqsj();
		String whsj = DateUtil.localDateToStringForYYYYMMDD(rqsj);

		Date rxdkssj = scljbywhjl.getRxdkssj();
		if(rxdkssj != null) {
			String rxdkssjStr = simpleDateFormatHour.format(rxdkssj);
			scljbywhjl.setRxdkssj(simpleDateFormat3.parse(whsj + " " + rxdkssjStr));
		}


		Date rxdjssj = scljbywhjl.getRxdjssj();
		if(rxdjssj != null) {
			String rxdjssjStr = simpleDateFormatHour.format(rxdjssj);
			scljbywhjl.setRxdjssj(simpleDateFormat3.parse(whsj + " " + rxdjssjStr));
		}


		Date hxxdkssj = scljbywhjl.getHxxdkssj();
		if(hxxdkssj != null) {
			String hxxdkssjStr = simpleDateFormatHour.format(hxxdkssj);
			scljbywhjl.setHxxdkssj(simpleDateFormat3.parse(whsj + " " + hxxdkssjStr));
		}


		Date hxxdjssj = scljbywhjl.getHxxdjssj();
		if(hxxdjssj != null) {
			String hxxdjssjStr = simpleDateFormatHour.format(hxxdjssj);
			scljbywhjl.setHxxdjssj(simpleDateFormat3.parse(whsj + " " + hxxdjssjStr));
		}
		
		scljbywhjlMapper.insertData(scljbywhjl);
	}

	/**
	 * 修改数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateData(Scljbywhjl scljbywhjl) throws Exception {
		LocalDate rqsj = scljbywhjl.getRqsj();
		String whsj = DateUtil.localDateToStringForYYYYMMDD(rqsj);

		Date rxdkssj = scljbywhjl.getRxdkssj();
		if(rxdkssj != null) {
			String rxdkssjStr = simpleDateFormatHour.format(rxdkssj);
			scljbywhjl.setRxdkssj(simpleDateFormat3.parse(whsj + " " + rxdkssjStr));
		}


		Date rxdjssj = scljbywhjl.getRxdjssj();
		if(rxdjssj != null) {
			String rxdjssjStr = simpleDateFormatHour.format(rxdjssj);
			scljbywhjl.setRxdjssj(simpleDateFormat3.parse(whsj + " " + rxdjssjStr));
		}


		Date hxxdkssj = scljbywhjl.getHxxdkssj();
		if(hxxdkssj != null) {
			String hxxdkssjStr = simpleDateFormatHour.format(hxxdkssj);
			scljbywhjl.setHxxdkssj(simpleDateFormat3.parse(whsj + " " + hxxdkssjStr));
		}


		Date hxxdjssj = scljbywhjl.getHxxdjssj();
		if(hxxdjssj != null) {
			String hxxdjssjStr = simpleDateFormatHour.format(hxxdjssj);
			scljbywhjl.setHxxdjssj(simpleDateFormat3.parse(whsj + " " + hxxdjssjStr));
		}
		scljbywhjlMapper.updateData(scljbywhjl);
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
				path = "static/excel/水处理机半月维护登记表（新院）.xls";
			}else if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
				path = "static/excel/水处理机半月维护登记表（老院）.xls";
			}
			is = this.getClass().getClassLoader().getResourceAsStream(path);
			tpWorkbook = new HSSFWorkbook(is);
			workBook = new HSSFWorkbook();
			workBook = tpWorkbook;
			CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
			Sheet sheet = workBook.getSheetAt(0);
			workBook.setSheetName(0, "水处理机半月维护登记表");

			int line = 2;
			Row row;
			Cell cell;
			List<Scljbywhjl> datas = scljbywhjlMapper.getAll(DateUtil.stringToLocalDateForYYYYMMDD(startTime),
					DateUtil.stringToLocalDateForYYYYMMDD(endTime), courtyardArea);
			CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
			if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("新院")) {
				for (Scljbywhjl data : datas) {
					row = sheet.createRow(line++);
					cell = row.createCell(0);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getRqsj()));

					cell = row.createCell(1);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYsyl());

					cell = row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getSljk());

					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getSlck());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getTlck());

					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getRhck());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJlck());

					cell = row.createCell(7);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjmj());

					cell = row.createCell(8);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjnc());

					cell = row.createCell(9);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjbqy());

					cell = row.createCell(10);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjmj());

					cell = row.createCell(11);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjnc());

					cell = row.createCell(12);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCshl());

					Date rxdkssj = data.getRxdkssj();
					if(rxdkssj != null) {
						cell = row.createCell(13);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(rxdkssj));
					}
					Date rxdjssj = data.getRxdjssj();
					if(rxdjssj != null) {
						cell = row.createCell(14);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(rxdjssj));
					}
					Date hxxdkssj = data.getHxxdkssj();
					if(hxxdkssj != null) {
						cell = row.createCell(15);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(hxxdkssj));
					}
					Date hxxdjssj = data.getHxxdjssj();
					if(hxxdjssj != null) {
						cell = row.createCell(16);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(hxxdjssj));
					}

					cell = row.createCell(17);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjmc());

					cell = row.createCell(18);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjnd());

					cell = row.createCell(19);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjyl());

					cell = row.createCell(20);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCljc());

					cell = row.createCell(21);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJly());
				}
			}else if(!StringUtils.isEmpty(courtyardArea) && courtyardArea.equals("老院")) {
				for (Scljbywhjl data : datas) {
					row = sheet.createRow(line++);
					cell = row.createCell(0);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getRqsj()));

					cell = row.createCell(1);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getSljk());

					cell = row.createCell(2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getSlck());

					cell = row.createCell(3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getTlck());

					cell = row.createCell(4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getRhck());

					cell = row.createCell(5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJlck());

					cell = row.createCell(6);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjmj());

					cell = row.createCell(7);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjmj());

					cell = row.createCell(8);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjbqy());

					cell = row.createCell(9);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjnc());

					cell = row.createCell(10);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getEjnc());

					cell = row.createCell(11);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCshl());

					Date rxdkssj = data.getRxdkssj();
					if(rxdkssj != null) {
						cell = row.createCell(12);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(rxdkssj));
					}
					Date rxdjssj = data.getRxdjssj();
					if(rxdjssj != null) {
						cell = row.createCell(13);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(rxdjssj));
					}
					Date hxxdkssj = data.getHxxdkssj();
					if(hxxdkssj != null) {
						cell = row.createCell(14);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(hxxdkssj));
					}
					Date hxxdjssj = data.getHxxdjssj();
					if(hxxdjssj != null) {
						cell = row.createCell(15);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(simpleDateFormatHour.format(hxxdjssj));
					}

					cell = row.createCell(16);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjmc());

					cell = row.createCell(17);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjnd());

					cell = row.createCell(18);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getYjyl());

					cell = row.createCell(19);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getCljc());

					cell = row.createCell(20);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.getJly());
				}
			}
			
			

			outputStream = new ByteArrayOutputStream();

			workBook.write(outputStream);
			DownloadUtil downloadUtil = new DownloadUtil();
			String fileName = "水处理机半月维护登记表.xls";
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
