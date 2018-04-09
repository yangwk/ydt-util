package com.github.yangwk.ydtutil.excel.write;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.yangwk.ydtutil.Eq;
import com.github.yangwk.ydtutil.collection.CollUtils;
import com.github.yangwk.ydtutil.collection.Range;
import com.github.yangwk.ydtutil.excel.ExcelUtils;
import com.github.yangwk.ydtutil.reflect.Reflector;

/**
 * @author yangwk
 * @see #ExcelWriter(int, int)
 */
public class ExcelWriter {
	/**
	 * xlsx文档
	 */
	public static final int WORKBOOK_TYPE_XLSX = 0;
	/**
	 * xls文档
	 */
	public static final int WORKBOOK_TYPE_XLS = 1;
	
	private Workbook workbook;
	private FormulaEvaluator formulaEvaluator;
	private Sheet sheet;
	
	private int startRowIndex;
	private final int startColumnIndex;
	
	/**
	 * excel输出。默认的输出会将行下标新增
	 * @author yangwk
	 * @param startRowIndex 要操作的起始行，从0开始
	 * @param startColumnIndex 要操作的起始列，从0开始
	 */
	public ExcelWriter(int startRowIndex, int startColumnIndex){
		this.startRowIndex = startRowIndex;
		this.startColumnIndex = startColumnIndex;
	}
	
	/**
	 * 设置操作的excel
	 * @author yangwk
	 *
	 * @param workbook
	 */
	public void setWorkbook(Workbook workbook){
		this.workbook = workbook;
		this.formulaEvaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
	}
	
	/**
	 * 根据workbookType创建一个空内容的excel
	 * @see #WORKBOOK_TYPE_XLS
	 * @see #WORKBOOK_TYPE_XLSX
	 * @see #setWorkbook(Workbook)
	 */
	public void setWorkbook(int workbookType){
		Workbook wb = createWorkbook(workbookType, null);
		setWorkbook(wb);
	}

	/**
	 * 
	 * 读取inputStream作为一个excel
	 * @param input will be closed
	 */
	public void setWorkbook(InputStream input){
		Workbook wb = createWorkbook(-1, input);
		setWorkbook(wb);
	}
	
	/**
	 * 设置操作的表格
	 * @author yangwk
	 *
	 * @param sheetIndex 表格下标，从0开始
	 * @return
	 */
	public void setSheet(int sheetIndex){
		this.sheet = createSheet(sheetIndex, null);
	}
	
