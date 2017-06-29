package com.github.yangwk.ydtutil.excel;

import java.math.BigDecimal;
import java.util.Date;

public class Student implements Cloneable{

	private String id;
	private String name;
	private String number;
	private String sex;
	private Date birthDate;
	private String idCardNo;
	private int rank;
	private Double money;
	private Boolean isChinese;
	private Long readHour;
	private Float weight;
	private BigDecimal bigMoney;
	private Integer rowIdx;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	public Boolean getIsChinese() {
		return isChinese;
	}
	public void setIsChinese(Boolean isChinese) {
		this.isChinese = isChinese;
	}
	public Long getReadHour() {
		return readHour;
	}
	public void setReadHour(Long readHour) {
		this.readHour = readHour;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public BigDecimal getBigMoney() {
		return bigMoney;
	}
	public void setBigMoney(BigDecimal bigMoney) {
		this.bigMoney = bigMoney;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public Integer getRowIdx() {
		return rowIdx;
	}
	public void setRowIdx(Integer rowIdx) {
		this.rowIdx = rowIdx;
	}
	
}
