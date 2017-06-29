package com.github.yangwk.ydtutil.chart;

import java.util.List;

/**
 * @see #SeriesNode(Object, Object, List)
 * @author yangwk
 */
public class SeriesNode {

	private Object name ;
	private Object type ;
	private List<Object> data;
	
	/**
	 * @see #SeriesNode(Object, Object, List)
	 */
	public SeriesNode(){
		super();
	}
	
	/**
	 *  图表的数据定义
	 * @param name	显示名称
	 * @param type	图表类型
	 * @param data	数据
	 */
	public SeriesNode(Object name, Object type, List<Object> data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}
	
	
}