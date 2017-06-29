package com.github.yangwk.ydtutil.excel.read;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.github.yangwk.ydtutil.excel.ExcelUtils;
import com.github.yangwk.ydtutil.reflect.Reflector;

/**
 * excel读取者
 */
public class ExcelReader<T> {
	public final String FIELD_ROWIDX = "rowIdx";	//写入行号的字段
	
	
	private Workbook workbook;
	private FormulaEvaluator formulaEvaluator;
	private Sheet sheet;
	private int startRowIndex;
	private int startColumnIndex;
	private String[] columnFields;
	
	private ReaderConvert readerConvert;
	
	private Reflector<T> reflector;
	
	/**
	 * excel读取者
	 * <br>Class如果存在字段row，将把当前数据所在行写入row字段
	 * @param clazz T的字段不建议使用基本类型：int,short,long等
	 */
	public ExcelReader(Class<T> clazz){
		this.reflector = new Reflector<T>(clazz);
	}
	
	/**
	 * 设置操作的excel
	 * @author yangwk
	 *
	 * @param workbook
	 * @return
	 */
	public void setWorkbook(Workbook workbook){
		this.workbook = workbook;
		this.formulaEvaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
	}
	
	/**
	 * 
	 * 读取inputStream作为excel
	 * @author yangwk
	 * @param input will be closed
	 * @see #setWorkbook(Workbook)
	 */
	public void setWorkbook(InputStream input){
		try {
			setWorkbook( WorkbookFactory.create(input) );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	/**
	 * 设置操作的表格
	 * @author yangwk
	 *
	 * @param sheetIndex 表格下标，从0开始
	 * @return
	 */
	public void setSheet(int sheetIndex){
		this.sheet = this.workbook.getSheetAt(sheetIndex);
	}
	
	/**
	 * 等同于start(startRowIndex, 0, columnFields, new DefaultReaderConvert() );
	 * @author yangwk
	 * @see #start(int, int, String[], ReaderConvert)
	 */
	public List<T> start(int startRowIndex,String[] columnFields){
		return start(startRowIndex, 0, columnFields, new DefaultReaderConvert() );
	}
	
	/**
	 * 开始excle读取
	 * @author yangwk
	 *
	 * @param startRowIndex 起始行，从0开始
	 * @param startColumnIndex 起始列 ，从0开始
	 * @param columnFields excle头按列从左到右顺序与T各字段的对应关系
	 * @param convert 类型转换器
	 * @return
	 */
	public List<T> start(int startRowIndex,int startColumnIndex,String[] columnFields,ReaderConvert convert){
		this.startRowIndex = startRowIndex;
		this.startColumnIndex = startColumnIndex;
		this.columnFields = columnFields;
		this.readerConvert = convert;
		return start();
	}
	
	public ReaderConvert getReaderConvert() {
		return this.readerConvert;
	}
	
	public Workbook getWorkbook(){
		return this.workbook;
	}
	
	public Sheet getSheet(){
		return this.sheet;
	}
	
	/**
	 * 返回excel的表格数
	 * @author yangwk
	 *
	 * @return
	 */
	public int getSheets(){
		return this.workbook.getNumberOfSheets();
	}
	
	/**
	 * 返回表格的实际行数
	 * @author yangwk
	 *
	 * @return
	 */
	public int getRows(){
		return this.sheet.getPhysicalNumberOfRows();
	}
	
	private int getCells(Row row){
		return row.getLastCellNum();	//row.getPhysicalNumberOfCells(); getPhysicalNumberOfCells有时候返回的不正确
	}

	/**
	 * 设置字段值
	 * @param ignoreNoField 忽略不存在的字段。将不执行赋值
	 */
	protected void setProperty(Object target, String fieldName, Object value, boolean ignoreNoField){
		Method setter = reflector.getSetter(fieldName);
		Field field = reflector.getField(fieldName);
		if(ignoreNoField && field == null){
			return ;	//不执行
		}
		Class<?> targetType = field.getType();
		reflector.invokeMethod(setter, target, readerConvert.convert(value, targetType) );
	}
	
	private boolean isBlank(Object obj){
		return obj == null || StringUtils.isBlank(obj.toString());
	}

	private List<T> start(){
		List<T> result = new ArrayList<T>();	//结果集合
		
		int rows = getRows();
		for(int r=Math.min(startRowIndex, rows); r<rows; r++){	//行范围，与头起始行相关
			Row row = sheet.getRow(r);
			if(row == null){	//必须判断空
				continue;
			}
			int cells = getCells(row);
			if(cells <= 0){
				continue;
			}
			T ttt = reflector.invokeConstructor();	//每行的实例化对象
			setProperty(ttt, FIELD_ROWIDX, r, true);	//设置行
			
			boolean hasNotEmptyCell = false;	//存在非空列
			
			int start= Math.min( Math.min( Math.max(startColumnIndex, 0), cells ), columnFields.length);
			int cellLen = Math.min(columnFields.length, cells);
			
			for(int c=start; c <cellLen ; c++ ){
				Cell cell = row.getCell(c);
				if(cell == null){	//必须判断空
					continue;
				}
				
				ValueCell valueCell= ExcelUtils.getValueCell(cell, sheet, formulaEvaluator);	//取值
				if(! isBlank(valueCell.value) ){	//不为空
					hasNotEmptyCell = true;
				}
				setProperty(ttt, columnFields[c - start], valueCell.value, false);
			}
			
			if(hasNotEmptyCell){	//列不全部为空，即该行非空行
				result.add( ttt );	//保存结果
			}
		}
		
		return result;
	}
	
	
}
