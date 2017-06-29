package com.github.yangwk.ydtutil.range;

/**
 * 范围
 * @see #Range(int, int)
 * @author yangwk
 */
public class Range {
	private int start;
	private int end;
	
	/**
	 * @see #Range(int, int)
	 */
	public Range(){
		super();
	}
	
	/**
	 * 范围，start <= end
	 * @author yangwk
	 * @param start 开始下标
	 * @param end 结束下标
	 */
	public Range(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
}
