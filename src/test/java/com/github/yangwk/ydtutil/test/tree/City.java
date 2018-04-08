package com.github.yangwk.ydtutil.test.tree;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class City{
	private Integer id;
	private String name;
	private Integer parentId;
	
	/**
	 * 必须显示声明无参构造函数，否则反射newInstance()失败
	 */
	public City(){
	}
	
	public City(Integer id, String name, Integer parentId) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	
}
