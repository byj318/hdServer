package com.huamai.hdServer.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @Auther: byj
 * @Date: 2019/1/16 15:38
 * @Description:
 */
public class PoiUtil {
	/**
	 * 表头样式
	 * @param workBook
	 * @return
	 */
	public static CellStyle getHeaderStyle(Workbook workBook) {
		CellStyle hcs = workBook.createCellStyle();
		Font hf = workBook.createFont();
		hf.setColor(IndexedColors.WHITE.getIndex());
		hf.setFontName("黑体");
		hcs.setFont(hf);

		hcs.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		hcs.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());

		hcs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		hcs.setAlignment(HorizontalAlignment.CENTER);
		hcs.setBorderBottom(BorderStyle.THIN);
		hcs.setBorderLeft(BorderStyle.THIN);
		hcs.setBorderTop(BorderStyle.THIN);
		hcs.setBorderRight(BorderStyle.THIN);
		hcs.setVerticalAlignment(VerticalAlignment.CENTER);
		return hcs;
	}

	/**
	 * tbody样式
	 * @param workBook
	 * @return
	 */
	public static CellStyle getCellStyle(Workbook workBook) {
		CellStyle hcs = workBook.createCellStyle();
		hcs.setAlignment(HorizontalAlignment.CENTER);
		hcs.setBorderBottom(BorderStyle.THIN);
		hcs.setBorderLeft(BorderStyle.THIN);
		hcs.setBorderTop(BorderStyle.THIN);
		hcs.setBorderRight(BorderStyle.THIN);
		return hcs;
	}

}
