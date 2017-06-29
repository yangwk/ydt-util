package com.github.yangwk.ydtutil.excel.write;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * 单元格样式
 * @author yangwk
 *
 */
public class CellStyle {
	/**
	 * No border
	 */
	public static final int BORDER_NONE = org.apache.poi.ss.usermodel.CellStyle.BORDER_NONE;
	/**
	 * Thin border
	 */
	public static final int BORDER_THIN = org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;
	
	
	/**
	 * center-aligned vertical alignment
	 */
	public static final int VERTICAL_CENTER = org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;

	/**
	 * general (normal) horizontal alignment
	 */
	public static final int ALIGN_GENERAL = org.apache.poi.ss.usermodel.CellStyle.ALIGN_GENERAL;
	/**
     * left-justified horizontal alignment
     */
	public static final int ALIGN_LEFT = org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
	/**
     * center horizontal alignment
     */
	public static final int ALIGN_CENTER = org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
	/**
     * right-justified horizontal alignment
     */
	public static final int ALIGN_RIGHT = org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;
	
	/**
	 * red color
	 */
	public static final int COLOR_RED = HSSFColor.RED.index;
	/**
	 * green color
	 */
	public static final int COLOR_GREEN = HSSFColor.GREEN.index;
	/**
	 * blue color
	 */
	public static final int COLOR_BLUE = HSSFColor.BLUE.index;
	/**
	 * black color
	 */
	public static final int COLOR_BLACK = HSSFColor.BLACK.index;
	
	
	private int columnWidth = 20 * 256;	//(in units of 1/256th of a character width
	
	private float rowHeight = 14.25f * 20;	//1/20th of a point;
	
	private int border = BORDER_NONE;
	
	private int verticalAlignment = VERTICAL_CENTER;

	private int alignment = ALIGN_GENERAL;
	
	private String fontName;
	
	private int fontHeight = -1;
	
	private int fontColor = -1;
	
	private int foregroundColor = -1;

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth * 256;
	}

	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public int getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(int foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public float getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(float rowHeight) {
		this.rowHeight = rowHeight * 20;
	}
	
}
