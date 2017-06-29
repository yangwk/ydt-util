package com.github.yangwk.ydtutil.excel.read;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 单元格值域
 * 
 * @author yangwk
 *
 */
public class ValueCell {
	/**
	 * 单元格
	 */
	public Cell cell;
	/**
	 * 单元格值，不为null
	 */
	public Object value;
	/**
	 * 单元格所属的合并范围（可能为null）
	 */
	public CellRangeAddress cellRangeAddress;
	
	/**
	 * 单元格值域
	 * @param cell	单元格
	 * @param value	单元格值
	 * @param cellRangeAddress	单元格所属的合并范围（可能为null）
	 */
	public ValueCell(Cell cell, Object value, CellRangeAddress cellRangeAddress) {
		super();
		this.cell = cell;
		this.value = value;
		this.cellRangeAddress = cellRangeAddress;
	}

}
