package com.github.yangwk.ydtutil.excel.write;

import java.io.OutputStream;
import java.util.List;

/**
 * @author yangwk
 * @see #WriterTemplate(Class)
 */
public class WriterTemplate<T> {

	private Class<T> clazz;
	private ExcelWriter excelWriter;
	
	/**
	 * 输出者模板
	 * @author yangwk
	 * @param clazz
	 */
	public WriterTemplate(Class<T> clazz) {
		this.clazz = clazz;
		this.excelWriter = new ExcelWriter(0, 0);
	}
	
	/**
	 * 创建excel
	 * @author yangwk
	 * @param workbookType excel文档类型
	 * @param sheetName 表名
	 * @see ExcelWriter#WORKBOOK_TYPE_XLS
	 * @see ExcelWriter#WORKBOOK_TYPE_XLSX
	 */
	public void createExcel(int workbookType,String sheetName){
		excelWriter.setWorkbook(workbookType);
		excelWriter.setSheet(sheetName);
	}
	
	/**
	 * 输出头部
	 * @author yangwk
	 * @param title 标题
	 * @param remark 备注
	 * @param headNames 列名称
	 */
	public void writeHead(String title,String remark,String[] headNames){
		int cells = headNames.length;
		if(title != null){
			excelWriter.start(title, cells, getTitleStyle() );
		}
		if(remark != null){
			excelWriter.start(remark, cells, getRemarkStyle());
		}
		excelWriter.start(headNames, getHeadStyle() );
	}
	
	private CellStyle getTitleStyle(){
		CellStyle cs = new CellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setFontName("宋体");
		cs.setBorder(CellStyle.BORDER_THIN);
		cs.setFontHeight(14);
		cs.setFontColor(CellStyle.COLOR_BLUE);
		cs.setRowHeight(35);
		return cs;
	}
	
	private CellStyle getRemarkStyle(){
		CellStyle cs = new CellStyle();
		cs.setFontName("宋体");
		cs.setBorder(CellStyle.BORDER_THIN);
		cs.setFontHeight(12);
		cs.setFontColor(CellStyle.COLOR_RED);
		cs.setRowHeight(20);
		return cs;
	}
	
	private CellStyle getHeadStyle(){
		CellStyle cs = new CellStyle();
		cs.setFontName("宋体");
		cs.setBorder(CellStyle.BORDER_THIN);
		cs.setFontHeight(14);
		cs.setRowHeight(16);
		return cs;
	}
	
	private CellStyle getBodyStyle(){
		CellStyle cs = new CellStyle();
		cs.setFontName("宋体");
		cs.setBorder(1);
		cs.setFontHeight(12);
		return cs;
	}
	
	/**
	 * 输出主体
	 * @author yangwk
	 * @param datas
	 * @param columnFields
	 * @param convert
	 */
	public void writeBody(List<T> datas,String[] columnFields,WriterConvert convert){
		excelWriter.start(clazz, datas, columnFields, convert, getBodyStyle() );
	}
	
	/**
	 * 
	 * @author yangwk
	 * @see ExcelWriter#mergeSameCell(int, int, int[])
	 */
	public void mergeSameCell(int rowIndex, int groupCellIndex, int[] mergeCellIndexs){
		excelWriter.mergeSameCell(rowIndex, groupCellIndex, mergeCellIndexs);
	}
	
	/**
	 * @see ExcelWriter#write(OutputStream)
	 * @author yangwk
	 * @param output
	 */
	public void write(OutputStream output){
		excelWriter.write(output);
	}
	
}
