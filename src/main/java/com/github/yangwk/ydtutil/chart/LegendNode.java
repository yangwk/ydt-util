package com.github.yangwk.ydtutil.chart;

/**
 * @see #LegendNode(Object, String, Object, String, Object, String, String, Object)
 * @author yangwk
 */
public class LegendNode {
	
	private Object id;
	private String idField;
	private Object name;
	private String nameField;
	private Object suffixName;
	private String suffixNameField;
	private String valueField;
	private Object defaultValue;
	
	/**
	 * 图表的视图
	 * <br>规则：<br>如果nameField不为null,则name取值为nameField对应字段值
	 * <br>如果suffixNameField不为null,则suffixName取值为suffixNameField对应字段值
	 * <br>如果valueField为null,则valueField取值为defaultValue
	 * @param id	主值
	 * @param idField	主值的字段
	 * @param name	显示名称
	 * @param nameField	显示名称的字段
	 * @param suffixName	显示名称后缀（可作为一些辅助说明, 比如单位'(万亩)'）
	 * @param suffixNameField	显示名称后缀的字段
	 * @param valueField	取值的字段
	 * @param defaultValue	取值的默认值
	 */
	public LegendNode(Object id, String idField, Object name, String nameField, Object suffixName,
			String suffixNameField, String valueField, Object defaultValue) {
		super();
		this.id = id;
		this.idField = idField;
		this.name = name;
		this.nameField = nameField;
		this.suffixName = suffixName;
		this.suffixNameField = suffixNameField;
		this.valueField = valueField;
		this.defaultValue = defaultValue;
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
	public Object getName() {
		return name;
	}
	public void setName(Object name) {
		this.name = name;
	}
	public String getNameField() {
		return nameField;
	}
	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	public Object getSuffixName() {
		return suffixName;
	}
	public void setSuffixName(Object suffixName) {
		this.suffixName = suffixName;
	}
	public String getSuffixNameField() {
		return suffixNameField;
	}
	public void setSuffixNameField(String suffixNameField) {
		this.suffixNameField = suffixNameField;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

}