	/**
	 * 设置操作的表格
	 * @author yangwk
	 * @param sheetName 表名
	 * @return
	 */
	public void setSheet(String sheetName){
		this.sheet = createSheet(-1, sheetName);
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
	 * 输出数据
	 * @author yangwk
	 * @param clazz 
	 * @param datas 输出数据
	 * @param columnFields 输出数据Class的字段名，按列从左到右顺序输出到列 
	 * @param convert 转换器，如果为null则使用默认转换器
	 * @param cellStyle 样式，如果为null则不设置样式
	 */
	public <T> void start(Class<T> clazz, final List<T> datas, String[] columnFields, WriterConvert convert,
			com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle){
		Reflector<T> reflector = new Reflector<T>(clazz);
		
		//转换
		if(convert == null){
			convert = new DefaultWriterConvert();
		}
		
		startRowIndex = Math.min( Math.max(startRowIndex, 0), getRows());
		int defaultCells = columnFields.length;	//默认列数
		
		//样式
		CellStyle poiCellStyle = ExcelUtils.wrapCellStyle(cellStyle, workbook);
				
		//数据输出
		for(T t : datas){
			Row row = getRow(startRowIndex);	//行
			int cells = getCells(row);
			if(cells <= 0){	//可能为新行
				cells = defaultCells;
			}
			int start= Math.min( Math.min( Math.max(startColumnIndex, 0), cells ), defaultCells);
			int cellLen = Math.min(defaultCells, cells);
			
			for(int c=start; c < cellLen; c++){
				Cell cell = getCell(row,c);	//列
				
				String fieldName = columnFields[c - start];
				
				Method getter = reflector.getGetter(fieldName);
				Object value = reflector.invokeMethod(getter, t);
				
				String cellValue = convert.convert(t, fieldName, value);
				
				setCellValue(cell, cellValue);
				
				setCellStyle(cell, poiCellStyle, cellStyle);
			}
			increaseStartRowIndex();
		}
	}
	
	/**
	 * 输出数据
	 * @author yangwk
	 * @param data 输出数据作为一行
	 * @param rowCells 一行定义的列数（将被合并）
	 * @param cellStyle 样式，如果为null则不设置样式
	 */
	private void start0(final String data, int rowCells, com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle){
		startRowIndex = Math.min( Math.max(startRowIndex, 0), getRows());
		int rowIndex = startRowIndex;
		int defaultCells = rowCells;	//默认列数
		
		//样式
		CellStyle poiCellStyle = ExcelUtils.wrapCellStyle(cellStyle, workbook);
		
		//数据输出
		for(int r=0; r<1; r++){	//一行
			Row row = getRow(startRowIndex);	//行
			int cells = getCells(row);
			if(cells <= 0){	//可能为新行
				cells = defaultCells;
			}
			int start= Math.min( Math.min( Math.max(startColumnIndex, 0), cells ), defaultCells);
			int cellLen = Math.min(defaultCells, cells);
			
			for(int c=start; c < cellLen; c++){
				Cell cell = getCell(row,c);	//列
				
				setCellValue(cell, data);
				
				setCellStyle(cell, poiCellStyle, cellStyle);
			}
			
			increaseStartRowIndex();
		}
		
		mergeOneRow(rowIndex, rowCells);	//合并
	}
	
	/**
	 * 新增行下标
	 * @author yangwk
	 */
	private void increaseStartRowIndex(){
		startRowIndex ++ ;
	}

	/**
	 * 输出数据
	 * @author yangwk
	 * @param data 输出数据作为一行
	 * @param rowCells 一行定义的列数（将被合并）
	 * @param cellStyle 样式，如果为null则不设置样式
	 */
	public void start(final String data, int rowCells, com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle){
		start0(data, rowCells, cellStyle);
	}
	
	/**
	 * 输出数据
	 * @author yangwk
	 * @param datas 输出数据，按列从左到右顺序输出到列
	 * @param cellStyle 样式，如果为null则不设置样式
	 */
	public void start(final String[] datas, com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle){
		startRowIndex = Math.min( Math.max(startRowIndex, 0), getRows());
		int defaultCells = datas.length;	//默认列数
		
		//样式
		CellStyle poiCellStyle = ExcelUtils.wrapCellStyle(cellStyle, workbook);
				
		//数据输出
		for(int r=0; r<1; r++){	//一行
			Row row = getRow(startRowIndex);	//行
			int cells = getCells(row);
			if(cells <= 0){	//可能为新行
				cells = defaultCells;
			}
			int start= Math.min( Math.min( Math.max(startColumnIndex, 0), cells ), defaultCells);
			int cellLen = Math.min(defaultCells, cells);
			
			for(int c=start; c < cellLen; c++){
				Cell cell = getCell(row,c);	//列
				
				setCellValue(cell, datas[c - start]);
				
				setCellStyle(cell, poiCellStyle, cellStyle);
			}
			increaseStartRowIndex();
		}
	}

	/**
	 * 
	 * 输出excel到outputStream
	 * @author yangwk
	 * @param output will be closed
	 */
	public void write(OutputStream output) {
		try {
			this.workbook.write(output);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(output);
		}
	}

	public Workbook getWorkbook(){
		return this.workbook;
	}
	
	public Sheet getSheet(){
		return this.sheet;
	}
	
	
	
	/**
	 * 如果inputStream为null，根据workbookType创建一个空内容的workbook；
	 * <br>否则，读取inputStream作为一个workbook，忽略workbookType
	 */
	private Workbook createWorkbook(int workbookType,InputStream input){
		Workbook wBook = null;
		if(input != null){	//读取inputStream作为一个workbook
			try {
				wBook = WorkbookFactory.create(input);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}finally{
				IOUtils.closeQuietly(input);
			}
		}
		else{	//创建一个空内容的workbook
			if(workbookType == WORKBOOK_TYPE_XLS ){
				wBook = new HSSFWorkbook();
			}
			else if(workbookType == WORKBOOK_TYPE_XLSX ){
				wBook = new XSSFWorkbook();
			}
		}
		
		return wBook;
	}
	
	/**
	 * 如果sheetIndex小于0，当sheetName不为null，将用workbook创建表名为sheetName的sheet；
	 * <br>当sheetName为null，将用workbook创建不指定表名的sheet；
	 * <br>如果sheetIndex不小于0，根据sheetIndex利用workbook获取sheet
	 */
	private Sheet createSheet(int sheetIndex,String sheetName){
		Sheet sh = null;
		if(sheetIndex < 0){
			if(sheetName != null){
				String safeName = WorkbookUtil.createSafeSheetName(sheetName);	//创建表名
				sh = this.workbook.createSheet(safeName);
			}
			else{
				sh = this.workbook.createSheet();
			}
		}
		else{
			sh = this.workbook.getSheetAt(sheetIndex);	//直接获取
		}
		return sh;
	}

	
	/**
	 * 根据行索引，获取行
	 * <br>如果行索引超出范围，将创建行；否则，根据行索引得到相应行
	 */
	private Row getRow(int rowIndex){
		int rows = getRows();
		Row row = null;
		if(rowIndex >= rows){	//没有该行
			row = sheet.createRow(rowIndex);	//创建
		}
		else{
			row = sheet.getRow(rowIndex);	//取该行
			if(row == null){	//空
				row = sheet.createRow(rowIndex);	//创建
			}
		}
		return row;
	}
	
	/**
	 * 根据行和列索引，获取列
	 * <br>如果列索引超出范围，将创建列；否则，根据列索引得到相应列
	 */
	private Cell getCell(Row row,int cellIndex){
		int cells = getCells(row);
		Cell cell = null;
		if(cellIndex >= cells){	//没有该列
			cell = row.createCell(cellIndex);	//创建
		}
		else{
			cell = row.getCell(cellIndex);	//取该列
			if(cell == null){	//空
				cell = row.createCell(cellIndex);	//创建
			}
		}
		return cell;
	}
	
	/**
	 * 合并相同列
	 * @author yangwk
	 *
	 * @param rowIndex 开始行号
	 * @param groupCellIndex 分组列，不会合并
	 * @param mergeCellIndexs 合并列，被合并
	 */
	public void mergeSameCell(int rowIndex, int groupCellIndex, int[] mergeCellIndexs){
		//该方法思路：将groupCellIndex和mergeCellIndexs相等的行抽取出来，过滤跨度大于1行的，再分别对mergeCellIndexs列合并
		List<Object[]> datas = new ArrayList<Object[]>();	//数据
		//获取数据
		for(int r=rowIndex; r<getRows(); r++){
			Row row = sheet.getRow(r);
			if(row == null){
				continue ;
			}
			Cell groupCell = row.getCell(groupCellIndex);
			if(groupCell == null){
				continue ;
			}
			Object groupValue = ExcelUtils.getValueCell(groupCell, sheet, formulaEvaluator).value;	//取值
			//合并列
			Object[] mergeValues = new Object[mergeCellIndexs.length];
			for(int k=0; k<mergeCellIndexs.length; k++){
				Cell mergeCell = row.getCell(mergeCellIndexs[k]);
				if(mergeCell == null){
					continue ;
				}
				Object mergeValue = ExcelUtils.getValueCell(mergeCell, sheet, formulaEvaluator).value;	//取值
				mergeValues[k] = mergeValue;
			}
			datas.add(new Object[]{groupValue, mergeValues});	//添加
		}
		//开始获取相邻等值范围
		List<Range> ranges = CollUtils.range(datas, new Eq<Object[], Object[]>() {
			
			@Override
			public boolean equals(Object[] prev, Object[] curr) {
				Object prevGroup = prev[0] ; 
				Object[] prevMerges = (Object[])prev[1]; 
				Object currGroup = curr[0];
				Object[] currMerge = (Object[])curr[1];
				//开始判断值
				if( prevGroup == null ? prevGroup == currGroup : prevGroup.equals(currGroup) ){
					for(int t=0; t<prevMerges.length; t++){
						if(prevMerges[t] == null ? prevMerges[t] == currMerge[t] : prevMerges[t].equals(currMerge[t])){
							;
						}else{
							return false;
						}
					}
				}else{
					return false;
				}
				return true;
			}
		});
		//开始合并
		for(Range range : ranges){
			if(range.getStart() != range.getEnd()){	//不在同一行（跨行）
				//只合并mergeCellIndexs列
				for(int k=0; k<mergeCellIndexs.length; k++){
					//加行号，转成excel下标
					addMergedRegion(range.getStart()+rowIndex, range.getEnd()+rowIndex, mergeCellIndexs[k], mergeCellIndexs[k]);
				}
			}
		}
	}
	
	private void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol){
		CellRangeAddress candidateRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(candidateRegion);
	}
	
