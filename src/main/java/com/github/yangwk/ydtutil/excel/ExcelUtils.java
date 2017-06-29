package com.github.yangwk.ydtutil.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.github.yangwk.ydtutil.excel.read.ValueCell;

/**
 * excel工具类
 * @author yangwk
 *
 */
public class ExcelUtils {
	/**
	 * 默认单元格值
	 */
	public static final String DEFAULT_CELLVALUE = "";

	/**
	 * 获取单元格值域
	 */
	public static ValueCell getValueCell(Cell cell, Sheet sheet, FormulaEvaluator formulaEvaluator){
		int count = sheet.getNumMergedRegions();
		for(int k=0; k<count; k++){
			CellRangeAddress address = sheet.getMergedRegion(k);	//直接取，不需要先取出所有的MergedRegion
			if(address.isInRange(cell.getRowIndex(), cell.getColumnIndex())){
				Row fRow = sheet.getRow( address.getFirstRow() );
				Cell fCell = fRow.getCell( address.getFirstColumn() );
				return new ValueCell(cell, getValue(fCell,formulaEvaluator), address);
			}
		}
		return new ValueCell(cell, getValue(cell,formulaEvaluator), null);
	}
	
	
	/**
	 * 获取单元格值
	 */
	private static Object getValue(Cell cell, FormulaEvaluator formulaEvaluator){
		Object cellValue = DEFAULT_CELLVALUE;
		int cellType = cell.getCellType();
		if(cellType == Cell.CELL_TYPE_NUMERIC){
			cellValue = getNumericCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_STRING){
			cellValue = getStringCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_FORMULA){
			cellValue = getFormulaCellValue(cell, formulaEvaluator);	//公式特殊处理
		}
		else if(cellType == Cell.CELL_TYPE_BLANK){	//空字符串, equals("")
			cellValue = getStringCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_BOOLEAN){
			cellValue = getBooleanCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_ERROR){
			cellValue = getErrorCellValue(cell);
		}
		return cellValue;
	}
	
	
	/**
	 * 数字
	 */
	private static Object getNumericCellValue(Cell cell){
		if(DateUtil.isCellDateFormatted(cell)){		//可能为date
			return cell.getDateCellValue();
		}
		return Double.valueOf( cell.getNumericCellValue() );
	}
	
	/**
	 * 字符串
	 */
	private static String getStringCellValue(Cell cell){
		return cell.getRichStringCellValue().getString();
	}
	
	/**
	 * 公式
	 */
	private  static Object getFormulaCellValue(Cell cell, FormulaEvaluator formulaEvaluator){
		Object cellValue = DEFAULT_CELLVALUE;
		CellValue cv = formulaEvaluator.evaluate(cell);
		int cellType = cv.getCellType();
		if(cellType == Cell.CELL_TYPE_NUMERIC){
			cellValue = getNumericCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_STRING){
			cellValue = getStringCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_BLANK){	//空字符串, equals("")
			cellValue = getStringCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_BOOLEAN){
			cellValue = getBooleanCellValue(cell);
		}
		else if(cellType == Cell.CELL_TYPE_ERROR){
			cellValue = getErrorCellValue(cell);
		}
		return cellValue;
	}
	
	/**
	 * 布尔
	 */
	private  static Boolean getBooleanCellValue(Cell cell){
		return Boolean.valueOf( cell.getBooleanCellValue() );
	}
	
	/**
	 * Error
	 */
	private static Byte getErrorCellValue(Cell cell){
		return Byte.valueOf( cell.getErrorCellValue() );
	}
	
	/**
	 * 单元格样式封装
	 * @author yangwk
	 * @param cellStyle
	 * @param workbook
	 * @return may be null
	 */
	public static CellStyle wrapCellStyle(com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle, Workbook workbook){
		if(cellStyle == null || workbook == null){
			return null;
		}
		CellStyle poiCellStyle = workbook.createCellStyle();
		
		poiCellStyle.setBorderBottom((short)cellStyle.getBorder());
		poiCellStyle.setBorderLeft((short)cellStyle.getBorder());
		poiCellStyle.setBorderRight((short)cellStyle.getBorder());
		poiCellStyle.setBorderTop((short)cellStyle.getBorder());
		
		poiCellStyle.setVerticalAlignment((short)cellStyle.getVerticalAlignment());
		
		poiCellStyle.setAlignment((short)cellStyle.getAlignment());
		
		if(StringUtils.isNotBlank(cellStyle.getFontName()) ){
			Font font = workbook.createFont();
			font.setFontName(cellStyle.getFontName());
			if(cellStyle.getFontHeight() >= 0 ){
				font.setFontHeightInPoints((short)cellStyle.getFontHeight());
			}
			if(cellStyle.getFontColor() >= 0){
				font.setColor((short)cellStyle.getFontColor());
			}
			poiCellStyle.setFont(font);
		}
		if(cellStyle.getForegroundColor() >= 0){
			poiCellStyle.setFillForegroundColor((short)cellStyle.getForegroundColor());
			poiCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		
		return poiCellStyle;
	}
	
}
