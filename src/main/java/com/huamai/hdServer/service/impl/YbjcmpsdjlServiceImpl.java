package com.huamai.hdServer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huamai.hdServer.domain.Ybjcmpsdjl;
import com.huamai.hdServer.enums.CourtyardAreaEnum;
import com.huamai.hdServer.enums.ExceptionEnum;
import com.huamai.hdServer.exception.MyException;
import com.huamai.hdServer.mapper.YbjcmpsdjlMapper;
import com.huamai.hdServer.service.YbjcmpsdjlService;
import com.huamai.hdServer.util.DateUtil;
import com.huamai.hdServer.util.DownloadUtil;
import com.huamai.hdServer.util.PoiUtil;
import com.huamai.hdServer.vo.ResultVO;
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
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 透析液电解质监测记录 业务逻辑层
 * 
 * @author bbd
 *
 */
@Service
public class YbjcmpsdjlServiceImpl implements YbjcmpsdjlService {
	@Autowired
	private YbjcmpsdjlMapper ybjcmpsdjlMapper;

	/**
	 * 分页查询 透析液电解质监测记录 page : 起始数据索引 非页数 size 每页数据大小
	 */
	public ResultVO listAll(int page, int size, String startTime, String endTime, String courtyardArea) {
		PageHelper.startPage(page / size + 1, size);
		//过滤
		Example example = new Example(Ybjcmpsdjl.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDelete",false);
		if(!StringUtils.isEmpty(startTime)){
			criteria.andGreaterThanOrEqualTo("rqsj",DateUtil.stringToLocalDateForYYYYMMDD(startTime));
		}
		if(!StringUtils.isEmpty(endTime)){
			criteria.andLessThanOrEqualTo("rqsj",DateUtil.stringToLocalDateForYYYYMMDD(endTime));
		}
		if(!StringUtils.isEmpty(courtyardArea) && !CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)){
			criteria.andEqualTo("courtyardArea",courtyardArea);
		}

		//排序
		example.setOrderByClause("rqsj DESC");

		//查询
		List<Ybjcmpsdjl> result = ybjcmpsdjlMapper.selectByExample(example);

		PageInfo<Ybjcmpsdjl> pageInfo = new PageInfo<>(result);
		AtomicInteger atomicInteger = new AtomicInteger();
		atomicInteger.set(page);
		result.forEach((e) -> {
			e.setNumber(atomicInteger.incrementAndGet());
		});
		return new ResultVO(pageInfo.getTotal(), result);
	}

	/**
	 * 删除数据 删除数据的inIds字符串 eg:1,2,3
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class) // 事务
	public void deleteDate(String inIds){
		String[] ids = inIds.split(",");
		for (String id : ids) {
			Ybjcmpsdjl ybjcmpsdjl = new Ybjcmpsdjl();
			ybjcmpsdjl.setId(new BigDecimal(id)).setIsDelete(true);
			int i = ybjcmpsdjlMapper.updateByPrimaryKeySelective(ybjcmpsdjl);
			if(i == 0){
				throw new MyException(ExceptionEnum.YBJCMPSDJL_DELETE_FAILED);
			}
		}
	}

	/**
	 * 新增数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void insertData(Ybjcmpsdjl ybjcmpsdjl){
		ybjcmpsdjl.setIsDelete(false);
		int i = ybjcmpsdjlMapper.insertSelective(ybjcmpsdjl);
		if(i == 0){
			throw new MyException(ExceptionEnum.YBJCMPSDJL_CREATE_FAILED);
		}
	}

	/**
	 * 修改数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateData(Ybjcmpsdjl ybjcmpsdjl) throws Exception {
		int i = ybjcmpsdjlMapper.updateByPrimaryKeySelective(ybjcmpsdjl);
		if(i == 0){
			throw new MyException(ExceptionEnum.YBJCMPSDJL_UPDATE_FAILED);
		}
	}

	@Override
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,
			String courtyardArea) {
		InputStream is = null;
		HSSFWorkbook tpWorkbook = null;
		HSSFWorkbook workBook = null;
		ByteArrayOutputStream outputStream = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("static/excel/仪表及触摸屏设定记录.xls");
			tpWorkbook = new HSSFWorkbook(is);
			workBook = new HSSFWorkbook();
			workBook = tpWorkbook;
			CellStyle hcs = PoiUtil.getHeaderStyle(workBook);
			Sheet sheet = workBook.getSheetAt(0);
			workBook.setSheetName(0, "仪表及触摸屏设定记录");

			int line = 1;
			Row row;
			Cell cell;
			//过滤
			Example example = new Example(Ybjcmpsdjl.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("isDelete",false);
			if(!StringUtils.isEmpty(startTime)){
				criteria.andGreaterThanOrEqualTo("rqsj",DateUtil.stringToLocalDateForYYYYMMDD(startTime));
			}
			if(!StringUtils.isEmpty(endTime)){
				criteria.andLessThanOrEqualTo("rqsj",DateUtil.stringToLocalDateForYYYYMMDD(endTime));
			}
			if(!StringUtils.isEmpty(courtyardArea) && !CourtyardAreaEnum.FULL_DISTRICT_ZH_CN.getValue().equals(courtyardArea)){
				criteria.andEqualTo("courtyardArea",courtyardArea);
			}

			//排序
			example.setOrderByClause("rqsj DESC");

			//查询
			List<Ybjcmpsdjl> datas = ybjcmpsdjlMapper.selectByExample(example);
			CellStyle cellStyle = PoiUtil.getCellStyle(workBook);
			for (Ybjcmpsdjl data : datas) {
				row = sheet.createRow(line++);
				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(DateUtil.localDateToStringForYYYYMMDD(data.getRqsj()));

				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getSdxm());

				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getYsdz());

				cell = row.createCell(3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getXsdz());

				cell = row.createCell(4);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getRemark());

				cell = row.createCell(5);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getCzry());

				cell = row.createCell(6);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(data.getCourtyardArea());

			}

			outputStream = new ByteArrayOutputStream();

			workBook.write(outputStream);
			DownloadUtil downloadUtil = new DownloadUtil();
			String fileName = "仪表及触摸屏设定记录.xls";
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