	/**
	 * 合并单元格
	 * @author yangwk
	 * @param firstRow 起始行，从0开始
	 * @param lastRow 结尾行
	 * @param firstCol 起始列
	 * @param lastCol 结尾列
	 */
	public void merge(int firstRow, int lastRow, int firstCol, int lastCol){
		addMergedRegion(firstRow, lastRow, firstCol, lastCol);
	}
	
	/**
	 * 合并一行
	 */
	private void mergeOneRow(int rowIndex, int mergeCells){
		addMergedRegion(rowIndex, rowIndex, startColumnIndex, mergeCells-1);
	}
	
	/**
	 * 设置值
	 */
	private void setCellValue(Cell cell, String cellValue){
		cell.setCellValue( cellValue == null ? ExcelUtils.DEFAULT_CELLVALUE : cellValue );
	}
	
	/**
	 * 设置样式
	 */
	private void setCellStyle(Cell cell, CellStyle poiCellStyle, com.github.yangwk.ydtutil.excel.write.CellStyle cellStyle){
		if(poiCellStyle == null || cellStyle == null){
			return ;
		}
		cell.getRow().setHeight((short)cellStyle.getRowHeight());
		sheet.setColumnWidth(cell.getColumnIndex(), cellStyle.getColumnWidth());
		cell.setCellStyle(poiCellStyle);
	}

	/**
	 * 返回当前操作行号
	 * @author yangwk
	 * @return
	 */
	public int getStartRowIndex() {
		return startRowIndex;
	}
	
}
